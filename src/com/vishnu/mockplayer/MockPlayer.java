package com.vishnu.mockplayer;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.vishnu.mockplayer.models.HotSpots;
import com.vishnu.mockplayer.models.Screen;
import com.vishnu.mockplayer.utilities.CustomImageView;
import com.vishnu.mockplayer.utilities.DatabaseHandler;
import com.vishnu.mockplayer.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

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
        Screen firstScreen = db.selectFirstScreen(mock_id);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Bitmap image = com.vishnu.mockplayer.utilities.Utilities.convertUriToImage(getApplicationContext(), firstScreen.getImage());
        imageView.setImageBitmap(image);
        final ArrayList<HotSpots> hotSpotsList = db.getHotSpots(firstScreen.getScreen_id());
        imageView.setOnTouchListener(new ImageView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float[] tranformedCoordinates = transformCoordinates(event.getX(), event.getY());
                Utilities.log(detectHit(tranformedCoordinates[0], tranformedCoordinates[1], hotSpotsList) + " --> GO TO");
                return false;
            }
            private int detectHit(float x, float y, ArrayList<HotSpots> hotspots) {
                for(HotSpots hotspot : hotspots) {
                    if(x < Math.max(hotspot.getX1(), hotspot.getX2()) && x > Math.min(hotspot.getX1(), hotspot.getX2()) && y < Math.max(hotspot.getY1(), hotspot.getY2()) && y > Math.min(hotspot.getY1(), hotspot.getY2())) {
                        return hotspot.getDestination();
                    }
                }
                return -1;
            }
            public float[] transformCoordinates(float x, float y) {
                float [] coordinates = new float [] {x, y};
                Matrix matrix = new Matrix();
                imageView.getImageMatrix().invert(matrix);
                matrix.postTranslate(imageView.getScrollX(), imageView.getScrollY());
                matrix.mapPoints(coordinates);
                return coordinates;
            }
        });
    }
}