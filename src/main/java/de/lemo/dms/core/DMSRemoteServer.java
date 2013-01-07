package de.lemo.dms.core;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;

import de.lemo.dms.core.config.ServerConfiguration;

/**
 * Server instance which can be run as a java application.
 * 
 * @author Boris Wenzlaff
 * @author Leonard Kappe
 */
public enum DMSRemoteServer {

    INSTANCE;

    private int port = 8081;
    private String host = "localhost";
    private Logger logger = Logger.getLogger(getClass());
    private HttpServer server;

    /**
     * Stop it.
     */
    private void stop() {
        if(server != null) {
            server.stop();
            logger.info("remote server stopped...");
            server = null;
        }
    }

    /**
     * Stop and start the server again.
     * 
     * @throws IllegalArgumentException
     * @throws IOException
     */
    protected void restart() throws IOException {
        this.stop();
        this.start();
    }

    /**
     * Start and initialize the server if it isn't already running.
     * 
     * @throws IllegalArgumentException
     * @throws IOException
     */
    protected void start() throws IOException {
        if(server == null) {
            server = GrizzlyServerFactory.createHttpServer("http://" + host + ":" + port, new DMSResourceConfig());
            server.start();
            logger.info("DMS started on " + host + ":" + port);
        }
        else {
            logger.info("DMS already running on " + host + ":" + port);
        }
    }
}
