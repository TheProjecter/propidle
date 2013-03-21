package com.googlecode.utterlyidle.migrations.persistence;

import com.googlecode.lazyrecords.Definition;

public interface RecordLock {
    public void aquire(Definition recordName);
}
