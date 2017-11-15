package com.example.kelemen.ocr;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kelemen.ocr.drawing.Draw;
import com.example.kelemen.ocr.read_and_write_file.ReadAndWriteFile;
import com.example.kelemen.ocr.bitmap_mgr.BitmapMgr;
import com.example.kelemen.ocr.ocr_engine.TargetOutputs;
import com.example.kelemen.ocr.ocr_engine.Train;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button clearButton;
    Button saveButton;
    Button trainButton;
    Button detect_button;
    ImageButton helpButton;
    Draw view;
    String selectedItem;
    Train networkTrain;
    TextView resultText;
    Boolean counter = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkTrain = new Train();
        initButtons();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.training_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        view = (Draw) findViewById(R.id.touch_view);
        view.setBackgroundColor(Color.WHITE);
        view.setDrawingCacheEnabled(true);

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());


        helpButton.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.information_layout);
            dialog.setTitle("Information");

            TextView textView = (TextView) dialog.findViewById(R.id.helpTextView);
            textView.setText("1:Train the network before you start to detect." + "\n" + "2:Use your finger to draw" + "\n" + "3:Push the 'Detect' button to detect your draw" + "\n" + "4:Save the draw, train ,and repeat the third step.");

            ImageButton dismissButton = (ImageButton) dialog.findViewById(R.id.closeButton);
            dismissButton.setOnClickListener(view1 -> dialog.dismiss());
            dialog.show();
        });

        detect_button.setOnClickListener(v -> {
            if (!counter) {
                Toast detectErrToast = Toast.makeText(getApplicationContext(),
                        "The Neuron network is silly.You should train it!", Toast.LENGTH_SHORT);
                detectErrToast.show();
            } else {
                networkTrain.setInputs(BitmapMgr.processBitmap(view));
                if (networkTrain.checkDataSet().size() == 0) {
                    Toast errToast = Toast.makeText(getApplicationContext(),
                            "Training set is empty!", Toast.LENGTH_SHORT);
                    errToast.show();
                } else {
                    setLetterToOutput();
                    view.buildDrawingCache();
                    view.destroyDrawingCache();
                }
            }
        });

        trainButton.setOnClickListener(v -> {
            counter = true;
            final ProgressDialog progressBar = new ProgressDialog(MainActivity.this, R.style.progressBarStyle);
            progressBar.setTitle("Training");
            progressBar.setMessage("Training is in progress");
            progressBar.show();

            if (networkTrain.checkDataSet().size() == 0) {
                Toast errToast = Toast.makeText(getApplicationContext(),
                        "Training set is empty!", Toast.LENGTH_SHORT);
                errToast.show();
            } else {
                new Thread(() -> {
                    training();
                    progressBar.cancel();
                }).start();
            }


        });

        clearButton.setOnClickListener(v -> {
            Draw.getPath().reset();
            view.invalidate();
        });

        saveButton.setOnClickListener(v -> {
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(view.getContext());
            saveDialog.setTitle("Save");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", (dialog, which) -> {

                if (BitmapMgr.processBitmap(view) != null) {
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved !", Toast.LENGTH_SHORT);
                    savedToast.show();
                    ReadAndWriteFile.writeToFile(BitmapMgr.processBitmap(view), getSelectedItem());
                    Draw.getPath().reset();
                    view.invalidate();

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

    private void initButtons() {
        helpButton = (ImageButton) findViewById(R.id.helpButton);
        clearButton = (Button) findViewById(R.id.button);
        saveButton = (Button) findViewById(R.id.button2);
        trainButton = (Button) findViewById(R.id.train_button);
        detect_button = (Button) findViewById(R.id.detect_button);
        resultText = (TextView) findViewById(R.id.resultText);
    }

    private void training() {
        networkTrain.loadDataSet();
        int trainNumber = 1000;
        networkTrain.train(trainNumber);
    }

    private void setLetterToOutput() {
        Map<String, Double> resultMap = new HashMap<String, Double>();
        StringBuilder sb = new StringBuilder();
        ArrayList<Double> outputs = networkTrain.getOutputs();
        for (int i = 0; i < outputs.size(); i++) {
            sb.append(TargetOutputs.getChars().get(i));
            double value = outputs.get(i);
            if (value < 0.01) {
                value = 0;
            } else if (value > 0.99) {
                value = 1;
            }
            value = Double.parseDouble(new DecimalFormat("##.##").format(value));
            resultMap.put(TargetOutputs.getChars().get(i), value);

            sb.append("\t ").append(value);
            sb.append("\n");
        }
        System.out.println("///////////////////////////////////////////////////////////////////////////");
        System.out.println((sb.toString()));

        resultText.setText(geMaxValueKey(resultMap).getKey());
    }

    public Map.Entry<String, Double> geMaxValueKey(Map<String, Double> map) {
        Map.Entry<String, Double> maxEntry = null;

        for (Map.Entry<String, Double> entry : map.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    private void setSelectedItem(String item) {
        selectedItem = item;
    }

    String getSelectedItem() {
        return selectedItem;
    }


}
