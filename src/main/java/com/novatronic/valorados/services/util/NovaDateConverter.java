package com.novatronic.valorados.services.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;
import org.apache.log4j.Logger;

@SuppressWarnings("rawtypes")
public class NovaDateConverter implements Converter {

    private final static Logger logger = LoggerHelper.getLogger();

    public Object convert(Class clazz, Object value) {
        if (clazz.equals(Date.class)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                return sdf.parse((String) value);
            } catch (ParseException pe) {
                logger.warn("No es posible parsear la fecha: " + value);
                return null;
            } catch (ClassCastException cce) {
                throw new IllegalArgumentException(cce);
            }
        } else {
            throw new IllegalArgumentException("Expected Date class");
        }
    }
}
