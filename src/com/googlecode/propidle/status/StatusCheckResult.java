package com.googlecode.propidle.status;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class StatusCheckResult {
    private final StatusCheckName name;
    private final Map<String, Object> properties = new LinkedHashMap<String,Object>();

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
}
