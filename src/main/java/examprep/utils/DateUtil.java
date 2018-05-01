package com.nihonsoftwork.smskitti.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String getAge(Date birthDate) {

        int yearNow, monthNow, dayNow, yearPast, monthPast, dayPast, yearDiff, monthDiff, dayDiff;

        Calendar cal = Calendar.getInstance();
        yearNow = cal.get(Calendar.YEAR);
        monthNow = cal.get(Calendar.MONTH) + 1;
        dayNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDate);
        yearPast = cal.get(Calendar.YEAR);
        monthPast = cal.get(Calendar.MONTH) + 1;
        dayPast = cal.get(Calendar.DAY_OF_MONTH);

        if (dayNow < dayPast) {
            dayNow += 31;
            monthNow -= 1;
        }
        dayDiff = dayNow - dayPast;

        if (monthNow < monthPast) {
            monthNow += 12;
            yearNow -= 1;
        }
        monthDiff = monthNow - monthPast;

        yearDiff = yearNow - yearPast;

        if (yearDiff <= 0) {
            if (monthDiff <= 0) {
                return (dayDiff + " days");
            }
            return (monthDiff + "m " + dayDiff + "d");
        }
        return (yearDiff + "y " + monthDiff + "m " + dayDiff + "d");
    }

    public static String getApproxAge(Date birthDate) {

        int yearNow, monthNow, dayNow, yearPast, monthPast, dayPast, yearDiff, monthDiff, dayDiff;

        Calendar cal = Calendar.getInstance();
        yearNow = cal.get(Calendar.YEAR);
        monthNow = cal.get(Calendar.MONTH) + 1;
        dayNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDate);
        yearPast = cal.get(Calendar.YEAR);
        monthPast = cal.get(Calendar.MONTH) + 1;
        dayPast = cal.get(Calendar.DAY_OF_MONTH);

        if (dayNow < dayPast) {
            dayNow += 31;
            monthNow -= 1;
        }
        dayDiff = dayNow - dayPast;

        if (monthNow < monthPast) {
            monthNow += 12;
            yearNow -= 1;
        }
        monthDiff = monthNow - monthPast;

        yearDiff = yearNow - yearPast;

        if (yearDiff <= 0) {
            if (monthDiff <= 0) {
                return ("~ " + dayDiff + " days");
            }
            return ("~ " + monthDiff + " months");
        }
        return ("~ " + yearDiff + " years");
    }

    public static Date getBirthDate(String age) throws ParseException {
        //Age must be year.month format
        String[] ageArray = age.split(".");
        int year, month;

        year = Integer.parseInt(ageArray[0]);
        month = Integer.parseInt(ageArray[1]);

        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);

        year = yearNow - year;
        month = Math.abs(monthNow - month);

        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy");
        return sdf.parse(dayNow + "/" + month + "/" + year);
    }

    public static Date currentDateTime() throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return stringToDateTime(format.format(new Date()));
    }

    public static Date currentDate() throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return stringToDate(format.format(new Date()));
    }

    public static String currentDateTimeString() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    public static String currentDateString() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }

    public static String dateToString(Date d) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(d);
    }

    public static String dateTimeToString(Date d) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(d);
    }

    public static Date stringToDate(String sDate) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(sDate);
    }

    public static Date stringToDateTime(String sDate) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(sDate);
    }

    public static Date millisecondsToDate(long milliSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return calendar.getTime();
    }

    public static Long dateToMilliseconds(String sDate) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(sDate).getTime();
    }

    public static Date AddYear(int year) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        c.add(Calendar.YEAR, year);
        return c.getTime();
    }
}
