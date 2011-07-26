package com.googlecode.propidle.scheduling;

import com.googlecode.propidle.client.DynamicProperties;
import com.googlecode.propidle.scheduling.SchedulableTask;
import com.googlecode.propidle.scheduling.Scheduler;
import com.googlecode.utterlyidle.Application;
import com.googlecode.utterlyidle.RequestBuilder;
import com.googlecode.utterlyidle.RestApplication;
import com.googlecode.yadic.Container;
import com.googlecode.yadic.SimpleContainer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.googlecode.propidle.client.DynamicProperties.*;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class SchedulerTest {

    private static final String NAME = "whatever";
    private final ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
    private final ScheduledFuture scheduledFuture = mock(ScheduledFuture.class);
    private Scheduler scheduler;

    @Before
    public void createScheduler() throws Exception {
        scheduler = new Scheduler(container());
    }

    @Test
    public void shouldScheduleTaskIfNotScheduledYet() throws Exception {
        scheduler.schedule(taskWith(NAME));
        verify(executorService).scheduleWithFixedDelay(Matchers.<Runnable>any(), anyLong(), anyLong(), Matchers.<TimeUnit>any());
    }

    @Test
    public void shouldCancelTaskIfAlreadyScheduled() throws Exception {
        when(executorService.scheduleWithFixedDelay(Matchers.<Runnable>any(), anyLong(), anyLong(), Matchers.<TimeUnit>any())).thenReturn(scheduledFuture);
        scheduler.schedule(taskWith(NAME));

        scheduler.schedule(taskWith(NAME));
        verify(scheduledFuture).cancel(true);
        verify(executorService, times(2)).scheduleWithFixedDelay(Matchers.<Runnable>any(), anyLong(), anyLong(), Matchers.<TimeUnit>any());
    }

    private Container container() throws Exception {
        return new SimpleContainer().
                addInstance(ScheduledExecutorService.class, executorService).
                addInstance(Application.class, new RestApplication()).addInstance(DynamicProperties.class, load(someProperties()));
    }

    private Callable<Properties> someProperties() {
        return new Callable<Properties>() {

            public Properties call() throws Exception {
                return new Properties();
            }
        };
    }

    private SchedulableTask taskWith(String name) {
        return new SchedulableTask(name, RequestBuilder.post("notimportant").build(), propertyName("this should be delay????"));
    }
}
