package com.googlecode.propidle.persistence;

import com.googlecode.totallylazy.records.Keyword;

public interface RecordLock {
    public void aquire(Keyword recordName);
}
