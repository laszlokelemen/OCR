package com.example.kelemen.ocr.ocr_engine;


import com.example.kelemen.ocr.ReadAndWriteFile;

import java.util.ArrayList;

public class Train {

    public static final int NEURON_NUMBER = 3;

    private Network network;
    private ArrayList<DataSet> dataSets;

    public Train() {
        this.network = new Network();
        this.network.addNeurons(NEURON_NUMBER);
    }

    public ArrayList<DataSet> checkDataSet() {
        return ReadAndWriteFile.getDataSet();
    }

    public void loadDataSet() {
        this.dataSets = ReadAndWriteFile.getDataSet();
    }


    public void train(long trainNumber) {
        for (int i = 0; i < trainNumber; i++) {
            int index = (int) (Math.random() * dataSets.size());
            DataSet dataSet = dataSets.get(index);
            network.setNeuronsInputValue(dataSet.getInput());
            network.adjusWages(dataSet.getOutput());
        }
    }

    public void setInputs(ArrayList<Integer> inputs) {
        network.setNeuronsInputValue(inputs);
    }

    public void addDataSet(DataSet dataSet) {
        dataSets.add(dataSet);
    }

    public ArrayList<Double> getOutputs() {
        return network.getOutputs();
    }

}
