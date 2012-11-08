package com.googlecode.propidle.properties;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.utterlyidle.annotations.POST;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;

@Path(ReloadPropertiesResource.NAME)
@Produces(TEXT_HTML)
public class ReloadPropertiesResource {
    public static final String NAME = "/reloadProperties";
    private final DynamicProperties dynamicProperties;

    public ReloadPropertiesResource(DynamicProperties dynamicProperties) {
        this.dynamicProperties = dynamicProperties;
    }

    @POST
    public String rebuildIndex() throws Exception {
        dynamicProperties.reload();
        return "Properties reloaded.";
    }

}
