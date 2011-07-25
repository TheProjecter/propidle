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
import java.util.concurrent.TimeUnit;

import static com.googlecode.propidle.client.DynamicProperties.*;
import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SchedulerTest {

    private static final String NAME = "whatever";
    ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
    private Scheduler scheduler;

    @Before
    public void createScheduler() throws Exception {
        scheduler = new Scheduler(container());
    }

    @Test
    public void shouldScheduleTaskIfNotScheduledYet() throws Exception {
        assertThat(scheduler.schedule(taskWith(NAME)), is(true));
        verify(executorService).scheduleWithFixedDelay(Matchers.<Runnable>any(), anyLong(), anyLong(), Matchers.<TimeUnit>any());
    }

    @Test
    public void shouldNotScheduleTaskIfAlreadyScheduled() throws Exception {
        scheduler.schedule(taskWith(NAME));

        assertThat(scheduler.schedule(taskWith(NAME)), is(false));
        verify(executorService, times(1)).scheduleWithFixedDelay(Matchers.<Runnable>any(), anyLong(), anyLong(), Matchers.<TimeUnit>any());
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
