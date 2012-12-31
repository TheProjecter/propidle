package acceptance;

import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.propidle.aliases.*;
import com.googlecode.totallylazy.*;
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
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Date;

import static acceptance.steps.thens.LastResponse.theLocationOf;
import static acceptance.steps.thens.LastResponse.theStatusOf;
import static com.googlecode.propidle.aliases.AliasDestination.*;
import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.totallylazy.UrlEncodedMessage.decode;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.Status.SEE_OTHER;
import static com.googlecode.utterlyidle.annotations.AnnotatedBindings.relativeUriOf;
import static com.googlecode.yatspec.state.givenwhenthen.SyntacticSugar.to;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(SpecRunner.class)
public class DeleteAliasesTest extends TestState {

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
        given(anAliasFrom("redirect_2", to("/properties/2")));

        when(weDeleteTheAlias("redirect_1"));

        then(aliases(), not(Matchers.contains(aliasPath("redirect_1"))));
        then(aliases(), contains(aliasPath("redirect_2")));
    }

    @Test
    public void ifAnAliasDoesNotExistThenTryingToGetItWillRedirectToTheEditPage() throws Exception {
        when(weGetTheAlias("production/myApplication/v123"));

        then(theAliasExists(), is(false));
    }

    @Test
    public void afterEditingAnAliasUsersAreRedirectedToTheEditPage() throws Exception {
        when(weCreateTheAlias("production/myApplication/v123"));
        then(aliases(), contains(aliasPath("production/myApplication/v123")));
    }

    private ActionUnderTest weCreateTheAlias(final String alias) {
        return new ActionUnderTest() {
            @Override
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                executeRequest(post(fullyQualifiedUri(relativeUriOf(method(on(AliasesResource.class).update(aliasPath(alias), null))))).form("to", aliasDestination("whatever"))).execute(interestingGivens, capturedInputAndOutputs);
                executeRequest(get(fullyQualifiedUri(relativeUriOf(method(on(AliasesResource.class).listAllAliases()))))).execute(interestingGivens, capturedInputAndOutputs);
                return capturedInputAndOutputs;
            }
        };
    }


    private ActionUnderTest weDeleteTheAlias(String alias) {
        return executeRequest(post(fullyQualifiedUri(relativeUriOf(method(on(AliasesResource.class).delete(aliasPath(alias)))))));
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
        return executeRequest(get(fullyQualifiedUri(relativeUriOf(method(on(AliasesResource.class).followRedirectHtml(aliasPath(alias)))))));
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
        return header.contains(MediaType.TEXT_HTML) || header.contains(MediaType.TEXT_PLAIN);
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