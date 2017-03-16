package com.onyx.gitdev;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by onyekaanene on 15/03/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "bdamobile.db";
    private static final int DATABASE_VERSION = 1;
    static String DEVELOPERS = "Developers";

    private static String TAG = DatabaseHelper.class.getName();
    private final Context mContext;

    final static String SQL_CREATE_DEVELOPER_TABLE = "CREATE TABLE IF NOT EXISTS "
            + DEVELOPERS + "("
            + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DataContract.DeveloperColumns.USER_NAME + " VARCHAR NOT NULL,"
            + DataContract.DeveloperColumns.IMAGE + " BLOB NOT NULL,"
            + DataContract.DeveloperColumns.URL + " VARCHAR NOT NULL "
            + ")";
    public static final String DELETE_TABLE="DROP TABLE IF EXISTS " + DEVELOPERS;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        /* Create developer table */
        db.execSQL(SQL_CREATE_DEVELOPER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        //Drop older table if existed
        db.execSQL(DELETE_TABLE);
        //Create tables again
        onCreate(db);
    }
}
