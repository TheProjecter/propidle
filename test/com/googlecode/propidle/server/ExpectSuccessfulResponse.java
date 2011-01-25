package com.googlecode.propidle.server;

import com.googlecode.utterlyidle.MemoryResponse;

import java.io.IOException;

public class ExpectSuccessfulResponse extends MemoryResponse {
    public static ExpectSuccessfulResponse expectSuccessfulResponse() {
        return new ExpectSuccessfulResponse();
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (status().code() >= 400) {
            throw new RuntimeException(String.format("Unexpected http error:\n%s%s", this, output().toString()));
        }
    }

}
