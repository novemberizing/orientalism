package net.novemberizing.core;

public class StringUtil {
    public static boolean equal(String x, String y) {
        if(x == null && y == null){
            return true;
        } else if(x != null) {
            if(y == null){
                return false;
            }
            return x.equals(y);
        }
        return false;
    }

    public static String merge(String[] strings, int n) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < n && i < strings.length; i++) {
            builder.append(strings[i]);
        }
        return builder.toString();
    }

    public static String get(CharSequence o) {
        return o != null ? o.toString() : null;
    }

    public static String get(String o, String value) {
        return o != null ? o : value;
    }
}
