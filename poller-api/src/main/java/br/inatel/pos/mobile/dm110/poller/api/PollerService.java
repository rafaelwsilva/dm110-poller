package br.inatel.pos.mobile.dm110.poller.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.inatel.pos.mobile.dm110.poller.to.IPTO;

@Path("/poller")
public interface PollerService {

	@GET
	@Path("/start/{IP}/{Mask}")
	void startPoller(@PathParam("IP") String ip, @PathParam("mask") String mask);
	
	@GET
	@Path("/status/{IP}")
	@Produces(MediaType.APPLICATION_JSON)
	IPTO checkStatus(@PathParam("IP") String ip);

}
