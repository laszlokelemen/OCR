package com.example.kelemen.ocr.calculator_mgr;

import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CalculatorMgrTest {

    private CalculatorMgr calculatorMgrSpy;
    private CalculatorMgr calculatorMgr;

    @Before
    public void initTest() {
        calculatorMgr = new CalculatorMgr();
        calculatorMgrSpy = spy(CalculatorMgr.class);
    }


    @Test
    public void testDoTranslation() {
        assertEquals(calculatorMgr.doTranslation("5+3"), "53+");
        assertEquals(calculatorMgr.doTranslation("5+3*4"), "534*+");
        assertEquals(calculatorMgr.doTranslation("5/4"), "54/");
    }

    @Test
    public void testGetOper() {
        Stack<Character> testStack = new Stack<>();
        testStack.push('1');
        testStack.push('2');
        testStack.push('+');

        Stack<Character> resultTestStack = new Stack<>();
        resultTestStack.push('1');
        resultTestStack.push('2');
        resultTestStack.push('+');
        resultTestStack.push('/');

        when(calculatorMgrSpy.getCharacterStack()).thenReturn(testStack);
        assertEquals(calculatorMgrSpy.gotOperator('/', 2, ""), "");
        assertEquals(calculatorMgrSpy.getCharacterStack(), resultTestStack);
    }

    @Test
    public void testCalculateResult() {
        calculatorMgr.calculateResult("53*", null);
    }


}