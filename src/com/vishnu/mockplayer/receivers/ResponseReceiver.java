package com.vishnu.mockplayer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.vishnu.mockplayer.utilities.Utilities;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 4/10/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResponseReceiver extends BroadcastReceiver {
    public static final String ACTION_UPDATE = "com.vishnu.mockplayer.updatebroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        Utilities.log("Received Data : "+intent.getStringExtra("message"));
    }
}
