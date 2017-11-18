package com.example.kelemen.ocr.ocr_mgr;

import java.util.ArrayList;

public class DataSet {

    private ArrayList<Integer> input;
    private ArrayList<Double> output;

    public DataSet(ArrayList<Integer> input, ArrayList<Double> output) {
        this.input = input;
        this.output = output;
    }

    public ArrayList<Integer> getInput() {
        return input;
    }

    public ArrayList<Double> getOutput() {
        return output;
    }
}
