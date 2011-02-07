package com.googlecode.propidle.server.usermanagement;

import com.googlecode.propidle.authorisation.groups.GroupId;
import com.googlecode.propidle.authorisation.groups.GroupName;
import com.googlecode.utterlyidle.Priority;
import com.googlecode.utterlyidle.rendering.Model;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import static javax.ws.rs.core.MediaType.TEXT_HTML;

@Path(GroupsResource.NAME)
@Produces(TEXT_HTML)
public class GroupsResource {
    public static final String NAME = "groups";

    @GET
    @Path("{id}")
    @Priority(Priority.High)
    public Model get(@PathParam("id") GroupId id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @GET
    @Path("{name}")
    public Model get(@PathParam("name") GroupName name) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
