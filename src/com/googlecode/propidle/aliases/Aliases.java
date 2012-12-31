package com.googlecode.propidle.aliases;

public interface Aliases {


    public Aliases put(Alias alias);

    public Alias get(AliasPath path);

    public Iterable<Alias> getAll();

    Aliases delete(AliasPath aliasToDelete);
}
