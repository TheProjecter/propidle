package com.googlecode.propidle;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.rendering.Model;

import static com.googlecode.totallylazy.Predicates.notNullValue;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.utterlyidle.handlers.HandlerRule.entity;
import static com.googlecode.utterlyidle.rendering.Model.model;

public class ModelName {
    private static final String MODEL_NAME = "MODEL_NAME";

    public static LogicalPredicate<Model> nameIs(final String name) {
        Predicate<Model> nameMatcher = new Predicate<Model>() {
            public boolean matches(Model model) {
                return model != null && model.containsKey(MODEL_NAME) && name.equals(model.first(MODEL_NAME));
            }
        };
        return notNullValue(Model.class).and(nameMatcher);
    }

    public static Model modelWithName(String name){
        return model().add(MODEL_NAME, name);
    }

    public static Model name(Model model, String name) {
        return model.add(MODEL_NAME, name);
    }

    public static LogicalPredicate<Pair<Request, Response>> modelNameIs(final String name) {
        return where(entity(Model.class), nameIs(name));
    }
}
