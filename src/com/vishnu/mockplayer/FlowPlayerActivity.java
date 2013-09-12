package com.vishnu.mockplayer;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/9/13
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlowPlayerActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utilities.displayToast(getApplicationContext(), "FlowPlayer entered");
        setContentView(R.layout.activity_flow_player);
    }
}