package com.zaki.grunttp.server;

import com.zaki.grunttp.config.Configuration;
import com.zaki.grunttp.config.log.ServerLogger;
import com.zaki.grunttp.constant.ContentType;
import com.zaki.grunttp.constant.FileExtension;
import com.zaki.grunttp.memory.MemoryPool;
import com.zaki.grunttp.server.request.HttpServerRequestImpl;
import com.zaki.grunttp.server.request.ServerRequest;
import com.zaki.grunttp.server.request.ServerRequestImpl;
import com.zaki.grunttp.server.response.HttpServerResponseImpl;
import com.zaki.grunttp.server.response.ServerResponse;
import com.zaki.grunttp.server.response.ServerResponseImpl;
import com.zaki.grunttp.server.session.Session;
import com.zaki.grunttp.util.exceptions.runtime.ServerRuntimeException;
import com.zaki.grunttp.util.exceptions.runtime.response.BadRequestException;
import com.zaki.grunttp.util.exceptions.runtime.response.InternalServerErrorException;
import com.zaki.grunttp.util.exceptions.runtime.response.ServerResponseException;

import java.io.*;
import java.net.Socket;

public abstract class ClientImpl implements Client {

    private final Socket socket;
    private ServerRequestImpl request;
    private ServerResponseImpl response;
    private Session session;
    private ServerResponseException clientException;

    // streams
    private final OutputStream out;
    private final InputStream in;

    ClientImpl(final Socket client) throws IOException {
        this.socket = client;
        this.out = new BufferedOutputStream(client.getOutputStream());
        this.in = new BufferedInputStream(client.getInputStream());
        initClientData();
        try {
            readRequest();
        } catch (ServerResponseException e) {
            clientException = e;
        }
        setContext();
    }

    @Override
    public ServerRequest getRequest() {
        return this.request;
    }

    @Override
    public ServerResponse getResponse() {
        return this.response;
    }

    @Override
    public OutputStream getOutputStream() {
        return this.out;
    }

    @Override
    public void performAction() {

        if (clientException != null) {
            prepareServerResponseExceptionForRethrow(clientException);
            throw clientException;
        }
        try {
            handleRequest();
            sendResponse();
        } catch (ServerRuntimeException e) {
            throw new InternalServerErrorException(e.getCause());
        } catch (ServerResponseException e) {
            prepareServerResponseExceptionForRethrow(e);
            throw e;
        }
    }

    @Override
    public void closeConnection() {

        // revalidate session
        session.revalidate();
        // close connection quietly
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                ServerLogger.severe(e);
            }
        }
    }

    @Override
    public Throwable getClientException() {
        return this.clientException;
    }

    /**
     * Sets the current client context
     */
    private void setContext() {
        ThreadContext.initContext(request, response);
    }

    /**
     * Initializes client data such as session, request, response
     */
    private void initClientData() {

        // set client session
        setClientSession();

        // get http request
        response = new HttpServerResponseImpl();
        request = new HttpServerRequestImpl(session);
    }

    /**
     * Reads the request
     * @throws Exception catches every exception when reading the request
     */
    private void readRequest() throws IOException {

        request.readRequest(in);
    }

    /**
     * Sets the current client session
     */
    private void setClientSession() {

        String clientHost = socket.getInetAddress().getHostAddress(); // TODO encrypt client data as map key
        this.session = MemoryPool.getClientSession(clientHost);
    }

    /**
     * Sets the response content type by given response file
     * @param responseFile response file
     */
    void setResponseContentType(final File responseFile) {

        String fileName = responseFile.getName();
        FileExtension fileExt = FileExtension.getExtensionFromFilePath(fileName);
        ContentType respContentType = ContentType.getContentTypeFromFileExtension(fileExt);
        response.setContentType(respContentType.toString());
    }

    /**
     * Sets status code and content type for the ones from the response
     * @param e response exception
     */
    private void prepareServerResponseExceptionForRethrow(ServerResponseException e) {

        Configuration config = Configuration.getConfiguration();
        if (!config.hasErrorPage()) {
            response.setStatusCode(e.getStatusCode());
        }
        response.setContentType(ContentType.TEXT_HTML.toString());
    }
}
