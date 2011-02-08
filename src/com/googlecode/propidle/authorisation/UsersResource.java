package com.googlecode.propidle.authorisation;

import com.googlecode.propidle.authorisation.groups.Group;
import static com.googlecode.propidle.authorisation.groups.GroupId.asGroupId;
import com.googlecode.propidle.authorisation.groups.GroupMembership;
import static com.googlecode.propidle.authorisation.groups.GroupMembership.getGroupId;
import com.googlecode.propidle.authorisation.groups.GroupMemberships;
import com.googlecode.propidle.authorisation.groups.Groups;
import com.googlecode.propidle.authorisation.users.*;
import static com.googlecode.propidle.authorisation.users.User.user;
import com.googlecode.propidle.server.PropertiesModule;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import static com.googlecode.totallylazy.Sequences.sequence;
import com.googlecode.utterlyidle.FormParameters;
import static com.googlecode.utterlyidle.HeaderParameters.headerParameters;
import com.googlecode.utterlyidle.Response;
import static com.googlecode.utterlyidle.Responses.response;
import com.googlecode.utterlyidle.Status;
import com.googlecode.utterlyidle.BasePath;
import static com.googlecode.utterlyidle.Status.status;
import static com.googlecode.utterlyidle.proxy.Resource.*;
import com.googlecode.utterlyidle.rendering.Model;
import static com.googlecode.utterlyidle.rendering.Model.model;

import javax.ws.rs.*;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import java.util.concurrent.Callable;

@Path(UsersResource.NAME)
@Produces(TEXT_HTML)
public class UsersResource {
    public static final String NAME = "users";
    public final Users users;
    public final GroupMemberships groupMemberships;
    public final Groups groups;
    private final BasePath basePath;
    public final PasswordHasher passwordHasher;

    public UsersResource(Users users, GroupMemberships groupMemberships, PasswordHasher passwordHasher, Groups groups, BasePath basePath) {
        this.users = users;
        this.groupMemberships = groupMemberships;
        this.passwordHasher = passwordHasher;
        this.groups = groups;
        this.basePath = basePath;
    }

    @GET
    @Path("{username}")
    public Response get(@PathParam("username") Username username) {
        Option<User> maybeUser = users.get(username);
        if (maybeUser.isEmpty()) {
            return response(status(404, "User does not exist"));
        }
        User user = maybeUser.get();
        Sequence<GroupMembership> memberships = sequence(groupMemberships.get(username));
        Iterable<Group> memberGroups = groups.get(memberships.map(getGroupId()));
        return response(Status.OK, headerParameters(), modelOf(user, memberGroups));
    }

    @POST
    @Path("{username}")
    public Response modify(
            @PathParam("username") Username username,
            @FormParam("password") Option<Password> password,
            @FormParam("addToGroup") String group,
            FormParameters form) {
        ensureUserExists(username, password);
        groupMemberships.add(username, sequence(form.getValues("group")).map(asGroupId()));
        return redirect(resource(UsersResource.class).get(username));
    }

    private Model modelOf(User user, Iterable<Group> memberGroups) {
        Model model = model().
                add(PropertiesModule.MODEL_NAME, NAME).
                add("username", user.username());
        return sequence(memberGroups).fold(sequence(groups.get()).fold(model, groupModel("allGroups")), groupModel("memberships"));
    }

    private Callable2<? super Model, ? super Group, Model> groupModel(final String itemName) {
        return new Callable2<Model, Group, Model>() {
            public Model call(Model model, Group group) throws Exception {
                return model.
                        add(itemName, model().
                                add("groupName", group.name()).
                                add("id", group.id()).
                                add("url", basePath.file(urlOf(resource(GroupsResource.class).get(group.name())))));
            }
        };
    }

    private User ensureUserExists(Username username, Option<Password> password) {
        return users.get(username).getOrElse(createUser(username, password));
    }

    private Callable<User> createUser(final Username username, final Option<Password> password) {
        return new Callable<User>() {
            public User call() throws Exception {
                if (password.isEmpty()) throw new IllegalArgumentException("Password must be specified for new users");
                return users.put(user(username, passwordHasher.hash(password.get())));
            }
        };
    }
}
