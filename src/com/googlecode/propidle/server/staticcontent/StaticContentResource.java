package com.googlecode.propidle.server.staticcontent;

import javax.ws.rs.*;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@Path(StaticContentResource.NAME)
@Produces({"text/css", "text/javascript", "text/html", "application/octet-stream", "image/x-icon"})
public class StaticContentResource {
    public static final String NAME = "static";

    @GET
    @Path("{name}")
    public StreamingOutput get(@PathParam("name") final String filename) {
        return new StreamingOutput() {
            public void write(OutputStream out) throws IOException, WebApplicationException {
                InputStream in = StaticContentResource.class.getResourceAsStream(filename);
                try {
                    int length = in.available(); // danger!
                    byte[] bytes = new byte[length];
                    in.read(bytes);
                    out.write(bytes);
                } 
                finally {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        };
    }
}
