package com.vishnu.mockplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_definer);
        com.vishnu.mockplayer.utilities.Utilities.displayToast(getApplicationContext(), "Click the portion of the image you want to assign a task to");
        Uri receivedImage = getIntent().getParcelableExtra("image");
        Bitmap image = com.vishnu.mockplayer.utilities.Utilities.convertUriToImage(getApplicationContext(), receivedImage);
        CustomImageView imageView = (CustomImageView) findViewById(R.id.imageView);
        Utilities.displayToast(getApplicationContext(), "Image displayed");
        imageView.setImageBitmap(image);
    }
}