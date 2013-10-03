package com.vishnu.mockplayer.utilities;

import android.content.Context;
import android.graphics.*;
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

    private enum DrawMode {
        DRAG, DRAW, EXPAND;
    }

    private enum ExpandType {
        BORDER, CORNER;
    }

    private enum Border {
        TOP, RIGHT, BOTTOM, LEFT;
    }

    private enum Corner {
        TOP_LEFT, TOP_RIGHT, BOTTOM_RIGHT, BOTTOM_LEFT;
    }

    private ExpandType expandType = ExpandType.BORDER;
    private DrawMode drawMode = DrawMode.DRAG;
    private Border expandBorder = Border.BOTTOM;
    private Corner expandCorner = Corner.BOTTOM_LEFT;
    private final static int MINIMUM_DISTANCE = 20;
    private final static int BORDER_THICKNESS = 5;
    private final static int CIRCLE_RADIUS = 5;
    private boolean portionSelected = false;

    private PointF start = new PointF(), startDrag = new PointF(), dragged = new PointF();

    private RectF selection = new RectF(), coordinates = new RectF();


    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.rgb(87, 151, 238));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(BORDER_THICKNESS);
        blurrer.setColor(Color.BLACK);
        blurrer.setAlpha(150);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RectF getCoordinates() {
        return transformCoordinates(selection);
    }

    private RectF transformCoordinates(RectF selection) {
        Matrix matrix = new Matrix();
        this.getImageMatrix().invert(matrix);
        matrix.postTranslate(this.getScrollX(), this.getScrollY());
        RectF coordinates = new RectF();
        matrix.mapRect(coordinates, selection);
        return coordinates;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        try{
            PointF clicked = new PointF(event.getX(), event.getY());
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    portionSelected = false;
                    if(touchedCorner(clicked.x, clicked.y)) {
                        drawMode = DrawMode.EXPAND;
                        expandType = ExpandType.CORNER;
                        break;
                    }
                    if(touchedBorder(clicked.x, clicked.y)) {
                        drawMode = DrawMode.EXPAND;
                        expandType = ExpandType.BORDER;
                        break;
                    }
                    if(touchedInside(clicked.x, clicked.y)) {
                        drawMode = DrawMode.DRAG;
                        startDrag.x = clicked.x;
                        startDrag.y = clicked.y;
                        break;
                    }
                    else {
                        drawMode = DrawMode.DRAW;
                        start.x = clicked.x;
                        start.y = clicked.y;
                        invalidate();
                        break;
                    }
                case MotionEvent.ACTION_MOVE:
                    portionSelected = true;
                    switch (drawMode) {
                        case DRAW:
                            selection = new RectF(start.x, start.y, clicked.x, clicked.y);
                            selection.sort();
                            break;
                        case DRAG:
                            dragSelection(clicked);
                            break;
                        case EXPAND:
                            expandSelection(clicked);
                            break;
                    }
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    portionSelected = true;
                    break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Utilities.log(e.getMessage());
        }
        return true;
    }

    private void expandSelection(PointF clicked) {
        switch (expandType) {
            case BORDER:
                expandSelectionBorder(clicked);
                break;
            case CORNER:
                expandSelectionCorner(clicked);
        }
    }

    private void dragSelection(PointF clicked) {
        dragged.x = clicked.x -startDrag.x;
        dragged.y = clicked.y -startDrag.y;
        startDrag.x = clicked.x;
        startDrag.y = clicked.y;
        selection.left += dragged.x;
        selection.top += dragged.y;
        selection.bottom += dragged.y;
        selection.right += dragged.x;
    }

    private void expandSelectionBorder(PointF clicked) {
        switch (expandBorder) {
            case TOP:
                if(clicked.y > selection.bottom) {
                    selection.top = selection.bottom;
                    selection.bottom = clicked.y;
                    expandBorder = Border.BOTTOM;
                }
                else
                    selection.top = clicked.y;
                break;
            case RIGHT:
                if(clicked.x < selection.left) {
                    selection.right = selection.left;
                    selection.left = clicked.x;
                    expandBorder = Border.LEFT;
                }
                else
                    selection.right = clicked.x;
                break;
            case BOTTOM:
                if(clicked.y < selection.top) {
                    selection.bottom = selection.top;
                    selection.top = clicked.y;
                    expandBorder = Border.TOP;
                }
                else
                    selection.bottom = clicked.y;
                break;
            case LEFT:
                if(clicked.x > selection.right) {
                    selection.left = selection.right;
                    selection.right = clicked.x;
                    expandBorder = Border.RIGHT;
                }
                else
                    selection.left = clicked.x;
                break;
        }
    }

    private void expandSelectionCorner(PointF clicked) {
        switch (expandCorner) {
            case TOP_LEFT:
                if(clicked.y > selection.bottom) {
                    selection.top = selection.bottom;
                    selection.bottom = clicked.y;
                    expandCorner = Corner.BOTTOM_LEFT;
                }
                else if(clicked.x > selection.right) {
                    selection.left = selection.right;
                    selection.right = clicked.x;
                    expandCorner = Corner.TOP_RIGHT;
                }
                else {
                    selection.top = clicked.y;
                    selection.left = clicked.x;
                }
                break;
            case TOP_RIGHT:
                if(clicked.y > selection.bottom) {
                    selection.top = selection.bottom;
                    selection.bottom = clicked.y;
                    expandCorner = Corner.BOTTOM_RIGHT;
                }
                else if(clicked.x < selection.left) {
                    selection.right = selection.left;
                    selection.left = clicked.x;
                    expandCorner = Corner.TOP_LEFT;
                }
                else {
                    selection.top = clicked.y;
                    selection.right = clicked.x;
                }
                break;
            case BOTTOM_RIGHT:
                if(clicked.y < selection.top) {
                    selection.bottom = selection.top;
                    selection.top = clicked.y;
                    expandCorner = Corner.TOP_RIGHT;
                }
                else if(clicked.x < selection.left) {
                    selection.right = selection.left;
                    selection.left = clicked.x;
                    expandCorner = Corner.BOTTOM_LEFT;
                }
                else {
                    selection.bottom = clicked.y;
                    selection.right = clicked.x;
                }
                break;
            case BOTTOM_LEFT:
                if(clicked.y < selection.top) {
                    selection.bottom = selection.top;
                    selection.top = clicked.y;
                    expandCorner = Corner.TOP_LEFT;
                }
                else if(clicked.x > selection.right) {
                    selection.left = selection.right;
                    selection.right = clicked.x;
                    expandCorner = Corner.BOTTOM_RIGHT;
                }
                else {
                    selection.bottom = clicked.y;
                    selection.left = clicked.x;
                }
                break;
        }
    }

    private boolean touchedInside(float x, float y) {
        return x < selection.right && x > selection.left && y < selection.bottom && y > selection.top;
    }

    private boolean touchedBorder(float x, float y) {
        if(isNearLine(y, selection.top) && isBetweenLines(selection.left, selection.right, x)) {
            expandBorder = Border.TOP;
            return true;
        }
        if(isNearLine(x, selection.right) && isBetweenLines(selection.top, selection.bottom, y)) {
            expandBorder = Border.RIGHT;
            return true;
        }
        if(isNearLine(y, selection.bottom) && isBetweenLines(selection.left, selection.right, x)) {
            expandBorder = Border.BOTTOM;
            return true;
        }
        if(isNearLine(x, selection.left) && isBetweenLines(selection.top, selection.bottom, y)) {
            expandBorder = Border.LEFT;
            return true;
        }
        return false;
    }

    private boolean touchedCorner(float x, float y) {
        if(isNearPoint(new PointF(x, y), new PointF(selection.left, selection.top))) {
            expandCorner = Corner.TOP_LEFT;
            return true;
        }
        if(isNearPoint(new PointF(x, y), new PointF(selection.right, selection.top))) {
            expandCorner = Corner.TOP_RIGHT;
            return true;
        }
        if(isNearPoint(new PointF(x, y), new PointF(selection.right, selection.bottom))) {
            expandCorner = Corner.BOTTOM_RIGHT;
            return true;
        }
        if(isNearPoint(new PointF(x, y), new PointF(selection.left, selection.bottom))) {
            expandCorner = Corner.BOTTOM_LEFT;
            return true;
        }
        return false;
    }

    private boolean isBetweenLines(float firstLine, float secondLine, float lineToBeCompared) {
        float startLine = Math.min(firstLine, secondLine);
        float endLine = Math.max(firstLine, secondLine);
        return (lineToBeCompared >= startLine-MINIMUM_DISTANCE && lineToBeCompared <= endLine+MINIMUM_DISTANCE);
    }

    private boolean isNearLine(float firstLine, float secondLine) {
        return Math.abs(firstLine-secondLine) <= MINIMUM_DISTANCE;
    }

    private boolean isNearPoint(PointF firstPoint, PointF secondPoint) {
        return distanceBetweenPoints(firstPoint, secondPoint) <= MINIMUM_DISTANCE;
    }

    private float distanceBetweenPoints(PointF firstPoint, PointF secondPoint) {
        return (float) Math.sqrt(Math.pow((secondPoint.x-firstPoint.x), 2) + Math.pow((secondPoint.y-firstPoint.y), 2));
    }

    private void constructRectangle(Canvas canvas) {
        canvas.drawRect(0, 0, selection.left, canvas.getHeight(), blurrer);
        canvas.drawRect(selection.right, 0, canvas.getWidth(), canvas.getHeight(), blurrer);
        canvas.drawRect(selection.left, 0, selection.right, selection.top, blurrer);
        canvas.drawRect(selection.left, selection.bottom, selection.right, canvas.getHeight(), blurrer);
        canvas.drawRect(selection.left, selection.top, selection.right, selection.bottom, paint);
        canvas.drawCircle(selection.centerX(), selection.bottom, CIRCLE_RADIUS, paint);
        canvas.drawCircle(selection.right, selection.centerY(), CIRCLE_RADIUS, paint);
        canvas.drawCircle(selection.centerX(), selection.top, CIRCLE_RADIUS, paint);
        canvas.drawCircle(selection.left, selection.centerY(), CIRCLE_RADIUS, paint);
        canvas.drawCircle(selection.left, selection.top, CIRCLE_RADIUS, paint);
        canvas.drawCircle(selection.right, selection.top, CIRCLE_RADIUS, paint);
        canvas.drawCircle(selection.right, selection.bottom, CIRCLE_RADIUS, paint);
        canvas.drawCircle(selection.left, selection.bottom, CIRCLE_RADIUS, paint);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(portionSelected) {
            constructRectangle(canvas);
        }
    }
}
