package wm.xmwei.core.database;

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

}
