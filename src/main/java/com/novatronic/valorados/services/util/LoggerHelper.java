package com.novatronic.valorados.services.util;

import org.apache.log4j.Logger;

public final class LoggerHelper {

    public static Logger getLogger() {
        final Throwable t = new Throwable();
        t.fillInStackTrace();
        return Logger.getLogger(t.getStackTrace()[1].getClassName());
    }
}
