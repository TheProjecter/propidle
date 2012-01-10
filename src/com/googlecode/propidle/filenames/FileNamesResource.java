package com.googlecode.propidle.filenames;

import com.googlecode.propidle.PathType;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.search.FileNameSearcher;
import com.googlecode.propidle.search.Query;
import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.propidle.urls.UrlResolver;
import com.googlecode.propidle.versioncontrol.changes.AllChanges;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.PathParam;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.annotations.QueryParam;
import com.googlecode.utterlyidle.rendering.Model;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.propidle.PathType.FILE;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path(FileNamesResource.NAME)
@Produces({TEXT_HTML})
public class FileNamesResource {
    public static final String NAME = "filenames";
    public static final String DIRECTORY_VIEW_NAME = "directory_view";
    private final FileNameSearcher searcher;
    private final UrlResolver urlResolver;
    private final AllChanges allChanges;

    public FileNamesResource(FileNameSearcher searcher, UrlResolver urlResolver, AllChanges allChanges) {
        this.searcher = searcher;
        this.urlResolver = urlResolver;
        this.allChanges = allChanges;
    }

    @GET
    @Produces({TEXT_HTML, TEXT_PLAIN})
    public Model get(@QueryParam("q") Query query) {
        Model model = modelWithName(NAME).
                add(PropertiesModule.TITLE, "Filenames \"" + query + "\"").
                add("searchUrl", urlResolver.searchUrl()).
                add("createPropertiesUrl", urlResolver.createPropertiesUrl()).
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
        Iterable<Pair<PropertiesPath, PathType>> paths = sequence(allChanges.childrenOf(path));
        Model model = modelWithName(DIRECTORY_VIEW_NAME).
                add("searchUrl", urlResolver.searchUrl()).
                add("createPropertiesUrl", urlResolver.createPropertiesUrl()).
                add(PropertiesModule.TITLE, "Children of \"" + path + "\"");
        return sequence(paths).
                sortBy(typeThenPath()).fold(model, pathsIntoModel());
    }

    private Callable1<Pair<PropertiesPath, PathType>, String> typeThenPath() {
        return new Callable1<Pair<PropertiesPath, PathType>, String>() {
            public String call(Pair<PropertiesPath, PathType> pathAndType) throws Exception {
                return pathAndType.second() + ":" + pathAndType.first();
            }
        };
    }

    private Callable2<Model, Pair<PropertiesPath, PathType>, Model> pathsIntoModel() {
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

    private Uri urlOf(Pair<PropertiesPath, PathType> pathAndTypes) {
        if (pathAndTypes.second().equals(FILE)) {
            return urlResolver.resolvePropertiesUrl(pathAndTypes.first());
        } else {
            return urlResolver.resolveFileNameUrl(pathAndTypes.first());
        }
    }

}
