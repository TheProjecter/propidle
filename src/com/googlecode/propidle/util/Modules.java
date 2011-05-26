package com.googlecode.propidle.util;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Runnables;
import com.googlecode.utterlyidle.modules.ApplicationScopedModule;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

import static com.googlecode.totallylazy.Runnables.VOID;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Modules {
    public static Callable1<? super Module, Callable1<Container, Container>> adaptUtterlyIdleModule() {
        return new Callable1<Module, Callable1<Container, Container>>() {
            public Callable1<Container, Container> call(final Module module) throws Exception {
                return new Callable1<Container, Container>() {
                    public Container call(Container container) throws Exception {
                        sequence(module).safeCast(ApplicationScopedModule.class).forEach(addPerApplicationObjects(container));
                        sequence(module).safeCast(RequestScopedModule.class).forEach(addPerRequestObjects(container));
                        return container;
                    }
                };
            }
        };
    }

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


    public static Callable1<ApplicationScopedModule, Void> addPerApplicationObjects(final Container applicationScope) {
        return new Callable1<ApplicationScopedModule, Void>() {
            public Void call(ApplicationScopedModule applicationScopedModule) {
                applicationScopedModule.addPerApplicationObjects(applicationScope);
                return VOID;
            }
        };
    }

    public static Callable1<RequestScopedModule, Void> addPerRequestObjects(final Container requestScope) {
        return new Callable1<RequestScopedModule, Void>() {
            public Void call(RequestScopedModule requestScopedModule) {
                requestScopedModule.addPerRequestObjects(requestScope);
                return VOID;
            }
        };
    }
}
