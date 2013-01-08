package com.googlecode.propidle.versioncontrol.changes;

import com.googlecode.lazyrecords.Keyword;
import com.googlecode.lazyrecords.Queryable;
import com.googlecode.lazyrecords.sql.expressions.Expression;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import static com.googlecode.lazyrecords.sql.expressions.Expressions.expression;
import static com.googlecode.propidle.properties.PropertiesPath.toPropertiesPath;
import static com.googlecode.propidle.versioncontrol.changes.AllChangesFromRecords.PROPERTIES_PATH;
import static java.lang.String.format;

public class ChildPathsFromSql implements ChildPaths {
    private final Queryable<Expression> queryable;

    public ChildPathsFromSql(Queryable<Expression> queryable) {
        this.queryable = queryable;
    }

    @Override
    public Sequence<PropertiesPath> childPaths(PropertiesPath parent) {
        return queryable.query(expression(
                "WITH  max_revision as (select properties_path, property_name, max(revision_number) as revision_number " +
                        "                  from   changes " +
                        "                  where  properties_path like ? and properties_path <> ?" +
                        "                  group by properties_path, property_name) " +
                        "select DISTINCT c.properties_path " +
                        "from   changes c, max_revision " +
                        "where  c.properties_path = max_revision.properties_path " +
                        "and    c.revision_number = max_revision.revision_number " +
                        "and    c.property_name   = max_revision.property_name " +
                        "and    c.updated_value   IS NOT NULL " +
                        "order by 1", format("%s%%", parent), parent), Sequences.<Keyword<?>>sequence(PROPERTIES_PATH)).
                map(PROPERTIES_PATH).
                map(toPropertiesPath());
    }
}
