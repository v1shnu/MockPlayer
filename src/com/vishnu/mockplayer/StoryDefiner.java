package com.vishnu.mockplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.vishnu.mockplayer.utilities.CustomImageView;
import com.vishnu.mockplayer.utilities.Utilities;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/9/13
 * Time: 3:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoryDefiner extends Activity {

    Uri sourceImage;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_definer);
        Utilities.displayToast(getApplicationContext(), "Click the portion of the image you want to assign a task to");
        sourceImage = getIntent().getParcelableExtra("image");
        Bitmap image = com.vishnu.mockplayer.utilities.Utilities.convertUriToImage(getApplicationContext(), sourceImage);
        CustomImageView imageView = (CustomImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);
    }

    public void assignTaskToSelectedPortion(View view) {
        CustomImageView imageView = (CustomImageView) findViewById(R.id.imageView);
        Utilities.displayToast(getApplicationContext(), "Selected the mock to be linked");
        Intent intent = new Intent(this, ImageSelectorActivity.class);
        intent.putExtra("action", true);
        intent.putExtra("source", sourceImage);
        intent.putExtra("x1", imageView.startX);
        intent.putExtra("y1", imageView.startY);
        intent.putExtra("x2", imageView.endX);
        intent.putExtra("y2", imageView.endY);
        startActivity(intent);
    }
}