package com.googlecode.propidle;

import java.util.Date;
import static java.lang.String.format;

public class Coercions {
    @SuppressWarnings("unchecked")
    public static <T> T coerce(Object o, Class<T> aClass) {
        if (o == null || aClass.isInstance(o)) {
            return (T) o;
        }
        if (o.getClass().getName().equals("oracle.sql.TIMESTAMP")) {
            if (Date.class.isAssignableFrom(aClass)) {
                return (T) OracleTimestampCoercer.toTimeStamp(o);
            }
        }
        throw new UnsupportedOperationException(format("Cannot convert %s to %s", o, aClass.getCanonicalName()));
    }
}
