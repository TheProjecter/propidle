package com.googlecode.propidle.aliases;

import com.googlecode.utterlyidle.io.Url;

public class AliasDestination {
    private final Url url;

    public static AliasDestination aliasDestination(String value) {
        return new AliasDestination(value);
    }

    private AliasDestination(String value) {
        this.url = Url.url(value);
    }

    public Url url() {
        return url;
    }

    @Override
    public String toString() {
        return url.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AliasDestination that = (AliasDestination) o;

        if (!url.equals(that.url)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
