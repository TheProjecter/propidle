package acceptance;

import org.junit.Test;

import static acceptance.Values.with;
import static acceptance.steps.givens.CurrentRevision.revisionNumbersStartAt;
import static acceptance.steps.givens.PropertiesExist.propertiesExist;
import static acceptance.steps.thens.Responses.*;
import static acceptance.steps.whens.RequestIsMade.browserRequests;
import static com.googlecode.propidle.Properties.properties;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.RegexMatcher.matches;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class RevisionsTest extends PropertiesApplicationTestCase {
    @Test
    public void itIsPossibleToSeeAPropertiesFileAsItWasAtAParticularRevision() throws Exception {
        given(revisionNumbersStartAt(revisionNumber(0)));
        given(propertiesExist(with(propertiesPath("properties.one")).and(properties("revision=0"))));
        given(propertiesExist(with(propertiesPath("properties.one")).and(properties("revision=1"))));

        when(browserRequests(get("/properties/properties.one.properties").withQuery("revision", "0")));

        then(response(content()), is("revision=0\n"));
    }

    @Test
    public void htmlRepresentationOfPreviousRevisionsOfPropertiesFilesIsReadOnly() throws Exception {
        given(revisionNumbersStartAt(revisionNumber(0)));
        given(propertiesExist(with(propertiesPath("properties.one")).and(properties("a=some value"))));

        when(browserRequests(get("/properties/properties.one").withQuery("revision", "0")));

        then(response(html()), not(matches("<form")));
        then(response(html()), matches("Revision 0"));
    }

    @Test
    public void propertiesInACompositeWillBeRestrictedToTheRevisionSpecified() throws Exception {
        given(revisionNumbersStartAt(revisionNumber(0)));
        given(propertiesExist(with(propertiesPath("properties.one")).and(properties("revision=0"))));
        given(propertiesExist(with(propertiesPath("properties.one")).and(properties("revision=1"))));

        when(browserRequests(get("/composite.properties?url=/properties/properties.one&revision=0")));

        then(response(content()), is("revision=0\n"));
    }
}