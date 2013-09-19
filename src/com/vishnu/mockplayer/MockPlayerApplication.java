package com.vishnu.mockplayer;

import android.app.Application;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 18/9/13
 * Time: 1:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockPlayerApplication extends Application {
    private static MockPlayerApplication instance = null;

    private int mock_id;
    private String mock_name;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MockPlayerApplication getInstance() {
        return instance;
    }

    public static void setInstance(MockPlayerApplication instance) {
        MockPlayerApplication.instance = instance;
    }

    public int getMock_id() {
        return mock_id;
    }

    public void setMock_id(int mock_id) {
        this.mock_id = mock_id;
    }

    public String getMock_name() {
        return mock_name;
    }

    public void setMock_name(String mock_name) {
        this.mock_name = mock_name;
    }
}
