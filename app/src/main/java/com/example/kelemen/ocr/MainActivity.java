package com.example.kelemen.ocr;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

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
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawClass.getPath().reset();
                view.invalidate();
                view.setDrawingCacheEnabled(false);
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setDrawingCacheEnabled(true);
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(view.getContext());
                saveDialog.setTitle("Save");
                saveDialog.setMessage("Save drawing to device Gallery?");
                saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String imgSaved = MediaStore.Images.Media.insertImage(
                                getContentResolver(), view.getDrawingCache(),
                                UUID.randomUUID().toString() + ".png", "drawing");
                        if (imgSaved != null) {
                            Toast savedToast = Toast.makeText(getApplicationContext(),
                                    "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                            savedToast.show();
                            Log.d("Arraylist:", String.valueOf(saveBitmapToArray(view)));
                            Log.d("!!!!!!!!!!!!!!", String.valueOf(saveBitmapToArray(view).size()));


                        } else {
                            Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                    "Image could not be saved.", Toast.LENGTH_SHORT);
                            unsavedToast.show();
                        }
                    }
                });
                saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                saveDialog.show();
                view.buildDrawingCache();

            }
        });


    }

    private String getFileName() {
        File f = new File("C:\\Hello\\AnotherFolder\\The File Name.PDF");
        return f.getName();
    }

    void out(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String asd = String.valueOf(byteArray.length);
        Log.d("asd", asd);
    }


    public ArrayList saveBitmapToArray(DrawClass view) {
        Bitmap resultBitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, 0,
                view.getDrawingCache().getWidth() - 1, view.getDrawingCache().getHeight() - 1);
        ArrayList<Integer> arrayList = new ArrayList<>();
        int width = resultBitmap.getWidth();
        int height = resultBitmap.getHeight();

        for (int x = 1; x < width; x++) {
            for (int y = 1; y < height; y++) {

                int pixel = resultBitmap.getPixel(x, y);
                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);

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
