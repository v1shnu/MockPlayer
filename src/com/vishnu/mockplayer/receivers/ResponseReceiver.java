package com.vishnu.mockplayer.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.vishnu.mockplayer.R;
import com.vishnu.mockplayer.activities.UpdateNotifier;
import com.vishnu.mockplayer.utilities.Utilities;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 4/10/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResponseReceiver extends BroadcastReceiver{
    public static final String ACTION_UPDATE = "com.vishnu.mockplayer.updatebroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        Utilities.log("Update Available : "+intent.getIntExtra("version", 0));
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Mock Player")
                .setContentText("Update Available")
                .setAutoCancel(true);
        Intent resultIntent = new Intent(context, UpdateNotifier.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(UpdateNotifier.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
