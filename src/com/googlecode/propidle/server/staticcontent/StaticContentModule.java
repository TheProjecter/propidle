package com.googlecode.propidle.server.staticcontent;

import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ResourcesModule;

import static com.googlecode.totallylazy.URLs.packageUrl;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.dsl.DslBindings.bindings;
import static com.googlecode.utterlyidle.dsl.StaticBindingBuilder.in;

public class StaticContentModule implements ResourcesModule {

    public static final String STATIC_RESOURCE_PATH = "static";

    public Module addResources(Resources resources) {
        resources.add(bindings(in(packageUrl(getClass())).path(STATIC_RESOURCE_PATH)));
        resources.add(annotatedClass(FavIconResource.class));
        return this;
    }
}
