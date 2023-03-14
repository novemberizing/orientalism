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
}
