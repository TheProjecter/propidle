package acceptance;

import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import org.junit.Ignore;
import org.junit.Test;

import static acceptance.steps.thens.LastResponse.theHtmlOf;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.containsString;

@Ignore
public class StatusTest extends PropertiesApplicationTestCase {
    @Test
    public void presentsAStatusReport() throws Exception {
        when(a(RequestIsMade.class).to(get("/status")));

        then(theHtmlOf(), the(LastResponse.class), containsString("<title>Status Report</title>"));
        then(theHtmlOf(), the(LastResponse.class), containsString("Can read Lucene Directory"));
    }
}
