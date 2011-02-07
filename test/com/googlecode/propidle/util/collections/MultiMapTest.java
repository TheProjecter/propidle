package com.googlecode.propidle.util.collections;

import com.googlecode.propidle.util.collections.MultiMap;
import com.googlecode.totallylazy.Option;
import org.junit.Test;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.matchers.IterableMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MultiMapTest {
    private final MultiMap<String, String> map = new MultiMap<String, String>();

    @Test
    public void shouldBeAbleToRemoveEntireKeys() {
        map.put("a", "1");
        map.put("a", "2");
        
        assertThat(map.remove("a"), hasExactly("1", "2"));
        assertThat(map.containsKey("a"), is(false));

        assertThat(map.remove("b"), isEmpty(String.class));
    }

    @Test
    public void shouldBeAbleToRemoveSingleItemsForAKey() {
        map.put("a", "1");
        map.put("a", "2");
        map.put("a", "1");

        assertThat(map.remove("a", "1"), is((Option)some("1")));
        assertThat(map.get("a"), hasExactly("2", "1"));

        assertThat(map.remove("a", "99999"), is((Option)none()));
    }

    @Test
    public void shouldBeAbleToGetTheFirstItemForAKey() {
        map.put("a", "1");
        map.put("a", "2");

        assertThat(map.first("a"), is((Option)some("1")));
        assertThat(map.first("b"), is((Option)none()));
    }
}
