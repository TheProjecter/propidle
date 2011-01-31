package acceptance;

import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import acceptance.PropertiesApplicationTestCase;
import static acceptance.Values.with;
import static acceptance.steps.givens.PropertiesExist.propertiesExist;
import static acceptance.steps.thens.Responses.html;
import static acceptance.steps.thens.Responses.response;
import static acceptance.steps.whens.RequestIsMade.browserRequests;
import static com.googlecode.propidle.util.HtmlRegexes.td;
import static com.googlecode.propidle.util.HtmlRegexes.tr;
import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import org.junit.Test;

public class DiffTest extends PropertiesApplicationTestCase {
    @Test
    public void canProvideADiffOfTwoPropertiesFiles() throws Exception {
        given(propertiesExist(with(propertiesPath("base")).and(properties("changed=changed value 1\nunchanged=unchanged value\nremoved=removed value"))));
        given(propertiesExist(with(propertiesPath("other")).and(properties("changed=changed value 2\nunchanged=unchanged value\nnew=new value"))));

        System.out.println(when(browserRequests(get("/properties/base"))));
        when(browserRequests(
                get("/diff").
                        withQuery("left", "/properties/base").
                        withQuery("right", "/properties/other")));

        then(response(html()), matches(tr(td("removed"), td("removed value"), td(""))));
        then(response(html()), matches(tr(td("changed"), td("changed value 1"), td("changed value 2"))));
        then(response(html()), matches(tr(td("unchanged"), td("unchanged value"), td("unchanged value"))));
        then(response(html()), matches(tr(td("new"), td(""), td("new value"))));
    }
}
