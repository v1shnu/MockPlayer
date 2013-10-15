package com.vishnu.mockplayer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.vishnu.mockplayer.R;
import com.vishnu.mockplayer.utilities.Utilities;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 4/10/13
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateNotifier extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayUpdateAvailableDialog();
    }

    public void displayUpdateAvailableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.update_available)
                .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = getString(R.string.download_url);
                        Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(openBrowser);
                        finish();
                    }
                })
                .setNegativeButton(R.string.ignore, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.create().show();
    }
}