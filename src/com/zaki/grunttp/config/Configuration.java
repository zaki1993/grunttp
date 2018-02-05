package com.zaki.grunttp.config;

import com.zaki.grunttp.config.log.ServerLogger;
import com.zaki.grunttp.constant.Global;
import com.zaki.grunttp.handler.Bean;
import com.zaki.grunttp.handler.RequestUrlHandler;
import com.zaki.grunttp.server.Server;
import com.zaki.grunttp.util.exceptions.base.ServerBaseException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Configuration {

    private static final String WEB_CONTENT = Global.USER_DIR + File.separator + "WebContent";
    private static Configuration reference;
    private Map<String, RequestUrlHandler> handlers;
    private String RESPONSE_DIR;
    private Bean grunttpBean;

    private Configuration() throws ServerBaseException {

        initProperties();
        readAnnotation();
        if (hasErrorPage()) {
            ServerLogger.info("Error page is: " + getErrorPage());
        }
        ServerLogger.info("Response dir is: " + getResponseFileDir());
    }

    private void initProperties() {
        this.handlers = new HashMap<>();
        RESPONSE_DIR = WEB_CONTENT + File.separator + "WEB-INF";
    }

    /**
     * This method does not check if the configuration is initialized
     * You must call init method to set properly the configuration
     * @return Configuration reference
     */
    public static Configuration getConfiguration() {

        return Configuration.reference;
    }

    /**
     * Initializes the server configuration
     */
    public static synchronized void init() throws ServerBaseException {

        if (Configuration.reference == null) {
            ServerLogger.info("Loading configuration..!");
            Configuration.reference = new Configuration();
        }
    }

    /**
     * Reads all annotated handlers server and client ones
     * @throws ServerBaseException if the handlers are not valid
     */
    private void readAnnotation() throws ServerBaseException {

        ServerLogger.info("Loading handlers..!");
        AnnotationReader ar = new AnnotationReader();
        this.handlers = ar.readHandlers();
        ServerLogger.info("Loading beans..!");
        this.grunttpBean = ar.readGrunttpBean();
        RESPONSE_DIR += File.separator + grunttpBean.getStaticLocation();
        ServerLogger.info("Static dir found, files will be searched in the following directory: " + RESPONSE_DIR);
    }

    /**
     * @return map of all read handlers
     */
    public Map<String, RequestUrlHandler> getHandlers() {
        return this.handlers;
    }

    /**
     * @return if error page is present in the configuration
     */
    public boolean hasErrorPage() {
        return grunttpBean.hasErrorPage();
    }

    /**
     * @return the error page
     */
    public String getErrorPage() {
        return getResponseFileDir() + File.separator + grunttpBean.getErrorPage();
    }

    /**
     * @return the response file directory
     */
    public String getResponseFileDir() {
        return RESPONSE_DIR;
    }
}
