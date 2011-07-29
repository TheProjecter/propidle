package com.googlecode.propidle.aliases;

import com.googlecode.propidle.util.NullArgumentException;

public class Alias {
    private final AliasPath from;
    private final AliasDestination to;

    public static Alias alias(AliasPath from, AliasDestination to){
        return new Alias(from, to);
    }

    protected Alias(AliasPath from, AliasDestination to) {
        if (from == null) throw new NullArgumentException("from");
        if (to == null) throw new NullArgumentException("to");
        this.from = from;
        this.to = to;
    }

    public AliasPath from() {
        return from;
    }

    public AliasDestination to() {
        return to;
    }

    @Override
    public String toString() {
        return from + "==>" + to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Alias alias = (Alias) o;

        if (!from.equals(alias.from)) return false;
        if (!to.equals(alias.to)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from.hashCode();
        result = 31 * result + to.hashCode();
        return result;
    }
}
