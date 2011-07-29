package com.googlecode.propidle.util;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

import static com.googlecode.totallylazy.Runnables.VOID;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.modules.Modules.activate;

public class Modules {

    public static Callable1<Callable1<Container, Container>, Module> asRequestScopeModule() {
        return new Callable1<Callable1<Container, Container>, Module>() {
            public Module call(final Callable1<Container, Container> containerCallable) throws Exception {
                return new RequestScopedModule() {
                    public Module addPerRequestObjects(Container container) {
                        try {
                            containerCallable.call(container);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        return this;
                    }
                };
            }
        };
    }

}
