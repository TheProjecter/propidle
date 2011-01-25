package com.googlecode.propidle;

import java.util.Properties;

public interface AllProperties {
    Properties get(PropertiesPath path);
    AllProperties put(PropertiesPath path, java.util.Properties properties);
}
