package com.googlecode.propidle.aliases;

import com.googlecode.lazyrecords.Definition;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.lazyrecords.Keyword;
import com.googlecode.lazyrecords.Record;
import com.googlecode.lazyrecords.Records;

import static com.googlecode.lazyrecords.Definition.constructors.definition;
import static com.googlecode.lazyrecords.Record.constructors.record;
import static com.googlecode.propidle.aliases.Alias.alias;
import static com.googlecode.propidle.aliases.AliasDestination.aliasDestination;
import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.lazyrecords.Keywords.keyword;

public class AliasesFromRecords implements Aliases{
    private static final Keyword<String> FROM = keyword("from_resource", String.class);
    private static final Keyword<String> TO = keyword("to_url", String.class);
    private static final Definition ALIASES = definition("aliases", FROM, TO);

    private final Records records;

    public AliasesFromRecords(Records records) {
        this.records = records;
    }

    public Aliases put(Alias alias) {
        records.remove(ALIASES, where(FROM, is(alias.from().toString())));
        records.add(ALIASES, serialise(alias));
        return this;
    }

    public Aliases delete(AliasPath alias) {
        records.remove(ALIASES, where(FROM, is(alias.toString())));
        return this;
    }

    public Alias get(AliasPath path) {
        Option<Record> record = records.get(ALIASES).
                filter(where(FROM, is(path.toString())))
                .headOption();
        if(record.isEmpty()) return null;

        return deserialise(record.get());
    }

    public Iterable<Alias> getAll() {
        return records.get(ALIASES).map(deserialise());
    }

    private Record serialise(Alias alias) {
        return record().set(FROM, alias.from().toString()).set(TO, alias.to().toString());
    }

    private Alias deserialise(Record record) {
        return alias(aliasPath(record.get(FROM)), aliasDestination(record.get(TO)));
    }

    private Callable1<? super Record, Alias> deserialise() {
        return new Callable1<Record, Alias>() {
            public Alias call(Record record) throws Exception {
                return deserialise(record);
            }
        };
    }

}
