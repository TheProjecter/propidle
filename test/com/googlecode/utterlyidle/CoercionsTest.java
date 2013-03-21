package com.googlecode.utterlyidle;

import com.googlecode.utterlyidle.migrations.util.Coercions;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.fail;

public class CoercionsTest {

    @Test
    public void shouldPassThroughNull() throws Exception {
        assertThat(Coercions.coerce(null, Object.class), is(nullValue()));
    }

    @Test
    public void shouldPassThroughSubclasses() throws Exception {
        Timestamp timestamp = new Timestamp(0);
        Date coerced = Coercions.coerce(timestamp, Date.class);
        assertThat(coerced, is(sameInstance((Date) timestamp)));
    }

    @Test
    public void willThrowAnExceptionForUnsupportedConversions() throws Exception {
        Object instance = new Object();
        try {
            Coercions.coerce(instance, Date.class);
            fail("Expected exception");
        } catch (UnsupportedOperationException e) {
            assertThat(e.getMessage(), containsString(Date.class.getCanonicalName()));
            assertThat(e.getMessage(), containsString(instance.toString()));
        }
    }
}
