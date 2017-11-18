package com.example.kelemen.ocr.ocr_mgr;

import com.example.kelemen.ocr.util.MathUtil;

import java.util.ArrayList;

public class Neuron {

    private static final double LEARNING_RATE = 0.1;
    private static final int BIAS = 1;
    private ArrayList<Integer> input;
    private ArrayList<Double> weights;

    private double output;
    private double weightForBias;

    public Neuron() {
        this.input = new ArrayList<>();
        this.weights = new ArrayList<>();
        weightForBias = Math.random();
    }

    private void generateRandomWeights() {
        for (int i = 0; i < input.size(); i++) {
            weights.add(Math.random());
        }
    }

    public void setInputList(ArrayList<Integer> input) {
        if (this.input.size() != 0) {
            this.input = new ArrayList<>(input);
        } else {
            this.input = new ArrayList<>(input);
            generateRandomWeights();
        }
    }

    public void adjustWeights(double delta) {
        for (int i = 0; i < input.size(); i++) {
            weights.set(i, calculateNewWeight(delta, i));
        }
        weightForBias = BIAS * delta * LEARNING_RATE;
    }

    private void calculateOutput() {
        double sum = 0;
        for (int i = 0; i < input.size(); i++) {
            sum += input.get(i) * weights.get(i);
        }
        sum += BIAS * weightForBias;

        output = MathUtil.sigmoid(sum);

    }

    public double getOutputValue() {
        calculateOutput();
        return output;
    }

    private double calculateNewWeight(double delta, int i) {
        double weight;
        weight = weights.get(i);
        weight += LEARNING_RATE * delta * input.get(i);
        return weight;
    }


}
