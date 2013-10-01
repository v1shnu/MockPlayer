package com.vishnu.mockplayer.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.vishnu.mockplayer.models.Action;
import com.vishnu.mockplayer.models.Screen;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 16/9/13
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseHandler {
    private DatabaseHelper dbHelper;
    public final static String MOCK_TABLE = "mocks";
    public final static String MOCK_ID = "_id";
    public final static String MOCK_NAME = "name";
    public final static String MOCK_TIMESTAMP = "timestamp";
    public final static String SCREEN_TABLE = "screens";
    public final static String SCREEN_ID = "_id";
    public final static String SCREEN_URI = "uri";
    public final static String SCREEN_MOCK_ID = "mock_id";
    public final static String ACTIONS_TABLE = "actions";
    public final static String SOURCE_ID = "source_id";
    public final static String DESTINATION_ID = "destination_id";

    public DatabaseHandler(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public int createMock(String name) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MOCK_NAME, name);
        values.put(MOCK_TIMESTAMP, java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        int mockId = (int) database.insert(MOCK_TABLE, null, values);
        database.close();
        return mockId;
    }

    public int createScreen(String uri, int mock_id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Utilities.log("Inserting URI : "+uri);
        ContentValues values = new ContentValues();
        values.put(SCREEN_URI, uri);
        values.put(SCREEN_MOCK_ID, mock_id);
        int screenId = (int) database.insert(SCREEN_TABLE, null, values);
        database.close();
        return screenId;
    }

    public int createAction(int source_id, float x1, float y1, float x2, float y2, String menuButton, String backButton, int destination_id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SOURCE_ID, source_id);
        values.put("x1", x1);
        values.put("y1", y1);
        values.put("x2", x2);
        values.put("y2", y2);
        values.put("menuButton", menuButton);
        values.put("backButton", backButton);
        values.put(DESTINATION_ID, destination_id);
        int action_id = (int) database.insert(ACTIONS_TABLE, null, values);
        database.close();
        return action_id;
    }

    public ArrayList<Action> getHotSpots(int screen_id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<Action> hotspots = new ArrayList<Action>();
        String[] columns = new String[] {"x1", "y1", "x2", "y2", "menuButton", "backButton", DESTINATION_ID};
        Cursor cursor = database.query(true, ACTIONS_TABLE, columns, SOURCE_ID + " =? ", new String[]{String.valueOf(screen_id)}, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                hotspots.add(new Action(Float.parseFloat(cursor.getString(0)), Float.parseFloat(cursor.getString(1)), Float.parseFloat(cursor.getString(2)), Float.parseFloat(cursor.getString(3)), Boolean.parseBoolean(cursor.getString(4)), Boolean.parseBoolean(cursor.getString(5)), Integer.parseInt(cursor.getString(6))));
            } while(cursor.moveToNext());
            database.close();
            return hotspots;
        }
        database.close();
        return null;
    }

    public Screen selectFirstScreen(int mock_id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] columns = new String[] {SCREEN_ID, SCREEN_URI};
        Cursor cursor = database.query(true, SCREEN_TABLE, columns, SCREEN_MOCK_ID+ " =? ", new String[] {String.valueOf(mock_id)}, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            database.close();
            return new Screen(Integer.parseInt(cursor.getString(0)), Uri.parse(cursor.getString(1)));
        }
        return null;
    }

    public Screen selectScreenById(int screen_id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] columns = new String[] {SCREEN_ID, SCREEN_URI};
        Cursor cursor = database.query(true, SCREEN_TABLE, columns, SCREEN_ID+ " =? ", new String[] {String.valueOf(screen_id)}, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            database.close();
            return new Screen(Integer.parseInt(cursor.getString(0)), Uri.parse(cursor.getString(1)));
        }
        return null;
    }

    public Cursor queryForMocks(String query) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+MOCK_TABLE+" WHERE name LIKE \""+query+"%\"";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            database.close();
            return cursor;
        }
        return null;
    }

    public void deleteMock(int id) {
        String deleteQuery = "DELETE FROM "+MOCK_TABLE+" WHERE "+MOCK_ID+"="+id;
        Utilities.log(deleteQuery);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        boolean result = database.delete(MOCK_TABLE, MOCK_ID+"="+id, null)>0;
        Utilities.log(String.valueOf(result));
        database.close();
    }
}
