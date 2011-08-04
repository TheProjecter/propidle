package com.googlecode.propidle.scheduling;

import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.Request;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SchedulableRequests {

    private final Map<String, RunnableRequest> tasks = new HashMap<String, RunnableRequest>();
    private final Application application;

    public SchedulableRequests(Application application) {
        this.application = application;
    }

    public Collection availableTaskNames() {
        return tasks.keySet();
    }

    public RunnableRequest runnableRequest(String name) {
        return tasks.get(name);
    }

    public void addTask(String taskName, Request request) {
        Container container = new SimpleContainer().
                addInstance(Application.class, application).
                addInstance(Request.class, request).
                add(RunnableRequest.class);
        tasks.put(taskName, container.get(RunnableRequest.class));
    }
}


