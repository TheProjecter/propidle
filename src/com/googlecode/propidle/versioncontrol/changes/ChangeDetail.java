package com.googlecode.propidle.versioncontrol.changes;

public class ChangeDetail {
    private final String name;
    private final String value;

    public ChangeDetail(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ChangeDetail && ((ChangeDetail) o).getName().equals(name) && ((ChangeDetail) o).getValue().equals(value);
    }

    @Override
    public int hashCode() {
        return  31 * name.hashCode() + value.hashCode();
    }
}
