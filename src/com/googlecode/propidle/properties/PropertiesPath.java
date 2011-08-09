package com.googlecode.propidle.properties;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.io.HierarchicalPath;
import com.googlecode.propidle.NormalisedHierarchicalPath;

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

    public static Callable1<String, PropertiesPath> toPropertiesPath() {
        return new Callable1<String, PropertiesPath>() {
            public PropertiesPath call(String value) throws Exception {
                return PropertiesPath.propertiesPath(value);
            }
        };
    }

}
