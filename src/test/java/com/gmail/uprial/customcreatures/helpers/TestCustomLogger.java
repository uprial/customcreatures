package com.gmail.uprial.customcreatures.helpers;

import com.gmail.uprial.customcreatures.common.CustomLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

class TestCustomLogger extends CustomLogger {
    private boolean failOnDebug = false;
    private boolean failOnAny = false;

    public TestCustomLogger() {
        super(Logger.getLogger("test"));
    }

    public void doFailOnDebug() {
        this.failOnDebug = true;
    }

    public void doFailOnAny() {
        this.failOnAny = true;
    }

    @Override
    protected void log(Level level, String message) {
        if (failOnAny) {
            fail(message);
        }
    }

    @Override
    public void debug(String message) {
        if (failOnDebug) {
            fail(message);
        }
    }

    private void fail(String message) {
        throw new RuntimeException(message);
    }
}
