package com.vishnu.mockplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.vishnu.mockplayer.R;
import com.vishnu.mockplayer.services.UpdateChecker;
import com.vishnu.mockplayer.utilities.Utilities;

public class MainActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent updateChecker = new Intent(this, UpdateChecker.class);
        startService(updateChecker);
    }

    public void createFlow(View view) {
        FlowNamingDialog flowNamingDialog = new FlowNamingDialog(this);
        flowNamingDialog.show(getFragmentManager(), "flowNamer");
    }

    public void playFlow(View view) {
        Intent intent = new Intent(this, ListOfFlows.class);
        startActivity(intent);
    }
}
