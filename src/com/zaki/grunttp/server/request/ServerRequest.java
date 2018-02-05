package com.zaki.grunttp.server.request;

import com.zaki.grunttp.constant.RequestMethod;
import com.zaki.grunttp.server.session.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface ServerRequest {

    /**
     * Reads the request
     * @param input input that contains raw bytes
     * @throws IOException
     */
    void readRequest(InputStream input) throws IOException;

    /**
     * @return request attributes
     */
    Map<String, String> getAttributes();

    /**
     * @return client session
     */
    Session getSession();

    /**
     * @return body bytes
     */
    byte[] getRequestBodyBytes();

    /**
     * @return request method type
     */
    RequestMethod getMethodType();

    /**
     * @return request url
     */
    String getURL();

    /**
     * @return http version
     */
    String getVersion();

    /**
     * @param name request property
     * @return property value if the property exists, returns null otherwise
     */
    String getRequestProperty(String name);

    /**
     * @param name request attribute
     * @return attribute value
     */
    String getAttribute(String name);
}
