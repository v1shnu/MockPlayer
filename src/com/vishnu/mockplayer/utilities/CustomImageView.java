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
    public static int MINIMUM_DISTANCE = 20;
    public static int EXPAND_BORDER = 0;
    public static int CIRCLE_RADIUS = 10;

    public float startX, startY, endX, endY, startDragX, dragX, startDragY, dragY;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        try{
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(touchedOnBorder(event.getX(), event.getY())) {
                        Utilities.log("Touched the border : "+EXPAND_BORDER);
                        MODE = EXPAND;
                        break;
                    }
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
                    else if(MODE == EXPAND) {
                        if(EXPAND_BORDER == 0)
                            startY = event.getY();
                        else if(EXPAND_BORDER == 1)
                            endX = event.getX();
                        else if(EXPAND_BORDER == 2)
                            endY = event.getY();
                        else if(EXPAND_BORDER == 3)
                            startX = event.getX();
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

    private boolean touchedOnBorder(float x, float y) {
        //Higher horizontal line
        if(isNearLine(y, startY) && isBetweenLines(startX, endX, x)) {
            EXPAND_BORDER = 0;
            return true;
        }
        //Second vertical line
        if(isNearLine(x, endX) && isBetweenLines(startY, endY, y)) {
            EXPAND_BORDER = 1;
            return true;
        }
        //Lower horizontal line
        if(isNearLine(y, endY) && isBetweenLines(startX, endX, x)) {
            EXPAND_BORDER = 2;
            return true;
        }
        //First vertical line
        if(isNearLine(x, startX) && isBetweenLines(startY, endY, y)) {
            EXPAND_BORDER = 3;
            return true;
        }
        return false;
    }

    private boolean isBetweenLines(float startLine, float endLine, float line) {
        return (line >= startLine-MINIMUM_DISTANCE && line <= endLine+MINIMUM_DISTANCE);
    }

    private boolean isNearLine(float p1, float p2) {
        return Math.abs(p1-p2) <= MINIMUM_DISTANCE;
    }

    private void constructRectangle(Canvas canvas) {
        canvas.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.max(startX, endX), Math.max(startY, endY), paint);
        canvas.drawCircle((startX+endX)/2, endY, CIRCLE_RADIUS, paint);
        canvas.drawCircle(endX, (startY+endY)/2, CIRCLE_RADIUS, paint);
        canvas.drawCircle((startX+endX)/2, startY, CIRCLE_RADIUS, paint);
        canvas.drawCircle(startX, (startY+endY)/2, CIRCLE_RADIUS, paint);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        constructRectangle(canvas);
    }
}
