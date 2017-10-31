package com.example.kelemen.ocr.ocr_engine;

import java.util.ArrayList;

public class Network {

    ArrayList<Neuron> neurons;

    public Network() {
        neurons = new ArrayList<>();
    }

    public void addNeurons(int numberOfNeurons) {
        for (int i = 0; i < numberOfNeurons; i++) {
            neurons.add(new Neuron());
        }
    }

    public void setNeuronsInputValue(ArrayList<Integer> input) {
        for (Neuron neuron : neurons) {
            neuron.setInputList(input);
        }
    }

    public ArrayList<Double> getOutputs() {
        ArrayList<Double> outputs = new ArrayList<>();
        for (Neuron n : neurons) {
            outputs.add(n.getOutputValue());
        }
        return outputs;
    }

    public void adjusWages(ArrayList<Double> goodOutput) {
        for (int i = 0; i < neurons.size(); i++) {
            double delta = goodOutput.get(i) - neurons.get(i).getOutputValue();
            neurons.get(i).adjustWeights(delta);
        }
    }

}
