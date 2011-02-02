package com.googlecode.propidle.search;

public interface Searcher<T> {
    Iterable<T> search(Query query);
}
