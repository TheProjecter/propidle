package com.googlecode.propidle.authorisation.users;

import com.googlecode.propidle.util.tinytype.StringTinyType;

public class Password extends StringTinyType<Password> {
    public static Password password(String value){
        return new Password(value);
    }
    protected Password(String value) {
        super(value);
    }
}