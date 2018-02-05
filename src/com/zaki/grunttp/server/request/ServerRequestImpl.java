package com.zaki.grunttp.server.request;

import com.zaki.grunttp.constant.RequestMethod;
import com.zaki.grunttp.server.session.Session;

import java.util.Map;

public abstract class ServerRequestImpl implements ServerRequest {

    private RequestHeader requestHeader;
    private RequestBody requestBody;
    private Session session;

    public ServerRequestImpl(Session session) {
        this.session = session;
    }

    @Override
    public Map<String, String> getAttributes() {
        return this.requestBody.getAttributes();
    }

    @Override
    public Session getSession() {
        return this.session;
    }

    @Override
    public RequestMethod getMethodType() {
        return this.requestHeader.getMethod();
    }

    @Override
    public String getURL() {
        return this.requestHeader.getURL();
    }

    @Override
    public String getVersion() {
        return this.requestHeader.getVersion();
    }

    @Override
    public String getRequestProperty(String name) {
        return this.requestHeader.getProperty(name);
    }

    @Override
    public String getAttribute(String name) {
        return this.getAttributes().get(name);
    }

    /**
     * Creates request header and returns it
     * @return request header
     */
    protected RequestHeader createHeader() {
        this.requestHeader = new RequestHeader();
        return this.requestHeader;
    }

    /**
     * Creates request body and returns it
     * @return request body
     */
    protected RequestBody createBody() {
        this.requestBody = new RequestBody();
        return this.requestBody;
    }
}
