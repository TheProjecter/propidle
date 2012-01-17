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
import static com.googlecode.propidle.util.matchers.HtmlRegexes.anchor;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.img;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.input;
import static com.googlecode.propidle.util.matchers.HtmlRegexes.li;
import static com.googlecode.propidle.util.matchers.RegexMatcher.matches;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.not;

public class CompositePropertiesResourceTest extends PropertiesApplicationTestCase {

    @Before
    public void setup() throws Exception {
        given(that(PropertiesExist.class).with(propertiesPath("pilot/ONE")).and(properties("a=1")));
        given(that(PropertiesExist.class).with(propertiesPath("pilot/TWO")).and(properties("a=1")));
    }

    @Test
    public void composePropertiesWorksForMultipleProperties() throws Exception {
        when(a(RequestIsMade.class).to(get("/composite?url=/properties/pilot/ONE&url=/properties/pilot/TWO")));

        then(theHtmlOf(), the(LastResponse.class), matches(input("to", "/composite\\?url=%2Fproperties%2Fpilot%2FONE&url=%2Fproperties%2Fpilot%2FTWO")));
    }
}
