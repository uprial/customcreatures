package com.gmail.uprial.customcreatures.common;

import java.util.List;

public final class Utils {
    public static int seconds2ticks(int seconds) {
        return seconds * 20;
    }

    public static <T> String joinStrings(String delimiter, List<T> contents) {
        if (contents.size() < 1) {
            return "";
        }

        String content = contents.get(0).toString();
        int contentsSize = contents.size();
        for (int i = 1; i < contentsSize; i++) {
            content += delimiter + contents.get(i);
        }

        return content;
    }

    public static String joinPaths(String rootPath, String childPath) {
        return !rootPath.isEmpty() ? rootPath + '.' + childPath : childPath;
    }
}
