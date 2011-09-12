package com.googlecode.propidle.status;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class StatusCheckResult {
    private final StatusCheckName name;
    private final Map<String, Object> properties = new LinkedHashMap<String,Object>();
    private boolean fatal = false;

    public static StatusCheckResult statusCheckResult(StatusCheckName name) {
        return new StatusCheckResult(name);
    }

    protected StatusCheckResult(StatusCheckName name) {
        this.name = name;
    }

    public StatusCheckName getName() {
        return name;
    }

    public StatusCheckResult add(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    public Set<Map.Entry<String, Object>> getProperties() {
        return properties.entrySet();
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public boolean isFatal() {
        return fatal;
    }

    public void setFatal(boolean fatal) {
        this.fatal = fatal;
    }
}
