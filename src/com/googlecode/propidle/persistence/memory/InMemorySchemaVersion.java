package com.googlecode.propidle.persistence.memory;

import com.googlecode.propidle.persistence.SchemaVersion;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Right;

public class InMemorySchemaVersion implements SchemaVersion {
    public Either<Throwable, Integer> versionNumber() {
        return Right.right(0);
    }
}
