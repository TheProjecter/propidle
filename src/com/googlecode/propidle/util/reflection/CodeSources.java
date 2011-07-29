package com.googlecode.propidle.util.reflection;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequence;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;

public class CodeSources {
    public static Iterable<URL> resourcesInCodeSource(Class type) {
        CodeSource src = type.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL codeSource = src.getLocation();
            if (isInAJar(codeSource)) {
                return resourcesInJar(src, codeSource);
            }
            return resourcesOnDisk(codeSource.getFile(), type);
        } else {
            throw new IllegalArgumentException(format("Could not find CodeSource of %s", type.getCanonicalName()));
        }
    }

    private static Iterable<URL> resourcesOnDisk(String basePath, Class theClass) {
        String pathToClassFromCodeSource = theClass.getCanonicalName().replaceAll("\\.", "/");
        String pathToCodeSource = basePath + pathToClassFromCodeSource;

        return sequence(new File(pathToCodeSource).getParentFile().listFiles()).map(new Callable1<File, URL>() {
            public URL call(File file) throws Exception {
                return file.toURI().toURL();
            }
        });
    }

    private static boolean isInAJar(URL url) {
        return url.getFile().endsWith(".jar");
    }

    private static Iterable<URL> resourcesInJar(java.security.CodeSource src, URL jar) {
        try {
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            return entries(zip).map(toJarUrl(src));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Callable1<? super String, URL> toJarUrl(final java.security.CodeSource src) {
        return new Callable1<String, URL>() {
            public URL call(String s) throws Exception {
                return new URL("jar:file://" + src.getLocation().getPath() + "!/" + s);
            }
        };
    }

    private static Sequence<String> entries(ZipInputStream zip) throws IOException {
        List<String> files = new ArrayList<String>();
        ZipEntry zipEntry = zip.getNextEntry();
        while (zipEntry != null) {
            files.add(zipEntry.getName());
            zipEntry = zip.getNextEntry();
        }
        return sequence(files);
    }
}
