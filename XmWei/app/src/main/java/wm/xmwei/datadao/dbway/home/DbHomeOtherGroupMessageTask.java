package wm.xmwei.datadao.dbway.home;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import wm.xmwei.bean.DataMessageDomain;
import wm.xmwei.bean.DataMessageGroupDomain;
import wm.xmwei.bean.DataMessageListDomain;
import wm.xmwei.bean.TimeLinePosition;
import wm.xmwei.core.database.DataBaseHelper;
import wm.xmwei.core.database.table.HomeOtherGroupTable;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.util.XmDataAppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: think
 * Date: 15-3-26
 * Time: 下午11:19
 * To change this template use File | Settings | File Templates.
 */
public class DbHomeOtherGroupMessageTask {

    private DbHomeOtherGroupMessageTask() {

    }

    private static SQLiteDatabase getWriteableDataBase() {
        DataBaseHelper databaseHelper = DataBaseHelper.getInstance();
        return databaseHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getReadableDataBase() {
        DataBaseHelper databaseHelper = DataBaseHelper.getInstance();
        return databaseHelper.getReadableDatabase();
    }

    private static void addHomeLineMsg(DataMessageListDomain list, String accountId, String groupId) {

        if (list == null || list.getSize() == 0) {
            return;
        }

        Gson gson = new Gson();
        List<DataMessageDomain> msgList = list.getItemList();

        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(getWriteableDataBase(),
                HomeOtherGroupTable.HomeOtherGroupDataTable.TABLE_NAME);
        final int mblogidColumn = ih.getColumnIndex(
                HomeOtherGroupTable.HomeOtherGroupDataTable.MBLOGID);
        final int accountidColumn = ih.getColumnIndex(
                HomeOtherGroupTable.HomeOtherGroupDataTable.ACCOUNTID);
        final int jsondataColumn = ih.getColumnIndex(
                HomeOtherGroupTable.HomeOtherGroupDataTable.JSONDATA);
        final int groupidColumn = ih.getColumnIndex(
                HomeOtherGroupTable.HomeOtherGroupDataTable.GROUPID);

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
                    ih.bind(groupidColumn, groupId);
                } else {
                    ih.bind(mblogidColumn, "-1");
                    ih.bind(accountidColumn, accountId);
                    ih.bind(jsondataColumn, "");
                    ih.bind(groupidColumn, groupId);
                }
                ih.execute();
            }
            getWriteableDataBase().setTransactionSuccessful();
        } catch (SQLException e) {


        } finally {
            getWriteableDataBase().endTransaction();
            ih.close();
        }
        reduceHomeOtherGroupTable(accountId, groupId);
    }

    // this is reduce the other 垃圾数据
    private static void reduceHomeOtherGroupTable(String accountId, String groupId) {
        String searchCount = "select count(" + HomeOtherGroupTable.HomeOtherGroupDataTable.ID
                + ") as total"
                + " from " + HomeOtherGroupTable.HomeOtherGroupDataTable.TABLE_NAME
                + " where " + HomeOtherGroupTable.HomeOtherGroupDataTable.ACCOUNTID
                + " = " + accountId
                + " and " + HomeOtherGroupTable.HomeOtherGroupDataTable.GROUPID
                + " = " + groupId;
        int total = 0;
        Cursor c = getWriteableDataBase().rawQuery(searchCount, null);
        if (c.moveToNext()) {
            total = c.getInt(c.getColumnIndex("total"));
        }

        c.close();


        AppLogger.e("total=" + total);

        int needDeletedNumber = total - XmDataAppConfig.DEFAULT_HOME_DB_CACHE_COUNT;

        if (needDeletedNumber > 0) {
            AppLogger.e("" + needDeletedNumber);
            String sql = " delete from " + HomeOtherGroupTable.HomeOtherGroupDataTable.TABLE_NAME
                    + " where " + HomeOtherGroupTable.HomeOtherGroupDataTable.ID + " in "
                    + "( select " + HomeOtherGroupTable.HomeOtherGroupDataTable.ID + " from "
                    + HomeOtherGroupTable.HomeOtherGroupDataTable.TABLE_NAME + " where "
                    + HomeOtherGroupTable.HomeOtherGroupDataTable.ACCOUNTID
                    + " in " + "(" + accountId + ") "
                    + " and " + HomeOtherGroupTable.HomeOtherGroupDataTable.GROUPID
                    + " = " + groupId
                    + " order by " + HomeOtherGroupTable.HomeOtherGroupDataTable.ID + " desc limit "
                    + needDeletedNumber + " ) ";

            getWriteableDataBase().execSQL(sql);
        }
    }

    static void replace(DataMessageListDomain list, String accountId, String groupId) {

        deleteGroupTimeLine(accountId, groupId);                   // 插入新的分组数据时，先删除掉之前的数据， 在add新的数据信息
        addHomeLineMsg(list, accountId, groupId);
    }

    static void deleteGroupTimeLine(String accountId, String groupId) {
        String sql = "delete from " + HomeOtherGroupTable.HomeOtherGroupDataTable.TABLE_NAME
                + " where " + HomeOtherGroupTable.HomeOtherGroupDataTable.ACCOUNTID + " in " + "("
                + accountId + ")"
                + " and " + HomeOtherGroupTable.HomeOtherGroupDataTable.GROUPID + " = " + groupId;

        getWriteableDataBase().execSQL(sql);
    }

    // 获取db缓存的数据
    static DataMessageGroupDomain getDatGroupMessage(String accountId, String groupId) {
        TimeLinePosition position = getPosition(accountId, groupId);
        DataMessageListDomain msgList = get(accountId, groupId,
                position.position + XmDataAppConfig.DB_CACHE_COUNT_OFFSET);
        return new DataMessageGroupDomain(groupId, msgList, position);
    }

    public static void updatePosition(TimeLinePosition position, String accountId, String groupId) {
        String sql = "select * from " + HomeOtherGroupTable.TABLE_NAME + " where "
                + HomeOtherGroupTable.ACCOUNTID + "  = " +
                accountId + " and " + HomeOtherGroupTable.GROUPID + " = " + groupId;
        Cursor c = getWriteableDataBase().rawQuery(sql, null);
        Gson gson = new Gson();
        if (c.moveToNext()) {
            try {
                String[] args = {accountId, groupId};
                ContentValues cv = new ContentValues();
                cv.put(HomeOtherGroupTable.TIMELINEDATA, gson.toJson(position));
                getReadableDataBase().update(HomeOtherGroupTable.TABLE_NAME, cv,
                        HomeOtherGroupTable.ACCOUNTID + "=? AND " + HomeOtherGroupTable.GROUPID
                                + " =? "
                        , args);
            } catch (JsonSyntaxException e) {

            }
        } else {

            ContentValues cv = new ContentValues();
            cv.put(HomeOtherGroupTable.ACCOUNTID, accountId);
            cv.put(HomeOtherGroupTable.GROUPID, groupId);
            cv.put(HomeOtherGroupTable.TIMELINEDATA, gson.toJson(position));
            getWriteableDataBase().insert(HomeOtherGroupTable.TABLE_NAME,
                    HomeOtherGroupTable.ID, cv);
        }
    }

    static TimeLinePosition getPosition(String accountId, String groupId) {
        String sql = "select * from " + HomeOtherGroupTable.TABLE_NAME + " where "
                + HomeOtherGroupTable.ACCOUNTID + "  = "
                + accountId + " and " + HomeOtherGroupTable.GROUPID + " = " + groupId;
        Cursor c = getReadableDataBase().rawQuery(sql, null);
        Gson gson = new Gson();
        while (c.moveToNext()) {
            String json = c.getString(c.getColumnIndex(HomeOtherGroupTable.TIMELINEDATA));
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

    static DataMessageListDomain get(String accountId, String groupId, int limitCount) {
        Gson gson = new Gson();
        DataMessageListDomain result = new DataMessageListDomain();
        int limit = limitCount > XmDataAppConfig.DEFAULT_MSG_COUNT_50 ? limitCount
                : XmDataAppConfig.DEFAULT_MSG_COUNT_50;
        List<DataMessageDomain> msgList = new ArrayList<DataMessageDomain>();
        String sql = "select * from " + HomeOtherGroupTable.HomeOtherGroupDataTable.TABLE_NAME
                + " where " + HomeOtherGroupTable.HomeOtherGroupDataTable.ACCOUNTID + "  = "
                + accountId + " and " + HomeOtherGroupTable.HomeOtherGroupDataTable.GROUPID + " =  "
                + groupId + " order by " + HomeOtherGroupTable.HomeOtherGroupDataTable.ID
                + " asc limit " + limit;
        Cursor c = getReadableDataBase().rawQuery(sql, null);
        while (c.moveToNext()) {
            String json = c.getString(
                    c.getColumnIndex(HomeOtherGroupTable.HomeOtherGroupDataTable.JSONDATA));
            if (!TextUtils.isEmpty(json)) {
                try {
                    DataMessageDomain value = gson.fromJson(json, DataMessageDomain.class);
//                    if (!value.isMiddleUnreadItem()) {
//                        value.getListViewSpannableString();
//                    }
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
