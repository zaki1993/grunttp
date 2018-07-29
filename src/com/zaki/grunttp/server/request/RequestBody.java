package com.zaki.grunttp.server.request;

import com.zaki.grunttp.config.log.ServerLogger;
import com.zaki.grunttp.util.Utilizer;
import com.zaki.grunttp.util.exceptions.runtime.response.BadRequestException;
import com.zaki.grunttp.util.exceptions.runtime.response.RequestEntityTooLarge;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zaki.grunttp.constant.Global.MAX_FILE_TRANSFER_SIZE;

public class RequestBody {

    private List<Byte> content;
    private Map<String, String> attributes;
    private byte[] uploadedFile;

    RequestBody() {
        this.attributes = new HashMap<>();
        this.content = new ArrayList<>();
    }

    /**
     * @return bytes from request parsed and tokenized to a file
     */
    public byte[] getUploadedFile() {
        return uploadedFile;
    }

    /**
     * @return all body attributes read from the request
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Set the uploaded file bytes
     * @param uploadedFile array of file bytes
     */
    public void setUploadedFile(byte[] uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     * Sets body attribute
     * @param key
     * @param value
     */
    public void setAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    /**
     * Gets a body attribute by given key
     * @key attribute name
     */
    public String getAttribute(String key) {
        return this.attributes.get(key);
    }

    /**
     * Adds content to the body
     * @param bytes request bytes
     */
    public void addBodyContent(List<Byte> bytes) {
        validateBodySize();
        this.content.addAll(bytes);
    }

    /**
     * @return if the body has any bytes
     */
    public boolean hasBytes() {
        return this.content.size() != 0;
    }

    /**
     * @return if the body has size that fits in the server limitations
     */
    private boolean validateBodySize() {
        if (this.content.size() > MAX_FILE_TRANSFER_SIZE) {
            throw new RequestEntityTooLarge();
        }
        return true;
    }

    /**
     * Gets the body properties
     * @param body
     */
    public void retrieveBodyProperties(String body) {

        String[] pairedAttr = body.split("&");
        for (String attr : pairedAttr) {
            String[] keyValueAttr = attr.split("=");
            String key = keyValueAttr[0];
            String value = keyValueAttr.length == 2 ? keyValueAttr[1] : "";
            try {
                this.attributes.put(key, java.net.URLDecoder.decode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                ServerLogger.severe(e); // TODO throw exception, won't come here
            }
        }
    }

    /**
     * Parses the body depending of the content type
     * @param header
     */
    public void parseData(RequestHeader header) {

        String contentType = header.getProperty("Content-Type");
        if (contentType != null) {
            if (contentType.equals("application/x-www-form-urlencoded")) {
                retrieveBodyProperties(new String(Utilizer.toByteArray(this.content)));
            } else if (contentType.equals("multipart/form-data")) {
                retrieveFileBytes(this.content);
            } else if (contentType.startsWith("application/json")) {
                processJson(new String(Utilizer.toByteArray(this.content)));
            }
        }
    }

    private void processJson(String jsonString) {
        System.out.println(jsonString);
    }

    /**
     * Formats all body bytes if the content-type is multipart/form-data to be used later with streams and save them in file
     * @param body
     */
    private void retrieveFileBytes(final List<Byte> body) {

        int startIdx = Utilizer.findCarriageReturnIndex(body);
        int endIdx = Utilizer.findEndOfFileIndex(body);
        String bodyProperties = new String(Utilizer.toByteArray(body.subList(0, startIdx)));
        retrieveBodyAttributes(bodyProperties);
        this.setUploadedFile(Utilizer.toByteArray(body.subList(startIdx, endIdx)));
    }

    /**
     * Finds all body attributes
     * @param body
     */
    private void retrieveBodyAttributes(String body) {
        for (String attr : body.split("\r\n")) {
            if (!attr.contains(":")) {
                continue;
            }
            retrieveAttribute(attr);
        }
    }

    /**
     * Finds body attribute
     * @param attr
     */
    private void retrieveAttribute(String attr) {

        int separatorIdx = attr.indexOf(":");
        if (separatorIdx == -1) {
            throw new BadRequestException();
        }
        String key = attr.substring(0, separatorIdx);
        String value = attr.substring(separatorIdx + 1);
        int semicolonIdx = value.lastIndexOf(";");
        if (semicolonIdx != -1) {
            value = value.substring(0, semicolonIdx);
        }
        attributes.put(key.trim(), value.trim());
    }
}
