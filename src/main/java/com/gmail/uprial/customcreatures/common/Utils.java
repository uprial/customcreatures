package com.gmail.uprial.customcreatures.common;

import java.util.List;

public class Utils {
    public static int seconds2ticks(int seconds) {
        return seconds * 20;
    }

    public static <T extends Object> String joinStrings(String delimiter, List<T> contents) {
        if (contents.size() < 1)
            return "";

        String content = contents.get(0).toString();
        for (int i = 1; i < contents.size(); i++) {
            content += delimiter + contents.get(i).toString();
        }

        return content;
    }

    public static String joinPaths(String rootPath, String childPath) {
        String path;
        if (rootPath.length() > 0) {
            path = rootPath + "." + childPath;
        } else {
            path = childPath;
        }

        return path;
    }

    public static String getParentPath(String path) throws InvalidConfigException {
        if (path.length() < 1) {
            throw new InvalidConfigException(String.format("Path '%s' doesn't have any parents", path));
        }
        int index = path.lastIndexOf(".");
        String parentPath;
        if (-1 == index) {
            parentPath = "";
        } else {
            parentPath = path.substring(0, index);
        }

        return parentPath;
    }
}
