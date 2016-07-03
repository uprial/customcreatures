package com.gmail.uprial.customcreatures.common;

import com.google.common.collect.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.common.Utils.*;
import static org.junit.Assert.assertEquals;

public class UtilsTest {
    @Rule
    public final ExpectedException e = ExpectedException.none();

    @Test
    public void testSeconds2ticks() throws Exception {
        assertEquals(40, seconds2ticks(2));
    }

    @Test
    public void testJoinEmptyStrings() throws Exception {
        assertEquals("", joinStrings(",", Lists.newArrayList(new String[]{})));
    }

    @Test
    public void testJoinOneString() throws Exception {
        assertEquals("a", joinStrings(",", Lists.newArrayList(new String[]{"a"})));
    }

    @Test
    public void testJoinSeveralStrings() throws Exception {
        assertEquals("a,b", joinStrings(",", Lists.newArrayList(new String[]{"a", "b"})));
    }

    @Test
    public void testJoinPaths() throws Exception {
        assertEquals("a.b", joinPaths("a", "b"));
    }

    @Test
    public void testJoinRootEmptyPaths() throws Exception {
        assertEquals("b", joinPaths("", "b"));
    }

    @Test
    public void testJoinRightEmptyPaths() throws Exception {
        assertEquals("a.", joinPaths("a", ""));
    }

    @Test
    public void testParentPath() throws Exception {
        assertEquals("a", getParentPath("a.b"));
    }

    @Test
    public void testParentPathLonger() throws Exception {
        assertEquals("a.b", getParentPath("a.b.c"));
    }

    @Test
    public void testRootParentPath() throws Exception {
        assertEquals("", getParentPath("a"));
    }

    @Test
    public void testPathWithoutParents() throws Exception {
        e.expect(InvalidConfigException.class);
        e.expectMessage("Path '' doesn't have any parents");

        getParentPath("");
    }
}