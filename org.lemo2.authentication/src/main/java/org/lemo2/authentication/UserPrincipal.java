package org.lemo2.authentication;

import java.security.Principal;

public class UserPrincipal implements Principal {
    private final String name;
    private final String role;
    private final int accessLevel;

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public int getAccessLevel() { return accessLevel; }

    public UserPrincipal(String name, String role, int accessLevel) {
        this.name = name;
        this.role = role;
        this.accessLevel = accessLevel;
    }
}
