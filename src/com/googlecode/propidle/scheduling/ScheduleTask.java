package com.googlecode.propidle.scheduling;

public interface ScheduleTask {
    void schedule(String taskName, long initialDelay, long delay) throws Exception;

    void schedule(String taskName, long delay) throws Exception;
}
