package com.googlecode.propidle.status;

import com.googlecode.totallylazy.Uri;

public class Action {
    private final ActionName name;
    private final Uri uri;

    public static Action action(ActionName name, Uri uri) {
        return new Action(name, uri);
    }

    private Action(ActionName name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public ActionName name() {
        return name;
    }

    public Uri url() {
        return uri;
    }
}
