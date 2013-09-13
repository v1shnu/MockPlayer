package com.vishnu.mockplayer.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    boolean drawRectangle = false;
    int x1, y1, x2, y2;

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        Utilities.log("Touched...");
        try{
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    drawRectangle = true; // Start drawing the rectangle
                    Utilities.log("Action Down : "+String.valueOf(event.getX()) + " : " + String.valueOf(event.getY()));
                    x1 = (int) event.getX();
                    y1 = (int) event.getY();
                    x2 = (int) event.getX();
                    y2 = (int) event.getY();
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Utilities.log("Action Move : "+String.valueOf(event.getX()) + " : " + String.valueOf(event.getY()));
                    x2 = (int) event.getX();
                    y2 = (int) event.getY();
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    Utilities.log("Action Up : "+String.valueOf(event.getX()) + " : " + String.valueOf(event.getY()));
                    drawRectangle = false;
                    //invalidate();
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Utilities.log(e.getMessage());
        }
        return true;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Utilities.log("Drawing...");
        if(drawRectangle) {
            canvas.drawRect(x1, y1, x2, y2, paint);
        }
    }
}
