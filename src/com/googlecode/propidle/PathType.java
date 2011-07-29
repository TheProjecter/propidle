package com.googlecode.propidle;

public enum PathType {
    DIRECTORY,
    FILE,
    UNKNOWN;

    public static PathType tryToResolve(String value) {
        try {
            return PathType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
