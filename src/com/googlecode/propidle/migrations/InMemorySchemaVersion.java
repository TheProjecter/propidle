package com.googlecode.propidle.migrations;

import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Right;

public class InMemorySchemaVersion implements SchemaVersion {
    public Either<Throwable, Integer> versionNumber() {
        return Right.right(0);
    }
}
