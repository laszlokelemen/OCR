package com.example.kelemen.ocr.ocr_mgr;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TargetOutputs {

    private static TargetOutputs targetOutput;

    private Map<String, ArrayList<Double>> targetValues;
    private ArrayList<Double> goodDoubleValueList = getTargetList();

    public TargetOutputs() {
        targetValues = new TreeMap<>();
        addTargetOutputs();
    }

    public static TargetOutputs checkInit() {
        if (targetOutput == null) {
            targetOutput = new TargetOutputs();
        }
        return targetOutput;
    }

    public ArrayList<Double> getTargetValues(String letter) {
        return targetValues.get(letter);
    }

    public ArrayList<Double> getTargetList() {
        ArrayList<Double> doubleArrayList = new ArrayList<>();
        for (int i = 0; i < Train.getNeronNumber(); i++) {
            doubleArrayList.add(0.0);
        }
        return doubleArrayList;
    }

    private void addTargetOutputs() {
        for (int i = 0; i < Train.getNeronNumber(); i++) {
            targetValues.put(getChars().get(i), getList(i));
        }
    }

    private ArrayList<Double> getList(int i) {
        if (i > 0) {
            goodDoubleValueList.set(i - 1, 0.0);
            goodDoubleValueList.set(i, 1.0);
        } else {
            goodDoubleValueList.set(i, 1.0);
        }
        return new ArrayList<>(goodDoubleValueList);
    }

    public static List<String> getChars() {
        List<String> stringArrayList = new ArrayList<>();

        stringArrayList.add("0");
        stringArrayList.add("1");
        stringArrayList.add("2");
        stringArrayList.add("3");
        stringArrayList.add("4");
        stringArrayList.add("5");
        stringArrayList.add("6");
        stringArrayList.add("7");
        stringArrayList.add("8");
        stringArrayList.add("9");
        stringArrayList.add("A");
        stringArrayList.add("B");
        stringArrayList.add("C");
        stringArrayList.add("D");
        stringArrayList.add("E");
        stringArrayList.add("F");
        stringArrayList.add("G");
        stringArrayList.add("H");
        stringArrayList.add("I");
        stringArrayList.add("J");
        stringArrayList.add("K");
        stringArrayList.add("L");
        stringArrayList.add("M");
        stringArrayList.add("N");
        stringArrayList.add("O");
        stringArrayList.add("P");
        stringArrayList.add("Q");
        stringArrayList.add("R");
        stringArrayList.add("S");
        stringArrayList.add("T");
        stringArrayList.add("U");
        stringArrayList.add("V");
        stringArrayList.add("W");
        stringArrayList.add("X");
        stringArrayList.add("Y");
        stringArrayList.add("Z");
        stringArrayList.add("+");
        stringArrayList.add("-");
        stringArrayList.add("div");
        stringArrayList.add("mult");
        stringArrayList.add("=");
        Collections.sort(stringArrayList);
        return stringArrayList;
    }
}
