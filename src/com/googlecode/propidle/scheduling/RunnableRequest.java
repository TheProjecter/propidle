package com.googlecode.propidle.scheduling;

import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.Request;
import com.googlecode.utterlyidle.Response;

public class RunnableRequest implements Runnable {

    private final Application application;
    private final Request request;

    public RunnableRequest(Application application, Request request) {
        this.application = application;
        this.request = request;
    }

    public void run() {
        try {
            Response response = application.handle(request);
            if(response.status().code() >= 400) {
                System.err.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Request request() {
        return request;
    }
}
