package com.googlecode.propidle.server.dashboard;

import com.googlecode.propidle.server.PropertiesApplicationTestCase;
import static com.googlecode.propidle.server.steps.whens.RequestIsMade.weMakeRequest;
import static com.googlecode.propidle.server.steps.thens.Responses.response;
import static com.googlecode.propidle.server.steps.thens.Responses.status;
import static com.googlecode.propidle.server.steps.thens.Responses.header;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.Status.SEE_OTHER;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DashboardTest extends PropertiesApplicationTestCase{
    @Test
    public void shouldRedirectToFilenamesRoot() throws Exception {
        when(weMakeRequest(get("/")));

        then(response(status()), is(SEE_OTHER));
        then(response(header("location")), is("/filenames/"));
    }
}
