package com.googlecode.utterlyidle.migrations.util.reflection;

import com.googlecode.utterlyidle.migrations.util.tinytype.StringTinyType;

public class PackageName extends StringTinyType<PackageName> {
    protected PackageName(String value) {
        super(value);
    }
}
