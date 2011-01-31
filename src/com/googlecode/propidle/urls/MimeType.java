package com.googlecode.propidle.urls;

import com.googlecode.propidle.util.StringTinyType;

import javax.ws.rs.core.MediaType;

public class MimeType extends StringTinyType<MimeType>{
    public static MimeType TEXT_PLAIN = mimeType(MediaType.TEXT_PLAIN);
    public static MimeType TEXT_HTML = mimeType(MediaType.TEXT_HTML);

    public static MimeType mimeType(String value) {
        return new MimeType(value);
    }
    protected MimeType(String value) {
        super(value);
    }
}
