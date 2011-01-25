package acceptance;

import acceptance.PropertiesApplicationTestCase;
import static acceptance.Values.with;
import static acceptance.steps.whens.RequestIsMade.weMakeRequest;
import static acceptance.steps.thens.Responses.response;
import static acceptance.steps.thens.Responses.content;
import static acceptance.steps.thens.Responses.html;
import static acceptance.steps.givens.PropertiesExist.propertiesExist;
import org.junit.Test;

import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.propidle.util.HtmlRegexes.tr;
import static com.googlecode.propidle.util.HtmlRegexes.td;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.Properties.properties;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;

public class BasicFunctionalityTest extends PropertiesApplicationTestCase {
    @Test
    public void allowsChangesToPropertyFilesByPostingFileContents() throws Exception {
        when(weMakeRequest(post("/properties/pilot/myapp/v1.5").withForm("properties", "a=1")));

        when(weMakeRequest(get("/properties/pilot/myapp/v1.5.properties")));

        then(response(content()), is("a=1\n"));
    }

    @Test
    public void recordsChangesWhenModifyingProperties() throws Exception {
        given(propertiesExist(with(propertiesPath("pilot/myapp")).and(properties("a=1"))));

        when(weMakeRequest(post("/properties/pilot/myapp").withForm("properties", "a=2")));
        when(weMakeRequest(get("/changes/pilot/myapp")));

        then(response(html()), matches(tr(td("/pilot/myapp"), td("\\d+"), td("a"), td(""), td("1"))));
        then(response(html()), matches(tr(td("/pilot/myapp"), td("\\d+"), td("a"), td("1"), td("2"))));
    }
}
