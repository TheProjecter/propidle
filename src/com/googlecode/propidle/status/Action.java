package com.googlecode.propidle.status;

import com.googlecode.utterlyidle.io.Url;

public class Action {
    private final ActionName name;
    private final Url url;

    public static Action action(ActionName name, Url url) {
        return new Action(name, url);
    }

    private Action(ActionName name, Url url) {
        this.name = name;
        this.url = url;
    }

    public ActionName name() {
        return name;
    }

    public Url url() {
        return url;
    }
}
