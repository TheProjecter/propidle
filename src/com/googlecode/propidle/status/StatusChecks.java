package com.googlecode.propidle.status;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.yadic.Resolver;
import com.googlecode.yadic.resolvers.Resolvers;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class StatusChecks {
    private final Resolver resolver;
    private final List<Class> classes = new ArrayList<Class>();


    public StatusChecks(Resolver resolver){
       this.resolver = resolver;
    }

    public void add(Class aClass) {
        classes.add(aClass);
    }

    public Iterable<StatusCheckResult> checks() {
        return sequence(classes).map(resolve()).safeCast(StatusCheck.class).map(performCheck());
    }

    private Callable1<Class, Object> resolve() {
        return new Callable1<Class, Object>(){

            public Object call(Class aClass) throws Exception {
                return Resolvers.create(aClass, resolver).resolve(aClass);
            }
        };
    }

    private Callable1<StatusCheck, StatusCheckResult> performCheck() {
        return new Callable1<StatusCheck, StatusCheckResult>() {
            public StatusCheckResult call(StatusCheck statusCheck) throws Exception {
                return statusCheck.check();
            }
        };
    }

}
