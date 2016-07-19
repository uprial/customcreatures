package com.gmail.uprial.customcreatures.config;

public class ConfigUtils {
    public static String getParentPath(String path) throws InvalidConfigException {
        if (path.length() < 1) {
            throw new InvalidConfigException(String.format("Path '%s' doesn't have any parents", path));
        }
        int index = path.lastIndexOf(".");
        String parentPath;
        if (index == -1) {
            parentPath = "";
        } else {
            parentPath = path.substring(0, index);
        }

        return parentPath;
    }
}
