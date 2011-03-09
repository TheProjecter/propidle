package com.googlecode.propidle.server;

import com.googlecode.propidle.status.StatusResource;
import com.googlecode.totallylazy.Pair;
import com.googlecode.utterlyidle.Renderer;
import com.googlecode.utterlyidle.handlers.UrlStringTemplateGroup;
import com.googlecode.utterlyidle.io.Url;
import com.googlecode.utterlyidle.rendering.Model;
import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.googlecode.totallylazy.Pair.pair;

public class ModelTemplateRenderer implements Renderer<Model> {
    private final String templateName;
    private final Class resource;
    private List<Pair<Class, Renderer>> customRenderers = new ArrayList<Pair<Class, Renderer>>();

    public ModelTemplateRenderer(String templateName, Class resource) {
        this.templateName = templateName;
        this.resource = resource;
    }

    public ModelTemplateRenderer withRenderer(Class type, Renderer renderer) {
        customRenderers.add(pair(type, renderer));
        return this;
    }
    public String render(final Model model) {
        final UrlStringTemplateGroup templateGroup = new UrlStringTemplateGroup(getUrlDirectoryContaining(templateName + ".st", resource));
        StringTemplate template = templateGroup.getInstanceOf(templateName, model);
        for (Pair<Class, Renderer> customRenderer : customRenderers) {
            templateGroup.registerRenderer(customRenderer.first(), adapt(customRenderer.second()));
        }
        return template.toString();
    }

    private AttributeRenderer adapt(final Renderer renderer) {
        return new AttributeRenderer() {
            public String toString(Object o) {
                try {
                    return renderer.render(o);
                } catch (Exception e) {
                    throw new RuntimeException("Rendering '" + o + "' using '"+ renderer + "'", e);
                }
            }

            public String toString(Object o, String s) {
                return toString(o);
            }
        };
    }

    private Url getUrlDirectoryContaining(String name, Class relativeTo) {
        try {
            return Url.url(relativeTo.getResource(name)).parent();
        } catch (RuntimeException e) {
            throw e;
        }
    }
}