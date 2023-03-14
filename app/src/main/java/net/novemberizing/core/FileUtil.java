package net.novemberizing.core;

import android.util.Log;

import java.io.File;

public class FileUtil {
    private static final String Tag = "FileUtil";
    public static String getOnlyName(File f) {
        String name = f.getName();

        String[] strings = name.split("\\.");

        return StringUtil.merge(strings, strings.length - 1);
    }
}
