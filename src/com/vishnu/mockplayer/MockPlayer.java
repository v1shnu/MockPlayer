package com.vishnu.mockplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import com.vishnu.mockplayer.utilities.CustomImageView;
import com.vishnu.mockplayer.utilities.DatabaseHandler;
import com.vishnu.mockplayer.utilities.Utilities;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 19/9/13
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockPlayer extends Activity {

    DatabaseHandler db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mock_player);
        db = new DatabaseHandler(getApplicationContext());
        int mock_id = getIntent().getIntExtra("mock_id", 0);
        Uri firstScreen = db.selectFirstScreen(mock_id);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap image = com.vishnu.mockplayer.utilities.Utilities.convertUriToImage(getApplicationContext(), firstScreen);
        imageView.setImageBitmap(image);
    }
}