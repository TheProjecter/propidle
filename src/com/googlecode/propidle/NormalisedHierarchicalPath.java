package com.googlecode.propidle;

import com.googlecode.utterlyidle.io.HierarchicalPath;

public class NormalisedHierarchicalPath extends HierarchicalPath {
    public NormalisedHierarchicalPath(String value) {
        super(ensureStartingSlash(removeEndingSlash(value.trim())));
    }

    public static String removeEndingSlash(String value) {
        return value.endsWith("/") ? value.substring(0,value.length()-1) : value;
    }

    public static String removeStartingSlash(String value) {
        return value.startsWith("/") ? value.substring(1,value.length()) : value;
    }

    public static String ensureStartingSlash(String value) {
        return value.startsWith("/") ? value : "/" + value;
    }
}
