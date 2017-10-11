package com.example.kelemen.ocr;


import android.graphics.Paint;
import android.graphics.Path;

public class DrawingHandlerClass {
    protected  Path path;
    protected  Paint paint;

    public Path getPath() {
        return path;
    }

    public  void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public  void setPaint(Paint paint) {
        this.paint = paint;
    }


}
