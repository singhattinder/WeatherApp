package com.attinder.weatherdemoapp.utils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dataParser {

    public static String unixTimeParse(long unixTimeStamp) {
        Date date = new Date(unixTimeStamp * 1000);
        SimpleDateFormat dateFormat =  new SimpleDateFormat("h:mm a");
        return dateFormat.format(date);
    }

    public static int tempParse(float temp){
        return  (int) (temp - 273.5);
    }
}
