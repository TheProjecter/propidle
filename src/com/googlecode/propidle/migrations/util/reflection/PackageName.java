package com.googlecode.propidle.migrations.util.reflection;

import com.googlecode.propidle.util.tinytype.StringTinyType;

public class PackageName extends StringTinyType<PackageName> {
    protected PackageName(String value) {
        super(value);
    }
}
