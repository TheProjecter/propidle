package com.googlecode.utterlyidle.migrations.util.reflection;

import com.googlecode.totallylazy.Callable1;

import java.net.URL;

public class CodeSourceEntry {
    private final URL root;
    private final URL entry;

    public CodeSourceEntry(URL root,URL entry) {
        this.root = root;
        this.entry = entry;
    }

    public URL url() {
        return entry;
    }

    public static Callable1<? super CodeSourceEntry, URL> getUrl() {
        return new Callable1<CodeSourceEntry, URL>() {
            public URL call(CodeSourceEntry codeSourceEntry) throws Exception {
                return codeSourceEntry.url();
            }
        };
    }


    public String entryWithoutRoot() {
        return entry.getPath().replace(root.getPath(), "");
    }


    @Override
    public String toString() {
        return String.format("Root = %s entry = %s", root, entry);
    }
}
