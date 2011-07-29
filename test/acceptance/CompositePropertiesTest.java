package acceptance;

import acceptance.steps.givens.PropertiesExist;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import com.googlecode.utterlyidle.MediaType;
import com.googlecode.utterlyidle.RequestBuilder;
import org.junit.Test;

import static acceptance.steps.thens.LastResponse.thePropertiesFileFrom;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.utterlyidle.MediaType.TEXT_PLAIN;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.is;

public class CompositePropertiesTest extends PropertiesApplicationTestCase {
    @Test
    public void allowsCompositionOfManyPropertyFilesUsingUrls() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("common/myApp")).and(properties("from.base=1\nfrom.override=basevalue")));
        given(that(PropertiesExist.class).with(propertiesPath("pilot/myApp/v123")).and(properties("from.override=overriddenvalue")));

        when(a(RequestIsMade.class).to(get("/composite.properties?url=/properties/common/myApp&url=/properties/pilot/myApp/v123&url=")));

        then(thePropertiesFileFrom(), the(LastResponse.class), is(properties("from.base=1\nfrom.override=overriddenvalue\n")));
    }
}
