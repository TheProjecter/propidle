package acceptance;

import acceptance.PropertiesApplicationTestCase;
import static acceptance.steps.whens.RequestIsMade.browserRequests;
import static acceptance.steps.thens.Responses.response;
import static acceptance.steps.thens.Responses.status;
import static acceptance.steps.thens.Responses.header;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.Status.SEE_OTHER;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RootResourceTest extends PropertiesApplicationTestCase {
    @Test
    public void shouldRedirectToFilenamesRoot() throws Exception {
        when(browserRequests(get("/")));

        then(response(status()), is(SEE_OTHER));
        then(response(header("location")), is("/filenames/"));
    }
}
