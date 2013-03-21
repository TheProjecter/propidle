package com.googlecode.utterlyidle.migrations;

import com.googlecode.totallylazy.Callable1;

import static java.lang.String.format;

public class Migration implements Runnable {
    protected final MigrationId id;
    private final Runnable action;

    public static Migration migration(MigrationId id, Runnable action) {
        return new Migration(id, action);
    }

    private Migration(MigrationId id, Runnable action) {
        this.id = id;
        this.action = action;
    }

    public void run() {
        try {
            action.run();
        } catch (Exception e) {
            throw new RuntimeException(format("Could not execute migration %s-%s", number(), name()), e);
        }
    }

    public MigrationNumber number() {
        return id.number();
    }

    public MigrationName name() {
        return id.name();
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public static Callable1<Migration, MigrationNumber> getMigrationNumber() {
        return new Callable1<Migration, MigrationNumber>() {
            public MigrationNumber call(Migration migrationFile) throws Exception {
                return migrationFile.number();
            }
        };
    }
}
