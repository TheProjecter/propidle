package com.googlecode.propidle.scheduling;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.googlecode.propidle.properties.PropertyName.propertyName;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

public class SchedulerTest {

    private final ScheduledExecutorService executorService = mock(ScheduledExecutorService.class);
    private final Runnable someTask = mock(Runnable.class);
    private final ScheduledFuture scheduledFuture = mock(ScheduledFuture.class);
    private final long delay = 1;
    private final long initialDelay = 2;
    private Scheduler scheduler;

    @Before
    public void createScheduler() throws Exception {
        scheduler = new Scheduler(executorService);
    }

    @Test
    public void shouldScheduleTaskIfNotScheduledYet() throws Exception {
        scheduler.schedule(someTask, initialDelay, delay, SECONDS);
        verify(executorService).scheduleWithFixedDelay(same(someTask), eq(initialDelay), eq(delay), eq(SECONDS));
    }

    @Test
    public void shouldCancelTaskIfAlreadyScheduled() throws Exception {
        whenTaskIsAlreadyScheduledWith(delay);

        long newDelay = 3;
        scheduler.schedule(someTask, initialDelay, newDelay, SECONDS);
        verify(scheduledFuture).cancel(true);
        verify(executorService).scheduleWithFixedDelay(same(someTask), eq(initialDelay), eq(newDelay), eq(SECONDS));
    }

    private void whenTaskIsAlreadyScheduledWith(final long delay) {
        when(executorService.scheduleWithFixedDelay(Matchers.<Runnable>any(), anyLong(), anyLong(), Matchers.<TimeUnit>any())).thenReturn(scheduledFuture);
        scheduler.schedule(someTask, initialDelay, delay, SECONDS);
    }

}
