package com.googlecode.propidle.versioncontrol.changes;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChangeDetails {
    private Map<String, String> changeDetails;

    public ChangeDetails() {
        this(new ConcurrentHashMap<String, String>());
        changeDetails.put("date", new Date().toString());
    }

    public ChangeDetails(Map<String, String> changeDetails) {
        this.changeDetails = changeDetails;
    }

    public ChangeDetails addDetail(String name, String value) {
        this.changeDetails.put(name, value);
        return this;
    }

    public Map<String, String> value() {
        return changeDetails;
    }


}
