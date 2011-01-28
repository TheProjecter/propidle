package acceptance;

import org.junit.Test;

import static acceptance.Values.with;
import static acceptance.steps.givens.CurrentRevision.revisionNumbersStartAt;
import static acceptance.steps.givens.PropertiesExist.propertiesExist;
import static acceptance.steps.thens.Responses.content;
import static acceptance.steps.thens.Responses.response;
import static acceptance.steps.whens.RequestIsMade.weMakeRequest;
import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.is;

public class RevisionsTest extends PropertiesApplicationTestCase {
    @Test
    public void itIsPossibleToSeeAPropertiesFileAsItWasAtAParticularRevision() throws Exception {
        given(revisionNumbersStartAt(revisionNumber(0)));
        given(propertiesExist(with(propertiesPath("properties.one")).and(properties("a=some value"))));
        given(propertiesExist(with(propertiesPath("properties.one")).and(properties("b=some other value"))));

        when(weMakeRequest(get("/properties/properties.one.properties").withQuery("revision", "0")));

        then(response(content()), is("a=some value\n"));
    }
}