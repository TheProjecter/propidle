package com.googlecode.propidle.authorisation.permissions;

import com.googlecode.propidle.util.StringTinyType;

public class Permission extends StringTinyType<Permission>{
    public static final Permission MANAGE_USERS = permission("Add user");

    public static Permission permission(String value) {
        return new Permission(value);
    }
    protected Permission(String value) {
        super(value);
    }
}
