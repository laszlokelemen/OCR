package com.example.kelemen.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawClass extends View {

    private Paint paint;
    private static Path path;
    Bitmap bitmap;
    Canvas canvas;
    private ArrayList<DrawingHandlerClass> drawingArr = new ArrayList<>();

    public DrawClass(Context context) {
        super(context);
        setPaintAndPath();
    }

    public DrawClass(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPaintAndPath();
    }

    public DrawClass(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPaintAndPath();
    }

    private void setPaintAndPath() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(12f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.SQUARE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    public static Path getPath() {
        return path;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        DrawingHandlerClass drawingHandlerClass = new DrawingHandlerClass();

        canvas.drawPath(path, paint);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());
                path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                drawingHandlerClass.setPath(path);
                drawingHandlerClass.setPaint(paint);
                drawingArr.add(drawingHandlerClass);
                break;

            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawingArr.size() > 0) {
            canvas.drawPath(drawingArr.get(drawingArr.size() - 1).getPath()
                    , drawingArr.get(drawingArr.size() - 1).getPaint());
        }
        setDrawingCacheEnabled(true);
    }

    public Bitmap getBitmap(){
        return bitmap;
    }


}
