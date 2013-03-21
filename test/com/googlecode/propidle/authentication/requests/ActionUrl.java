package com.googlecode.propidle.authentication.requests;

import com.googlecode.totallylazy.Uri;
import com.googlecode.propidle.util.tinytype.StringTinyType;

public class ActionUrl extends StringTinyType<ActionUrl>{
    private ActionUrl(String value) {
        super(value);
    }

    public static ActionUrl actionUrl(String value) {
        return new ActionUrl(value);
    }

    public Uri uri() {
        return Uri.uri(value());
    }
}
