package com.googlecode.propidle.search;

import com.googlecode.propidle.ModelName;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import static com.googlecode.propidle.ModelName.modelWithName;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import com.googlecode.propidle.urls.UrlResolver;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path(SearchResource.NAME)
@Produces(TEXT_HTML)
public class SearchResource {
    public static final String NAME = "search";
    private final PropertiesSearcher searcher;
    private final UrlResolver urlResolver;

    public SearchResource(PropertiesSearcher searcher, UrlResolver urlResolver) {
        this.searcher = searcher;
        this.urlResolver = urlResolver;
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
                        add("url", urlResolver.resolvePropertiesUrl(searchResult.path())).
                        add("propertyName", searchResult.propertyName()).
                        add("propertyValue", searchResult.propertyValue())
                );
            }
        };
    }
}
