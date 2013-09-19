package com.vishnu.mockplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.vishnu.mockplayer.utilities.DatabaseHandler;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/9/13
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class FlowCreatorActivity extends Activity {
    private static final int SELECT_PHOTO = 100;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MockPlayerApplication app = MockPlayerApplication.getInstance();
        String activityName = getIntent().getExtras().getString("activityName");
        app.setMock_name(activityName);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        int mock_id = (int) db.createMock(activityName);
        app.setMock_id(mock_id);
        setTitle(activityName);
        setContentView(R.layout.activity_flow_creator_start);
    }

    public void pickImage(View view) {
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(imagePickerIntent, "Select Picture"), SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Intent storyDefinerIntent = new Intent(this, StoryDefiner.class);
                    storyDefinerIntent.putExtra("image",selectedImage);
                    startActivity(storyDefinerIntent);
                }
        }
    }
}