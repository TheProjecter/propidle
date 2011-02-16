package com.googlecode.propidle.migrations.history;

import com.googlecode.propidle.migrations.MigrationNumber;
import com.googlecode.propidle.migrations.MigrationName;
import com.googlecode.propidle.migrations.Migration;
import com.googlecode.propidle.Dates;
import static com.googlecode.propidle.Dates.stripMillis;

import java.util.Date;
import static java.lang.String.format;

public class MigrationEvent {
    private final Date dateRun;
    private final MigrationNumber number;
    private final MigrationName name;

    public MigrationEvent(Date dateRun, Migration migration) {
        this(dateRun, migration.number(), migration.name());
    }

    public MigrationEvent(Date dateRun, MigrationNumber number, MigrationName name) {
        this.dateRun = dateRun;
        this.number = number;
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

    @Override
    public String toString() {
        return format("%s-%s on %s", number(), name, dateRun);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MigrationEvent that = (MigrationEvent) o;

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
}
