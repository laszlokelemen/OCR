package com.example.kelemen.ocr;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kelemen.ocr.ocr_engine.Train;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Button clearButton;
    Button saveButton;
    Button trainButton;
    Button detect_button;
    DrawClass view;
    String selectedItem;
    Train networkTrain;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkTrain = new Train();
        clearButton = (Button) findViewById(R.id.button);
        saveButton = (Button) findViewById(R.id.button2);
        trainButton = (Button) findViewById(R.id.train_button);
        detect_button = (Button) findViewById(R.id.detect_button);
//        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.training_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        view = (DrawClass) findViewById(R.id.touch_view);
        view.setBackgroundColor(Color.WHITE);
        view.setDrawingCacheEnabled(true);

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());


        detect_button.setOnClickListener(v ->{
            networkTrain.setInputs(BitmapHandler.saveBitmapToArray(view));
            ArrayList<Double> outputs = networkTrain.getOutputs();
            int index = 0;
            for(int i=0; i< outputs.size();i++){
                if(outputs.get(i) > outputs.get(index)){
                    index = i;
                }
            }
            updateTextArea();
            view.buildDrawingCache();
            view.destroyDrawingCache();

        });

        trainButton.setOnClickListener(v -> {
            int trainNumber = 5000;
            networkTrain.train(trainNumber);
        });

        clearButton.setOnClickListener(v -> {
            DrawClass.getPath().reset();
            view.invalidate();
        });

        saveButton.setOnClickListener(v -> {
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(view.getContext());
            saveDialog.setTitle("Save");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", (dialog, which) -> {

                if (BitmapHandler.saveBitmapToArray(view) != null) {
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved !", Toast.LENGTH_SHORT);
                    savedToast.show();
                    ReadAndWriteFile.writeToFile(BitmapHandler.saveBitmapToArray(view), getSelectedItem());

                } else {
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Drawing could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
            });
            saveDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            saveDialog.show();
            view.buildDrawingCache();
            view.destroyDrawingCache();
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setSelectedItem(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateTextArea() {
        StringBuilder sb = new StringBuilder();
        ArrayList<Double> outputs = networkTrain.getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            int letterValue = i + 65;
            sb.append((char) letterValue);
            double value = outputs.get(i);
            if (value < 0.01)
            {
                value = 0;}
            if (value > 0.99)
            {value = 1;}

            value *= 1000;
            int x = (int) (value);
            value = x / 1000.0;

            sb.append("\t ").append(value);
            sb.append("\n");
        }
        System.out.println("///////////////////////////////////////////////////////////////////////////");
        System.out.println((sb.toString()));

    }

    private void setSelectedItem(String item) {
        selectedItem = item;
    }

    public String getSelectedItem() {
        return selectedItem;
    }



}
