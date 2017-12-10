package com.example.kelemen.ocr.calculator_mgr;


import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kelemen.ocr.util.MathUtil;

import java.util.Stack;

import static android.text.TextUtils.isDigitsOnly;

public class CalculatorMgr {

    private Stack<Character> characterStack = new Stack<>();
    private Stack<String> stack = new Stack<>();
    private StringBuilder stringBuilder = new StringBuilder();

    public String calculatorEngine(String result, Context context, TextView calcText) {
        String input = "";
        if (isDigitsOnly(result) && stack.isEmpty()) {
            stack.push(result);
            stringBuilder.append(result);
        } else if (!stack.isEmpty() && isDigitsOnly(stack.peek()) && isDigitsOnly(result)) {
            Toast errToast = Toast.makeText(context.getApplicationContext(),
                    "Add a mathematical operator!", Toast.LENGTH_SHORT);
            errToast.show();
        } else if (!stack.isEmpty() && isDigitsOnly(stack.peek()) && result.matches("[+*/-]")) {
            stack.push(result);
            stringBuilder.append(result);
        } else if (!stack.isEmpty() && stack.peek().matches("[+*/-]") && isDigitsOnly(result)) {
            stack.push(result);
            stringBuilder.append(result);
        } else if (result.equals("=") && stack.size() > 2 && isDigitsOnly(stack.peek())) {
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

    String doTranslation(String input) {
        String output = "";
        for (int j = 0; j < input.length(); j++) {
            char ch = input.charAt(j);
            switch (ch) {
                case '+':
                case '-':
                    output = gotOperator(ch, 1, output);
                    break;
                case '*':
                case '/':
                    output = gotOperator(ch, 2, output);
                    break;
                default:
                    output = output + ch;
                    break;
            }
        }
        while (!getCharacterStack().isEmpty()) {
            output = output + getCharacterStack().pop();
        }
        return output;
    }

    public Stack<Character> getCharacterStack() {
        return characterStack;
    }

    String gotOperator(char opThis, int prec1, String output) {
        while (!getCharacterStack().isEmpty()) {
            char opTop = getCharacterStack().pop();
            int prec2;
            if (opTop == '+' || opTop == '-') {
                prec2 = 1;
            } else {
                prec2 = 2;
            }
            if (prec2 < prec1) {
                getCharacterStack().push(opTop);
                break;
            } else output = output + opTop;
        }
        getCharacterStack().push(opThis);
        return output;
    }

    int calculateResult(String input, Context context) {
        Stack<Integer> stack = new Stack<>();
        String numOrOperand;
        for (int i = 0; i < input.length(); i++) {
            numOrOperand = String.valueOf(input.charAt(i));
            if (isDigitsOnly(numOrOperand)) {
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
                    } else {
                        stack.push(0);
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
