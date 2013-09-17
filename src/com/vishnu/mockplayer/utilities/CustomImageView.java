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
    Paint blurrer = new Paint();

    public CustomImageView(Context context, int startX) {
        super(context);
        this.startX = startX;
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.rgb(87,151,238));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(BORDER_THICKNESS);
        blurrer.setColor(Color.BLACK);
        blurrer.setAlpha(150);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static int DRAG = 0;
    public static int DRAW = 1;
    public static int EXPAND = 2;
    public static int EXPAND_TYPE = 0;
    public static int BORDER = 0;
    public static int CORNER = 1;
    public static int MODE = DRAG;
    public static int MINIMUM_DISTANCE = 20;
    public static int EXPAND_BORDER = 0;
    public static int EXPAND_CORNER = 0;
    public static int BORDER_THICKNESS = 5;
    public static int CIRCLE_RADIUS = 5;
    public static boolean PORTION_SELECTED = false;

    public float startX, startY, endX, endY, startDragX, dragX, startDragY, dragY;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        try{
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    PORTION_SELECTED = false;
                    if(touchedCorner(event.getX(), event.getY())) {
                        Utilities.log("Touched the corner : "+EXPAND_CORNER);
                        MODE = EXPAND;
                        EXPAND_TYPE = CORNER;
                        break;
                    }
                    if(touchedBorder(event.getX(), event.getY())) {
                        Utilities.log("Touched the border : "+EXPAND_BORDER);
                        MODE = EXPAND;
                        EXPAND_TYPE = BORDER;
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
                    if(MODE != EXPAND && ((distanceBetweenPoints(event.getX(), event.getY(), startX, startY) < MINIMUM_DISTANCE) ||
                            (Math.abs(startX - event.getX()) < MINIMUM_DISTANCE) ||
                            (Math.abs(startY- event.getY()) < MINIMUM_DISTANCE)
                    )) {
                        PORTION_SELECTED = false;
                        invalidate();
                        break;
                    }
                    PORTION_SELECTED = true;
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
                        if(EXPAND_TYPE == BORDER) {
                            if(EXPAND_BORDER == 0) {
                                if(startY < endY) startY = event.getY();
                                else endY = event.getY();
                            }
                            else if(EXPAND_BORDER == 1) {
                                if(endX > startX) endX = event.getX();
                                else startX = event.getX();
                            }
                            else if(EXPAND_BORDER == 2) {
                                if(endY > startY) endY = event.getY();
                                else startY = event.getY();
                            }
                            else if(EXPAND_BORDER == 3) {
                                if(startX<endX) startX = event.getX();
                                else endX = event.getX();
                            }
                        }
                        else if(EXPAND_TYPE == CORNER) {
                            if(EXPAND_CORNER == 0) {
                                if(startX < endX) startX = event.getX();
                                else endX = event.getX();
                                if(startY < endY) startY = event.getY();
                                else endY = event.getY();
                            }
                            else if(EXPAND_CORNER == 1) {
                                if(startX > endX) startX = event.getX();
                                else endX = event.getX();
                                if(startY < endY) startY = event.getY();
                                else endY = event.getY();
                            }
                            else if(EXPAND_CORNER == 2) {
                                if(startX > endX) startX = event.getX();
                                else endX = event.getX();
                                if(startY > endY) startY = event.getY();
                                else endY = event.getY();
                            }
                            else if(EXPAND_CORNER == 3) {
                                if(startX < endX) startX = event.getX();
                                else endX = event.getX();
                                if(startY > endY) startY = event.getY();
                                else endY = event.getY();
                            }
                        }
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    PORTION_SELECTED = true;
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

    private boolean touchedBorder(float x, float y) {
        //Higher horizontal line
        if(isNearLine(y, Math.min(startY, endY)) && isBetweenLines(startX, endX, x)) {
            EXPAND_BORDER = 0;
            return true;
        }
        //Second vertical line
        if(isNearLine(x, Math.max(startX, endX)) && isBetweenLines(startY, endY, y)) {
            EXPAND_BORDER = 1;
            return true;
        }
        //Lower horizontal line
        if(isNearLine(y, Math.max(startY, endY)) && isBetweenLines(startX, endX, x)) {
            EXPAND_BORDER = 2;
            return true;
        }
        //First vertical line
        if(isNearLine(x, Math.min(startX, endX)) && isBetweenLines(startY, endY, y)) {
            EXPAND_BORDER = 3;
            return true;
        }
        return false;
    }

    private boolean touchedCorner(float x, float y) {
        //Top left corner
        if(isNearPoint(x, y, Math.min(startX, endX), Math.min(startY, endY))) {
            EXPAND_CORNER = 0;
            return true;
        }
        //Top right corner
        if(isNearPoint(x, y, Math.max(startX, endX), Math.min(startY, endY))) {
            EXPAND_CORNER = 1;
            return true;
        }
        //Bottom right corner
        if(isNearPoint(x, y, Math.max(startX, endX), Math.max(startY, endY))) {
            EXPAND_CORNER = 2;
            return true;
        }
        //Bottom left corner
        if(isNearPoint(x, y, Math.min(startX, endX), Math.max(startY, endY))) {
            EXPAND_CORNER = 3;
            return true;
        }
        return false;
    }

    private boolean isBetweenLines(float line1, float line2, float line) {
        float startLine = Math.min(line1, line2);
        float endLine = Math.max(line1, line2);
        return (line >= startLine-MINIMUM_DISTANCE && line <= endLine+MINIMUM_DISTANCE);
    }

    private boolean isNearLine(float p1, float p2) {
        return Math.abs(p1-p2) <= MINIMUM_DISTANCE;
    }

    private boolean isNearPoint(float x1, float y1, float x2, float y2) {
        return distanceBetweenPoints(x1, y1, x2, y2) <= MINIMUM_DISTANCE;
    }

    private float distanceBetweenPoints(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(((x2-x1)*(x2-x1)) + ((y2-y1)*(y2-y1)));
    }

    private void constructRectangle(Canvas canvas) {
        canvas.drawRect(0, 0, Math.min(startX, endX), canvas.getHeight(), blurrer);
        canvas.drawRect(Math.max(startX, endX), 0, canvas.getWidth(), canvas.getHeight(), blurrer);
        canvas.drawRect(Math.min(startX, endX), 0, Math.max(startX, endX), Math.min(startY, endY), blurrer);
        canvas.drawRect(Math.min(startX, endX), Math.max(startY, endY), Math.max(startX, endX), canvas.getHeight(), blurrer);
        canvas.drawRect(Math.min(startX, endX), Math.min(startY, endY), Math.max(startX, endX), Math.max(startY, endY), paint);
        canvas.drawCircle((startX + endX) / 2, endY, CIRCLE_RADIUS, paint);
        canvas.drawCircle(endX, (startY + endY) / 2, CIRCLE_RADIUS, paint);
        canvas.drawCircle((startX + endX) / 2, startY, CIRCLE_RADIUS, paint);
        canvas.drawCircle(startX, (startY + endY) / 2, CIRCLE_RADIUS, paint);
        canvas.drawCircle(Math.min(startX, endX), Math.min(startY, endY), CIRCLE_RADIUS, paint);
        canvas.drawCircle(Math.max(startX, endX), Math.min(startY, endY), CIRCLE_RADIUS, paint);
        canvas.drawCircle(Math.max(startX, endX), Math.max(startY, endY), CIRCLE_RADIUS, paint);
        canvas.drawCircle(Math.min(startX, endX), Math.max(startY, endY), CIRCLE_RADIUS, paint);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(PORTION_SELECTED)
            constructRectangle(canvas);
    }
}
