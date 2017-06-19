package services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;

@Path("/")
public class RESTServices {
	
	@GET
	@Path("/pathParam/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response testPathParamRESTService(@PathParam("id") String id) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("path_param_id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(jsonObject.toString()).build();
	}

	@GET
	@Path("/queryParam")
	@Produces(MediaType.APPLICATION_JSON)
	public Response testQueryParamRESTService(@QueryParam("id") String id) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("query_param_id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(jsonObject.toString()).build();
	}
}
