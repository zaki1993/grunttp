package com.zaki.grunttp.server.request;

import com.zaki.grunttp.constant.Global;
import com.zaki.grunttp.constant.RequestMethod;
import com.zaki.grunttp.util.exceptions.runtime.response.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.zaki.grunttp.constant.Global.MAX_FILE_TRANSFER_SIZE;
import static com.zaki.grunttp.constant.Global.MAX_URI_LENGTH;

public class RequestHeader {

    private enum HttpVersion {
        HTTP1_0("HTTP/1.0"),
        HTTP1_1("HTTP/1.1"),
        HTTP2_0("HTTP/2.0");

        HttpVersion(String version) {
            this.version = version;
        }
        private String version;

        @Override
        public String toString() {
            return version;
        }
    }

    private HttpVersion version;
    private RequestMethod method;
    private String url;
    private String protocol; // todo use for http or https
    private Map<String, String> headerProperties;
    private StringBuilder headerContent;

    RequestHeader() {
        this.headerProperties = new HashMap<>();
        this.version = HttpVersion.HTTP1_1;
        this.headerContent = new StringBuilder("");
    }

    /**
     * @return the http version
     */
    public String getVersion() {
        return version.toString();
    }

    /**
     * @return the request method
     */
    public RequestMethod getMethod() {
        return method;
    }

    /**
     * @return the url
     */
    public String getURL() {
        return url;
    }

    /**
     * Appends to the current header content the newly read data
     * @param s
     */
    void addHeaderContent(String s) {
        this.headerContent.append(s);
    }

    /**
     * Parses the bytes read from the request
     * @param body
     */
    void parseData(RequestBody body) {
        findHeaderProperties(body, this.headerContent.toString().split(Global.CARRIAGE_LINE));
        validateHeaderProperties();
    }

    /**
     * Gets a header property by name
     * @param property
     * @return
     */
    String getProperty(String property) {
        return this.headerProperties.get(property);
    }

    /**
     * Parses the header attributes if the request method is GET
     * @param body
     * @param attributes
     */
    private void parseGetAttributes(RequestBody body, String attributes) {

        if (this.method == RequestMethod.GET) {
            body.retrieveBodyProperties(attributes);
        }
    }

    /**
     * Finds all header properteis
     * @param body
     * @param properties
     */
    private void findHeaderProperties(RequestBody body, String[] properties) {
        retrieveHeaderInfo(body, properties[0].trim());
        Stream.of(properties).skip(1).forEach(this::findProperty);
    }

    /**
     * Parses the header
     * @param body
     * @param headerProperty
     * @throws BadRequestException
     */
    private void retrieveHeaderInfo(RequestBody body, String headerProperty) throws BadRequestException {

        // get request method method
        headerProperty = findRequestMethod(headerProperty);

        if (headerProperty.isEmpty() || !headerProperty.contains(" ")) {
            throw new BadRequestException();
        }

        headerProperty = findURI(headerProperty);

        if (headerProperty.isEmpty()) {
            throw new BadRequestException();
        }

        // get http version
        headerProperty = findHttpVersion(headerProperty);

        if (!headerProperty.isEmpty()) {
            throw new BadRequestException();
        }

        boolean hasAttributes = url.contains("?");
        if (hasAttributes) {
            parseGetAttributes(body, url.substring(url.indexOf("?")).trim());
        }
    }

    /**
     * Retrieves the URI
     * @param headerProperty
     * @return
     */
    private String findURI(String headerProperty) {
        int spaceIdx = headerProperty.indexOf(" ");
        this.url = headerProperty.substring(0, spaceIdx);
        return headerProperty.substring(spaceIdx).trim();
    }

    /**
     * Finds the request method
     * @param headerProperty
     * @return
     */
    private String findRequestMethod(String headerProperty) {

        if (headerProperty.startsWith(RequestMethod.GET.toString())) this.method = RequestMethod.GET;
        if (headerProperty.startsWith(RequestMethod.POST.toString())) this.method = RequestMethod.POST;
        if (headerProperty.startsWith(RequestMethod.PUT.toString())) this.method = RequestMethod.PUT;
        if (headerProperty.startsWith(RequestMethod.DELETE.toString())) this.method = RequestMethod.DELETE;

        if (this.method == null) {
            throw new UnsupportedRequestMethod();
        }

        return headerProperty.substring(method.toString().length()).trim();
    }

    /**
     * Finds header property
     * @param property
     */
    private void findProperty(String property) {

        int separatorIdx = property.indexOf(":");
        if (separatorIdx == -1) {
            throw new BadRequestException();
        }
        String key = property.substring(0, separatorIdx);
        String value = property.substring(separatorIdx + 1);
        int semicolonIdx = value.lastIndexOf(";");
        if (semicolonIdx != -1) {
            value = value.substring(0, semicolonIdx);
        }
        this.headerProperties.put(key.trim(), value.trim());
    }

    /**
     * Finds the http version
     * @param headerProperty
     * @return
     */
    private String findHttpVersion(String headerProperty) {

        Arrays.stream(RequestHeader.HttpVersion.values())
                .filter(version -> headerProperty.equals(version.toString()))
                .findFirst()
                .ifPresentOrElse(this::setVersion, () -> new HttpVersionNotSupportedException());
        return headerProperty.substring(version.toString().length()).trim();
    }

    /**
     * Sets the http version
     * @param version
     */
    private void setVersion(HttpVersion version) {
        this.version = version;
    }


    /**
     * Validates http header properties
     */
    private void validateHeaderProperties() {
        validateContentLength();
        validateUriLength();
        validateHttpVersion();
        if (version == HttpVersion.HTTP1_1) {
            validateIfHostPresent();
        }
    }

    /**
     * Validates if the host header is present if the http version is 1.1
     */
    private void validateIfHostPresent() {

        String host = getProperty("Host");
        if (host == null || host.trim().isEmpty()) {
            throw new BadRequestException();
        }
    }

    /**
     * Validate http version if it is correct
     */
    private void validateHttpVersion() {

        // TODO with other version(feature)
        if (this.version != RequestHeader.HttpVersion.HTTP1_1) {
            throw new HttpVersionNotSupportedException();
        }
    }

    /**
     * Validate uri length if fits in server limitations
     */
    private void validateUriLength() {

        if (this.url.length() > MAX_URI_LENGTH) {
            throw new UriLengthTooLongException();
        }
    }

    /**
     * Validate content-length if fits in server limitations
     */
    private void validateContentLength() {

        if (this.headerProperties.containsKey("Content-Length")) {
            long contentLength = Long.valueOf(this.headerProperties.get("Content-Length"));
            if (contentLength > MAX_FILE_TRANSFER_SIZE) {
                // TODO
                throw new RequestEntityTooLarge();
            }
        }
    }
}
