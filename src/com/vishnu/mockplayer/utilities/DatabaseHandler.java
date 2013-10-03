package com.vishnu.mockplayer.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.vishnu.mockplayer.models.Action;
import com.vishnu.mockplayer.models.Screen;
import static com.vishnu.mockplayer.contracts.MockPlayerContract.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHandler {
    private DatabaseHelper dbHelper;

    public DatabaseHandler(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public int createMock(String name) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MockEntry.COLUMN_NAME_ENTRY_NAME, name);
        values.put(MockEntry.COLUMN_NAME_TIMESTAMP, java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
        int mockId = (int) database.insert(MockEntry.TABLE_NAME, null, values);
        database.close();
        return mockId;
    }

    public int createScreen(String uri, int mockId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ScreenEntry.COLUMN_NAME_URI, uri);
        values.put(ScreenEntry.COLUMN_NAME_MOCK_ID, mockId);
        int screenId = (int) database.insert(ScreenEntry.TABLE_NAME, null, values);
        database.close();
        return screenId;
    }

    public int createAction(int source, float x1, float y1, float x2, float y2, String menuButton, String backButton, int destination) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ActionEntry.COLUMN_NAME_SOURCE_ID, source);
        values.put(ActionEntry.COLUMN_NAME_X1, x1);
        values.put(ActionEntry.COLUMN_NAME_Y1, y1);
        values.put(ActionEntry.COLUMN_NAME_X2, x2);
        values.put(ActionEntry.COLUMN_NAME_Y2, y2);
        values.put(ActionEntry.COLUMN_NAME_MENU_BUTTON, menuButton);
        values.put(ActionEntry.COLUMN_NAME_BACK_BUTTON, backButton);
        values.put(ActionEntry.COLUMN_NAME_DESTINATION_ID, destination);
        int action_id = (int) database.insert(ActionEntry.TABLE_NAME, null, values);
        database.close();
        return action_id;
    }

    public List<Action> getHotSpots(int screenId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ArrayList<Action> hotspots = new ArrayList<Action>();
        String[] columns = new String[] {
                ActionEntry.COLUMN_NAME_X1,
                ActionEntry.COLUMN_NAME_Y1,
                ActionEntry.COLUMN_NAME_X2,
                ActionEntry.COLUMN_NAME_Y2,
                ActionEntry.COLUMN_NAME_MENU_BUTTON,
                ActionEntry.COLUMN_NAME_BACK_BUTTON,
                ActionEntry.COLUMN_NAME_DESTINATION_ID
        };
        Cursor cursor = database.query(true, ActionEntry.TABLE_NAME, columns, ActionEntry.COLUMN_NAME_SOURCE_ID + " =? ", new String[]{String.valueOf(screenId)}, null, null, null, null);
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

    public Screen selectFirstScreen(int mockId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] columns = new String[] {
                ScreenEntry._ID,
                ScreenEntry.COLUMN_NAME_URI
        };
        Cursor cursor = database.query(true, ScreenEntry.TABLE_NAME, columns, ScreenEntry.COLUMN_NAME_MOCK_ID+ " =? ", new String[] {String.valueOf(mockId)}, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            database.close();
            return new Screen(Integer.parseInt(cursor.getString(0)), Uri.parse(cursor.getString(1)));
        }
        return null;
    }

    public Screen selectScreenById(int screenId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] columns = new String[] {
                ScreenEntry._ID,
                ScreenEntry.COLUMN_NAME_URI
        };
        Cursor cursor = database.query(true, ScreenEntry.TABLE_NAME, columns, ScreenEntry._ID+ " =? ", new String[] {String.valueOf(screenId)}, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            database.close();
            return new Screen(Integer.parseInt(cursor.getString(0)), Uri.parse(cursor.getString(1)));
        }
        return null;
    }

    public Cursor queryForMocks(String query) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM "+MockEntry.TABLE_NAME+" WHERE "+MockEntry.COLUMN_NAME_ENTRY_NAME+" LIKE \""+query+"%\"";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()) {
            database.close();
            return cursor;
        }
        return null;
    }

    public void deleteMock(int mockId) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(MockEntry.TABLE_NAME, MockEntry._ID+"="+mockId, null);
        database.close();
    }
}
