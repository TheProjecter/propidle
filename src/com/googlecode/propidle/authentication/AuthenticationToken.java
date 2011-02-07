package com.googlecode.propidle.authentication;

import com.googlecode.propidle.util.tinytype.UuidTinyType;

import java.util.UUID;

public class AuthenticationToken extends UuidTinyType<AuthenticationToken> {
    public static AuthenticationToken authenticationToken(){
        return authenticationToken(UUID.randomUUID());
    }

    public static AuthenticationToken authenticationToken(UUID value){
        return new AuthenticationToken(value);
    }
    protected AuthenticationToken(UUID value) {
        super(value);
    }
}
