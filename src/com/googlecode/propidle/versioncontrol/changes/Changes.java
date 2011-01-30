package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.propidle.PropertiesPath;
import com.googlecode.propidle.PropertyComparison;
import com.googlecode.propidle.PropertyName;
import static com.googlecode.propidle.versioncontrol.changes.Change.change;
import com.googlecode.propidle.versioncontrol.revisions.RevisionNumber;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Sequence;

import java.util.Properties;

public class Changes {
    public static Callable2<? super Properties, ? super Change, Properties> applyChange() {
        return new Callable2<Properties, Change, Properties>() {
            public Properties call(Properties properties, Change change) throws Exception {
                return change.applyTo(properties);
            }
        };
    }

    public static Iterable<Change> changes(PropertiesPath path, RevisionNumber revisionNumber, Sequence<PropertyComparison> comparisons) {
        return comparisons.map(toChange(path, revisionNumber));
    }

    public static Callable1<? super PropertyComparison, Change> toChange(final PropertiesPath path, final RevisionNumber revisionNumber) {
        return new Callable1<PropertyComparison, Change>() {
            public Change call(PropertyComparison propertyComparison) throws Exception {
                return change(revisionNumber, path, propertyComparison);
            }
        };
    }

    public static Properties properties(Sequence<Change> changes) {
        return changes.sortBy(revisionNumberOfChange()).fold(new Properties(), applyChange());
    }

    public static Callable1<? super Change, RevisionNumber> revisionNumberOfChange() {
        return new Callable1<Change, RevisionNumber>() {
            public RevisionNumber call(Change change) throws Exception {
                return change.revisionNumber();
            }
        };
    }

    public static Callable1<? super Change, PropertyName> propertyNameOfChange() {
        return new Callable1<Change, PropertyName>() {
            public PropertyName call(Change change) throws Exception {
                return change.propertyName();
            }
        };
    }
}
