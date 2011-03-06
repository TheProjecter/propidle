package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.Renderer;
import com.googlecode.utterlyidle.handlers.UrlStringTemplateGroup;
import com.googlecode.utterlyidle.io.Url;
import com.googlecode.utterlyidle.rendering.Model;
import org.antlr.stringtemplate.StringTemplate;

public class ModelTemplateRenderer implements Renderer<Model> {
    private final String templateName;
    private final Class resource;

    public ModelTemplateRenderer(String templateName, Class resource) {
        this.templateName = templateName;
        this.resource = resource;
    }

    public String render(final Model model) {
//        UrlStringTemplateGroup sharedGroup = new UrlStringTemplateGroup(getUrlDirectoryContaining("input.st", ModelTemplateRenderer.class));
        UrlStringTemplateGroup templateGroup = new UrlStringTemplateGroup(getUrlDirectoryContaining(templateName + ".st", resource));
//        templateGroup.setSuperGroup(sharedGroup);f
        StringTemplate template = templateGroup.getInstanceOf(templateName, model);
        return template.toString();
    }

    private Url getUrlDirectoryContaining(String name, Class relativeTo) {
        try {
            return Url.url(relativeTo.getResource(name)).parent();
        } catch (RuntimeException e) {
            throw e;
        }
    }

}
