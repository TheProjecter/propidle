package com.googlecode.propidle.util;

import static java.lang.String.format;

public class HumanReadable extends StringTinyType<HumanReadable>{
    public static HumanReadable humanReadable(String format, Object... args) {
        return humanReadable(format(format, args));
    }
    public static HumanReadable humanReadable(String value) {
        return new HumanReadable(value);
    }

    private HumanReadable(String value) {
        super(value);
    }
}
