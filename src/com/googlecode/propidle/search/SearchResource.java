package com.googlecode.propidle.search;

import com.googlecode.propidle.ModelName;
import com.googlecode.propidle.PropidlePath;
import com.googlecode.propidle.properties.PropertiesResource;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.Redirector;
import com.googlecode.utterlyidle.rendering.Model;

import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.annotations.QueryParam;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;

import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import com.googlecode.propidle.urls.UrlResolver;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path(SearchResource.NAME)
@Produces(TEXT_HTML)
public class SearchResource {
    public static final String NAME = "search";
    private final PropertiesSearcher searcher;
    private final PropidlePath propidlePath;

    public SearchResource(PropertiesSearcher searcher, PropidlePath propidlePath) {
        this.searcher = searcher;
        this.propidlePath = propidlePath;
    }

    @GET
    public Model get(@QueryParam("q") Option<Query> query) {
        Model model = modelWithName(NAME).
                add(TITLE, "Search \"" + query.getOrNull() + "\"");
        if (!query.isEmpty()) {
            Iterable<SearchResult> results = searcher.search(query.get());
            model.add("query", query.getOrElse(Query.query("")).query());
            sequence(results).fold(model, addResultToModel());
        }
        return model;
    }

    private Callable2<? super Model, ? super SearchResult, Model> addResultToModel() {
        return new Callable2<Model, SearchResult, Model>() {
            public Model call(Model model, SearchResult searchResult) throws Exception {
                return model.add("matches", model().
                        add("url", model().add("name", propidlePath.path(method(on(PropertiesResource.class).getProperties(searchResult.path())))).add("url", propidlePath.absoluteUriOf(method(on(PropertiesResource.class).getProperties(searchResult.path()))))).
                        add("propertyName", searchResult.propertyName()).
                        add("propertyValue", searchResult.propertyValue())
                );
            }
        };
    }
}
