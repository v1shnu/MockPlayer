package com.vishnu.mockplayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.vishnu.mockplayer.models.HotSpots;
import com.vishnu.mockplayer.models.Screen;
import com.vishnu.mockplayer.utilities.DatabaseHandler;
import com.vishnu.mockplayer.utilities.Utilities;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 19/9/13
 * Time: 10:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class MockPlayer extends Activity {

    DatabaseHandler db;
    ImageView imageView;
    ArrayList<HotSpots> hotSpots;
    Stack<Screen> backStack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mock_player);

        db = new DatabaseHandler(getApplicationContext());
        imageView = (ImageView) findViewById(R.id.imageView);
        backStack = new Stack<Screen>();

        showScreen(db.selectFirstScreen(getIntent().getIntExtra("mock_id", 0)).getScreen_id(), true);

        imageView.setOnTouchListener(new ImageView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(hotSpots == null) {
                    exitPlayer();
                    return false;
                }
                float[] transformedCoordinates = transformCoordinates(event.getX(), event.getY());
                int destination = detectHit(transformedCoordinates[0], transformedCoordinates[1], hotSpots);
                if(destination != -1) showScreen(destination, true);
                return false;
            }
            private int detectHit(float x, float y, ArrayList<HotSpots> hotSpots) {
                for(HotSpots hotSpot : hotSpots) {
                    if(x < Math.max(hotSpot.getX1(), hotSpot.getX2()) && x > Math.min(hotSpot.getX1(), hotSpot.getX2()) && y < Math.max(hotSpot.getY1(), hotSpot.getY2()) && y > Math.min(hotSpot.getY1(), hotSpot.getY2())) {
                        return hotSpot.getDestination();
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

    private void showScreen(int screen_id, boolean push) {
        Screen screen = db.selectScreenById(screen_id);
        displayImage(screen.getImage());
        setHotSpots(screen.getScreen_id());
        if(push)
            backStack.push(screen);
        if(hotSpots == null) {
            Utilities.displayToast(getApplicationContext(), "End of flow. Tap to exit.");
        }
    }

    private void setHotSpots(int screen_id) {
        hotSpots = db.getHotSpots(screen_id);
    }

    private void displayImage(Uri screenImage) {
        Bitmap image = com.vishnu.mockplayer.utilities.Utilities.convertUriToImage(getApplicationContext(), screenImage);
        imageView.setImageBitmap(image);
    }

    private void exitPlayer() {
        Intent intent = new Intent(this, ListOfFlows.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        try {
            for(HotSpots hotspot : hotSpots) {
                if(hotspot.isBackButton() == true) {
                    emptyBackStack();
                    showScreen(hotspot.getDestination(), true);
                    return;
                }
            }
        }
        catch(Exception e) {
        }

        if(backStack.size() == 1) {
            super.onBackPressed();
            return;
        }
        backStack.pop();
        Screen screen = backStack.peek();
        showScreen(screen.getScreen_id(), false);
    }

    private void emptyBackStack() {
        while(!backStack.empty())
            backStack.pop();
    }

    public void onMenuButtonPressed() {
        try {
            for(HotSpots hotspot : hotSpots) {
                if(hotspot.isMenuButton() == true) {
                    showScreen(hotspot.getDestination(), true);
                    return;
                }
            }
        }
        catch(Exception e) {
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Utilities.log("Backstack Size : "+backStack.size());
        if(keyCode == KeyEvent.KEYCODE_MENU) {
            onMenuButtonPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}