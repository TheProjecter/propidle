package com.googlecode.propidle.util;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TemporaryIndex {
    public static Directory emptyFileSystemDirectory() {
        File indexDirectory = createTempDirectory();
        return directory(indexDirectory);
    }

    private static File createTempDirectory() {
        try {
            File temp = File.createTempFile("index", UUID.randomUUID().toString());

            if (!(temp.delete())) {
                throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
            }

            if (!(temp.mkdir())) {
                throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
            }
            return temp;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FSDirectory directory(File file) {
        if (file.exists()) {
            assertThat("Should have deleted lucene directory before recreating", file.delete());
        }
        try {
            FSDirectory directory = FSDirectory.open(file);
            System.out.println("Created index at " + file.getAbsolutePath());
            return directory;
        } catch (IOException e) {
            throw new RuntimeException("Could not create file system index at " + file.getAbsolutePath(), e);
        }
    }
}
