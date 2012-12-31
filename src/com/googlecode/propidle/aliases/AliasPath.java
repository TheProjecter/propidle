package com.googlecode.propidle.aliases;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.utterlyidle.io.HierarchicalPath;
import com.googlecode.propidle.NormalisedHierarchicalPath;

public class AliasPath extends NormalisedHierarchicalPath {
    public static AliasPath aliasPath(String value) {
        return value == null ? null : new AliasPath(value);
    }

    private AliasPath(String value) {
        super(value);
    }


    public static class constructors {
        public static Callable1<String, AliasPath> aliasPath() {
            return new Callable1<String, AliasPath>() {
                @Override
                public AliasPath call(String value) throws Exception {
                    return AliasPath.aliasPath(value);
                }
            };
        }
    }

}