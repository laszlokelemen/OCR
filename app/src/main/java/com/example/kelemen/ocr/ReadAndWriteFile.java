package com.example.kelemen.ocr;


import com.example.kelemen.ocr.ocr_engine.DataSet;
import com.example.kelemen.ocr.ocr_engine.TargetOutputs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ReadAndWriteFile {

    private static final String FOLDER = "data/user/0/com.example.kelemen.ocr/files/";


    public static void writeToFile(ArrayList<Integer> arr, String selectedItem) {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(FOLDER + selectedItem + ".txt", true));) {
            for (Integer i : arr) {
                writer.append(String.valueOf(i));
            }
            writer.append('\n');
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ArrayList<Integer>> readFromFile(File fileName) {
        ArrayList<ArrayList<Integer>> lines = new ArrayList<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName));) {
            while ((line = br.readLine()) != null) {
                lines.add(getLinesValues(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private static ArrayList<Integer> getLinesValues(String line) {
        ArrayList<Integer> binaryValues = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            binaryValues.add(Integer.parseInt(String.valueOf(line.charAt(i))));
        }
        return binaryValues;
    }

    public static ArrayList<DataSet> getDataSet() {
        ArrayList<DataSet> dataSet = new ArrayList<>();
        File fileDirectory = new File(FOLDER);
        File[] files = fileDirectory.listFiles();
        if (files.length != 0) {
            for (File file : files) {
                String fileName = file.getName();
                String fileNameWithoutExtension = fileName.split(".txt")[0];
                for (ArrayList<Integer> line : readFromFile(file)) {
                    dataSet.add(new DataSet(line, TargetOutputs.checkInit().getTargetValues(fileNameWithoutExtension)));
                }

            }
        }




        return dataSet;
    }

}
