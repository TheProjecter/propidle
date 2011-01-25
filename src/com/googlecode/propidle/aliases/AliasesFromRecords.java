package com.googlecode.propidle.aliases;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

import static com.googlecode.propidle.aliases.Alias.alias;
import static com.googlecode.propidle.aliases.AliasDestination.aliasDestination;
import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;
import static com.googlecode.utterlyidle.io.Url.url;

public class AliasesFromRecords implements Aliases{
    private static final Keyword ALIASES = keyword("alias");
    private static final Keyword<String> FROM = keyword("from_resource", String.class);
    private static final Keyword<String> TO = keyword("to_url", String.class);

    private final Records records;

    public AliasesFromRecords(Records records) {
        this.records = records;
    }

    public Aliases put(Alias alias) {
        records.remove(ALIASES, where(FROM, is(alias.from().toString())));
        records.add(ALIASES, serialise(alias));
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

    public static Records defineAliasRecord(Records records){
        records.define(ALIASES, FROM, TO);
        return records;
    }
}
