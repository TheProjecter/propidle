package com.googlecode.propidle.scheduling;

import com.googlecode.propidle.client.DynamicProperties;
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
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class SchedulerTest {

    private static final String NAME = "whatever";
    private final ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
    private final ScheduledFuture scheduledFuture = mock(ScheduledFuture.class);
    private final long delay = 1;
    private final long initialDelay = 2;
    private Scheduler scheduler;

    @Before
    public void createScheduler() throws Exception {
        scheduler = new Scheduler(container(), executorService);
    }

    @Test
    public void shouldScheduleTaskIfNotScheduledYet() throws Exception {
        scheduler.schedule(taskWith(NAME), initialDelay, delay, SECONDS);
        verify(executorService).scheduleWithFixedDelay(any(Runnable.class), eq(initialDelay), eq(delay), eq(SECONDS));
    }

    @Test
    public void shouldCancelTaskIfAlreadyScheduled() throws Exception {
        whenTaskIsAlreadyScheduledWith(delay);

        long newDelay = 3;
        scheduler.schedule(taskWith(NAME), initialDelay, newDelay, SECONDS);
        verify(scheduledFuture).cancel(true);
        verify(executorService).scheduleWithFixedDelay(any(Runnable.class), eq(initialDelay), eq(newDelay), eq(SECONDS));
    }

    private void whenTaskIsAlreadyScheduledWith(final long delay) {
        when(executorService.scheduleWithFixedDelay(Matchers.<Runnable>any(), anyLong(), anyLong(), Matchers.<TimeUnit>any())).thenReturn(scheduledFuture);
        scheduler.schedule(taskWith(NAME), initialDelay, delay, SECONDS);
    }

    private Container container() throws Exception {
        return new SimpleContainer().
                addInstance(ScheduledExecutorService.class, executorService).
                addInstance(Application.class, new RestApplication()).
                addInstance(DynamicProperties.class, load(someProperties()));
    }

    private Callable<Properties> someProperties() {
        return new Callable<Properties>() {

            public Properties call() throws Exception {
                return new Properties();
            }
        };
    }

    private SchedulableTask taskWith(String name) {
        return new SchedulableTask(name, RequestBuilder.post("notimportant").build());
    }
}
