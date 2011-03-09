package com.googlecode.propidle.util;

public class Strings {
    public static String reduceToAlphaNumerics(String value) {
        return value.replaceAll("[^0-9a-zA-Z]+", " ");
    }

    public static boolean isEmpty(String value) {
        return value == null || "".equals(value.trim());
    }
}
