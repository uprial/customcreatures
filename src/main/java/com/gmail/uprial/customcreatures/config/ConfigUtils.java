package com.gmail.uprial.customcreatures.config;

public final class ConfigUtils {
    public static String getParentPath(String path) throws InvalidConfigException {
        if (path.length() < 1) {
            throw new InvalidConfigException(String.format("Path '%s' doesn't have any parents", path));
        }
        int index = path.lastIndexOf(".");

        return (index == -1) ? "" : path.substring(0, index);
    }
}
