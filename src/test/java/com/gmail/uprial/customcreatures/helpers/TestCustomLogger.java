package com.gmail.uprial.customcreatures.helpers;

import com.gmail.uprial.customcreatures.common.CustomLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

class TestCustomLogger extends CustomLogger {
    private boolean failOnDebug = false;
    private boolean failOnAny = false;
    private boolean failOnError = true;

    public TestCustomLogger() {
        super(Logger.getLogger("test"));
    }

    public void doFailOnDebug() {
        this.failOnDebug = true;
    }

    public void doFailOnAny() {
        this.failOnAny = true;
    }

    public void doNotFailOnError() {
        this.failOnError = false;
    }

    @Override
    protected void log(Level level, String message) {
        if (failOnAny) {
            fail(message);
        } else if ((failOnError) && (level == Level.SEVERE || level == Level.WARNING)) {
            fail(message);
        }
    }

    @Override
    public void debug(String message) {
        if (failOnDebug || failOnAny) {
            fail(message);
        }
    }

    private void fail(String message) {
        throw new RuntimeException(message);
    }
}
