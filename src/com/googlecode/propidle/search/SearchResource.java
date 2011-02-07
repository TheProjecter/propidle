package com.googlecode.propidle.search;

import com.googlecode.propidle.search.PropertiesSearcher;
import com.googlecode.propidle.search.Query;
import com.googlecode.propidle.search.SearchResult;
import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path(SearchResource.NAME)
@Produces(TEXT_HTML)
public class SearchResource {
    public static final String NAME = "search";
    private final PropertiesSearcher searcher;

    public SearchResource(PropertiesSearcher searcher) {
        this.searcher = searcher;
    }

    @GET
    public Model get(@QueryParam("q") Option<Query> query) {
        Model model = model().
                add(PropertiesModule.MODEL_NAME, NAME).
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
                        add("url", searchResult.url()).
                        add("propertyName", searchResult.propertyName()).
                        add("propertyValue", searchResult.propertyValue())
                );
            }
        };
    }
}
