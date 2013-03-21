package com.googlecode.utterlyidle.migrations.log;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.utterlyidle.migrations.Migration;
import com.googlecode.utterlyidle.migrations.MigrationName;
import com.googlecode.utterlyidle.migrations.MigrationNumber;
import com.googlecode.utterlyidle.migrations.ModuleName;

import java.util.Date;

import static com.googlecode.utterlyidle.migrations.util.Dates.stripMillis;
import static java.lang.String.format;

public class MigrationLogItem {
    private final Date dateRun;
    private final MigrationNumber number;
    private final ModuleName moduleName;
    private final MigrationName name;

    public MigrationLogItem(Date dateRun, Migration migration, ModuleName moduleName) {
        this(dateRun, migration.number(), migration.name(), moduleName);
    }

    public MigrationLogItem(Date dateRun, MigrationNumber number, MigrationName name, ModuleName moduleName) {
        this.dateRun = dateRun;
        this.number = number;
        this.moduleName = moduleName;
        this.name = name;
    }

    public Date dateRun() {
        return dateRun;
    }

    public MigrationNumber number() {
        return number;
    }

    public MigrationName name() {
        return name;
    }

    public ModuleName moduleName() {
        return moduleName;
    }

    @Override
    public String toString() {
        return format("%S: %s-%s (%s)", dateRun, number(), name, moduleName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MigrationLogItem that = (MigrationLogItem) o;

        if (!stripMillis(dateRun).equals(stripMillis(that.dateRun))) return false;
        if (!name.equals(that.name)) return false;
        if (!number.equals(that.number)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dateRun.hashCode();
        result = 31 * result + number.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public static Callable1<MigrationLogItem, MigrationNumber> getMigrationNumber() {
        return new Callable1<MigrationLogItem, MigrationNumber>() {
            public MigrationNumber call(MigrationLogItem migrationLogItem) throws Exception {
                return migrationLogItem.number();
            }
        };
    }

    public static Predicate<MigrationLogItem> forModule(final ModuleName moduleName) {
        return new Predicate<MigrationLogItem>() {
            public boolean matches(MigrationLogItem migrationLogItem) {
                return migrationLogItem.moduleName().equals(moduleName);
            }
        };
    }
}
