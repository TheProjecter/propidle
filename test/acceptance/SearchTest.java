package acceptance;

import static com.googlecode.propidle.Properties.properties;
import acceptance.PropertiesApplicationTestCase;
import static acceptance.Values.with;
import static acceptance.steps.whens.RequestIsMade.weMakeRequest;
import static acceptance.steps.thens.Responses.response;
import static acceptance.steps.thens.Responses.html;
import static acceptance.steps.givens.PropertiesExist.propertiesExist;
import static com.googlecode.propidle.util.HtmlRegexes.*;
import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.not;
import org.junit.Test;

public class SearchTest extends PropertiesApplicationTestCase {
    @Test
    public void allowsSearchingForAStringAcrossAllFiles() throws Exception {
        given(propertiesExist(with(propertiesPath("properties.one")).and(properties("property_one=stifled core dump"))));
        given(propertiesExist(with(propertiesPath("properties.two")).and(properties("property_two=massive short faced bear"))));

        when(weMakeRequest(get("/search?q=core")));

        then(response(html()), matches(tr(td(a("/properties/properties.one")), td("property_one"), td("stifled core dump"))));
        then(response(html()), not(anyOf(matches("properties.two"), matches("property_two"), matches("massive short-faced bear"))));
    }
}
