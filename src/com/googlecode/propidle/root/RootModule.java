package com.googlecode.propidle.root;

import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ResourcesModule;

import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;

public class RootModule implements ResourcesModule {
    public Module addResources(Resources resources) {
        resources.add(annotatedClass(RootResource.class));
        return this;
    }
}
