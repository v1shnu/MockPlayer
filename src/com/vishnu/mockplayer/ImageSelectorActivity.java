package com.vishnu.mockplayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.vishnu.mockplayer.utilities.CustomImageView;
import com.vishnu.mockplayer.utilities.DatabaseHandler;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/9/13
 * Time: 2:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSelectorActivity extends Activity {
    private static final int SELECT_PHOTO = 100;
    private DatabaseHandler db;
    private MockPlayerApplication application;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getApplicationContext());
        application = MockPlayerApplication.getInstance();
        pickImage();
    }

    public void pickImage() {
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
                    int screen_id = db.createScreen(imageReturnedIntent.toString(), application.getMock_id());
                    db.createAction(getIntent().getIntExtra("source_id", 0), getIntent().getFloatExtra("x1", 0), getIntent().getFloatExtra("y1", 0), getIntent().getFloatExtra("x2", 0), getIntent().getFloatExtra("y2", 0), screen_id);
                    storyDefinerIntent.putExtra("source_id", screen_id);
                    storyDefinerIntent.putExtra("image",selectedImage);
                    startActivity(storyDefinerIntent);
                }
        }
    }
}