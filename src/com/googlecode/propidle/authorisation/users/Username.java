package com.googlecode.propidle.authorisation.users;

import com.googlecode.propidle.util.StringTinyType;

public class Username extends StringTinyType<Username>{
    public static final Username GUEST = username("guest");

    public static Username username(String value){
        return new Username(value);
    }
    protected Username(String value) {
        super(value);
    }
}
