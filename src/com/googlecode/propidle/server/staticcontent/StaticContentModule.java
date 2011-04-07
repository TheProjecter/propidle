package com.googlecode.propidle.server.staticcontent;

import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.modules.Module;
import com.googlecode.utterlyidle.modules.ResourcesModule;

public class StaticContentModule implements ResourcesModule {
    public Module addResources(Resources resources) {
        resources.add(StaticContentResource.class);
        resources.add(FavIconResource.class);
        return this;
    }
}
