package com.vishnu.mockplayer.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.vishnu.mockplayer.models.Mock;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 16/9/13
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBController {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public final static String MOCK_TABLE = "mocks";
    public final static String MOCK_ID = "id";
    public final static String MOCK_NAME = "name";

    public DBController(Context context) {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long createMock(String id, String name) {
        ContentValues values = new ContentValues();
        values.put(MOCK_ID, id);
        values.put(MOCK_NAME, name);
        return database.insert(MOCK_TABLE, null, values);
    }

    public Mock selectMock(int id) {
        String[] columns = new String[] {MOCK_ID, MOCK_NAME};
        Cursor cursor = database.query(true, MOCK_TABLE, columns, MOCK_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
            return new Mock(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        }
        return null;
    }
    public ArrayList<Mock> selectAllMocks() {
        ArrayList <Mock> mockList = new ArrayList<Mock>();
        String selectQuery = "SELECT * FROM "+MOCK_TABLE;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            do {
                cursor.moveToFirst();
                mockList.add(new Mock(Integer.parseInt(cursor.getString(0)), cursor.getString(1)));
            } while(cursor.moveToNext());
        }
        return mockList;
    }
}
