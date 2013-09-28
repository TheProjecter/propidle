package com.googlecode.propidle.navigation;

import com.googlecode.propidle.PropidlePath;
import com.googlecode.propidle.aliases.AliasesResource;
import com.googlecode.propidle.compositeproperties.CompositePropertiesResource;
import com.googlecode.propidle.diff.DiffResource;
import com.googlecode.propidle.properties.PropertiesResource;
import com.googlecode.propidle.search.Query;
import com.googlecode.propidle.search.SearchResource;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.proxy.Call;
import com.googlecode.utterlyidle.rendering.Model;
import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;

import java.util.LinkedList;
import java.util.List;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path(NavigationResource.PATH)
@Produces(TEXT_HTML)
public class NavigationResource {
    public static final String PATH = "nav";
    private final PropidlePath propidlePath;

    public NavigationResource(PropidlePath propidlePath) {
        this.propidlePath = propidlePath;
    }

    @GET
    public Model nav() throws Throwable {
        return modelWithName(PATH).add("links", links());
    }

    private List links() throws Throwable {
        return sequence().
                append(model().add("name", "Properties").add("url", propidlePath.absoluteUriOf(Call.method(Call.on(PropertiesResource.class).getAll())))).
                append(model().add("name", "Compose Properties").add("url", propidlePath.absoluteUriOf(Call.method(Call.on(CompositePropertiesResource.class).getHtml(Option.none(String.class), null))))).
                append(model().add("name", "Aliases").add("url", propidlePath.absoluteUriOf(Call.method(Call.on(AliasesResource.class).listAllAliases())))).
                append(model().add("name", "Search").add("url", propidlePath.absoluteUriOf(Call.method(Call.on(SearchResource.class).get(Option.none(Query.class)))))).
                append(model().add("name", "Compare Properties").add("url", propidlePath.absoluteUriOf(Call.method(Call.on(DiffResource.class).get())))).toList();

    }
}
