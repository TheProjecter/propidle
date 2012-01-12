package acceptance;

import acceptance.steps.whens.RequestIsMade;

import static acceptance.steps.thens.LastResponse.theLocationOf;
import static acceptance.steps.thens.LastResponse.theStatusOf;

import acceptance.steps.thens.LastResponse;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.Status.SEE_OTHER;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RootResourceTest extends PropertiesApplicationTestCase {
    @Test
    public void shouldRedirectToFilenamesRoot() throws Exception {
        when(a(RequestIsMade.class).to(get("/")));

        then(theStatusOf(), the(LastResponse.class), is(SEE_OTHER));
        then(theLocationOf(), the(LastResponse.class), is(absoluteUrl("filenames/")));
    }
}
