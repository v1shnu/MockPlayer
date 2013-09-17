package com.vishnu.mockplayer.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.vishnu.mockplayer.models.Mock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 16/9/13
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHandler {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public final static String MOCK_TABLE = "mocks";
    public final static String MOCK_ID = "_id";
    public final static String MOCK_NAME = "name";
    public final static String MOCK_TIMESTAMP = "timestamp";
    SimpleDateFormat dateFormatter = new SimpleDateFormat("ddMMyyyyHHmmsss");

    public DatabaseHandler(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createMock(String name) {
        ContentValues values = new ContentValues();
        values.put(MOCK_NAME, name);
        values.put(MOCK_TIMESTAMP, dateFormatter.format(new Date()));
        return database.insert(MOCK_TABLE, null, values);
    }

    public Mock selectMockById(int id) {
        String[] columns = new String[] {MOCK_ID, MOCK_NAME, MOCK_TIMESTAMP};
        Cursor cursor = database.query(true, MOCK_TABLE, columns, MOCK_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            return new Mock(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        }
        return null;
    }

    public Mock selectMockByName(String name) {
        String[] columns = new String[] {MOCK_ID, MOCK_NAME, MOCK_TIMESTAMP};
        Cursor cursor = database.query(true, MOCK_TABLE, columns, MOCK_NAME+ "=?", new String[] {name}, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            return new Mock(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
        }
        return null;
    }

    public Cursor selectAllMocks() {
        String selectQuery = "SELECT * FROM "+MOCK_TABLE;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    public ArrayList<Mock> selectAllMocksAsList() {
        ArrayList <Mock> mockList = new ArrayList<Mock>();
        String selectQuery = "SELECT * FROM "+MOCK_TABLE;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                cursor.moveToFirst();
                mockList.add(new Mock(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2)));
            } while(cursor.moveToNext());
        }
        return mockList;
    }
}
