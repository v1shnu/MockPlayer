package com.vishnu.mockplayer.utilities;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.vishnu.mockplayer.receivers.ResponseReceiver;
import com.vishnu.mockplayer.receivers.UpdatesReceiver;
import com.vishnu.mockplayer.services.UpdateChecker;

import java.util.Calendar;

public class MockPlayerApplication extends Application {
    private static MockPlayerApplication instance = null;

    private int mockId;
    private String mockName;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        scheduleUpdateChecker();
    }

    private void scheduleUpdateChecker() {
        Intent alarmIntent = new Intent(getApplicationContext(), UpdatesReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar timer = Calendar.getInstance();
        timer.set(Calendar.HOUR_OF_DAY, 12);
        timer.set(Calendar.MINUTE, 0);
        timer.set(Calendar.SECOND, 0);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC, timer.getTimeInMillis(), 12*60*60*1000, pendingIntent);
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
