package com.vishnu.mockplayer.utilities;

import android.app.Application;

public class MockPlayerApplication extends Application {
    private static MockPlayerApplication instance = null;

    private int mockId;
    private String mockName;

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

    public int getMockId() {
        return mockId;
    }

    public void setMockId(int mockId) {
        this.mockId = mockId;
    }

    public String getMockName() {
        return mockName;
    }

    public void setMockName(String mockName) {
        this.mockName = mockName;
    }
}
