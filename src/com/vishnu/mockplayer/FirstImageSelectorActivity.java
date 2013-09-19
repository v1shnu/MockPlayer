package com.vishnu.mockplayer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.vishnu.mockplayer.utilities.DatabaseHandler;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 19/9/13
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class FirstImageSelectorActivity extends Activity {
    private static final int SELECT_PHOTO = 100;
    private DatabaseHandler db;
    private MockPlayerApplication application;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getApplicationContext());
        application = MockPlayerApplication.getInstance();
        String activityName = application.getMock_name();
        int mock_id = db.createMock(activityName);
        application.setMock_id(mock_id);
        setTitle(activityName);
        setContentView(R.layout.first_image_picker);
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
                    int screen_id = db.createScreen(selectedImage.toString(), application.getMock_id());
                    storyDefinerIntent.putExtra("source_id", screen_id);
                    storyDefinerIntent.putExtra("image",selectedImage);
                    startActivity(storyDefinerIntent);
                }
        }
    }
}