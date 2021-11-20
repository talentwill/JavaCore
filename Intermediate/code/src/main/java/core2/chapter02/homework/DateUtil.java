package core2.chapter02.homework;

public class DateUtil {
    public static boolean isLeapYear(int year) {

        // The year must be greater than 0 and less than or equal to 10000.
        if (year <= 0 || year > 10000)
            return false;

        // 1. The year is not divisible by 100, but is divisible by 4.
        if (year % 100 != 0 && year % 4 == 0)
            return true;

        // 2. The year is divisible by 100 and divisible by 400.
        if (year % 100 == 0 && year % 400 == 0)
            return true;

        return false;
    }
}
