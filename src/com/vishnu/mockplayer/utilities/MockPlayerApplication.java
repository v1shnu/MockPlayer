package com.vishnu.mockplayer.utilities;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import com.vishnu.mockplayer.receivers.ResponseReceiver;
import com.vishnu.mockplayer.services.UpdateChecker;

public class MockPlayerApplication extends Application {
    private static MockPlayerApplication instance = null;

    private int mockId;
    private String mockName;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Intent updateChecker = new Intent(this, UpdateChecker.class);
        startService(updateChecker);
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
