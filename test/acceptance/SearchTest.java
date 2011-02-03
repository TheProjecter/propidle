package acceptance;

import org.junit.Test;

import acceptance.steps.givens.PropertiesExist;
import static acceptance.steps.thens.LastResponse.theHtmlOf;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.HtmlRegexes.*;
import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.not;

public class SearchTest extends PropertiesApplicationTestCase {
    @Test
    public void allowsSearchingForAStringAcrossAllFiles() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("properties.one")).and(properties("property_one=stifled core dump")));
        given(that(PropertiesExist.class).with(propertiesPath("properties.two")).and(properties("property_two=massive short faced bear")));

        when(a(RequestIsMade.class).to(get("/search?q=core")));

        then(theHtmlOf(), the(LastResponse.class), matches(tr(td(anchor("/properties/properties.one")), td("property_one"), td("stifled core dump"))));
        then(theHtmlOf(), the(LastResponse.class), not(anyOf(matches("properties.two"), matches("property_two"), matches("massive short-faced bear"))));
    }
}
