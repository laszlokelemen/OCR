package com.example.kelemen.ocr.drawing;


import android.graphics.Paint;
import android.graphics.Path;

public class Drawing {
    private Path path;
    private Paint paint;

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
