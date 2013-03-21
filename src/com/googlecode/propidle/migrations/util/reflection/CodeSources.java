package com.googlecode.propidle.migrations.util.reflection;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Files;
import com.googlecode.totallylazy.Sequence;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;

public class CodeSources {
    public static Iterable<CodeSourceEntry> resourcesInCodeSource(Class type) {
        CodeSource src = type.getProtectionDomain().getCodeSource();
        if (src != null) {
            URL codeSource = src.getLocation();
            if (isInAJar(codeSource)) {
                return resourcesInJar(src, codeSource);
            }
            return resourcesOnDisk(codeSource, type);
        } else {
            throw new IllegalArgumentException(format("Could not find CodeSource of %s", type.getCanonicalName()));
        }
    }

    private static Iterable<CodeSourceEntry> resourcesOnDisk(final URL basePath, Class theClass) {
        String pathToClassFromCodeSource = theClass.getCanonicalName().replaceAll("\\.", "/");
        String pathToCodeSource = basePath.getFile() + pathToClassFromCodeSource;

        File parentFile = new File(pathToCodeSource).getParentFile();
        return Files.recursiveFiles(parentFile).map(asUrl(basePath));
    }

    private static Callable1<File, CodeSourceEntry> asUrl(final URL basePath) {
        return new Callable1<File, CodeSourceEntry>() {
            public CodeSourceEntry call(File file) throws Exception {
                return new CodeSourceEntry(basePath,file.toURI().toURL());
            }
        };
    }

    private static boolean isInAJar(URL url) {
        return url.getFile().endsWith(".jar");
    }

    private static Iterable<CodeSourceEntry> resourcesInJar(java.security.CodeSource src, URL jar) {
        try {
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            return entries(zip).map(toJarCodeSourceEntry(jar,src));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Callable1<? super String, CodeSourceEntry> toJarCodeSourceEntry(final URL jar, final CodeSource src) {
        return new Callable1<String, CodeSourceEntry>() {
            public CodeSourceEntry call(String s) throws Exception {
                return new CodeSourceEntry(toJarUrl(jar,""), toJarUrl(src, s));
            }
        };
    }

    private static URL toJarUrl(CodeSource src, String s) throws MalformedURLException {
        return toJarUrl(src.getLocation(), s);
    }

    private static URL toJarUrl(URL location, String s) throws MalformedURLException {
        return new URL("jar:file://" + location.getPath() + "!/" + s);
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
