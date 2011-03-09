package com.googlecode.propidle.aliases;

import com.googlecode.utterlyidle.io.HierarchicalPath;
import com.googlecode.propidle.NormalisedHierarchicalPath;

public class AliasPath extends NormalisedHierarchicalPath {
    public static AliasPath aliasPath(HierarchicalPath value) {
        return aliasPath(value.toString());
    }
    public static AliasPath aliasPath(String value) {
        return value == null ? null : new AliasPath(value);
    }

    private AliasPath(String value) {
        super(value);
    }

}