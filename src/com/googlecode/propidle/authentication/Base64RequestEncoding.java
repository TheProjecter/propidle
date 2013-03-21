package com.googlecode.propidle.authentication;

import com.googlecode.propidle.commonscodec.Base64;
import com.googlecode.totallylazy.Strings;
import com.googlecode.utterlyidle.Request;

import java.io.IOException;

import static com.googlecode.utterlyidle.HttpMessageParser.parseRequest;
import static com.googlecode.propidle.commonscodec.Base64.decodeBase64;
import static com.googlecode.propidle.commonscodec.Base64.encodeBase64String;

public class Base64RequestEncoding {


    public String encode(Request request){
        return Base64.encodeBase64String(request.toString().getBytes());
    }

    public Request decode(String encodedRequest) throws IOException {
        return parseRequest(Strings.toString(Base64.decodeBase64(encodedRequest)));
    }
}
