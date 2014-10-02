package com.googlecode.propidle.compositeproperties;

import acceptance.PropertiesApplicationTestCase;
import acceptance.steps.givens.PropertiesExist;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import org.junit.Before;
import org.junit.Test;

import static acceptance.steps.thens.LastResponse.theHtmlOf;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.input;
import static com.googlecode.propidle.util.matchers.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;

public class CompositePropertiesResourceTest extends PropertiesApplicationTestCase {

    @Before
    public void setup() throws Exception {
        usingHsql();
    }

    @Test
    public void composePropertiesWorksForMultipleProperties() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("pilot/ONE")).and(properties("a=1")));
        given(that(PropertiesExist.class).with(propertiesPath("pilot/TWO")).and(properties("a=1")));
        when(a(RequestIsMade.class).to(get("/composite?url=/properties/pilot/ONE&url=/properties/pilot/TWO").header("Accept", "text/html")));

        then(theHtmlOf(), the(LastResponse.class), matches(input("to", "/composite\\?url=%2Fproperties%2Fpilot%2FONE&url=%2Fproperties%2Fpilot%2FTWO")));
    }
}
