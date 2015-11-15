package org.bridge.util;

import junit.framework.TestCase;

/**
 * Created by bridgegeorge on 15/11/14.
 */
public class DateUtilTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void testFormatTime() throws Exception {
        String actual = DateUtil.formatTime("2015-11-14 18:50:10");
        String expected = "30分钟前";
        assertEquals(expected, actual);
    }
}