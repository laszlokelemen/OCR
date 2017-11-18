package com.example.kelemen.ocr.read_and_write_file;


import com.example.kelemen.ocr.ocr_mgr.DataSet;
import com.example.kelemen.ocr.ocr_mgr.TargetOutputs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ReadAndWriteFile {

    private static final String FOLDER = "data/data/com.example.kelemen.ocr/ocrFiles/";

    public static void writeToFile(ArrayList<Integer> arr, String selectedItem) {
        createOcrFilesFolder();

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(FOLDER + selectedItem + ".txt", true))) {
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

    private static void createOcrFilesFolder() {
        File file = new File(FOLDER);
        if (!file.exists()) {
            file.mkdirs();
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
        ArrayList<DataSet> dataSet = new ArrayList<DataSet>();
        File fileDirectory = new File(FOLDER);
        File[] files = fileDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
//                file.delete();
                String fileName = file.getName();
                String fileNameWithoutExtension = fileName.split(".txt")[0];
                putLineAndLetterToMap(dataSet, file, fileNameWithoutExtension);
            }
        }
        return dataSet;
    }

    private static void putLineAndLetterToMap(ArrayList<DataSet> dataSet, File file, String fileNameWithoutExtension) {
        for (ArrayList<Integer> line : readFromFile(file)) {
            dataSet.add(new DataSet(line, TargetOutputs.checkInit().getTargetValues(fileNameWithoutExtension)));
        }
    }

}
