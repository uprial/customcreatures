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

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(contents.get(0));
        int contentsSize = contents.size();
        for (int i = 1; i < contentsSize; i++) {
            stringBuilder.append(delimiter);
            stringBuilder.append(contents.get(i));
        }

        return stringBuilder.toString();
    }

    public static String joinPaths(String rootPath, String childPath) {
        return !rootPath.isEmpty() ? (rootPath + '.' + childPath) : childPath;
    }
}
