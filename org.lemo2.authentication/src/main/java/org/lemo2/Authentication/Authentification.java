package org.lemo2.Authentication;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

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

		if(containerRequestContext.getUriInfo().getPath().equals("/user/badinfo")) {
			containerRequestContext.abortWith(
					Response.status(Response.Status.UNAUTHORIZED)
					.entity("User is not authorized!")
					.build());
		}

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
}
