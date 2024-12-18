package com.gmail.uprial.customcreatures.common;

import com.google.common.collect.Lists;
import org.junit.Test;

import static com.gmail.uprial.customcreatures.common.Utils.*;
import static org.junit.Assert.assertEquals;

public class UtilsTest {
    @Test
    public void testSeconds2ticks() {
        assertEquals(40, seconds2ticks(2));
    }

    @Test
    public void testJoinEmptyStrings() {
        assertEquals("", joinStrings(",", Lists.newArrayList(new String[]{})));
    }

    @Test
    public void testJoinOneString() {
        assertEquals("a", joinStrings(",", Lists.newArrayList("a")));
    }

    @Test
    public void testJoinSeveralStrings() {
        assertEquals("a,b", joinStrings(",", Lists.newArrayList("a", "b")));
    }

    @Test
    public void testJoinPaths() {
        assertEquals("a.b", joinPaths("a", "b"));
    }

    @Test
    public void testJoinRootEmptyPaths() {
        assertEquals("b", joinPaths("", "b"));
    }

    @Test
    public void testJoinRightEmptyPaths() {
        assertEquals("a.", joinPaths("a", ""));
    }
}