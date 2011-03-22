package com.googlecode.propidle.migrations;

import com.googlecode.propidle.util.tinytype.StringTinyType;

public class ModuleName extends StringTinyType<ModuleName>{
    public static ModuleName moduleName(String value) {
        return new ModuleName(value);
    }

    protected ModuleName(String value) {
        super(value);
    }
}
