package net.novemberizing.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String str() {
        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", java.util.Locale.getDefault());
        return date.format(new Date());
    }
}
