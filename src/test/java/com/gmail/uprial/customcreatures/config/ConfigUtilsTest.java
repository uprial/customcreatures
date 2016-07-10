package com.gmail.uprial.customcreatures.config;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.gmail.uprial.customcreatures.config.ConfigUtils.getParentPath;
import static org.junit.Assert.assertEquals;

public class ConfigUtilsTest {
    @Rule
    public final ExpectedException e = ExpectedException.none();

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