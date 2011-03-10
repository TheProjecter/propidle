package com.googlecode.propidle.server;

import com.googlecode.propidle.persistence.SchemaVersion;
import com.googlecode.totallylazy.Either;

import java.util.concurrent.Callable;

public class ReportSchemaVersion implements Callable<Either<Throwable, Integer>> {
    private final SchemaVersion schemaVersion;

    public ReportSchemaVersion(SchemaVersion schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public Either<Throwable, Integer> call() throws Exception {
        return schemaVersion.versionNumber();
    }
}
