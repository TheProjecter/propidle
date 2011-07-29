package com.googlecode.propidle.util.reflection;

import static com.googlecode.propidle.util.reflection.CodeSources.resourcesInCodeSource;
import com.googlecode.totallylazy.Predicate;
import static com.googlecode.totallylazy.Sequences.sequence;

import java.net.MalformedURLException;
import java.net.URL;

public class Packages {
    public static Iterable<URL> filesInSamePackageAs(Class aClass) {
            return sequence(resourcesInCodeSource(aClass)).filter(packageIs(aClass.getPackage()));
    }

    private static Predicate<? super URL> packageIs(final Package aPackage) {
        return new Predicate<URL>() {
            public boolean matches(URL other) {
                String packagePath = sequence(other.getPath().split("/")).reverse().drop(1).reverse().toString("/");
                return packagePath.endsWith("/"+ aPackage.getName().replaceAll("\\.", "/"));
            }
        };
    }
}
