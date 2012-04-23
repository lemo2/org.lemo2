package de.lemo.dms.service;

import java.text.SimpleDateFormat;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import de.lemo.dms.service.servicecontainer.SCTime;

/**
 * REST Webservice for the start time of the server
 * @author Boris Wenzlaff
 *
 */
@Path("/starttime")
public class ServiceStartTime extends BaseService{
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public SCTime startTimeJson() {
		super.logger.info("call for service: startTimeJson");
		SCTime rs = new SCTime();
		rs.setTime(super.config.getStartTime());
		return rs;
	}
	
	@GET @Produces(MediaType.TEXT_HTML)
	public String startTimeHtml() {
		super.logger.info("call for service: startTimeHtml");
		SimpleDateFormat sd = new SimpleDateFormat("dd.MM.yyyy 'at' HH:mm:ss z");
		String fdd = sd.format(super.config.getStartTime()).toString();
		return "<html><title>Start Time</title><body><h2>The server was started on "+ fdd + "</h2></body></html>";
	}
}
