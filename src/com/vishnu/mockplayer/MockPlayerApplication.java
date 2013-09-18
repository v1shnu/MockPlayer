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

}
