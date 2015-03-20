package wm.xmwei.datadao.dbway;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import wm.xmwei.bean.UserBingDomain;
import wm.xmwei.bean.UserDomain;
import wm.xmwei.core.database.DataBaseHelper;
import wm.xmwei.core.database.table.UserBingDomainTable;
import wm.xmwei.core.debug.AppLogger;
import wm.xmwei.ui.activity.login.XmSoLoginActivity;

/**
 * Created by
 */
public class DbUserBingTask {


    private DbUserBingTask() {

    }

    private static SQLiteDatabase getWriteableDataBase() {
        DataBaseHelper databaseHelper = DataBaseHelper.getInstance();
        return databaseHelper.getWritableDatabase();
    }

    private static SQLiteDatabase getReadableDataBase() {
        DataBaseHelper databaseHelper = DataBaseHelper.getInstance();
        return databaseHelper.getReadableDatabase();
    }

    public static XmSoLoginActivity.OauthDbResult addOrUpdateBingInfo(UserBingDomain account, boolean blackMagic) {
        ContentValues cv = new ContentValues();
        cv.put(UserBingDomainTable.UID, account.getUid());
        cv.put(UserBingDomainTable.OAUTH_TOKEN, account.getAccess_token());
        cv.put(UserBingDomainTable.OAUTH_TOKEN_EXPIRES_TIME, String.valueOf(account.getExpires_time()));
        cv.put(UserBingDomainTable.BLACK_MAGIC, blackMagic);

        String json = new Gson().toJson(account.getInfo());
        cv.put(UserBingDomainTable.INFOJSON, json);

        Cursor c = getWriteableDataBase().query(UserBingDomainTable.TABLE_NAME, null, UserBingDomainTable.UID + "=?",
                new String[]{account.getUid()}, null, null, null);

        if (c != null && c.getCount() > 0) {
            String[] args = {account.getUid()};
            getWriteableDataBase().update(UserBingDomainTable.TABLE_NAME, cv, UserBingDomainTable.UID + "=?", args);
            return XmSoLoginActivity.OauthDbResult.update_successfully;
        } else {
            getWriteableDataBase().insert(UserBingDomainTable.TABLE_NAME,
                    UserBingDomainTable.UID, cv);
            return XmSoLoginActivity.OauthDbResult.add_successfuly;
        }
    }

    public static List<UserBingDomain> getUserBingList() {
        List<UserBingDomain> accountList = new ArrayList<UserBingDomain>();
        String sql = "select * from " + UserBingDomainTable.TABLE_NAME;
        Cursor c = getWriteableDataBase().rawQuery(sql, null);
        while (c.moveToNext()) {
            UserBingDomain account = new UserBingDomain();
            int colid = c.getColumnIndex(UserBingDomainTable.OAUTH_TOKEN);
            account.setAccess_token(c.getString(colid));

            colid = c.getColumnIndex(UserBingDomainTable.OAUTH_TOKEN_EXPIRES_TIME);
            account.setExpires_time(Long.valueOf(c.getString(colid)));

            colid = c.getColumnIndex(UserBingDomainTable.BLACK_MAGIC);
            account.setBlack_magic(c.getInt(colid) == 1);

            colid = c.getColumnIndex(UserBingDomainTable.NAVIGATION_POSITION);
            account.setNavigationPosition(c.getInt(colid));

            Gson gson = new Gson();
            String json = c.getString(c.getColumnIndex(UserBingDomainTable.INFOJSON));
            try {
                UserDomain value = gson.fromJson(json, UserDomain.class);
                account.setInfo(value);
            } catch (JsonSyntaxException e) {
                AppLogger.e(e.getMessage());
            }

            accountList.add(account);
        }
        c.close();
        return accountList;
    }


}
