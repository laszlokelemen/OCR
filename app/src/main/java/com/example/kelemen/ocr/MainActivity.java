package com.example.kelemen.ocr;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    Button clearButton;
    Button saveButton;
    DrawClass view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        clearButton = (Button) findViewById(R.id.button);
        saveButton = (Button) findViewById(R.id.button2);

        view = (DrawClass) findViewById(R.id.touch_view);
        view.setBackgroundColor(Color.WHITE);
        view.setDrawingCacheEnabled(true);

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        clearButton.setOnClickListener(v -> {
            DrawClass.getPath().reset();
            view.invalidate();
//                view.setDrawingCacheEnabled(false);
        });


        saveButton.setOnClickListener(v -> {
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(view.getContext());
            saveDialog.setTitle("Save");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", (dialog, which) -> {

                if (saveBitmapToArray(view) != null) {
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                    Log.d("Arraylist:", String.valueOf(saveBitmapToArray(view)));
                    Log.d("!!!!!!!!!!!!!!", String.valueOf(saveBitmapToArray(view).size()));
                    writeToFile(saveBitmapToArray(view));

                } else {
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
            });
            saveDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            saveDialog.show();
            view.buildDrawingCache();
            view.destroyDrawingCache();
        });


    }

    void writeToFile(ArrayList<Integer> arr) {
        String root = Environment.getExternalStorageState();
        File myDir = new File(root + "/savedLetter");
        myDir.mkdirs();
        Random generRandom = new Random();
        int n = 10000;
        n = generRandom.nextInt(n);
        String fileName = "letter-" + n + ".jpg";
        File file = new File(myDir, fileName);
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(fileName))) {
            writingFile(arr, bf);
            bf.flush();
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writingFile(ArrayList<Integer> arr, BufferedWriter bf) {
        arr.forEach(number -> {
            try {
                bf.write(number);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public ArrayList<Integer> saveBitmapToArray(DrawClass view) {
        Bitmap resultBitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0,
                view.getDrawingCache().getWidth() - 1, view.getDrawingCache().getHeight() - 1);
        ArrayList<Integer> arrayList = new ArrayList<>();
        int width = resultBitmap.getWidth();
        int height = resultBitmap.getHeight();

        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {

                int pixel = resultBitmap.getPixel(x, y);
                if (Color.rgb(Color.red(Color.BLACK), Color.green(Color.BLACK), Color.blue(Color.BLACK)) == Color.rgb(Color.red(pixel), Color.green(pixel), Color.blue(pixel))) {
                    arrayList.add(1);
                } else {
                    arrayList.add(0);
                }
            }
        }
        return arrayList;
    }


}
