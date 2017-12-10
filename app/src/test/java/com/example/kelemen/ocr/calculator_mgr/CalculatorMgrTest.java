package com.example.kelemen.ocr.calculator_mgr;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Stack;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CalculatorMgrTest {

    private CalculatorMgr calculatorMgrSpy;
    private CalculatorMgr calculatorMgr;
    private Context contextMock;
    private Toast toastMock;
    private TextView textViewMock;

    @Before
    public void initTest() {
        calculatorMgr = new CalculatorMgr();
        calculatorMgrSpy = spy(calculatorMgr);
        contextMock = mock(Context.class);
        toastMock = mock(Toast.class);
        textViewMock = mock(TextView.class);
    }


    @Test
    public void testDoTranslation() {
        assertEquals(calculatorMgr.doTranslation("5+3"), "53+");
        assertEquals(calculatorMgr.doTranslation("5+3*4"), "534*+");
        assertEquals(calculatorMgr.doTranslation("5/4"), "54/");
    }

    @Test
    public void testGetOperator() {
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
        assertEquals(calculatorMgrSpy.getOperator('/', 2, ""), "");
        assertEquals(calculatorMgrSpy.getCharacterStack(), resultTestStack);
    }

    @Test
    public void testCalculateResult() {
        doReturn(false).when(calculatorMgrSpy).isDigitsOnly("*");
        doReturn(true).when(calculatorMgrSpy).isDigitsOnly("5");
        doReturn(true).when(calculatorMgrSpy).isDigitsOnly("3");
        assertThat(calculatorMgrSpy.calculateResult("53*", null), is(15));
    }

    @Test
    public void testDividerIsNull() {
        doReturn(toastMock).when(calculatorMgrSpy).getToast(any(), any());
        calculatorMgrSpy.isDividerNull(0, contextMock);
        verify(toastMock, Mockito.times(1)).show();
    }

    @Test
    public void testDividerIsNotNull() {
        calculatorMgrSpy.isDividerNull(1, contextMock);
        verify(toastMock, Mockito.times(0)).show();
    }

    @Test
    public void testCalculatorEngine() {
        Stack<String> testStack = new Stack<>();
        doReturn(toastMock).when(calculatorMgrSpy).getToast(any(), any());
        when(calculatorMgrSpy.getStack()).thenReturn(testStack);
        doReturn(true).when(calculatorMgrSpy).isDigitsOnly("1");

        calculatorMgrSpy.calculatorEngine("1", contextMock, textViewMock);
        assertThat(testStack.size(),is(1));
    }

    @Test
    public void testCalculatorEngineWithWrongInput() {
        Stack<String> testStack = new Stack<>();
        doReturn(toastMock).when(calculatorMgrSpy).getToast(any(), any());
        when(calculatorMgrSpy.getStack()).thenReturn(testStack);
        doReturn(false).when(calculatorMgrSpy).isDigitsOnly("/");
        doReturn(toastMock).when(calculatorMgrSpy).getToast(any(), any());

        calculatorMgrSpy.calculatorEngine("/", contextMock, textViewMock);
        assertThat(testStack.size(),is(0));
        verify(toastMock, Mockito.times(1)).show();
    }


}