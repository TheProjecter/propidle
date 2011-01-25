package com.googlecode.propidle;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;
import com.googlecode.totallylazy.records.Records;

import java.util.Map;
import java.util.Properties;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.Keyword.keyword;
import static com.googlecode.totallylazy.records.MapRecord.record;

public class AllPropertiesFromRecords implements AllProperties {
    private final Records records;

    private static final Keyword PROPERTIES = keyword("properties");
    private static final Keyword<String> PATH = keyword("path", String.class);
    private static final Keyword<String> NAME = keyword("name", String.class);
    private static final Keyword<String> VALUE = keyword("value", String.class);

    public AllPropertiesFromRecords(Records records) {
        this.records = records;
    }

    public Properties get(PropertiesPath path) {
        return records.get(PROPERTIES).
                filter(where(PATH, is(path.toString()))).
                fold(new Properties(), deserialise());
    }

    public AllProperties put(PropertiesPath path, Properties properties) {
        records.remove(PROPERTIES, where(PATH, is(path.toString())));
        records.add(PROPERTIES, serialise(path, properties));
        return this;
    }

    private Callable2<? super Properties, ? super Record, Properties> deserialise() {
        return new Callable2<Properties, Record, Properties>() {
            public Properties call(Properties properties, Record record) throws Exception {
                properties.setProperty(record.get(NAME), record.get(VALUE));
                return properties;
            }
        };
    }

    private Sequence<Record> serialise(PropertiesPath path, Properties properties) {
        return sequence(properties.entrySet()).zipWithIndex().map(serialiseWithPath(path));
    }

    private Callable1<? super Pair<Number, Map.Entry<Object, Object>>, Record> serialiseWithPath(final PropertiesPath path) {
        return new Callable1<Pair<Number, Map.Entry<Object, Object>>, Record>() {
            public Record call(Pair<Number, Map.Entry<Object, Object>> indexAndEntry) throws Exception {
                Map.Entry<Object, Object> entry = indexAndEntry.second();
                return record().
                        set(PATH, path.toString()).
                        set(NAME, (String) entry.getKey()).
                        set(VALUE, (String) entry.getValue());
            }
        };
    }

    public static Records definePropertiesRecord(Records records){
        records.define(PROPERTIES, PATH, NAME, VALUE);
        return records;
    }
}
