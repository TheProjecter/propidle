package com.googlecode.propidle.status;

import com.googlecode.propidle.util.tinytype.StringTinyType;

public class ActionName extends StringTinyType<ActionName>{
    public static ActionName actionName(String value) {
        return new ActionName(value);
    }
    
    protected ActionName(String value) {
        super(value);
    }
}
