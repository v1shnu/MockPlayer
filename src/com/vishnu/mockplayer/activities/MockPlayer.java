package com.vishnu.mockplayer.activities;

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
import com.vishnu.mockplayer.R;
import com.vishnu.mockplayer.models.Action;
import com.vishnu.mockplayer.models.Screen;
import com.vishnu.mockplayer.utilities.DatabaseHandler;
import com.vishnu.mockplayer.utilities.Utilities;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MockPlayer extends Activity {

    ImageView imageView;
    List<Action> hotSpots;
    Stack<Screen> backStack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mock_player);

        imageView = (ImageView) findViewById(R.id.imageView);
        backStack = new Stack<Screen>();

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
            private int detectHit(float x, float y, List<Action> hotSpots) {
                for(Action hotSpot : hotSpots) {
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        showScreen(db.selectFirstScreen(getIntent().getIntExtra("mockId", 0)).getScreenId(), true);
    }

    private void showScreen(int screen_id, boolean push) {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        Screen screen = db.selectScreenById(screen_id);
        displayImage(screen.getImage());
        setHotSpots(screen.getScreenId());
        if(push)
            backStack.push(screen);
        if(hotSpots == null) {
            Utilities.displayToast(getApplicationContext(), "End of flow. Tap to exit.");
        }
    }

    private void setHotSpots(int screen_id) {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        hotSpots = db.getHotSpots(screen_id);
    }

    private void displayImage(Uri screenImage) {
        InputStream stream = Utilities.convertUriToStream(getApplicationContext(), screenImage);
        Bitmap image = Utilities.decodeSampledBitmapFromStream(stream, imageView.getWidth(), imageView.getHeight());
        imageView.setImageBitmap(image);
        Utilities.log("Displaying : "+imageView.getHeight());
    }

    private void exitPlayer() {
        Intent intent = new Intent(this, ListOfFlows.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (hotSpots != null) {
            for(Action hotspot : hotSpots) {
                if(hotspot.isBackButton()) {
                    emptyBackStack();
                    showScreen(hotspot.getDestination(), true);
                    return;
                }
            }
        }

        if(backStack.size() == 1) {
            super.onBackPressed();
            return;
        }
        backStack.pop();
        Screen screen = backStack.peek();
        showScreen(screen.getScreenId(), false);
    }

    private void emptyBackStack() {
        while(!backStack.empty())
            backStack.pop();
    }

    public void onMenuButtonPressed() {
        if(hotSpots != null) {
            for(Action hotspot : hotSpots) {
                if(hotspot.isMenuButton()) {
                    showScreen(hotspot.getDestination(), true);
                    return;
                }
            }
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