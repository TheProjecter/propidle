package com.googlecode.propidle.util;

import java.util.Date;

public class SystemClock implements Clock{
    public Date currentTime() {
        return new Date();
    }
}
