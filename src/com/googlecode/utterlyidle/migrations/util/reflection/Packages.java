package com.googlecode.utterlyidle.migrations.util.reflection;

import com.googlecode.totallylazy.Predicate;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.migrations.util.reflection.CodeSources.resourcesInCodeSource;

public class Packages {
    public static Iterable<CodeSourceEntry> filesInOrUnderPackage(Class aClass) {
            return sequence(resourcesInCodeSource(aClass)).filter(packageContains(aClass.getPackage()));
    }

    private static Predicate<? super CodeSourceEntry> packageContains(final Package aPackage) {
        return new Predicate<CodeSourceEntry>() {
            public boolean matches(CodeSourceEntry other) {
                String packageName = aPackage.getName().replaceAll("\\.", "/");
                String entryWithoutRoot = other.entryWithoutRoot();
                return entryWithoutRoot.startsWith(packageName);
            }
        };
    }
}
