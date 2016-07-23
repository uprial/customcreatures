package com.gmail.uprial.customcreatures.schema.exceptions;

public class MethodIsNotSupportedException extends Exception {
    public MethodIsNotSupportedException(String method) {
        super(String.format("Method '%s' isn't supported by this version of Minecraft", method));
    }
}
