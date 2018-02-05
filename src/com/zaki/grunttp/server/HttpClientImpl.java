package com.zaki.grunttp.server;

import com.zaki.grunttp.config.Configuration;
import com.zaki.grunttp.constant.Global;
import com.zaki.grunttp.handler.RequestUrlHandler;
import com.zaki.grunttp.server.request.ServerRequest;
import com.zaki.grunttp.server.response.ResponseParser;
import com.zaki.grunttp.server.response.ServerResponse;
import com.zaki.grunttp.util.exceptions.runtime.HandlerMethodNotFoundException;
import com.zaki.grunttp.util.exceptions.runtime.response.CallHandlerException;
import com.zaki.grunttp.util.exceptions.runtime.response.RequestEntityTooLarge;
import com.zaki.grunttp.util.exceptions.runtime.response.ServerResponseException;
import com.zaki.grunttp.util.exceptions.runtime.response.UrlNotHandledException;
import com.zaki.grunttp.writer.Writer;
import com.zaki.grunttp.writer.WriterImpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.Map;

import static com.zaki.grunttp.constant.Global.MAX_FILE_TRANSFER_SIZE;

public final class HttpClientImpl extends ClientImpl {

    private ServerRequest request;
    private ServerResponse response;
    private String respPage;

    HttpClientImpl(Socket client) throws IOException {
        super(client);
        this.request = getRequest();
        this.response = getResponse();
    }

    @Override
    public void handleRequest() {

        Configuration config = Configuration.getConfiguration();
        String url = request.getURL();
        String action;
        do {
            if (url != null && config.getHandlers().containsKey(url)) {
                RequestUrlHandler urlHandler = config.getHandlers().get(url);
                String result = callHandler(urlHandler);
                ResponseParser validator = new ResponseParser(response, result);
                Map.Entry<String, String> parsed = validator.parseResponse();
                respPage = parsed.getValue();
                action = parsed.getKey();
                url = respPage;
            } else {
                respPage = url;
                break;
            }
        } while (action.equals(Global.FORWARD)); // forward until action = REDIRECT

        if (respPage != null) {
            respPage = respPage.trim();
        }
    }

    @Override
    public void sendResponse() {

        if (response.getHeader(Global.HEADER_LOCATION) != null) {
            sendRedirect();
            return;
        }
        if (respPage == null || respPage.trim().isEmpty()) {
            throw new UrlNotHandledException();
        }
        Configuration config = Configuration.getConfiguration();
        String respFileDir = config.getResponseFileDir();
        String respFileName = new StringBuilder().append(respFileDir)
                                                 .append(File.separator)
                                                 .append(respPage).toString();
        File respFile = Paths.get(respFileName).toFile();
        if (!respFile.exists() || respFile.isDirectory()) {
            throw new UrlNotHandledException();
        } else {
            sendForwardFile(respFile);
        }
    }

    /**
     * Calls the request handler and invokes the request method if the client provided such
     * @param urlHandler the url handler
     * @return response file name or url
     */
    private String callHandler(RequestUrlHandler urlHandler) {

        Object instance = urlHandler.getInstance();
        Method method = urlHandler.getMethod(request.getMethodType());
        String result;
        if (method == null) {
            throw new HandlerMethodNotFoundException("Handler method in " + urlHandler.getClassName() + " for type " + request.getMethodType().toString() + " not found");
        }
        try {
            result = (String) method.invoke(instance, request, response);
        } catch (Exception e) {
            if (e.getCause() instanceof ServerResponseException) {
                throw (ServerResponseException) e.getCause();
            }
            throw new CallHandlerException(e.getCause());
        }
        return result;
    }

    /**
     * Forwards the request
     * @param respFile file to be forwarded to
     */
    private void sendForwardFile(File respFile) {

        if (respFile.length() > MAX_FILE_TRANSFER_SIZE) {
            throw new RequestEntityTooLarge();
        }
        // parse whatever the response file is
        setResponseContentType(respFile);
        Writer writer = new WriterImpl();
        writer.writeBytes(getOutputStream(), response, respFile);
    }

    /**
     * Sends redirect to given location
     */
    private void sendRedirect() {
        
        Writer writer = new WriterImpl();
        writer.writeHtml(getOutputStream(), response, "");
    }
}
