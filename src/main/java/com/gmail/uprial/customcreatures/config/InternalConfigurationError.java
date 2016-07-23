package com.gmail.uprial.customcreatures.config;

@SuppressWarnings("ExceptionClassNameDoesntEndWithException")
public class InternalConfigurationError extends RuntimeException {
    public InternalConfigurationError(String message) {
        super(message);
    }
}
