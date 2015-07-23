package org.lemo2.authentication;

import java.security.Principal;
import javax.ws.rs.core.SecurityContext;

public class LemoSecurityContext implements SecurityContext{
	
	String userName;
	String role;
	int accessLevel;
	
	LemoSecurityContext(String userName, String role, int accessLevel){
		this.userName = userName;
		this.role = role;
		this.accessLevel = accessLevel;
	}
	
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
}
