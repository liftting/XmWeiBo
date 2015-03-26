package wm.xmwei.datadao.dbway.home;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.bean.DataMessageListDomain;
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
//            HomeOtherGroupTimeLineDBTask.replace(list, accountId, groupId);
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

    private static String getRecentGroupId(String accountId) {
        // need add logic


        return Constants.ALL_GROUP_ID; //
    }

    public static DataMessageListDomain getRecentGroupData(String accountId) {
        String groupId = getRecentGroupId(accountId);
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


}
