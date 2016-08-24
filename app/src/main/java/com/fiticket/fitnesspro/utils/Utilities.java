package com.fiticket.fitnesspro.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fiticket on 01/02/16.
 */
public class Utilities {

    public static String convertEpochToDate(long epochtime){
        String date="";
        if(epochtime!=0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
            date = sdf.format(new Date(epochtime));
            if (date == null)
                return "";
        }
        return date;
    }


}
