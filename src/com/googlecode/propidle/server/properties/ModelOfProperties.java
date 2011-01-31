package com.googlecode.propidle.server.properties;

import com.googlecode.propidle.PropertiesPath;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.utterlyidle.rendering.Model;

import java.util.Map;
import java.util.Properties;

import static com.googlecode.propidle.Properties.key;
import static com.googlecode.propidle.server.PropertiesModule.TITLE;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.utterlyidle.rendering.Model.model;

public class ModelOfProperties {
    public static Model modelOfProperties(PropertiesPath path, Properties properties) {
        return modelOfProperties(properties).
                add("propertiesPath", path).
                add(TITLE, "Properties \"" + path + "\"");
    }

    public static Model modelOfProperties(Properties properties) {
        return sequence(properties.entrySet()).
                sortBy(key()).
                foldLeft(model(), propertyToModel());
    }

    public static Callable2<? super Model, ? super Map.Entry<Object, Object>, Model> propertyToModel() {
        return new Callable2<Model, Map.Entry<Object, Object>, Model>() {
            public Model call(Model model, Map.Entry<Object, Object> entry) throws Exception {
                return model.
                        add("properties",
                            model().
                                    add("name", entry.getKey()).
                                    add("value", entry.getValue()));
            }
        };
    }
}