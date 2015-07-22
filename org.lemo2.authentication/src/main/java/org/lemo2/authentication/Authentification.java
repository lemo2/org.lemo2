package org.lemo2.authentication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;

import org.apache.felix.ipojo.annotations.Component;
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

		final String userName = "testuser";
		final String role = "tester";
		final int accessLevel = 5;

		logger.info("Requested URI: " + containerRequestContext.getUriInfo().getPath().toString());
		if(containerRequestContext.getUriInfo().getPath().equals("tools/activitytime/")) {
			containerRequestContext.abortWith(
					Response.status(Response.Status.UNAUTHORIZED)
					.entity("User is not authorized!")
					.build());
		}
		
        MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
        final String basicAuthentication = getBasicAuthentication(userName, "test");
        headers.add("Authorization", basicAuthentication);
        
        
		logger.info("Setting customSecurityContext");
		containerRequestContext.setSecurityContext(new SecurityContext() {
			@Override
			public Principal getUserPrincipal() {
				return new UserPrincipal(userName, role, accessLevel);
			}

			@Override
			public boolean isUserInRole(String role) {
				return role.equals(role);
			}

			@Override
			public boolean isSecure() {
				return false;
			}

			@Override
			public String getAuthenticationScheme() {
				return "custom";
			}
		});
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
