package com.vishnu.mockplayer.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
        Utilities.log("Update Service Started");
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

    private class CheckLatestVersionAvailable extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                return fetchContent(params[0]);
            } catch(IOException error) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Utilities.log("Latest Version Available : "+result);
            stopSelf();
        }
    }
}
