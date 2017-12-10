package com.example.kelemen.ocr;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kelemen.ocr.bitmap_mgr.BitmapMgr;
import com.example.kelemen.ocr.calculator_mgr.CalculatorMgr;
import com.example.kelemen.ocr.drawing.Draw;
import com.example.kelemen.ocr.ocr_mgr.TargetOutputs;
import com.example.kelemen.ocr.ocr_mgr.Train;
import com.example.kelemen.ocr.read_and_write_file.ReadAndWriteFile;

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
    TextView resultText;
    TextView calcText;
    Switch calcSwitch;
    Draw view;
    Train networkTrain;
    Boolean counter = false;
    String selectedItem;
    CalculatorMgr calculatorMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkTrain = new Train();
        calculatorMgr = new CalculatorMgr();
        calcSwitch = (Switch) findViewById(R.id.calculatorSwitch);
        initButtons();
        Spinner spinner = initSpinner();
        setView();

        helpButton.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(R.layout.information_layout);
            dialog.setTitle("Information");

            TextView textView = dialog.findViewById(R.id.helpTextView);
            textView.setText("1:Train the network before you start to detect." + "\n" + "2:Use your finger to draw" + "\n" + "3:Push the 'Detect' button to detect your draw" + "\n" + "4:Save the draw, train ,and repeat the third step.");

            ImageButton dismissButton = dialog.findViewById(R.id.closeButton);
            dismissButton.setOnClickListener(view1 -> dialog.dismiss());
            dialog.show();
        });

        detect_button.setOnClickListener(v -> {
            if (!counter) {
                Toast detectErrToast = Toast.makeText(getApplicationContext(),
                        "The Neuron network is unlearned.You should train it!", Toast.LENGTH_SHORT);
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
            if (networkTrain.checkDataSet().size() == 0) {
                Toast errToast = Toast.makeText(getApplicationContext(),
                        "Training set is empty!", Toast.LENGTH_SHORT);
                errToast.show();
            } else {
                final ProgressDialog progressBar = new ProgressDialog(MainActivity.this, R.style.progressBarStyle);
                progressBar.setTitle("Training");
                progressBar.setMessage("Training is in progress");
                progressBar.setCanceledOnTouchOutside(false);
                progressBar.setCancelable(false);
                progressBar.show();

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
            saveDialog.setMessage("Save drawing to device?");
            saveDialog.setPositiveButton("Yes", (dialog, which) -> {

                if (BitmapMgr.processBitmap(view) != null) {
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved as " + getSelectedItem() + "!", Toast.LENGTH_SHORT);
                    savedToast.show();
                    ReadAndWriteFile.writeToFile(BitmapMgr.processBitmap(view), checkSelectedItem(getSelectedItem()));
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

    private void setView() {
        view = (Draw) findViewById(R.id.touch_view);
        view.setBackgroundColor(Color.WHITE);
        view.setDrawingCacheEnabled(true);

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    @NonNull
    private Spinner initSpinner() {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.training_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return spinner;
    }

    private void initButtons() {
        helpButton = (ImageButton) findViewById(R.id.helpButton);
        clearButton = (Button) findViewById(R.id.button);
        saveButton = (Button) findViewById(R.id.button2);
        trainButton = (Button) findViewById(R.id.train_button);
        detect_button = (Button) findViewById(R.id.detect_button);
        resultText = (TextView) findViewById(R.id.resultText);
        calcText = (TextView) findViewById(R.id.calculatorTextView);

    }

    private void training() {
        networkTrain.loadDataSet();
        int trainNumber = 2500;
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
            resultMap.put(checkSelectedItem(TargetOutputs.getChars().get(i)), value);

            sb.append("\t ").append(value);
            sb.append("\n");
        }
        System.out.println("///////////////////////////////////////////////////////////////////////////");
        System.out.println((sb.toString()));

        showResultText(resultMap);
    }

    private void showResultText(Map<String, Double> resultMap) {

        if (calcSwitch.isChecked()) {
            String result = (calculatorMgr.calculatorEngine(getMaxValueKey(resultMap).getKey(), getApplicationContext(), calcText));
            if (result.equals("")) {
                resultText.setText(getMaxValueKey(resultMap).getKey());
            } else {
                resultText.setText(result);
                calcText.setText("");
            }
        } else {
            resultText.setText(getMaxValueKey(resultMap).getKey());
        }
    }

    public Map.Entry<String, Double> getMaxValueKey(Map<String, Double> map) {
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

    private String checkSelectedItem(String selectedItem) {
        switch (selectedItem) {
            case "/":
                return "div";
            case "*":
                return "mult";
            case "div":
                return "/";
            case "mult":
                return "*";
        }
        return selectedItem;
    }


}
