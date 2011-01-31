package com.googlecode.propidle;

import com.googlecode.utterlyidle.io.HierarchicalPath;

public class PropertiesPath extends NormalisedHierarchicalPath {
    public static PropertiesPath propertiesPath(HierarchicalPath value) {
        return propertiesPath(value.toString());
    }
    public static PropertiesPath propertiesPath(String value) {
        return value == null ? null : new PropertiesPath(value);
    }

    protected PropertiesPath(String value) {
        super(value);
    }

}
