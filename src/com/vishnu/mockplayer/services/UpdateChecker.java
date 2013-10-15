package com.vishnu.mockplayer.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import com.vishnu.mockplayer.receivers.ResponseReceiver;
import com.vishnu.mockplayer.utilities.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker extends IntentService {

    private static final String server = "http://v1shnu.net/mockplayer/latest-version/";

    public UpdateChecker() {
        super("UpdateCheckerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(connectedToNetwork())
            new CheckLatestVersionAvailable().execute(server);
        else Utilities.log("Not connected to network.");
    }

    public boolean connectedToNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected())
            return true;
        else return false;
    }

    private String fetchContent(String address) throws IOException {
        InputStream inputStream = null;
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            inputStream = connection.getInputStream();
            String content = Utilities.convertInputStreamToString(inputStream);
            return content;
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
        }
    }

    private class CheckLatestVersionAvailable extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            try {
                return Integer.parseInt(fetchContent(params[0]));
            } catch(IOException error) {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer latestVersion) {
            int currentVersion = 0;
            try {
                currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Utilities.log("Current Version : "+currentVersion);
            Utilities.log("Latest Version Available : "+latestVersion);
            if(latestVersion > currentVersion) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(ResponseReceiver.ACTION_UPDATE);
                broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
                broadcastIntent.putExtra("version", latestVersion);
                sendBroadcast(broadcastIntent);
            }
            else Utilities.log("No update available");
            stopSelf();
        }
    }
}
