package com.googlecode.propidle.status;

import com.googlecode.propidle.util.tinytype.StringTinyType;


public class StatusCheckName extends StringTinyType {
    public static StatusCheckName statusCheckName(String value) {
        return new StatusCheckName(value);
    }
    
    protected StatusCheckName(String value) {
        super(value);
    }
}
