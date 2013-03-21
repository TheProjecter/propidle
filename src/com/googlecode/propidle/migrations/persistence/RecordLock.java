package com.googlecode.propidle.migrations.persistence;

import com.googlecode.lazyrecords.Definition;

public interface RecordLock {
    public void aquire(Definition recordName);
}
