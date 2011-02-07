package acceptance;

import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static acceptance.steps.thens.LastResponse.theHtmlOf;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.givens.PropertiesExist;
import acceptance.steps.whens.RequestIsMade;
import static com.googlecode.propidle.util.HtmlRegexes.td;
import static com.googlecode.propidle.util.HtmlRegexes.tr;
import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import org.junit.Test;

public class DiffTest extends PropertiesApplicationTestCase {
    @Test
    public void canProvideADiffOfTwoPropertiesFiles() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("base")).and(properties("changed=changed value 1\nunchanged=unchanged value\nremoved=removed value")));
        given(that(PropertiesExist.class).with(propertiesPath("other")).and(properties("changed=changed value 2\nunchanged=unchanged value\nnew=new value")));

        when(a(RequestIsMade.class).to(get("/properties/base")));
        when(a(RequestIsMade.class).to(
                get("/diff").
                        withQuery("left", "/properties/base").
                        withQuery("right", "/properties/other")));

        then(theHtmlOf(), the(LastResponse.class), matches(tr(td("removed"), td("removed value"), td(""))));
        then(theHtmlOf(), the(LastResponse.class), matches(tr(td("changed"), td("changed value 1"), td("changed value 2"))));
        then(theHtmlOf(), the(LastResponse.class), matches(tr(td("unchanged"), td("unchanged value"), td("unchanged value"))));
        then(theHtmlOf(), the(LastResponse.class), matches(tr(td("new"), td(""), td("new value"))));
    }
}
