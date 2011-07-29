package acceptance;

import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import static acceptance.steps.thens.LastResponse.theContentOf;
import static acceptance.steps.thens.LastResponse.theHeader;
import static acceptance.steps.thens.LastResponse.theHtmlOf;
import static com.googlecode.utterlyidle.HttpHeaders.CONTENT_TYPE;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class StatusTest extends PropertiesApplicationTestCase {
    @Test
    public void presentsAStatusReport() throws Exception {
        when(a(RequestIsMade.class).to(get("/status")));

        then(theHtmlOf(), the(LastResponse.class), containsString("<title>Status Report</title>"));
        then(theHtmlOf(), the(LastResponse.class), containsString("Can read Lucene Directory"));
    }

}
