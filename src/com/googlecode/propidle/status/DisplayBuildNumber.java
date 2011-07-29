package com.googlecode.propidle.status;

import com.googlecode.totallylazy.Callable1;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static com.googlecode.propidle.status.StatusCheckName.statusCheckName;
import static com.googlecode.propidle.status.StatusCheckResult.statusCheckResult;
import static com.googlecode.totallylazy.Closeables.using;
import static java.util.jar.Attributes.Name.IMPLEMENTATION_VERSION;

public class DisplayBuildNumber implements StatusCheck {
    public StatusCheckResult check() throws Exception {
        final StatusCheckResult result = statusCheckResult(statusCheckName("Build Info"));
        URL manifestFilePath = new URL(globalScopePath() + "/META-INF/MANIFEST.MF");
        if (manifestFilePath.getProtocol().equals("jar")) {
            reportImplementationVersion(result, manifestFilePath);
        } else {
            result.add("Implementation-Version", "unknown");
        }
        return result;
    }

    private void reportImplementationVersion(final StatusCheckResult result, URL manifestFilePath) throws IOException {
        using(manifestFilePath.openStream(), reportImplementationVersions(result));
    }

    private Callable1<InputStream, StatusCheckResult> reportImplementationVersions(final StatusCheckResult result) {
        return new Callable1<InputStream, StatusCheckResult>() {
            public StatusCheckResult call(InputStream inputStream) throws Exception {
                Manifest manifest = new Manifest(inputStream);
                Map<String,Attributes> entries = manifest.getEntries();
                for (Map.Entry<String, Attributes> entry : entries.entrySet()) {
                    String implementationVersion = entry.getValue().getValue(IMPLEMENTATION_VERSION);
                    if(implementationVersion != null) {
                        result.add(entry.getKey(), implementationVersion);
                    }
                }
                return result;
            }
        };
    }

    private String globalScopePath() {
        String myResourcePath = "/" + getClass().getName().replaceAll("\\.", "/") + ".class";
        URL resource = getClass().getResource(myResourcePath);
        return resource.toString().replace(myResourcePath, "");
    }
}
