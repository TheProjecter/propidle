package com.googlecode.propidle.util.tinytype;

import java.util.Date;

public abstract class DateTinyType<T extends TinyType<Date,?>> extends TinyType<Date, T>{
    protected DateTinyType(Date value) {
        super(value);
    }
}
