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
@Path("pay")
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
        return "Payment Service is alive!";
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String pay(
        @FormParam("card") String card,
        @FormParam("firstName") String firstName,
        @FormParam("lastName") String lastName
        ){
            if(
                card.compareTo("") > 0 &&
                firstName.compareTo("") > 0 &&
                lastName.compareTo("") > 0)
            {
                return "Payment Processed!";
            }
            else{
                return "Error Processing Payment";
            }
    }
}
