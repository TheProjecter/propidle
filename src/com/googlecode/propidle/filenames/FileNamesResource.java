package com.googlecode.propidle.filenames;

import com.googlecode.propidle.PathType;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.properties.PropertiesResource;
import com.googlecode.propidle.search.FileNameSearcher;
import com.googlecode.propidle.search.Query;
import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.urls.UrlResolver;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.utterlyidle.io.Url;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.*;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.io.Url.url;
import static com.googlecode.utterlyidle.rendering.Model.model;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path(FileNamesResource.NAME)
@Produces(TEXT_HTML)
public class FileNamesResource {
    public static final String NAME = "filenames";
    public static final String DIRECTORY_VIEW_NAME = "directory_view";
    private final FileNameSearcher searcher;
    private final UrlResolver urlResolver;

    public FileNamesResource(FileNameSearcher searcher, UrlResolver urlResolver) {
        this.searcher = searcher;
        this.urlResolver = urlResolver;
    }

    @GET
    public Model get(@QueryParam("q") Query query) {
        Model model = modelWithName(NAME).
                add(PropertiesModule.TITLE, "Filenames \"" + query + "\"").
                add("searchUrl", searchUrl()).
                add("createPropertiesUrl", createPropertiesUrl()).
                add("q", query.query());
        if (!query.isEmpty()) {
            Iterable<Pair<PropertiesPath, PathType>> paths = searcher.search(query);
            sequence(paths).
                    sortBy(typeThenPath()).
                    fold(model, pathsIntoModel());
        }
        return model;
    }

    @GET
    public Model getBase() {
        return getChildrenOf(propertiesPath("/"));
    }

    @GET
    @Path("{path:.*$}")
    public Model getChildrenOf(@PathParam("path") PropertiesPath path) {
        Iterable<Pair<PropertiesPath, PathType>> paths = searcher.childrenOf(path);
        Model model = modelWithName(DIRECTORY_VIEW_NAME).
                add("searchUrl", searchUrl()).
                add("createPropertiesUrl", createPropertiesUrl()).
                add(PropertiesModule.TITLE, "Children of \"" + path + "\"");
        return sequence(paths).
                sortBy(typeThenPath()).
                fold(model, pathsIntoModel());
    }

    private Callable1<? super Pair<PropertiesPath, PathType>, ? extends Comparable> typeThenPath() {
        return new Callable1<Pair<PropertiesPath, PathType>, Comparable>() {
            public Comparable call(Pair<PropertiesPath, PathType> pathAndType) throws Exception {
                return pathAndType.second() + ":" + pathAndType.first();
            }
        };
    }

    private Callable2<? super Model, ? super Pair<PropertiesPath, PathType>, Model> pathsIntoModel() {
        return new Callable2<Model, Pair<PropertiesPath, PathType>, Model>() {
            public Model call(Model model, Pair<PropertiesPath, PathType> pathAndTypes) throws Exception {
                PathType pathType = pathAndTypes.second();
                PropertiesPath path = pathAndTypes.first();
                Model fileNameModel = model().
                        add("filename", path).
                        add("url", urlOf(pathAndTypes)).
                        add("pathType", pathType.name().toLowerCase());
                return model.add("filenames", fileNameModel);
            }
        };
    }

    private Url urlOf(Pair<PropertiesPath, PathType> pathAndTypes) {
        if (pathAndTypes.second().equals(PathType.FILE)) {
            return urlResolver.resolvePropertiesUrl(pathAndTypes.first());
        } else {
            return urlResolver.resolveFileNameUrl(pathAndTypes.first());
        }
    }

    private Url searchUrl() {
        return urlResolver.resolve(url(NAME));
    }

    private Url createPropertiesUrl() {
        return urlResolver.resolve(url(PropertiesResource.NAME));
    }
}
