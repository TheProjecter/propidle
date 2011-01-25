package com.googlecode.propidle;

import org.junit.Before;
import org.junit.Test;

import static com.googlecode.propidle.AllPropertiesFromRecords.definePropertiesRecord;
import static com.googlecode.propidle.util.TemporaryRecords.temporaryRecords;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static com.googlecode.propidle.PropertiesPath.propertiesPath;
import static org.junit.Assert.fail;

import java.util.Properties;
import java.util.UUID;

public class AllPropertiesFromRecordsTest {
    private AllPropertiesFromRecords repository;
    private static final PropertiesPath PATH = propertiesPath(UUID.randomUUID().toString());

    @Before
    public void createRepository() throws Exception {
        repository = new AllPropertiesFromRecords(definePropertiesRecord(temporaryRecords()));
    }

    @Test
    public void shouldGetById() {
        Properties properties = new Properties();
        properties.setProperty("spang", "whoopit");

        repository.put(PATH, properties);

        assertThat(repository.get(PATH), is(properties));
    }

    @Test
    public void shouldDeleteProperties() {
        Properties original = new Properties();
        original.setProperty("will.be.deleted", "whoopit");

        repository.put(PATH, original);

        Properties updated = new Properties();
        updated.setProperty("new.property", "spang");
        repository.put(PATH, updated);

        assertThat(repository.get(PATH), is(updated));
    }

    @Test
    public void shouldCreateNonExistentProperties() {
        assertThat(repository.get(PATH), is(new Properties()));
    }

    @Test
    public void shouldBeAbleToHandleEmptyProperties() {
        Properties properties = new Properties();

        repository.put(PATH, properties);

        assertThat(repository.get(PATH), is(properties));
    }
}
