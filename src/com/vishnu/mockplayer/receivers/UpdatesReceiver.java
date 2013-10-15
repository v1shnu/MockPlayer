package com.vishnu.mockplayer.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.vishnu.mockplayer.services.UpdateChecker;
import com.vishnu.mockplayer.utilities.Utilities;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 15/10/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdatesReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Utilities.log("Checking for updates...");
        Intent updateChecker = new Intent(context, UpdateChecker.class);
        context.startService(updateChecker);
    }
}
