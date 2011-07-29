package com.googlecode.propidle.monitoring;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Callable;

public class RegisterCountingMBeans implements Callable<Void> {
    private final HttpRequestCounter httpRequestCounter;

    public RegisterCountingMBeans(HttpRequestCounter httpRequestCounter) {
        this.httpRequestCounter = httpRequestCounter;
    }

    public Void call() throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName mBeanName = new ObjectName("propidle.monitoring:type=HttpRequestCounter");
        mBeanServer.registerMBean(httpRequestCounter, mBeanName);

        return null;
    }
}
