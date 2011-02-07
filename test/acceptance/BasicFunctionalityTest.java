package acceptance;

import static acceptance.steps.thens.LastResponse.thePropertiesFileFrom;
import static acceptance.steps.thens.LastResponse.theHtmlOf;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.givens.PropertiesExist;
import acceptance.steps.whens.RequestIsMade;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.td;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.tr;
import static com.googlecode.propidle.util.matchers.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

public class BasicFunctionalityTest extends PropertiesApplicationTestCase {
    @Test
    public void allowsChangesToPropertyFilesByPostingFileContents() throws Exception {
        when(a(RequestIsMade.class).to(post("/properties/pilot/myapp/v1.5").withForm("properties", "a=1")));

        when(a(RequestIsMade.class).to(get("/properties/pilot/myapp/v1.5.properties")));

        then(thePropertiesFileFrom(), the(LastResponse.class), is(properties("a=1\n")));
    }

    @Test
    public void recordsChangesWhenModifyingProperties() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("pilot/myapp")).and(properties("a=1")));

        when(a(RequestIsMade.class).to(post("/properties/pilot/myapp").withForm("properties", "a=2")));
        when(a(RequestIsMade.class).to(get("/changes/pilot/myapp")));

        then(theHtmlOf(), the(LastResponse.class), matches(tr(td("/pilot/myapp"), td("\\d+"), td("a"), td(""), td("1"))));
        then(theHtmlOf(), the(LastResponse.class), matches(tr(td("/pilot/myapp"), td("\\d+"), td("a"), td("1"), td("2"))));
    }
}
