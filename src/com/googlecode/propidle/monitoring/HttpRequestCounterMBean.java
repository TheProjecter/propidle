package com.googlecode.propidle.monitoring;

import java.util.concurrent.atomic.AtomicLong;

public interface HttpRequestCounterMBean {

   public AtomicLong getCount();
}
