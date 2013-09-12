package com.vishnu.mockplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
        Utilities.displayToast(getApplicationContext(), "StoryDefiner");
        Uri receivedImage = getIntent().getParcelableExtra("image");
        Bitmap image = Utilities.convertUriToImage(getApplicationContext(), receivedImage);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(image);
    }
}