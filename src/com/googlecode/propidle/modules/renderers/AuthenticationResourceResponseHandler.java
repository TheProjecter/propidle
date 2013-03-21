package com.googlecode.propidle.modules.renderers;

import com.googlecode.propidle.authentication.AuthenticationResource;
import com.googlecode.propidle.authentication.MemoryRequestRenderer;
import com.googlecode.utterlyidle.MemoryRequest;
import com.googlecode.utterlyidle.Response;
import com.googlecode.utterlyidle.ResponseHandler;

import static com.googlecode.utterlyidle.handlers.RenderingResponseHandler.renderer;


public class AuthenticationResourceResponseHandler implements ResponseHandler {
    private ModelTemplateRenderer modelTemplateRenderer;

   public AuthenticationResourceResponseHandler(MemoryRequestRenderer memoryRequestRenderer){
        modelTemplateRenderer = new ModelTemplateRenderer("AuthenticationResource_html", AuthenticationResource.class).withRenderer(MemoryRequest.class, memoryRequestRenderer);
    }

    public Response handle(Response response) throws Exception {
        return renderer(modelTemplateRenderer).handle(response);
    }
}
