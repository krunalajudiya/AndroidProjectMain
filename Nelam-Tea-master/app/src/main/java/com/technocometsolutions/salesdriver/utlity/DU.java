package com.technocometsolutions.salesdriver.utlity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DU {

    public static final String CALENDAR_VIEW_DATE_FORMAT = "yyyy-MM-dd";
    public static final SimpleDateFormat calenderViewDate = new SimpleDateFormat(CALENDAR_VIEW_DATE_FORMAT, Locale.getDefault());

    public static final String APP_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";


    public static TimeZone timeZone = null;

    public static TimeZone setUserTimeZone() {

        timeZone = TimeZone.getTimeZone("US/Eastern");
        return timeZone;
    }

    public static String getSimpleTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(APP_DATE_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        Date tempDate = null;
        long millis = 0;
        try {
            tempDate = sdf.parse(date);
            millis = tempDate.getTime();

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SimpleDateFormat dateFormatUser = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        try {


            return dateFormatUser.format(millis);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDate(String date,String month,String year){
        return date + "-" + month + "-" + year;
    }


}