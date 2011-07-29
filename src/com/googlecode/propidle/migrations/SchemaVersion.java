package com.googlecode.propidle.migrations;

import com.googlecode.totallylazy.Either;

public interface SchemaVersion {
    Either<Throwable, Integer> versionNumber() throws Exception;
}
