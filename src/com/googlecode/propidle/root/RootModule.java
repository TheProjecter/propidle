package com.googlecode.propidle.root;

import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ResourcesModule;

public class RootModule implements ResourcesModule {
    public Module addResources(Resources resources) {
        resources.add(RootResource.class);
        return this;
    }
}
