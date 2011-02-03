package acceptance;

import org.junit.Test;

import acceptance.steps.givens.CurrentRevision;
import acceptance.steps.givens.PropertiesExist;
import static acceptance.steps.thens.LastResponse.*;
import acceptance.steps.thens.LastResponse;
import acceptance.steps.whens.RequestIsMade;
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
        given(that(CurrentRevision.class).startsAt(revisionNumber(0)));
        given(that(PropertiesExist.class).with(propertiesPath("properties.one")).and(properties("revision=0")));
        given(that(PropertiesExist.class).with(propertiesPath("properties.one")).and(properties("revision=1")));

        when(a(RequestIsMade.class).to(get("/properties/properties.one.properties").withQuery("revision", "0")));

        then(theContentOf(), the(LastResponse.class), is("# /properties/properties.one?revision=0\nrevision=0\n"));
    }

    @Test
    public void htmlRepresentationOfPreviousRevisionsOfPropertiesFilesIsReadOnly() throws Exception {
        given(that(CurrentRevision.class).startsAt(revisionNumber(0)));
        given(that(PropertiesExist.class).with(propertiesPath("properties.one")).and(properties("a=some value")));

        when(a(RequestIsMade.class).to(get("/properties/properties.one").withQuery("revision", "0")));

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