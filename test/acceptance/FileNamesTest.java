package acceptance;

import acceptance.steps.givens.PropertiesExist;
import acceptance.steps.thens.LastResponse;
import static acceptance.steps.thens.LastResponse.theHtmlOf;
import acceptance.steps.whens.RequestIsMade;
import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.HtmlRegexes.*;
import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.not;
import org.junit.Test;

public class FileNamesTest extends PropertiesApplicationTestCase {
    @Test
    public void listsPropertiesFilesFilteredByName() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("pilot/ONE")).and(properties("a=1")));
        given(that(PropertiesExist.class).with(propertiesPath("pilot/TWO")).and(properties("a=1")));

        when(a(RequestIsMade.class).to(get("/filenames").withQuery("q", "+ONE +pilot")));

        then(theHtmlOf(), the(LastResponse.class), matches(li(anchor("/properties/pilot/ONE", img(".*file.png") + "/pilot/ONE"))));
        then(theHtmlOf(), the(LastResponse.class), not(matches(anchor("/properties/pilot/TWO", img(".*file.png") + "/pilot/TWO"))));

        when(a(RequestIsMade.class).to(get("/filenames?q=pilot")));

        then(theHtmlOf(), the(LastResponse.class), matches(li((anchor("/properties/pilot/ONE", "/pilot/ONE")))));
        then(theHtmlOf(), the(LastResponse.class), matches(li((anchor("/properties/pilot/TWO", "/pilot/TWO")))));
    }
}
