package com.vishnu.mockplayer.utilities;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import static com.vishnu.mockplayer.contracts.MockPlayerContract.*;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 16/9/13
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "MockPlayerDB";
    private static final int DATABASE_VERSION = 2;
    private static final String CREATE_MOCKS_TABLE = "create table if not exists "+ MockEntry.TABLE_NAME+" ( "+ MockEntry._ID+" integer primary key autoincrement not null, "+ MockEntry.COLUMN_NAME_ENTRY_NAME+" text not null, "+ MockEntry.COLUMN_NAME_TIMESTAMP+" text not null);";
    private static final String CREATE_SCREENS_TABLE = "create table if not exists "+ ScreenEntry.TABLE_NAME+" ( "+ ScreenEntry._ID+" integer primary key autoincrement not null, "+ ScreenEntry.COLUMN_NAME_URI+" text not null, "+ ScreenEntry.COLUMN_NAME_MOCK_ID+" integer not null);";
    private static final String CREATE_ACTIONS_TABLE = "create table if not exists "+ActionEntry.TABLE_NAME+" ( "+ActionEntry._ID+" integer primary key autoincrement not null, "+ActionEntry.COLUMN_NAME_SOURCE_ID+" integer not null, "+ActionEntry.COLUMN_NAME_X1+" real not null, "+ActionEntry.COLUMN_NAME_Y1+" real not null, "+ActionEntry.COLUMN_NAME_X2+" real not null, "+ActionEntry.COLUMN_NAME_Y2+" real not null, "+ActionEntry.COLUMN_NAME_MENU_BUTTON+" text not null, "+ActionEntry.COLUMN_NAME_BACK_BUTTON+" text not null, "+ActionEntry.COLUMN_NAME_DESTINATION_ID+" integer not null);";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MOCKS_TABLE);
        db.execSQL(CREATE_SCREENS_TABLE);
        db.execSQL(CREATE_ACTIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Utilities.log("Upgrading database from version " + oldVersion + " to "+ newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS "+DATABASE_NAME);
        onCreate(db);
    }
}
