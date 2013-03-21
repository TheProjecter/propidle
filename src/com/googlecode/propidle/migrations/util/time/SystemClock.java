package com.googlecode.propidle.migrations.util.time;

import java.util.Date;

public class SystemClock implements Clock{
    public Date time() {
        return new Date();
    }
}
