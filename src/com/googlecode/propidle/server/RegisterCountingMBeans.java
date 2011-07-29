package com.googlecode.propidle.server;

import com.googlecode.propidle.monitoring.HttpRequestCounter;

import javax.management.*;
import java.io.Closeable;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Callable;

public class RegisterCountingMBeans implements Callable<Void>, Closeable {
    private final HttpRequestCounter httpRequestCounter;
    private ObjectName mBeanName;

    public RegisterCountingMBeans(HttpRequestCounter httpRequestCounter) throws MalformedObjectNameException {
        this.httpRequestCounter = httpRequestCounter;
        mBeanName = new ObjectName("propidle.monitoring:type=HttpRequestCounter");
    }

    public Void call() throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        mBeanServer.registerMBean(httpRequestCounter, mBeanName);
        return null;
    }

    public void close() throws IOException {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            mBeanServer.unregisterMBean(mBeanName);
        } catch (InstanceNotFoundException e) {
            throw new IOException(e);
        } catch (MBeanRegistrationException e) {
            throw new IOException(e);
        }
    }
}
