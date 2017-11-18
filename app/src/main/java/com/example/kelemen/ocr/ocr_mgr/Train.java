package com.example.kelemen.ocr.ocr_mgr;


import com.example.kelemen.ocr.read_and_write_file.ReadAndWriteFile;

import java.util.ArrayList;

public class Train {

    private static final int NEURON_NUMBER = 41;

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

    public ArrayList<Double> getOutputs() {
        return network.getOutputs();
    }

    public static int getNeronNumber(){
        return NEURON_NUMBER;
    }
}
