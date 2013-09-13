package com.vishnu.mockplayer.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/9/13
 * Time: 7:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomImageView extends ImageView {

    Paint paint = new Paint();

    public CustomImageView(Context context, int startX) {
        super(context);
        this.startX = startX;
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.rgb(87,151,238));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static int DRAG = 0;
    public static int DRAW = 1;
    public static int EXPAND = 2;
    public static int MODE = DRAG;

    public int startX, startY, endX, endY, startDragX, dragX, startDragY, dragY, mode=0;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        try{
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(touchedInside(event.getX(), event.getY())) {
                        MODE = DRAG;
                        startDragX = (int) event.getX();
                        startDragY = (int) event.getY();
                        break;
                    }
                    MODE = DRAW;
                    startX = (int) event.getX();
                    startY = (int) event.getY();
                    endX = (int) event.getX();
                    endY = (int) event.getY();
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(MODE == DRAG) {
                        Utilities.log("Moving");
                        dragX = (int) event.getX()-startDragX;
                        dragY = (int) event.getY()-startDragY;
                        startDragX = (int) event.getX();
                        startDragY = (int) event.getY();
                        endX += dragX;
                        startX += dragX;
                        startY += dragY;
                        endY += dragY;
                    }
                    else if(MODE == DRAW) {
                        endX = (int) event.getX();
                        endY = (int) event.getY();
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if(MODE == DRAG)
                        Utilities.log("Moved to : "+"("+startX+", "+startY+") - ("+endX+", "+endY+")");
                    else
                        Utilities.log("Selected Area : "+"("+startX+", "+startY+") - ("+endX+", "+endY+")");
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Utilities.log(e.getMessage());
        }
        return true;
    }

    private boolean touchedInside(float x, float y) {
        return x < Math.max(startX, endX) && x > Math.min(startX, endX) && y < Math.max(startY, endY) && y > Math.min(startY, endY);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(MODE == DRAW || MODE == DRAG) {
            canvas.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.max(startX, endX), Math.max(startY, endY), paint);
        }
    }
}
