package acceptance;

import acceptance.steps.givens.PropertiesExist;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import org.junit.Before;
import org.junit.Test;

import static acceptance.steps.thens.LastResponse.theContentOf;
import static acceptance.steps.thens.LastResponse.theHtmlOf;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.*;
import static com.googlecode.propidle.util.matchers.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class FileNamesTest extends PropertiesApplicationTestCase {


    @Before
    public void setup() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("pilot/ONE")).and(properties("a=1")));
        given(that(PropertiesExist.class).with(propertiesPath("pilot/TWO")).and(properties("a=1")));
    }
    @Test
    public void listsPropertiesFilesFilteredByName() throws Exception {

        when(a(RequestIsMade.class).to(get("/filenames").withQuery("q", "+ONE +pilot")));

        then(theHtmlOf(), the(LastResponse.class), matches(li(anchor(absoluteUrl("properties/pilot/ONE"), img(".*file.png") + "/pilot/ONE"))));
        then(theHtmlOf(), the(LastResponse.class), not(matches(anchor(absoluteUrl("properties/pilot/TWO"), img(".*file.png") + "/pilot/TWO"))));

        when(a(RequestIsMade.class).to(get("/filenames?q=pilot")));

        then(theHtmlOf(), the(LastResponse.class), matches(li((anchor(absoluteUrl("properties/pilot/ONE"), "/pilot/ONE")))));
        then(theHtmlOf(), the(LastResponse.class), matches(li((anchor(absoluteUrl("properties/pilot/TWO"), "/pilot/TWO")))));
    }

    @Test
    public void shouldReturnFileNamesInCSVFormat() throws Exception {
        when(a(RequestIsMade.class).to(get("/filenames").withQuery("q", "p*").withHeader("Accept", "text/plain")));
        then(theContentOf(),the(LastResponse.class), is("/properties/pilot/ONE,/properties/pilot/TWO"));
    }
}
