package wm.xmwei.core.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import wm.xmwei.XmApplication;

/**
 *
 *
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static DataBaseHelper dataBaseHelper = null;

    private static final String DATABASE_NAME = "xmWei.db";

    private static final int DATABASE_VERSION = 1;

    public static DataBaseHelper getInstance() {
        if (dataBaseHelper == null) {
            dataBaseHelper = new DataBaseHelper(XmApplication.getInstance());
        }
        return dataBaseHelper;
    }

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstants.CREATE_BING_TABLE_SQL);

        db.execSQL(DbConstants.CREATE_ATUSERS_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
