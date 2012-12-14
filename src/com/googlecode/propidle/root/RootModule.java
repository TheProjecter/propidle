package com.googlecode.propidle.root;

import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ResourcesModule;

import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;

public class RootModule implements ResourcesModule {
    public Resources addResources(Resources resources) {
        return resources.add(annotatedClass(RootResource.class));
    }
}
