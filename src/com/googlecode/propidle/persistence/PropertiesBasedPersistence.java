package com.googlecode.propidle.persistence;

import com.googlecode.propidle.persistence.jdbc.SqlPersistenceModule;
import com.googlecode.propidle.persistence.jdbc.hsql.HsqlModule;
import com.googlecode.propidle.persistence.jdbc.oracle.OracleModule;
import com.googlecode.propidle.persistence.memory.InMemoryPersistenceModule;
import static com.googlecode.propidle.properties.Properties.getOrFail;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.utterlyidle.modules.Module;

import static java.lang.String.format;
import java.util.Properties;

public class PropertiesBasedPersistence {
    public static final String PERSISTENCE = "persistence";

    public static enum Option {
        HSQL,
        ORACLE,
        IN_MEMORY
    }

    public static Sequence<Module> persistenceStrategy(Properties properties) {
        return persistenceStrategy(parse(getOrFail(properties, PERSISTENCE)));
    }

    public static Sequence<Module> persistenceStrategy(Option option) {
        switch (option) {
            case HSQL:
                return sequence(new SqlPersistenceModule(), new HsqlModule()).safeCast(Module.class);
            case ORACLE:
                return sequence(new SqlPersistenceModule(), new OracleModule()).safeCast(Module.class);
            case IN_MEMORY:
                return sequence(new InMemoryPersistenceModule()).safeCast(Module.class);
            default:
                throw new UnsupportedOperationException(format("Peristence for '%s' is not implemented", option));
        }
    }

    private static Option parse(String value) {
        try {
            return Option.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(format("'%s' is not a supported persistence method", value), e);
        }
    }
}
