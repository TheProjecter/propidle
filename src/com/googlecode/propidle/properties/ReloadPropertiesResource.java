package com.googlecode.propidle.properties;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.server.IndexRebuilder;
import com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords;
import com.googlecode.propidle.versioncontrol.changes.Change;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.lazyrecords.Record;
import com.googlecode.lazyrecords.Records;
import com.googlecode.utterlyidle.annotations.POST;
import com.googlecode.utterlyidle.annotations.Path;
import com.googlecode.utterlyidle.annotations.Produces;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static com.googlecode.propidle.client.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.CHANGES;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.PROPERTIES_PATH;
import static com.googlecode.propidle.versioncontrol.changes.Change.applyChange;
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
