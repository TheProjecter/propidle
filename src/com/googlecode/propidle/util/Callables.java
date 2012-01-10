package com.googlecode.propidle.util;

import com.googlecode.propidle.urls.MimeType;
import com.googlecode.propidle.urls.UriGetter;
import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Either;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Uri;

import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;
import static com.googlecode.totallylazy.Uri.uri;
import static com.googlecode.utterlyidle.io.Converter.asString;

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

    public static Callable1<? super Uri, String> urlGet(final UriGetter uriGetter) {
        return new Callable1<Uri, String>() {
            public String call(Uri url) throws Exception {
                return asString(uriGetter.get(url.toURI(), MimeType.TEXT_PLAIN));
            }
        };
    }

    public static Callable1<? super String, Uri> toUrl() {
        return new Callable1<String, Uri>() {
            public Uri call(String path) throws Exception {
                return uri(path);
            }
        };
    }

    public static <T> Callable2<T, Callable1<T, T>, T> chain(Class<T> aClass) {
        return chain();
    }
    public static <T> Callable2<T, Callable1<T, T>, T> chain() {
        return new Callable2<T, Callable1<T, T>, T>() {
            public T call(T t, Callable1<T, T> callable) throws Exception {
                return callable.call(t);
            }
        };
    }
}
