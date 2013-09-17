package com.vishnu.mockplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.vishnu.mockplayer.utilities.DatabaseHandler;
import com.vishnu.mockplayer.utilities.DatabaseHelper;
import com.vishnu.mockplayer.utilities.Utilities;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 16/9/13
 * Time: 1:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlowNamingActivity extends Activity {
    SharedPreferences preferences;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        setContentView(R.layout.activity_flow_naming);
    }

    public void startFlowCreation(View view) {
        EditText flowName = (EditText) findViewById(R.id.activity_name);
        String activityName = flowName.getText().toString();
        preferences.edit().putString(getString(R.string.flow_name), activityName).commit();
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long id = db.createMock(activityName);
        Intent intent = new Intent(this, FlowCreatorActivity.class);
        startActivity(intent);
    }
}