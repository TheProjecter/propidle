package com.googlecode.propidle.persistence;

import com.googlecode.totallylazy.Either;

public interface SchemaVersion {
    Either<Throwable, Integer> versionNumber() throws Exception;
}
