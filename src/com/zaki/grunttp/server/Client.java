package com.zaki.grunttp.server;

import com.zaki.grunttp.server.request.ServerRequest;
import com.zaki.grunttp.server.response.ServerResponse;

import java.io.OutputStream;

public interface Client {

    /**
     * Handles the request
     */
    void handleRequest();

    /**
     * Sends the response
     */
    void sendResponse();

    /**
     * Close client connection
     */
    void closeConnection();

    /**
     * Performs main action
     */
    void performAction();

    /**
     * @return the request
     */
    ServerRequest getRequest();

    /**
     * @return the response
     */
    ServerResponse getResponse();

    /**
     * @return the output stream
     */
    OutputStream getOutputStream();

    /**
     * @return client exception during reading the request stream
     */
    Throwable getClientException();
}
