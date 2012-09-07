package com.googlecode.propidle.urls;

import com.googlecode.propidle.util.tinytype.StringTinyType;


public class MimeType extends StringTinyType<MimeType> {
    public static MimeType TEXT_PLAIN = mimeType("text/plain");
    public static MimeType TEXT_HTML = mimeType("text/html");

    public static MimeType mimeType(String value) {
        return new MimeType(value);
    }
    protected MimeType(String value) {
        super(value);
    }
}
