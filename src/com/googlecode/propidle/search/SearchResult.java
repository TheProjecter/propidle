package com.googlecode.propidle.search;

import com.googlecode.propidle.properties.PropertyName;
import com.googlecode.propidle.properties.PropertyValue;
import com.googlecode.utterlyidle.io.Url;

public class SearchResult {
    private final PropertyValue propertyValue;
    private final PropertyName propertyName;
    private final Url url;

    public static SearchResult searchResult(Url url, PropertyName propertyName, PropertyValue propertyValue) {
        return new SearchResult(url, propertyName, propertyValue);
    }

    protected SearchResult(Url url, PropertyName propertyName, PropertyValue propertyValue) {
        this.propertyValue = propertyValue;
        this.propertyName = propertyName;
        this.url = url;
    }

    public PropertyValue propertyValue() {
        return propertyValue;
    }

    public PropertyName propertyName() {
        return propertyName;
    }

    public Url url() {
        return url;
    }
}
