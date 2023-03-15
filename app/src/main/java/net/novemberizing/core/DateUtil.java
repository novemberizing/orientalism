package net.novemberizing.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date from(String str) {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());
        try {
            return date.parse(str);
        } catch (ParseException e) {
            return new Date();
        }
    }
}
