package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.properties.PropertiesResource;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.utterlyidle.Redirector;
import com.googlecode.utterlyidle.annotations.GET;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.PathParam;
import com.googlecode.utterlyidle.annotations.Produces;
import com.googlecode.utterlyidle.annotations.QueryParam;
import com.googlecode.utterlyidle.rendering.Model;

import static com.googlecode.propidle.ModelName.modelWithName;
import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.MediaType.TEXT_HTML;
import static com.googlecode.utterlyidle.rendering.Model.model;

@Path(ChangesResource.NAME)
@Produces(TEXT_HTML)
public class ChangesResource {
    public static final String NAME = "changes";
    private final AllChanges changes;
    private final Redirector redirector;

    public ChangesResource(AllChanges changes, Redirector redirector) {
        this.changes = changes;
        this.redirector = redirector;
    }

    @GET
    @Path("{path:.+$}")
    public Model get(@PathParam("path") PropertiesPath path, @QueryParam("forRevision") Option<RevisionNumber> revisionNumber) {
        Iterable<Change> changesForProperties = revisionNumber.isEmpty() ? changes.get(path) : changes.get(path, revisionNumber.get());
        String propertiesUrl = redirector.uriOf(method(on(PropertiesResource.class).getProperties(path))).toString();
        Model model = sequence(changesForProperties).
                sortBy(method(on(Change.class).revisionNumber())).
                fold(modelWithName(NAME), addChangesToModel()).
                add("propertiesUrl", propertiesUrl).
                add(TITLE, "Changes to \"" + path + "\"");
        if (!revisionNumber.isEmpty()) {
            model.add("revisionNumber", revisionNumber.getOrNull());
        }
        return model;
    }


    private Callable2<? super Model, ? super Change, Model> addChangesToModel() {
        return new Callable2<Model, Change, Model>() {
            public Model call(Model model, Change change) throws Exception {
                return model.add("changes", model().
                        add("propertiesPath", change.propertiesPath()).
                        add("revisionNumber", change.revisionNumber()).
                        add("propertyName", change.propertyName()).
                        add("previous", change.previous()).
                        add("updated", change.updated()).
                        add("status", change.status()));
            }
        };
    }
}
