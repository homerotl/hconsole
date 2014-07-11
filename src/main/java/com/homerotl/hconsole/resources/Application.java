package com.homerotl.hconsole.resources;

import java.util.HashSet;
import java.util.Set;

/**
 * Jersey configuration for resources
 */
public class Application extends javax.ws.rs.core.Application {
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(HConsole.class);
        return s;
    }
}
