package com.googlecode.propidle.util;

import com.googlecode.propidle.urls.MimeType;
import com.googlecode.propidle.urls.UriGetter;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Either;
import com.googlecode.utterlyidle.io.Url;

import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.utterlyidle.io.Converter.asString;
import static com.googlecode.utterlyidle.io.Url.url;

public class Callables {
    public static <Input, Output> Callable1<? super Input, Either<Output, Exception>> eitherExceptionOr(final Callable1<? super Input, Output> callable) {
        return new Callable1<Input, Either<Output, Exception>>() {
            public Either<Output, Exception> call(Input input) throws Exception {
                try {
                    return left(callable.call(input));
                } catch (Exception e) {
                    return right(e);
                }
            }
        };
    }

    public static Callable1<? super Url, String> urlGet(final UriGetter uriGetter) {
        return new Callable1<Url, String>() {
            public String call(Url url) throws Exception {
                return asString(uriGetter.get(url.toURI(), MimeType.TEXT_PLAIN));
            }
        };
    }

    public static Callable1<? super String, Url> toUrl() {
        return new Callable1<String, Url>() {
            public Url call(String path) throws Exception {
                return url(path);
            }
        };
    }
}
