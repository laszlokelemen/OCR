package com.example.kelemen.ocr.calculator_mgr;


import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kelemen.ocr.util.MathUtil;

import java.util.Stack;

public class CalculatorMgr {

    private Stack<Character> characterStack = new Stack();
    private Stack<String> stack = new Stack<>();
    private StringBuilder stringBuilder = new StringBuilder();

    public String calculatorEngine(String result, Context context, TextView calcText) {
        String input = "";
        if (android.text.TextUtils.isDigitsOnly(result) && stack.isEmpty()) {
            stack.push(result);
            stringBuilder.append(result);
        } else if (!stack.isEmpty() && android.text.TextUtils.isDigitsOnly(stack.peek()) && android.text.TextUtils.isDigitsOnly(result)) {
            Toast errToast = Toast.makeText(context.getApplicationContext(),
                    "Add a mathematical operator!", Toast.LENGTH_SHORT);
            errToast.show();
        } else if (!stack.isEmpty() && android.text.TextUtils.isDigitsOnly(stack.peek()) && result.matches("[+*/-]")) {
            stack.push(result);
            stringBuilder.append(result);
        } else if (!stack.isEmpty() && stack.peek().matches("[+*/-]") && android.text.TextUtils.isDigitsOnly(result)) {
            stack.push(result);
            stringBuilder.append(result);
        } else if (result.equals("=") && stack.size() > 2) {
            while (!stack.isEmpty()) {
                input += stack.pop();
            }
            input = new StringBuilder(input).reverse().toString();
            stringBuilder.setLength(0);
            return String.valueOf(calculateResult(doTranslation(input), context));
        } else {
            Toast errToast = Toast.makeText(context.getApplicationContext(),
                    "Wrong mathematics sequence!", Toast.LENGTH_SHORT);
            errToast.show();
        }
        setCalculatorTextViewContent(calcText, stringBuilder);
        return "";
    }

    private void setCalculatorTextViewContent(TextView calcText, StringBuilder stringBuilder) {
        calcText.setText(stringBuilder);
    }

    private Boolean isDenominatorNull(int denomination, Context context) {
        if (denomination == 0) {
            Toast errToast = Toast.makeText(context.getApplicationContext(),
                    "Denominator is null!", Toast.LENGTH_SHORT);
            errToast.show();
            return true;
        }
        return false;
    }

    private String doTranslation(String input) {
        String output = "";
        for (int j = 0; j < input.length(); j++) {
            char ch = input.charAt(j);
            switch (ch) {
                case '+':
                case '-':
                    output = gotOper(ch, 1, output);
                    break;
                case '*':
                case '/':
                    output = gotOper(ch, 2, output);
                    break;
                default:
                    output = output + ch;
                    break;
            }
        }
        while (!characterStack.isEmpty()) {
            output = output + characterStack.pop();
        }
        return output;
    }

    private String gotOper(char opThis, int prec1, String output) {
        while (!characterStack.isEmpty()) {
            char opTop = characterStack.pop();
            int prec2;
            if (opTop == '+' || opTop == '-')
                prec2 = 1;
            else
                prec2 = 2;
            if (prec2 < prec1) {
                characterStack.push(opTop);
                break;
            } else output = output + opTop;
        }
        characterStack.push(opThis);
        return output;
    }

    private int calculateResult(String input, Context context) {
        Stack<Integer> stack = new Stack();
        String numOrOperand;
        for (int i = 0; i < input.length(); i++) {
            numOrOperand = String.valueOf(input.charAt(i));
            if (android.text.TextUtils.isDigitsOnly(numOrOperand)) {
                int intNumOrOperand = Integer.parseInt(numOrOperand);
                stack.push(intNumOrOperand);
            } else {
                if (numOrOperand.equals("*")) {
                    stack.push(MathUtil.mult(stack.pop(), stack.pop()));
                } else if (numOrOperand.equals("/")) {
                    int numberA = stack.pop();
                    int numberB = stack.pop();
                    if (!isDenominatorNull(numberB, context)) {
                        stack.push(MathUtil.div(numberB, numberA));
                    }
                } else if (numOrOperand.equals("+")) {
                    stack.push(MathUtil.add(stack.pop(), stack.pop()));
                } else if (numOrOperand.equals("-")) {
                    int numberA = stack.pop();
                    int numberB = stack.pop();
                    stack.push(MathUtil.sub(numberB, numberA));
                }
            }
        }
        return stack.pop();
    }
}
