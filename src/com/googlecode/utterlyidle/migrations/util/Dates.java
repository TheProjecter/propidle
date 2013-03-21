package com.googlecode.utterlyidle.migrations.util;

import java.util.Date;

public class Dates {
    public static Date stripMillis(Date date) {
        return new Date(Math.abs(date.getTime()/1000)*1000);
    }
}
