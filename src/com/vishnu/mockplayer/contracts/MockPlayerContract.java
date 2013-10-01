package com.vishnu.mockplayer.contracts;

import android.provider.BaseColumns;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 1/10/13
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockPlayerContract {

    public MockPlayerContract() {}

    public static abstract class MockEntry implements BaseColumns {
        public static final String TABLE_NAME = "mocks";
        public static final String COLUMN_NAME_ENTRY_NAME = "name";
        public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    }

    public static abstract class ScreenEntry implements BaseColumns {
        public static final String TABLE_NAME = "screens";
        public static final String COLUMN_NAME_URI = "uri";
        public static final String COLUMN_NAME_MOCK_ID = "mock_id";
    }

    public static abstract class ActionEntry implements BaseColumns {
        public static final String TABLE_NAME = "actions";
        public static final String COLUMN_NAME_SOURCE_ID = "source_id";
        public static final String COLUMN_NAME_X1 = "x1";
        public static final String COLUMN_NAME_Y1 = "y1";
        public static final String COLUMN_NAME_X2 = "x2";
        public static final String COLUMN_NAME_Y2 = "y2";
        public static final String COLUMN_NAME_MENU_BUTTON = "menuButton";
        public static final String COLUMN_NAME_BACK_BUTTON = "backButton";
        public static final String COLUMN_NAME_DESTINATION_ID = "destination_id";
    }
}
