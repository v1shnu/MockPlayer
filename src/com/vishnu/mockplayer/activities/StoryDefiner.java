package com.vishnu.mockplayer.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.vishnu.mockplayer.utilities.MockPlayerApplication;
import com.vishnu.mockplayer.R;
import com.vishnu.mockplayer.utilities.CustomImageView;
import com.vishnu.mockplayer.utilities.DatabaseHandler;
import com.vishnu.mockplayer.utilities.Utilities;

import java.io.InputStream;

public class StoryDefiner extends Activity {

    private static final int SELECT_PHOTO = 100;
    private MockPlayerApplication application;
    private static boolean menuButton = false;
    private static boolean backButton = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_definer);
        application = MockPlayerApplication.getInstance();
        menuButton = false;
        backButton = false;
        Utilities.displayToast(getApplicationContext(), "Select the portion of the image you want to assign a task to");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setImage();
    }

    public void setImage() {
        Uri sourceImage = getIntent().getParcelableExtra("image");
        CustomImageView imageView = (CustomImageView) findViewById(R.id.imageView);
        InputStream stream = Utilities.convertUriToStream(getApplicationContext(), sourceImage);
        Bitmap image = Utilities.decodeSampledBitmapFromStream(stream, imageView.getWidth(), imageView.getHeight());
        imageView.setImageBitmap(image);
    }

    public void assignTaskToMenuButton(View view) {
        menuButton = true;
        pickImage();
    }

    public void assignTaskToBackButton(View view) {
        backButton = true;
        pickImage();
    }

    public void assignTaskToSelectedPortion(View view) {
        pickImage();
    }

    public void pickImage() {
        Utilities.displayToast(getApplicationContext(), "Selected the mock to be linked");
        Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
        imagePickerIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(imagePickerIntent, "Select Picture"), SELECT_PHOTO);
    }

    public void saveFlow(View view) {
        Utilities.displayToast(getApplicationContext(), "Flow saved :)");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri sourceImage= imageReturnedIntent.getData();
                    CustomImageView imageView = (CustomImageView) findViewById(R.id.imageView);
                    //Push the selected image into screens
                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    int destination = db.createScreen(sourceImage.toString(), application.getMockId());
                    //Push the selected co-ordinates, source and destination into action
                    RectF selectedCoordinates = imageView.getCoordinates();
                    db.createAction(getIntent().getIntExtra("source", 0), selectedCoordinates.left, selectedCoordinates.top, selectedCoordinates.right, selectedCoordinates.bottom, String.valueOf(menuButton), String.valueOf(backButton), destination);

                    Intent storyDefinerIntent = new Intent(this, StoryDefiner.class);
                    storyDefinerIntent.putExtra("source", destination);
                    storyDefinerIntent.putExtra("image",sourceImage);
                    startActivity(storyDefinerIntent);
                }
        }
    }
}