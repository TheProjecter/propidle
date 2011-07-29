package com.googlecode.propidle.server.staticcontent;

import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ResourcesModule;

import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;

public class StaticContentModule implements ResourcesModule {
    public Module addResources(Resources resources) {
        resources.add(annotatedClass(StaticContentResource.class));
        resources.add(annotatedClass(FavIconResource.class));
        return this;
    }
}
