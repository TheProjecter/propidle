package com.googlecode.propidle.aliases;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.propidle.util.TestRecords.testRecordsWithAllMigrationsRun;
import static com.googlecode.propidle.aliases.Alias.alias;
import static com.googlecode.propidle.aliases.AliasPath.aliasPath;
import static com.googlecode.propidle.aliases.AliasDestination.aliasDestination;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.proxy.Call.method;
import static com.googlecode.totallylazy.proxy.Call.on;
import static com.googlecode.utterlyidle.io.Url.url;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AliasesFromRecordsTest {
    private final AliasesFromRecords aliases = new AliasesFromRecords(testRecordsWithAllMigrationsRun());

    @Test
    public void shouldGetIndividualAliasesByResourcePath() {
        AliasPath path = aliasPath("/spangDoodle");
        Alias alias = alias(path, aliasDestination("www.google.com"));

        aliases.put(alias);

        assertThat(aliases.get(path), is(alias));
    }

    @Test
    public void shouldGetAllAliases() {
        Alias alias1 = alias(aliasPath("/spangDoodle"), aliasDestination("www.google.com"));
        Alias alias2 = alias(aliasPath("/doobieTumble"), aliasDestination("www.yahoo.com"));

        aliases.put(alias1);
        aliases.put(alias2);

        assertThat(alphaSorted(aliases.getAll()), hasExactly(alias2, alias1));
    }

    @Test
    public void shouldOverwriteExistingAliases() {
        Alias alias1 = alias(aliasPath("/spangDoodle"), aliasDestination("www.google.com"));
        Alias alias2 = alias(aliasPath("/spangDoodle"), aliasDestination("www.yahoo.com"));

        aliases.put(alias1);
        aliases.put(alias2);

        assertThat(aliases.getAll(), hasExactly(alias2));
    }

    private Sequence<Alias> alphaSorted(Iterable<Alias> aliases) {
        return sequence(aliases).sortBy(method(on(Alias.class).from()));
    }
}
