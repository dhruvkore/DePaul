package edu.se457;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.FormParam;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("login")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "User Service is up!";
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String login(
        @FormParam("user") String user,
        @FormParam("password") String password
        ){
            if(
                user.compareTo("admin") == 0 &&
                password.compareTo("test1234") == 0)
            {
                return "User Authenticated!";
            }
            else{
                return "Invalid User or Password!";
            }
    }
}
