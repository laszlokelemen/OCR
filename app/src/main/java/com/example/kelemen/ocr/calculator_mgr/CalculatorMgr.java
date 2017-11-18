package com.example.kelemen.ocr.calculator_mgr;


import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kelemen.ocr.util.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CalculatorMgr {

    public static String calculatorEngine(String result, Stack<String> stack, Context context, TextView calcText, StringBuilder stringBuilder) {
        List<Integer> numbers = new ArrayList<>();
        if (android.text.TextUtils.isDigitsOnly(result) && stack.isEmpty()) {
            stack.push(result);
            stringBuilder.append(result);
        } else if (!stack.isEmpty() && android.text.TextUtils.isDigitsOnly(stack.peek()) && android.text.TextUtils.isDigitsOnly(result)) {
            Toast errToast = Toast.makeText(context.getApplicationContext(),
                    "Add a mathematical operator!", Toast.LENGTH_SHORT);
            errToast.show();
        } else if (!stack.isEmpty() && android.text.TextUtils.isDigitsOnly(stack.peek()) && result.matches("[+*/-]") && stack.size() != 3) {
            stack.push(result);
            stringBuilder.append(result);
        } else if (!stack.isEmpty() && stack.peek().matches("[+*/-]") && android.text.TextUtils.isDigitsOnly(result)) {
            stack.push(result);
            stringBuilder.append(result);
        } else if (result.equals("=") && stack.size() == 3) {
            String operation = "";
            while (!stack.isEmpty()) {
                String stackPop = stack.pop();  
                if (android.text.TextUtils.isDigitsOnly(stackPop)) {
                    numbers.add(Integer.valueOf(stackPop));
                } else if (stackPop.matches("[+*/-]")) {
                    operation = stackPop;
                }
            }
            int calculatedResult = calculateResult(operation, numbers, context);
            numbers.clear();
            return String.valueOf(calculatedResult);
        } else {
            Toast errToast = Toast.makeText(context.getApplicationContext(),
                    "Wrong mathematics sequence!", Toast.LENGTH_SHORT);
            errToast.show();
        }
        setCalculatorTextViewContent(stack, calcText, stringBuilder);
        return "";
    }

    private static void setCalculatorTextViewContent(Stack<String> stack, TextView calcText, StringBuilder stringBuilder) {
        if (stack.size() == 0) {
            stringBuilder.setLength(0);
        } else {
            calcText.setText(stringBuilder);
        }
    }

    private static int calculateResult(String operation, List<Integer> numbers, Context context) {
        int numberA = numbers.get(1);
        int numberB = numbers.get(0);

        switch (operation) {
            case "+":
                return MathUtil.add(numberA, numberB);
            case "-":
                return MathUtil.sub(numberA, numberB);
            case "/":
                if (!isDenominatorNull(numberA, context)) {
                    return MathUtil.div(numberA, numberB);
                }
            case "*":
                return MathUtil.mult(numberA, numberB);
        }
        return 0;
    }

    private static Boolean isDenominatorNull(int denomination, Context context) {
        if (denomination == 0) {
            Toast errToast = Toast.makeText(context.getApplicationContext(),
                    "Denominator is null!", Toast.LENGTH_SHORT);
            errToast.show();
            return true;
        }
        return false;
    }

}
