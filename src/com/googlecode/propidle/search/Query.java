package com.googlecode.propidle.search;

import com.googlecode.propidle.util.Strings;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.ArrayList;

import static com.googlecode.totallylazy.Sequences.sequence;

public class Query {
    private final String query;
    private final Sequence<String> againstFieldNames;

    public static Query query(String query) {
        return query(query, new ArrayList<String>());
    }
    public static Query query(String query, String... againstFieldNames) {
        return query(query, sequence(againstFieldNames));
    }
    public static Query query(String query, Iterable<String> againstFieldNames) {
        return new Query(query, againstFieldNames);
    }

    protected Query(String query, Iterable<String> againstFieldNames) {
        this.query = query == null ? "" : query;
        this.againstFieldNames = againstFieldNames == null ? Sequences.<String>empty() : sequence(againstFieldNames);
    }

    public String query() {
        return query;
    }

    public Iterable<String> fieldNames() {
        return againstFieldNames;
    }

    public boolean isEmpty() {
        return Strings.isEmpty(query);
    }

    @Override
    public String toString() {
        String fields = againstFieldNames.isEmpty() ? "" : " against fields: " + againstFieldNames.toString(", ");
        return query + fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Query query1 = (Query) o;

        if (!againstFieldNames.equals(query1.againstFieldNames)) return false;
        if (!query.equals(query1.query)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = query.hashCode();
        result = 31 * result + againstFieldNames.hashCode();
        return result;
    }

    public Query againstFields(Iterable<String> fieldNames) {
        return query(query, fieldNames);
    }

    public Query and(String expression) {
        String newQuery = String.format("(%s) AND %s", query, expression);
        return query(newQuery, againstFieldNames);
    }
}
