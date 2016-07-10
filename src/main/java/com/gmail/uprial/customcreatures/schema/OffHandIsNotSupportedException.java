package com.gmail.uprial.customcreatures.schema;

public class OffHandIsNotSupportedException extends Exception {
    public OffHandIsNotSupportedException() {
        super("off-hand isn't supported by this version of Minecraft");
    }
}