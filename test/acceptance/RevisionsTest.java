package acceptance;

import acceptance.steps.givens.CurrentRevision;
import acceptance.steps.givens.PropertiesExist;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
import org.junit.Before;
import org.junit.Test;

import static acceptance.steps.thens.LastResponse.*;
import static com.googlecode.propidle.properties.Properties.properties;
import static com.googlecode.propidle.properties.PropertiesPath.propertiesPath;
import static com.googlecode.propidle.util.matchers.RegexMatcher.matches;
import static com.googlecode.propidle.versioncontrol.revisions.RevisionNumber.revisionNumber;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class RevisionsTest extends PropertiesApplicationTestCase {

    @Before
    public void withHsql() throws Exception {
        usingHsql();
    }

    @Test
    public void itIsPossibleToSeeAPropertiesFileAsItWasAtAParticularRevision() throws Exception {
        given(that(CurrentRevision.class).startsAt(revisionNumber(0)));
        given(that(PropertiesExist.class).with(propertiesPath("properties.one")).and(properties("revision=0")));
        given(that(PropertiesExist.class).with(propertiesPath("properties.one")).and(properties("revision=1")));

        when(a(RequestIsMade.class).to(get("/properties/properties.one.properties").query("revision", "0")));

        then(theContentOf(), the(LastResponse.class), is("revision=0\r"));
    }

    @Test
    public void htmlRepresentationOfPreviousRevisionsOfPropertiesFilesIsReadOnly() throws Exception {
        given(that(CurrentRevision.class).startsAt(revisionNumber(0)));
        given(that(PropertiesExist.class).with(propertiesPath("properties.one")).and(properties("a=some value")));

        when(a(RequestIsMade.class).to(get("/properties/properties.one").query("revision", "0")));

        then(theHtmlOf(), the(LastResponse.class), not(matches("<form")));
        then(theHtmlOf(), the(LastResponse.class), matches("Revision 0"));
    }

    @Test
    public void propertiesInACompositeWillBeRestrictedToTheRevisionSpecified() throws Exception {
        given(that(CurrentRevision.class).startsAt(revisionNumber(0)));
        given(that(PropertiesExist.class).with(propertiesPath("properties.one")).and(properties("revision=0")));
        given(that(PropertiesExist.class).with(propertiesPath("properties.one")).and(properties("revision=1")));

        when(a(RequestIsMade.class).to(get("/composite.properties?url=/properties/properties.one&revision=0")));

        then(thePropertiesFileFrom(), the(LastResponse.class), is(properties("revision=0\n")));
    }
}