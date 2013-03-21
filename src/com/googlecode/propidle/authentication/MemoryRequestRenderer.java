package com.googlecode.propidle.authentication;

import com.googlecode.utterlyidle.MemoryRequest;
import com.googlecode.utterlyidle.Renderer;

public class MemoryRequestRenderer implements Renderer<MemoryRequest> {
    private final Base64RequestEncoding base64RequestEncoding;


    public MemoryRequestRenderer(Base64RequestEncoding base64RequestEncoding){
        this.base64RequestEncoding = base64RequestEncoding;
    }

    public String render(MemoryRequest request) throws Exception {
        return base64RequestEncoding.encode(request);
    }
}
