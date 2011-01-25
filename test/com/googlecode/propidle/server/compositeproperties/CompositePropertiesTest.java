package com.googlecode.propidle.server.compositeproperties;

import com.googlecode.propidle.server.PropertiesApplicationTestCase;
import static com.googlecode.propidle.server.Values.with;
import static com.googlecode.propidle.server.steps.whens.RequestIsMade.weMakeRequest;
import static com.googlecode.propidle.server.steps.thens.Responses.content;
import static com.googlecode.propidle.server.steps.thens.Responses.response;
import static com.googlecode.propidle.server.steps.givens.PropertiesExist.propertiesExist;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CompositePropertiesTest extends PropertiesApplicationTestCase{
    @Test
    public void allowsCompositionOfManyPropertyFilesUsingUrls() throws Exception {
        given(propertiesExist(with(propertiesPath("common/myApp")).and(properties("from.base=1\nfrom.override=basevalue"))));
        given(propertiesExist(with(propertiesPath("pilot/myApp/v123")).and(properties("from.override=overriddenvalue"))));

        when(weMakeRequest(get("/composite.properties?url=/properties/common/myApp&url=/properties/pilot/myApp/v123&url=")));

        then(response(content()), is("from.base=1\nfrom.override=overriddenvalue\n"));
    }
}
