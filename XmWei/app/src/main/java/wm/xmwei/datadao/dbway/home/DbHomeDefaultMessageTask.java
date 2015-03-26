package wm.xmwei.datadao.dbway.home;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import wm.xmwei.XmApplication;
import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.bean.DataMessageGroupDomain;
import wm.xmwei.bean.DataMessageListDomain;
import wm.xmwei.bean.TimeLinePosition;
import wm.xmwei.core.data.Constants;
import wm.xmwei.core.database.DataBaseHelper;
import wm.xmwei.core.database.table.HomeMessageTable;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.util.XmDataAppConfig;

/**
 * one group hash a DataMessageListDomain ,
 * and in the table save the recent groupid
 * so, wo can get the recent Data is a DataMessageListDomain
 * and when we get the others group id data ,we can get the list DataMessageListDomain data
 * <p/>
 * <p/>
 * home table has the recent groupid  and has the child table
 */
public class DbHomeDefaultMessageTask {


    private DbHomeDefaultMessageTask() {

    }

    private static SQLiteDatabase getWriteableDataBase() {
        DataBaseHelper databaseHelper = DataBaseHelper.getInstance();
        return databaseHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getReadableDataBase() {
        DataBaseHelper databaseHelper = DataBaseHelper.getInstance();
        return databaseHelper.getReadableDatabase();
    }

    private static void replace(DataMessageListDomain list, String accountId, String groupId) {
        if (Constants.ALL_GROUP_ID.equals(groupId)) {
            // 所有人分组
            deleteAllHomes(accountId); // first delete is that the list data is all data,
            addHomeLineMsg(list, accountId);
        } else {
            //其他的分组数据加载
            DbHomeOtherGroupMessageTask.replace(list, accountId, groupId);
        }
    }

    public static void asyncReplace(final DataMessageListDomain list, final String accountId,
                                    final String groupId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (DbHomeDefaultMessageTask.class) {
                    replace(list, accountId, groupId);
                }
            }
        }).start();
    }

    private static void deleteAllHomes(String accountId) {
        String sql = "delete from " + HomeMessageTable.HomeDataTable.TABLE_NAME + " where "
                + HomeMessageTable.HomeDataTable.ACCOUNTID + " in " + "(" + accountId + ")";

        getWriteableDataBase().execSQL(sql);
    }

    private static void addHomeLineMsg(DataMessageListDomain list, String accountId) {

        if (list == null || list.getSize() == 0) {
            return;
        }

        Gson gson = new Gson();
        List<DataMessageDomain> msgList = list.getItemList();
        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(getWriteableDataBase(),
                HomeMessageTable.HomeDataTable.TABLE_NAME);
        final int mblogidColumn = ih.getColumnIndex(HomeMessageTable.HomeDataTable.MBLOGID);
        final int accountidColumn = ih.getColumnIndex(HomeMessageTable.HomeDataTable.ACCOUNTID);
        final int jsondataColumn = ih.getColumnIndex(HomeMessageTable.HomeDataTable.JSONDATA);
        try {
            getWriteableDataBase().beginTransaction();
            for (int i = 0; i < msgList.size(); i++) {
                DataMessageDomain msg = msgList.get(i);
                ih.prepareForInsert();
                if (msg != null) {
                    ih.bind(mblogidColumn, msg.getId());
                    ih.bind(accountidColumn, accountId);
                    String json = gson.toJson(msg);
                    ih.bind(jsondataColumn, json);
                } else {
                    ih.bind(mblogidColumn, "-1");
                    ih.bind(accountidColumn, accountId);
                    ih.bind(jsondataColumn, "");
                }
                ih.execute();
            }
            getWriteableDataBase().setTransactionSuccessful();
        } catch (SQLException e) {


        } finally {
            getWriteableDataBase().endTransaction();
            ih.close();
        }
    }

    // get recent group id , this is to get data when user resee the app data
    private static String getRecentGroupId(String accountId) {
        // need add logic

        String sql = "select * from " + HomeMessageTable.TABLE_NAME + " where " + HomeMessageTable.ACCOUNTID
                + "  = "
                + accountId;
        Cursor c = getWriteableDataBase().rawQuery(sql, null);
        Gson gson = new Gson();
        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(HomeMessageTable.RECENT_GROUP_ID));
            if (!TextUtils.isEmpty(id)) {
                return id;
            }
        }
        c.close();

        return Constants.ALL_GROUP_ID; //
    }

    public static void asyncUpdateRecentGroupId(String accountId, final String groupId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateRecentGroupId(XmApplication.getInstance().getUserBingDomain().getUid(),
                        groupId);
            }
        };

        new Thread(runnable).start();
    }

    private static void updateRecentGroupId(String accountId, String groupId) {

        String sql = "select * from " + HomeMessageTable.TABLE_NAME + " where " + HomeMessageTable.ACCOUNTID
                + "  = "
                + accountId;
        Cursor c = getWriteableDataBase().rawQuery(sql, null);
        if (c.moveToNext()) {
            try {
                String[] args = {accountId};
                ContentValues cv = new ContentValues();
                cv.put(HomeMessageTable.RECENT_GROUP_ID, groupId);
                getWriteableDataBase().update(HomeMessageTable.TABLE_NAME, cv, HomeMessageTable.ACCOUNTID + "=?", args);
            } catch (JsonSyntaxException e) {

            }
        } else {

            ContentValues cv = new ContentValues();
            cv.put(HomeMessageTable.ACCOUNTID, accountId);
            cv.put(HomeMessageTable.RECENT_GROUP_ID, groupId);
            getWriteableDataBase().insert(HomeMessageTable.TABLE_NAME,
                    HomeMessageTable.ID, cv);
        }
    }

    // load recent data
    public static DataMessageListDomain getCurrentGroupData(String accountId, String groupId) {
        DataMessageListDomain msgList = new DataMessageListDomain();
//        TimeLinePosition position;
        if (groupId.equals(Constants.ALL_GROUP_ID)) {
//            position = getPosition(accountId);
            msgList = getHomeLineMsgList(accountId, 0);
            // cache the data cache 1000 ,but we need see is only 60 ,so need set position


        } else {
//            position = HomeOtherGroupTimeLineDBTask.getPosition(accountId, groupId);
//            msgList = HomeOtherGroupTimeLineDBTask.get(accountId, groupId,
//                    position.position + AppConfig.DB_CACHE_COUNT_OFFSET);
            getOtherGroupData(accountId, groupId);
        }

        return msgList;
    }

    private static DataMessageListDomain getHomeLineMsgList(String accountId, int limitCount) {
        Gson gson = new Gson();
        DataMessageListDomain result = new DataMessageListDomain();
        int limit = limitCount > XmDataAppConfig.DEFAULT_MSG_COUNT_50 ? limitCount
                : XmDataAppConfig.DEFAULT_MSG_COUNT_50;
        List<DataMessageDomain> msgList = new ArrayList<DataMessageDomain>();
        String sql = "select * from " + HomeMessageTable.HomeDataTable.TABLE_NAME + " where "
                + HomeMessageTable.HomeDataTable.ACCOUNTID + "  = "
                + accountId + " order by " + HomeMessageTable.HomeDataTable.ID + " asc limit " + limit;
        Cursor c = getReadableDataBase().rawQuery(sql, null);
        while (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(HomeMessageTable.HomeDataTable.JSONDATA));
            if (!TextUtils.isEmpty(json)) {
                try {
                    DataMessageDomain value = gson.fromJson(json, DataMessageDomain.class);
                    if (!value.isMiddleUnreadItem() && !TextUtils.isEmpty(value.getText())) {
                        value.getListViewSpannableString();
                    }
                    msgList.add(value);
                } catch (JsonSyntaxException e) {
                    AppLogger.e(e.getMessage());
                }
            } else {
                msgList.add(null);
            }
        }

        //delete the null flag at the head positon and the end position
        for (int i = msgList.size() - 1; i >= 0; i--) {
            if (msgList.get(i) == null) {
                msgList.remove(i);
            } else {
                break;
            }
        }

        for (int i = 0; i < msgList.size(); i++) {
            if (msgList.get(i) == null) {
                msgList.remove(i);
            } else {
                break;
            }
        }

        result.setStatuses(msgList);
        c.close();
        return result;
    }

    private static TimeLinePosition getPosition(String accountId) {
        String sql = "select * from " + HomeMessageTable.TABLE_NAME + " where " + HomeMessageTable.ACCOUNTID
                + "  = "
                + accountId;
        Cursor c = getReadableDataBase().rawQuery(sql, null);
        Gson gson = new Gson();
        while (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(HomeMessageTable.TIMELINEDATA));
            if (!TextUtils.isEmpty(json)) {
                try {
                    TimeLinePosition value = gson.fromJson(json, TimeLinePosition.class);
                    c.close();
                    return value;
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        c.close();
        return TimeLinePosition.empty();
    }

    // 来获取其他的分组group的数据
    public static DataMessageListDomain getOtherGroupData(String accountId,
                                                          String currentGroupId) {

//        if (!"0".equals(exceptGroupId)) {
//            // 如果要拿取的不是所有 的分组数据，就将所有用户的分组数据也先获取到
//            TimeLinePosition position = getPosition(accountId);
//            DataMessageListDomain msgList = getHomeLineMsgList(accountId,
//                    position.position + AppConfig.DB_CACHE_COUNT_OFFSET);
//            DataMessageGroupDomain home = new DataMessageGroupDomain("0", msgList, position);
//            data.add(home);
//        }

        DataMessageGroupDomain dbMsg = DbHomeOtherGroupMessageTask
                .getDatGroupMessage(accountId, currentGroupId);


        return dbMsg.msgList;
    }


}
