package com.example.kelemen.ocr.bitmap_mgr;


import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.kelemen.ocr.drawing.Draw;

import java.util.ArrayList;

public class BitmapMgr {

    public static ArrayList<Integer> processBitmap(Draw view) {
        Bitmap resultBitmap = Bitmap.createScaledBitmap(view.getDrawingCache(), 20, 20, true);
        ArrayList<Integer> arrayList = new ArrayList<>();
        int width = resultBitmap.getWidth();
        int height = resultBitmap.getHeight();
        for (int x = 0; x < width; x++) {
            setPixelColorValue(resultBitmap, arrayList, height, x);
        }
        return arrayList;
    }

    private static void setPixelColorValue(Bitmap resultBitmap, ArrayList<Integer> arrayList, int height, int x) {
        for (int y = 0; y < height; y++) {
            int pixel = resultBitmap.getPixel(x, y);
            if (Color.rgb(Color.red(Color.WHITE), Color.green(Color.WHITE), Color.blue(Color.WHITE)) != Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel))) {
                arrayList.add(1);
            } else {
                arrayList.add(0);
            }
        }
    }


}
