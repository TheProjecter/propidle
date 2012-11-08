package acceptance;

import acceptance.steps.givens.PropertiesExist;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import org.junit.Test;

import static acceptance.steps.thens.LastResponse.theHtmlOf;
import static com.googlecode.propidle.client.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.*;
import static com.googlecode.propidle.util.matchers.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.not;

public class SearchTest extends PropertiesApplicationTestCase {
    @Test
    public void allowsSearchingForAStringAcrossAllFiles() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("properties.one")).and(properties("property_one=stifled core dump")));
        given(that(PropertiesExist.class).with(propertiesPath("properties.two")).and(properties("property_two=massive short faced bear")));

        when(a(RequestIsMade.class).to(get("/search?q=core")));

        then(theHtmlOf(), the(LastResponse.class), matches(result("properties/properties.one", "property_one", "stifled core dump")));
        then(theHtmlOf(), the(LastResponse.class), not(anyOf(matches("properties.two"), matches("property_two"), matches("massive short-faced bear"))));
    }

    @Test
    public void caseInsensitivePropertyNameCanBeSearched() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("path")).and(properties("a.Property.name=foodbar")));
        when(a(RequestIsMade.class).to(get("/search?q=a.property.name")));
        then(theHtmlOf(), the(LastResponse.class), matches(result("properties/path", "a.Property.name", "foodbar")));
    }

    private String result(String path, String name, String value) {
        return tr(td(anchor(absoluteUrl(path), "/" + path)), td(name), td(value));
    }
}
