package com.googlecode.propidle.status;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.yadic.Resolver;
import com.googlecode.yadic.resolvers.Resolvers;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

public class StatusChecks {
    private List<Class> classes = new ArrayList<Class>();
    private Resolver resolver;


    public StatusChecks(Resolver resolver){
       this.resolver = resolver;
    }

    public void add(Class aClass) {
        classes.add(aClass);
    }

    public Iterable<StatusCheckResult> checks() {
        return sequence(classes).map(resolve()).safeCast(StatusCheck.class).map(performCheck());

    }

    private Callable1<StatusCheck, StatusCheckResult> performCheck() {
        return new Callable1<StatusCheck, StatusCheckResult>() {
            public StatusCheckResult call(StatusCheck statusCheck) throws Exception {
                return statusCheck.check();
            }
        };
    }

    private Callable1<Class, Object> resolve() {
        return new Callable1<Class, Object>(){

            public Object call(Class aClass) throws Exception {
                return Resolvers.create(aClass, resolver).resolve(aClass);
            }
        };
    }
}
