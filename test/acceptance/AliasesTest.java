package acceptance;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.propidle.aliases.Alias;
import com.googlecode.propidle.aliases.AliasPath;
import com.googlecode.propidle.aliases.Aliases;
import com.googlecode.propidle.aliases.AliasesResource;
import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.proxy.Invocation;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.handlers.Auditor;
import com.googlecode.utterlyidle.handlers.Auditors;
import com.googlecode.utterlyidle.handlers.ClientHttpHandler;
import com.googlecode.utterlyidle.handlers.RedirectHttpHandler;
import com.googlecode.utterlyidle.httpserver.RestServer;
import com.googlecode.utterlyidle.modules.AuditModule;
import com.googlecode.yadic.Container;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Date;

import static com.googlecode.propidle.aliases.AliasDestination.aliasDestination;
import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.totallylazy.UrlEncodedMessage.decode;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.relativeUriOf;
import static com.googlecode.utterlyidle.html.Html.html;
import static com.googlecode.yatspec.state.givenwhenthen.SyntacticSugar.to;
import static org.hamcrest.Matchers.*;

@RunWith(SpecRunner.class)
public class AliasesTest extends TestState  {

    public static final String LAST_RESPONSE = "Last Response";
    private RestServer server;
    private TestPropertiesApplication application;

    @Before
    public void startServer() throws Exception {
        application = new TestPropertiesApplication();
        captureRequestsAndResponsesTo(application);
        server = new RestServer(application, ServerConfiguration.defaultConfiguration());
    }

    @After
    public void stopServer() throws Exception {
        Closeables.close(server);
    }

    @Test
    public void weCanDeleteAnAlias() throws Exception {
        given(anAliasFrom("redirect_1", to("/properties/1")));
        and(anAliasFrom("redirect_2", to("/properties/2")));

        when(weDeleteTheAlias("redirect_1"));

        then(aliases(), not(contains(aliasPath("redirect_1"))));
        then(aliases(), contains(aliasPath("redirect_2")));
    }

    @Test
    public void ifAnAliasDoesNotExistThenTryingToGetItWillRedirectToTheEditPage() throws Exception {
        when(weGetTheAlias("production/myApplication/v123"));

        then(theAliasExists(), is(false));
    }

    @Test
    public void canCreateAnAlias() throws Exception {
        when(weCreateTheAlias("production/myApplication/v123", to("destination1")));
        then(theAliasExists(), is(true));
        then(destinationOfAliasIs(), is("destination1"));
    }

    @Test
    public void aliasShouldRedirectToDestination() throws Exception {
        given(anAliasFrom("redirect_1", to("/properties/1")));
        when(weGetTheAlias("redirect_1"));
        then(currentPageTitle(), is("Properties \"/1\""));
    }

    @Test
    public void aliasShouldReturnTextContentWhenGettingProperties() throws Exception {
        given(anAliasFrom("redirect_1", to("/properties/1")));
        when(weGetTheAlias("redirect_1.properties"));
        then(responseContentType(), containsString(TEXT_PLAIN));
    }

    private StateExtractor<String> responseContentType() {
        return new StateExtractor<String>() {
            @Override
            public String execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return lastResponse(capturedInputAndOutputs).headers().getValue(HttpHeaders.CONTENT_TYPE);
            }
        };
    }

    @Test
    public void afterEditingAnAliasUsersAreRedirectedToTheEditPage() throws Exception {
        given(anAliasFrom("redirect_1", to("/properties/1")));

        when(weChangeAlias("redirect_1", to("/properties/2")));

        then(theAliasExists(), is(true));
        then(destinationOfAliasIs(), is("/properties/2"));
    }

    @Test
    public void weCanSeeAListOfAllAvailableAliases() throws Exception {
        given(anAlias("redirect_1"));
        and(anAlias("redirect_2"));

        when(weGetAllAliases());

        then(aliases(), hasItems(aliasPath("redirect_1"), aliasPath("redirect_2")));
    }

    @Test
    public void weCanFilterTheListOfAvailableAliases() throws Exception {
        given(anAlias("first_alias"));
        and(anAlias("second_alias"));

        when(weGetAllAliasesWithFilter("first"));

        then(aliases(), hasItem(aliasPath("first_alias")));
        then(aliases(), not(hasItem(aliasPath("second_alias"))));
    }

    private ActionUnderTest weGetAllAliasesWithFilter(String filter) {
        return executeRequest(get(uriOf(method(on(AliasesResource.class).listAliases("")))).query("filter", filter));

    }

    private Uri uriOf(Invocation<Object, ? extends Object> method) {
        return fullyQualifiedUri(relativeUriOf(method));
    }


    private StateExtractor<String> destinationOfAliasIs() {
        return new StateExtractor<String>() {
            @Override
            public String execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return new AliasPage(lastResponse(capturedInputAndOutputs)).destination();
            }
        };
    }

    private ActionUnderTest weChangeAlias(final String alias, final String newDestination) {
        return new ActionUnderTest() {
            @Override
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                executeRequest(post(uriOf(method(on(AliasesResource.class).update(aliasPath(alias), null)))).form("to", aliasDestination(newDestination))).execute(interestingGivens, capturedInputAndOutputs);
                return capturedInputAndOutputs;
            }
        };
    }

    private StateExtractor<String> currentPageTitle() {
        return new StateExtractor<String>() {
            @Override
            public String execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return html(lastResponse(capturedInputAndOutputs)).selectContent("//title");
            }
        };
    }


    private ActionUnderTest weCreateTheAlias(final String alias, final String destination) {
        return new ActionUnderTest() {
            @Override
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                executeRequest(post(uriOf(method(on(AliasesResource.class).update(aliasPath(alias), null)))).form("to", destination)).execute(interestingGivens, capturedInputAndOutputs);
                return capturedInputAndOutputs;
            }
        };
    }


    private ActionUnderTest weDeleteTheAlias(String alias) {
        return executeRequest(post(uriOf(method(on(AliasesResource.class).delete(aliasPath(alias))))));
    }

    private ActionUnderTest weGetAllAliases() {
        return executeRequest(get(uriOf(method(on(AliasesResource.class).listAllAliases()))));
    }

    private StateExtractor<Boolean> theAliasExists() {
        return new StateExtractor<Boolean>() {
            @Override
            public Boolean execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return new AliasPage(lastResponse(capturedInputAndOutputs)).aliasExists();
            }
        };
    }

    private ActionUnderTest weGetTheAlias(String alias) {
        return executeRequest(get(uriOf(method(on(AliasesResource.class).followRedirectHtml(aliasPath(alias))))));
    }

    private Uri fullyQualifiedUri(Uri relativeUri) {
        return server.uri().mergePath(relativeUri.path());
    }


    private StateExtractor<Collection<AliasPath>> aliases() {
        return new StateExtractor<Collection<AliasPath>>() {
            @Override
            public Collection<AliasPath> execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return new AliasesPage(lastResponse(capturedInputAndOutputs)).getAliases();
            }
        };
    }

    private Response lastResponse(CapturedInputAndOutputs capturedInputAndOutputs) {
        return capturedInputAndOutputs.getType(LAST_RESPONSE, Response.class);
    }

    protected void captureRequestsAndResponsesTo(final Application restApplication) {
        final InternalRequestMarker internalRequestMarker = restApplication.applicationScope().get(InternalRequestMarker.class);
        restApplication.add(new AuditModule() {
            @Override
            public Auditors addAuditors(Auditors auditors) throws Exception {

                auditors.add(new Auditor() {
                    @Override
                    public void audit(Pair<Request, Date> request, Pair<Response, Date> response) throws Exception {
                        if ((!internalRequestMarker.isInternal(request.first()) && responseIsHtmlOrText(response)) || Debug.inDebug()) {
                            String requestKey = "Request to " + decode(request.first().uri().toString()) + " at: " + request.second();
                            String responseKey = "Response from " + decode(request.first().uri().toString()) + " at: " + request.second();
                            if (capturedInputAndOutputs.contains(requestKey)) {
                                requestKey = requestKey + " (Subsequent request)";
                                responseKey = responseKey + " (Subsequent response)";
                            }
                            capturedInputAndOutputs.add(requestKey, request);
                            capturedInputAndOutputs.add(responseKey, response.first().entity().toString());
                        }
                    }
                });
                return auditors;
            }
        });
    }


    private boolean responseIsHtmlOrText(Pair<Response, Date> response) {
        String header = Response.methods.header(response.first(), CONTENT_TYPE);
        return header.contains(MediaType.TEXT_HTML) || header.contains(TEXT_PLAIN);
    }

    private ActionUnderTest executeRequest(final RequestBuilder request) {
        return new ActionUnderTest() {
            @Override
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                Response aliasesResponse = httpHandler().handle(request.build());
                capturedInputAndOutputs.remove(LAST_RESPONSE);
                capturedInputAndOutputs.add(LAST_RESPONSE, aliasesResponse);
                return capturedInputAndOutputs;
            }
        };
    }

    private GivensBuilder anAlias(final String alias) {
        return anAliasFrom(alias, "not important");
    }

    private GivensBuilder anAliasFrom(final String from, final String to) {
        return new GivensBuilder() {
            @Override
            public InterestingGivens build(InterestingGivens interestingGivens) throws Exception {
                application.usingRequestScope(new Callable1<Container, Object>() {
                    @Override
                    public Object call(Container container) throws Exception {
                        return container.get(Aliases.class).put(Alias.alias(aliasPath(from), aliasDestination(to)));
                    }
                });
                return interestingGivens;
            }
        };
    }

    private RedirectHttpHandler httpHandler() {
        return new RedirectHttpHandler(new ClientHttpHandler());
    }
}
