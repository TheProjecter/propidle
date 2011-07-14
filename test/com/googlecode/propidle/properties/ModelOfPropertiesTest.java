package com.googlecode.propidle.properties;

import com.googlecode.totallylazy.Callable2;
import com.googlecode.utterlyidle.rendering.Model;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import static com.googlecode.propidle.properties.ModelOfProperties.modelOfProperties;
import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ModelOfPropertiesTest {

    @Test
    public void shouldConvertPropertiesToAReloadableFile() throws IOException {
        Properties properties = new Properties();
        properties.load(new StringReader("weird\\ property1=value1\n" +
                "weird\\\\property1=value2\n" +
        "more : weird stuff\n" +
        "even\\=more  = cool stuff\n" +
        "enough=of=the equals\n" +
        "keep\\=on going"));

        String propertiesAsString = sequence(modelOfProperties(properties).get("properties")).toString("\n");
        Properties reloadedProperties = com.googlecode.propidle.properties.Properties.properties(propertiesAsString);

        assertThat(reloadedProperties, is(equalTo(properties)));
    }

}
