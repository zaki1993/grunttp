package com.zaki.grunttp.server.response;

import com.zaki.grunttp.constant.ContentType;
import com.zaki.grunttp.constant.StatusCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class ServerResponseImpl implements ServerResponse {

    private StatusCode statusCode;
    private String contentType;
    private String connection;
    private Date date;

    private Map<String, Object> headerProperties;

    public ServerResponseImpl() {
        this.headerProperties = new HashMap<>();
        this.statusCode = StatusCode.OK;
        this.contentType = ContentType.TEXT_HTML.toString();
        this.connection = "keep-alive";
        this.date = new Date();
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public StatusCode getStatusCode() {
        return statusCode;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getConnection() {
        return connection;
    }

    @Override
    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setConnection(String connection) {
        this.connection = connection;
    }

    @Override
    public Map<String, Object> getHeaders() {
        return this.headerProperties;
    }

    @Override
    public void addHeader(String property, Object value) {
        this.headerProperties.put(property, value);
    }

    @Override
    public Object getHeader(String property) {
        return this.headerProperties.get(property);
    }
}
