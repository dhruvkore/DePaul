package hw4;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/Payment")
public class paymentProcessor {
	@POST
	@Path("/pay")
	public Response pay(
			@FormParam("card") String card,
			@FormParam("FirstName") String firstName,
			@FormParam("LastName") String lastName
			) {
		
		if(card.compareTo("") > 0 &&
				firstName.compareTo("") > 0 &&
				lastName.compareTo("") > 0) {
			return Response.status(200).entity(
					"Paid the Bill!"
					).build();
		}
		
		return Response.status(500).entity(
				"Something went wrong!"
				).build();
	}
}
