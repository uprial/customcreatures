package com.gmail.uprial.customcreatures.common;

import org.junit.Test;

import static com.gmail.uprial.customcreatures.common.DoubleHelper.*;
import static org.junit.Assert.*;

public class DoubleHelperTest {
    private static final double INFINITE_DECIMAL = 10000000000000000000000000000000000.200000000000000000000000002;


    @Test
    public void testLog() {
        assertEquals(2.0, log(2.0, 4.0), Double.MIN_VALUE);
        assertEquals(3.0, log(2.0, 8.0), Double.MIN_VALUE);
    }

    @Test
    public void testMaxRightSize() {
        assertEquals(4, MAX_RIGHT_SIZE);
    }

    @Test
    public void testMaxLeftSize() {
        /*
            A float point variable have three parts: sign, mantissa and power.
            Max power value is Double.MAX_EXPONENT.
            Sign requires one more bit.
            Then number of bits for mantissa is:

            MantissaBits = SizeOf(Double) - 1 - ceil(log(2, Double.MAX_EXPONENT + 1))
        */
        int mantissaBits = Double.SIZE - 1 - (int)Math.ceil(log(2.0, Double.MAX_EXPONENT + 1.0));
        assertEquals(53, mantissaBits);
        /*
            If we have MAX_RIGHT_SIZE decimals for right part, then size of left part should be:

            floor(log(10, pow(2, MantissaBits - log(2, pow(10, MAX_RIGHT_SIZE))))).
         */
        assertEquals(MAX_LEFT_SIZE, (int)Math.floor(log(10.0, Math.pow(2.0,
                mantissaBits - log(2.0, Math.pow(10.0, MAX_RIGHT_SIZE))))));
    }

    @Test
    public void testLeftDigits() {
        assertEquals(1, getLeftDigits(0.0));
        assertEquals(1, getLeftDigits(0.1));
        assertEquals(1, getLeftDigits(1.1));
        assertEquals(2, getLeftDigits(10.0));
        assertEquals(2, getLeftDigits(21.1));
        assertEquals(9, getLeftDigits(0123456789.1));
        assertEquals(11, getLeftDigits(10123456789.1));
        assertEquals(11, getLeftDigits(10000000000.0));
    }

    @Test
    public void testRound() {
        assertEquals(1.0, round(1.46464, 0), Double.MIN_VALUE);
        assertEquals(1.5, round(1.46464, 1), Double.MIN_VALUE);
        assertEquals(1.46, round(1.46464, 2), Double.MIN_VALUE);
        assertEquals(1.465, round(1.46464, 3), Double.MIN_VALUE);
        assertEquals(1.4646, round(1.46464, 4), Double.MIN_VALUE);
        assertEquals(1.46464, round(1.46464, 5), Double.MIN_VALUE);
        assertEquals(1.46464, round(1.46464, 6), Double.MIN_VALUE);
    }

    @Test
    public void testRightDigits() {
        assertEquals(0,  getRightDigitsSlowly(1.0));
        assertEquals(1,  getRightDigitsSlowly(1.1));
        assertEquals(2,  getRightDigitsSlowly(1.12));
        assertEquals(0,  getRightDigitsSlowly(1.00000000000000003));
        assertEquals(16, getRightDigitsSlowly(1.0000000000000003));
        assertEquals(17, getRightDigitsSlowly(0.00000000000000003));
        assertEquals(16, getRightDigitsSlowly(0.0000000000000003));
        assertEquals(16, getRightDigitsSlowly(1.1000000000000003));
        assertEquals(1,  getRightDigitsSlowly(1.10000000000000003));
        // Found accidentally, I don't know how to reproduce that.
        assertEquals(-Double.MIN_EXPONENT, getRightDigitsSlowly(INFINITE_DECIMAL));
    }

    @Test
    public void testLimitedRightDigits() {
        assertEquals(0,  getRightDigits(1.0, 5));
        assertEquals(5, getRightDigits(1.0000000000000003, 5));
        assertEquals(5, getRightDigits(INFINITE_DECIMAL, 5));
    }

    @Test
    public void testFormat() {
        assertEquals("1", formatDoubleValue(1));
        assertEquals("1.2", formatDoubleValue(1.2));
        assertEquals("1.2345", formatDoubleValue(1.2345));
        assertEquals("1.2346", formatDoubleValue(1.23456));
        assertEquals("0.1", formatDoubleValue(0.1));
        assertEquals("0.01", formatDoubleValue(0.01));
        assertEquals("0.5", formatDoubleValue(0.5));
        assertEquals("0.05", formatDoubleValue(0.05));
    }

    @Test
    public void testLeftPartChecker() {
        assertTrue(isLengthOfLeftPartOfDoubleGood(1.0));
        assertTrue(isLengthOfLeftPartOfDoubleGood(12345678901.0));
        assertFalse(isLengthOfLeftPartOfDoubleGood(123456789012.0));
    }

    @Test
    public void testRightPartChecker() {
        assertTrue(isLengthOfRightPartOfDoubleGood(1.0));
        assertTrue(isLengthOfRightPartOfDoubleGood(1.0001));
        assertFalse(isLengthOfRightPartOfDoubleGood(1.00001));
    }

    @Test
    public void testChecker() {
        assertTrue(isLengthOfDoubleGood(12345678901.0001));
        assertFalse(isLengthOfDoubleGood(12345678901.00001));
        assertFalse(isLengthOfDoubleGood(123456789012.0001));
        assertFalse(isLengthOfDoubleGood(123456789012.00001));
    }

    @Test
    public void testMaxValue() {
        assertEquals(99999999999.9999, MAX_DOUBLE_VALUE, Double.MIN_VALUE);
        assertTrue(isLengthOfDoubleGood(MAX_DOUBLE_VALUE));
        assertFalse(isLengthOfDoubleGood(MAX_DOUBLE_VALUE + 1));
    }

    @Test
    public void testMinValue() {
        assertEquals(0.0001, MIN_DOUBLE_VALUE, Double.MIN_VALUE);
        assertTrue(isLengthOfDoubleGood(MIN_DOUBLE_VALUE));
        assertFalse(isLengthOfDoubleGood(MIN_DOUBLE_VALUE * 0.9));
    }

    @Test
    public void testFloatPreciseness() {
        assertTrue((float)(MIN_DOUBLE_VALUE * 1.0000001D) > MIN_DOUBLE_VALUE);
        assertFalse((float)(MIN_DOUBLE_VALUE * 1.00000001D) > MIN_DOUBLE_VALUE);
    }

    // This version is slow because of potential loop with 1k iterations.
    private static int getRightDigitsSlowly(double value) {
        return getRightDigits(value, -Double.MIN_EXPONENT);
    }
}