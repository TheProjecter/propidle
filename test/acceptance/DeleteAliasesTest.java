package acceptance;

import com.googlecode.propidle.TestPropertiesApplication;
import com.googlecode.propidle.aliases.*;
import com.googlecode.totallylazy.*;
import com.googlecode.utterlyidle.*;
import com.googlecode.utterlyidle.annotations.AnnotatedBindings;
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

import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.totallylazy.UrlEncodedMessage.decode;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.yatspec.state.givenwhenthen.SyntacticSugar.to;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;

@RunWith(SpecRunner.class)
public class DeleteAliasesTest extends TestState {

    public static final String ALIASES_AFTER_DELETION = "Aliases after deletion";
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

        when(deleteAlias("redirect_1"));

        then(aliases(), not(Matchers.contains(aliasPath("redirect_1"))));
        then(aliases(), contains(aliasPath("redirect_2")));
    }

    private StateExtractor<Collection<AliasPath>> aliases() {
        return new StateExtractor<Collection<AliasPath>>() {
            @Override
            public Collection<AliasPath> execute(CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                return new AliasesPage(capturedInputAndOutputs.getType(ALIASES_AFTER_DELETION, Response.class)).getAliases();
            }
        };
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

    private ActionUnderTest deleteAlias(final String alias) {
        return new ActionUnderTest() {
            @Override
            public CapturedInputAndOutputs execute(InterestingGivens interestingGivens, CapturedInputAndOutputs capturedInputAndOutputs) throws Exception {
                Uri uri = AnnotatedBindings.relativeUriOf(method(on(AliasesResource.class).delete(aliasPath(alias))));
                Request request = RequestBuilder.post(server.uri().mergePath(uri.path())).build();
                Response aliasesResponse = httpHandler().handle(request);
                capturedInputAndOutputs.add(ALIASES_AFTER_DELETION, aliasesResponse);
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
                        return container.get(Aliases.class).put(Alias.alias(aliasPath(from), AliasDestination.aliasDestination(to)));
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
