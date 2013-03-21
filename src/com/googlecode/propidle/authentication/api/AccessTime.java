package com.googlecode.propidle.authentication.api;


import com.googlecode.propidle.util.tinytype.LongTinyType;

public class AccessTime extends LongTinyType<AccessTime> {

    public AccessTime(Long value) {
        super(value);
    }

    public static AccessTime accessTime(Long value) {
         return new AccessTime(value);
    }

    public static AccessTime neverAccessed() {
        return accessTime(0L);
    }
}
