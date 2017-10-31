package com.example.kelemen.ocr;


import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;

public class BitmapHandler {

    public static ArrayList<Integer> saveBitmapToArray(DrawClass view) {
//        Bitmap resultBitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0,
//                view.getDrawingCache().getWidth(), view.getDrawingCache().getHeight());
        Bitmap resultBitmap = Bitmap.createScaledBitmap(view.getDrawingCache(), 20, 20, true);
        ArrayList<Integer> arrayList = new ArrayList<>();
        int width = resultBitmap.getWidth();
        int height = resultBitmap.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = resultBitmap.getPixel(x, y);
                if (Color.rgb(Color.red(Color.WHITE), Color.green(Color.WHITE), Color.blue(Color.WHITE)) != Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel))) {
                    arrayList.add(1);
                } else {
                    arrayList.add(0);
                }
            }
        }
        return arrayList;
    }
}
