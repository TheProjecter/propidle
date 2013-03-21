package com.googlecode.utterlyidle.modules.renderers;

import com.googlecode.funclate.stringtemplate.EnhancedStringTemplateGroup;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.URLs;
import com.googlecode.utterlyidle.Renderer;
import com.googlecode.utterlyidle.rendering.Model;
import org.antlr.stringtemplate.AttributeRenderer;
import org.antlr.stringtemplate.StringTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Predicates.instanceOf;
import static com.googlecode.totallylazy.URLs.packageUrl;

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
        final EnhancedStringTemplateGroup templateGroup = new EnhancedStringTemplateGroup(packageUrl(resource));
        StringTemplate template = templateGroup.getInstanceOf(templateName, model);
        for (Pair<Class, Renderer> customRenderer : customRenderers) {
            templateGroup.registerRenderer(instanceOf(customRenderer.first()), adapt(customRenderer.second()));
        }
        return template.toString();
    }

    private Callable1<Object, String> adapt(final Renderer renderer) {
        return new Callable1<Object, String>() {
            public String call(Object o) throws Exception {
                try {
                    return renderer.render(o);
                } catch (Exception e) {
                    throw new RuntimeException("Rendering '" + o + "' using '" + renderer + "'", e);
                }
            }
        };
    }

}