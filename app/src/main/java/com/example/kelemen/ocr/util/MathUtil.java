package com.example.kelemen.ocr.util;

public class MathUtil {

    public MathUtil() {
    }

    public static double sigmoid(double sum) {
        return (1 / (1 + Math.exp(-sum)));
    }

    public static int add(int x, int y) {
        int result = x + y;
        return result;
    }

    public static int sub(int x, int y) {
        int result = x - y;
        return result;
    }

    public static int mult(int x, int y) {
        int result = x * y;
        return result;
    }

    public static int div(int x, int y) {
        int result = x / y;
        return result;
    }
}
