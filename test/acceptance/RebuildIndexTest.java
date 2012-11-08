package acceptance;

import acceptance.steps.givens.PropertiesExistInDatabase;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import org.junit.Test;

import static acceptance.steps.thens.LastResponse.theHtmlOf;
import static acceptance.steps.thens.LastResponse.theStatusOf;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.*;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.td;
import static com.googlecode.propidle.util.matchers.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.Status.*;
import static org.hamcrest.Matchers.is;

public class RebuildIndexTest extends PropertiesApplicationTestCase {

    @Test
    public void shouldRebuildIndexes() throws Exception {
        given(that(PropertiesExistInDatabase.class).with(propertiesPath("pilot/myapp")).and(properties("a=1")));
        when(a(RequestIsMade.class).to(post("/rebuildIndex")));

        then(theStatusOf(), the(LastResponse.class), is(OK));
        
        when(a(RequestIsMade.class).to(get("/filenames?q=pilot")));

        then(theHtmlOf(), the(LastResponse.class), matches(li((anchor(absoluteUrl("properties/pilot/myapp"), "/pilot/myapp")))));

        when(a(RequestIsMade.class).to(get("/search?q=a*")));

        then(theHtmlOf(), the(LastResponse.class), matches(tr(td(anchor(absoluteUrl("properties/pilot/myapp"), "/properties/pilot/myapp")), td("a"), td("1"))));
    }

}
