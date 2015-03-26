package wm.xmwei.core.database;

import wm.xmwei.core.database.table.GroupTable;
import wm.xmwei.core.database.table.HomeMessageTable;
import wm.xmwei.core.database.table.HomeOtherGroupTable;
import wm.xmwei.core.database.table.UserBingDomainTable;
import wm.xmwei.core.database.table.UserDomainTable;

/**
 * Created by wm on 15-3-19.
 */
public class DbConstants {

    // 用户绑定 表创建字段
    static final String CREATE_BING_TABLE_SQL = "create table " + UserBingDomainTable.TABLE_NAME
            + "("
            + UserBingDomainTable.UID + " integer primary key autoincrement,"
            + UserBingDomainTable.OAUTH_TOKEN + " text,"
            + UserBingDomainTable.OAUTH_TOKEN_EXPIRES_TIME + " text,"
            + UserBingDomainTable.OAUTH_TOKEN_SECRET + " text,"
            + UserBingDomainTable.BLACK_MAGIC + " boolean,"
            + UserBingDomainTable.NAVIGATION_POSITION + " integer,"
            + UserBingDomainTable.INFOJSON + " text"
            + ");";

    // 用户信息
    public static final String CREATE_ATUSERS_TABLE_SQL = "create table " + UserDomainTable.TABLE_NAME
            + "("
            + UserDomainTable.ID + " integer,"
            + UserDomainTable.USERID + " text,"
            + UserDomainTable.ACCOUNTID + " text,"
            + UserDomainTable.JSONDATA + " text,"
            + "primary key (" + UserDomainTable.USERID + "," + UserDomainTable.ACCOUNTID + ")"
            + ");";

    static final String CREATE_GROUP_TABLE_SQL = "create table " + GroupTable.TABLE_NAME
            + "("
            + GroupTable.ID + " integer primary key autoincrement,"
            + GroupTable.ACCOUNTID + " text,"
            + GroupTable.JSONDATA + " text"
            + ");";

    static final String CREATE_HOME_TABLE_SQL = "create table " + HomeMessageTable.TABLE_NAME
            + "("
            + HomeMessageTable.ID + " integer primary key autoincrement,"
            + HomeMessageTable.ACCOUNTID + " text,"
            + HomeMessageTable.TIMELINEDATA + " text,"
            + HomeMessageTable.RECENT_GROUP_ID + " text"
            + ");";

    static final String CREATE_HOME_DATA_TABLE_SQL = "create table "
            + HomeMessageTable.HomeDataTable.TABLE_NAME
            + "("
            + HomeMessageTable.HomeDataTable.ID + " integer primary key autoincrement,"
            + HomeMessageTable.HomeDataTable.ACCOUNTID + " text,"
            + HomeMessageTable.HomeDataTable.MBLOGID + " text,"
            + HomeMessageTable.HomeDataTable.JSONDATA + " text"
            + ");";

    static final String CREATE_HOME_OTHER_GROUP_TABLE_SQL = "create table "
            + HomeOtherGroupTable.TABLE_NAME
            + "("
            + HomeOtherGroupTable.ID + " integer primary key autoincrement,"
            + HomeOtherGroupTable.ACCOUNTID + " text,"
            + HomeOtherGroupTable.GROUPID + " text,"
            + HomeOtherGroupTable.TIMELINEDATA + " text"
            + ");";

    static final String CREATE_HOME_OTHER_GROUP_DATA_TABLE_SQL = "create table "
            + HomeOtherGroupTable.HomeOtherGroupDataTable.TABLE_NAME
            + "("
            + HomeOtherGroupTable.HomeOtherGroupDataTable.ID + " integer primary key autoincrement,"
            + HomeOtherGroupTable.HomeOtherGroupDataTable.ACCOUNTID + " text,"
            + HomeOtherGroupTable.HomeOtherGroupDataTable.MBLOGID + " text,"
            + HomeOtherGroupTable.HomeOtherGroupDataTable.GROUPID + " text,"
            + HomeOtherGroupTable.HomeOtherGroupDataTable.JSONDATA + " text"
            + ");";


}
