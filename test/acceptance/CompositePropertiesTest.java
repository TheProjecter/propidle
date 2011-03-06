package acceptance;

import acceptance.steps.givens.PropertiesExist;
import acceptance.steps.thens.LastResponse;
import static acceptance.steps.thens.LastResponse.thePropertiesFileFrom;
import acceptance.steps.whens.RequestIsMade;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.is;
import org.junit.Test;

public class CompositePropertiesTest extends PropertiesApplicationTestCase {
    @Test
    public void allowsCompositionOfManyPropertyFilesUsingUrls() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("common/myApp")).and(properties("from.base=1\nfrom.override=basevalue")));
        given(that(PropertiesExist.class).with(propertiesPath("pilot/myApp/v123")).and(properties("from.override=overriddenvalue")));

        when(a(RequestIsMade.class).to(get("/composite.properties?url=/properties/common/myApp&url=/properties/pilot/myApp/v123&url=")));

        then(thePropertiesFileFrom(), the(LastResponse.class), is(properties("from.base=1\nfrom.override=overriddenvalue\n")));
    }
}
