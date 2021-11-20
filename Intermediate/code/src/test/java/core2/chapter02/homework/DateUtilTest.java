package core2.chapter02.homework;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DateUtilTest {
    @Test
    public void testIsLeapYear() {
        assertEquals(false, DateUtil.isLeapYear(-100));
        assertEquals(false, DateUtil.isLeapYear(1000));
        assertEquals(false, DateUtil.isLeapYear(20000));
        assertEquals(true, DateUtil.isLeapYear(2020));
        assertEquals(false, DateUtil.isLeapYear(2019));
        assertEquals(true, DateUtil.isLeapYear(2000));
        assertEquals(false, DateUtil.isLeapYear(1900));
    }
}
