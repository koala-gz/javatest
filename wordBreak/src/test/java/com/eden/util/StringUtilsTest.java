package com.eden.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {
    @Test
    public void trimTest() {
        assertEquals("abc", StringUtils.trim(" abc "));

        assertEquals("", StringUtils.trim(null));
    }
}