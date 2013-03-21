package com.googlecode.utterlyidle.migrations.util;

import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.UnaryFunction;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.RequestScopedModule;
import com.googlecode.yadic.Container;

public class Modules {
    public static Mapper<UnaryFunction<Container>, Module> asRequestScopeModule() {
        return new Mapper<UnaryFunction<Container>, Module>() {
            public Module call(final UnaryFunction<Container> containerCallable) throws Exception {
                return new RequestScopedModule() {
                    public Container addPerRequestObjects(Container container) {
                        try {
                           return containerCallable.call(container);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
            }
        };
    }
}