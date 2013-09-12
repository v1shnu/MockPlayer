package com.vishnu.mockplayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void createFlow(View view) {
        Intent intent = new Intent(this, FlowCreatorActivity.class);
        startActivity(intent);
    }

    public void playFlow(View view) {
        Intent intent = new Intent(this, FlowPlayerActivity.class);
        startActivity(intent);
    }
}
