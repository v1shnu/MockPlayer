package com.vishnu.mockplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.vishnu.mockplayer.utilities.MockPlayerApplication;
import com.vishnu.mockplayer.R;
import com.vishnu.mockplayer.utilities.DatabaseHandler;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 19/9/13
 * Time: 6:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class FirstImageSelector extends Activity {
    private static final int SELECT_PHOTO = 100;
    private MockPlayerApplication application;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        application = MockPlayerApplication.getInstance();
        String activityName = application.getMockName();
        int mock_id = db.createMock(activityName);
        application.setMockId(mock_id);
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
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    int screenId = db.createScreen(selectedImage.toString(), application.getMockId());
                    storyDefinerIntent.putExtra("source", screenId);
                    storyDefinerIntent.putExtra("image",selectedImage);
                    startActivity(storyDefinerIntent);
                }
        }
    }
}