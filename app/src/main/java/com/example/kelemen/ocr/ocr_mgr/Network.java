package com.example.kelemen.ocr.ocr_mgr;

import java.util.ArrayList;

public class Network {

    private ArrayList<Neuron> neurons;

    Network() {
        neurons = new ArrayList<>();
    }

    void addNeurons(int numberOfNeurons) {
        for (int i = 0; i < numberOfNeurons; i++) {
            neurons.add(new Neuron());
        }
    }

    void setNeuronsInputValue(ArrayList<Integer> input) {
        for (Neuron neuron : neurons) {
            neuron.setInputList(input);
        }
    }

    ArrayList<Double> getOutputs() {
        ArrayList<Double> outputs = new ArrayList<>();
        for (Neuron neuron : neurons) {
            outputs.add(neuron.getOutputValue());
        }
        return outputs;
    }

    void adjustWages(ArrayList<Double> goodOutput) {
        for (int i = 0; i < neurons.size(); i++) {
            double delta = goodOutput.get(i) - neurons.get(i).getOutputValue();
            neurons.get(i).adjustWeights(delta);
        }
    }

}
