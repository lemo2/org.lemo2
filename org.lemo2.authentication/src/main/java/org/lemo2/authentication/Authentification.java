package org.lemo2.authentication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Set;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;

import org.glassfish.jersey.internal.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Priority(Priorities.AUTHENTICATION)
@PreMatching
public class Authentification implements ContainerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(Authentification.class);


	public Authentification() {
		logger.info("Initializing Custom Authorization Filter...");
	}


	@Override
	public void filter(ContainerRequestContext containerRequestContext) throws IOException {

		final String role = "tester";
		final int accessLevel = 5;

		logger.info("Requested URI: " + containerRequestContext.getUriInfo().getPath().toString());


		MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
		Set<String> keys = headers.keySet();
		boolean authorized = false;
		String[] userAndPassword = null;
		for(String key : keys){
			if(key.equals("Authorization")){
				List<String> credentials = headers.get(key);
				if (credentials != null && credentials.get(0).startsWith("Basic")) {
					userAndPassword = getCredentials(credentials.get(0));
				}
				logger.info("User name: " + userAndPassword[0] + " Password: " + userAndPassword[1]);
				authorized = true;
			}
		}
		if(authorized){
			final String basicAuthentication = getBasicAuthentication(userAndPassword[0], "test");
			headers.add("Authorization", basicAuthentication);        	
		}else{  		
			containerRequestContext.abortWith(Response.serverError()
					.status(401)
					.header("WWW-Authenticate", "Basic realm=\"Login\"")
					.build());        	
		}     
		logger.info("Setting customSecurityContext");
		containerRequestContext.setSecurityContext(new LemoSecurityContext(userAndPassword[0], role, accessLevel));
	}

	private String[] getCredentials(String authorization){
		String[] userAndPassword = new String[2];
		String base64Credentials = authorization.substring("Basic".length()).trim();
		String credentials = new String(Base64.decodeAsString(base64Credentials));
		// credentials = username:password
		userAndPassword = credentials.split(":",2);
		return userAndPassword;
	}
	
	private String getBasicAuthentication(String user, String password) {
		String token = user + ":" + password;
		try {
			return "BASIC " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException("Cannot encode with UTF-8", ex);
		}
	}
}
