package com.googlecode.propidle.status;

import org.apache.lucene.store.Directory;

import java.io.IOException;

import static com.googlecode.propidle.status.StatusCheckName.statusCheckName;
import static com.googlecode.propidle.status.StatusCheckResult.statusCheckResult;

public class LuceneDirectoryCheck implements StatusCheck{
    private Directory luceneDirectory;

    public LuceneDirectoryCheck(Directory luceneDirectory) {
        this.luceneDirectory = luceneDirectory;
    }

    public StatusCheckResult check() {
        return statusCheckResult(
                statusCheckName(LuceneDirectoryCheck.class.getSimpleName())).
                add("Can read Lucene Directory", canRead() ? "PASS" : "FAIL");
    }

    public boolean canRead() {
        try {
            return luceneDirectory.listAll() != null;
        } catch (IOException e) {
            return false;
        }
    }
}
