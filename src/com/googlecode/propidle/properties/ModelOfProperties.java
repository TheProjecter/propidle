package com.googlecode.propidle.properties;

import com.googlecode.totallylazy.*;
import com.googlecode.utterlyidle.rendering.Model;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.or;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Strings.startsWith;
import static com.googlecode.utterlyidle.rendering.Model.model;

public class ModelOfProperties {

    public static Model modelOfProperties(PropertiesPath path, Properties properties) {
        return modelOfProperties(properties).
                add("propertiesPath", path).
                add(TITLE, "Properties \"" + path + "\"");
    }

    public static Model modelOfProperties(Properties properties) {
        Model baseModel = model();
        return modelOfProperties(baseModel, properties);
    }

    public static Model modelOfProperties(Model baseModel, Properties properties) {
        return toReloadableProperties(properties).sortBy(asString()).foldLeft(baseModel, propertyToModel());
    }

    private static Sequence<String> toReloadableProperties(Properties properties) {
            String[] lines = com.googlecode.propidle.properties.Properties.asString(properties).split("\n");
            return sequence(lines).filter(not(comment()));
    }

    private static Predicate<String> comment() {
        return or(startsWith("#"), startsWith("!"));
    }

    public static Callable2<? super Model, ? super String, Model> propertyToModel() {
        return new Callable2<Model, String, Model>() {
            public Model call(Model model, String entry) throws Exception {
                return model.
                        add("properties", entry);
            }
        };
    }

}