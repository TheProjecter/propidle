package com.googlecode.propidle.search;

import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.propidle.properties.PropertiesPath;
import com.googlecode.propidle.util.NullArgumentException;
import com.googlecode.utterlyidle.io.Url;

import static java.lang.String.format;

public class SearchResult {
    private final PropertiesPath path;
    private final PropertyValue propertyValue;
    private final PropertyName propertyName;

    public static SearchResult searchResult(PropertiesPath path, PropertyName propertyName, PropertyValue propertyValue) {
        return new SearchResult(path, propertyName, propertyValue);
    }

    protected SearchResult(PropertiesPath path, PropertyName propertyName, PropertyValue propertyValue) {
        if (path == null) throw new NullArgumentException("url");
        if (propertyName == null) throw new NullArgumentException("propertyName");
        if (propertyValue == null) throw new NullArgumentException("propertyValue");

        this.propertyValue = propertyValue;
        this.propertyName = propertyName;
        this.path = path;
    }

    public PropertyValue propertyValue() {
        return propertyValue;
    }

    public PropertyName propertyName() {
        return propertyName;
    }

    public PropertiesPath path() {
        return path;
    }

    @Override
    public String toString() {
        return format("%s.%s=%s", path, propertyName, propertyValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;

        if (!propertyName.equals(that.propertyName)) return false;
        if (!propertyValue.equals(that.propertyValue)) return false;
        if (!path.equals(that.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = propertyValue.hashCode();
        result = 31 * result + propertyName.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }
}
