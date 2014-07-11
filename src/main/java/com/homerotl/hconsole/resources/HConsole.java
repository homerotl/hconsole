package com.homerotl.hconsole.resources;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.UUID;

/**
 * HConsole implementation with Jersey
 */
@Path("/")
public class HConsole {

    @POST
    @Path("/session")
    @Produces(MediaType.APPLICATION_JSON)
    public String getNewSession() {
        JsonObject jsonResponse =
                Json.createObjectBuilder()
                        .add("sessionId", UUID.randomUUID().toString())
                        .add("prompt","anonymous@homerotl[~]$")
                        .add("message","Last login: Tue May 27 15:13:23 on ttys002")
                        .build();
        return jsonResponse.toString();
    }

    @PUT
    @Path("/session/{sessionId}/command")
    @Consumes(MediaType.APPLICATION_JSON)
    public String sendCommand(@PathParam("sessionId") String sessionId, String body) {

        JsonFactory f = new JsonFactory();

        String command = null;

        try {

            JsonParser jp = f.createJsonParser(body);

            jp.nextToken();
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jp.getCurrentName();
                jp.nextToken();
                if ("command".equals(fieldName)) { // contains an object
                    command = jp.getText();
                }
            }
            jp.close(); // ensure resources get cleaned up timely and properly
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonObjectBuilder builder = null;

        if (command!=null) {
            boolean clearScreen = false;
            if (command.trim().length()>0) {
                if (command.trim().equals("ls -al")) {
                    builder = Json.createObjectBuilder()
                            .add("response", "total 4\r\ndrwxr-xr-x  3 anonymous admin 102 Jun  3 16:54 .\r\n"+
                                    "drwxr-xr-x 24 anonymous admin 816 Jun  3 16:53 ..\r\n"+
                                    "-rw-r--r--  1 anonymous admin   3 Jun  3 16:54 afile.txt");
                } else if (command.trim().equals("clear")) {
                    clearScreen = true;
                } else {
                    builder = Json.createObjectBuilder()
                            .add("response", "sh: "+command.trim()+" command not found");
                }
            }

            builder = builder.add("clearScreen",clearScreen).add("prompt", "anonymous@homerotl[~]$");

            JsonObject jsonResponse = builder.build();

            return jsonResponse.toString();

        } else {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

    }

    @DELETE
    @Path("/session/{sessionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteSession(@PathParam("sessionId") String sessionId) {
        JsonObject jsonResponse =
                Json.createObjectBuilder()
                        .add("result", "success")
                        .build();
        return jsonResponse.toString();
    }

}
