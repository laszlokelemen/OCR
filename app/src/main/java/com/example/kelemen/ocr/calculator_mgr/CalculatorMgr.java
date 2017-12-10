package com.example.kelemen.ocr.calculator_mgr;


import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kelemen.ocr.util.MathUtil;

import java.util.Stack;


public class CalculatorMgr {

    private Stack<Character> characterStack = new Stack<>();
    private Stack<String> stack = new Stack<>();
    private StringBuilder stringBuilder = new StringBuilder();

    public String calculatorEngine(String result, Context context, TextView calcText) {
        String input = "";
        if (isDigitsOnly(result) && getStack().isEmpty()) {
            getStack().push(result);
            stringBuilder.append(result);
        } else if (!getStack().isEmpty() && isDigitsOnly(getStack().peek()) && isDigitsOnly(result)) {
            Toast errToast = getMakeMatOpToast(context);
            errToast.show();
        } else if (!getStack().isEmpty() && isDigitsOnly(getStack().peek()) && result.matches("[+*/-]")) {
            getStack().push(result);
            stringBuilder.append(result);
        } else if (!getStack().isEmpty() && getStack().peek().matches("[+*/-]") && isDigitsOnly(result)) {
            getStack().push(result);
            stringBuilder.append(result);
        } else if (result.equals("=") && getStack().size() > 2 && isDigitsOnly(getStack().peek())) {
            while (!getStack().isEmpty()) {
                input += getStack().pop();
            }
            input = new StringBuilder(input).reverse().toString();
            stringBuilder.setLength(0);
            return String.valueOf(calculateResult(doTranslation(input), context));
        } else {
            Toast errToast = getWrongSequenceToast(context);
            errToast.show();
        }
        setCalculatorTextViewContent(calcText, stringBuilder);
        return "";
    }

    Stack<String> getStack() {
        return stack;
    }

    private void setCalculatorTextViewContent(TextView calcText, StringBuilder stringBuilder) {
        calcText.setText(stringBuilder);
    }

    Boolean isDividerNull(int denomination, Context context) {
        if (denomination == 0) {
            Toast errToast = getDividerNullToast(context);
            errToast.show();
            return true;
        }
        return false;
    }

    Toast getToast(Context context, String text) {
        return getToast(context, text);
    }


    String doTranslation(String input) {
        String output = "";
        for (int j = 0; j < input.length(); j++) {
            char ch = input.charAt(j);
            switch (ch) {
                case '+':
                case '-':
                    output = getOperator(ch, 1, output);
                    break;
                case '*':
                case '/':
                    output = getOperator(ch, 2, output);
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

    String getOperator(char opThis, int prec1, String output) {
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
                    if (!isDividerNull(numberB, context)) {
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

    boolean isDigitsOnly(String numOrOperand) {
        return TextUtils.isDigitsOnly(numOrOperand);
    }

    Toast getWrongSequenceToast(Context context) {
        return Toast.makeText(context.getApplicationContext(),
                "Wrong mathematics sequence!", Toast.LENGTH_SHORT);
    }

    Toast getMakeMatOpToast(Context context) {
        return Toast.makeText(context.getApplicationContext(),
                "Add a mathematical operator!", Toast.LENGTH_SHORT);
    }

    Toast getDividerNullToast(Context context) {
        return Toast.makeText(context.getApplicationContext(),
                "Denominator is null!", Toast.LENGTH_SHORT);
    }
}
