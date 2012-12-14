package com.googlecode.propidle.server;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.properties.PropertiesResource;
import com.googlecode.utterlyidle.Resources;
import com.googlecode.utterlyidle.handlers.ResponseHandlers;
import com.googlecode.utterlyidle.modules.ArgumentScopedModule;
import com.googlecode.utterlyidle.modules.ResourcesModule;
import com.googlecode.utterlyidle.modules.ResponseHandlersModule;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.Resolver;

import java.lang.reflect.Type;

import static com.googlecode.propidle.ModelName.nameIs;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.annotatedClass;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;

@SuppressWarnings("unchecked")
public class PropertiesModule implements ArgumentScopedModule, ResourcesModule, ResponseHandlersModule {
    public static final String TITLE = "title";

    public Resources addResources(Resources resources) {
        return resources.add(annotatedClass(PropertiesResource.class));
    }

    @Override
    public ResponseHandlers addResponseHandlers(ResponseHandlers handlers) {
        handlers.add(where(entity(Model.class), nameIs(PropertiesResource.HTML_EDITABLE)), renderer(new ModelTemplateRenderer("EditablePropertiesResource_html", PropertiesResource.class)));
        handlers.add(where(entity(Model.class), nameIs(PropertiesResource.HTML_READ_ONLY)), renderer(new ModelTemplateRenderer("PropertiesResource_html", PropertiesResource.class)));
        return handlers.add(where(entity(Model.class), nameIs(PropertiesResource.PLAIN_NAME)), renderer(new ModelTemplateRenderer("PropertiesResource_properties", PropertiesResource.class)));
    }

    public Container addPerArgumentObjects(final Container container) {
        container.addType(PropertiesPath.class, PropertiesPathFromStringResolver.class);
        return container;
    }

    public static class PropertiesPathFromStringResolver implements Resolver<PropertiesPath> {
        private final String theValue;

        public PropertiesPathFromStringResolver(String theValue) {
            this.theValue = theValue;
        }

        public PropertiesPath resolve(Type type) throws Exception {
            return propertiesPath(theValue);
        }
    }
}
