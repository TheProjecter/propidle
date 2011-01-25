package acceptance;

import acceptance.PropertiesApplicationTestCase;
import static acceptance.Values.with;
import static acceptance.steps.whens.RequestIsMade.weMakeRequest;
import static acceptance.steps.thens.Responses.response;
import static acceptance.steps.thens.Responses.html;
import static acceptance.steps.givens.PropertiesExist.propertiesExist;
import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.propidle.util.HtmlRegexes.li;
import static com.googlecode.propidle.util.HtmlRegexes.a;
import static com.googlecode.propidle.util.HtmlRegexes.img;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class FileNamesTest extends PropertiesApplicationTestCase {
    @Test
    public void listsPropertiesFilesFilteredByName() throws Exception {
        given(propertiesExist(with(propertiesPath("pilot/ONE")).and(properties("a=1"))));
        given(propertiesExist(with(propertiesPath("pilot/TWO")).and(properties("a=1"))));

        when(weMakeRequest(get("/filenames").withQuery("q", "+ONE +pilot")));

        then(response(html()), matches(li(a("/properties/pilot/ONE", img(".*file.png") + "/pilot/ONE"))));
        then(response(html()), not(matches(a("/properties/pilot/TWO", img(".*file.png") + "/pilot/TWO"))));

        when(weMakeRequest(get("/filenames?q=pilot")));

        then(response(html()), matches(li((a("/properties/pilot/ONE", "/pilot/ONE")))));
        then(response(html()), matches(li((a("/properties/pilot/TWO", "/pilot/TWO")))));
    }
}
