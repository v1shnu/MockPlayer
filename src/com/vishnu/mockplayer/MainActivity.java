package com.vishnu.mockplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
