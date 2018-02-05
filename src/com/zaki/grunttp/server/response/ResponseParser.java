package com.zaki.grunttp.server.response;

import com.zaki.grunttp.constant.StatusCode;
import com.zaki.grunttp.util.exceptions.runtime.InvalidResponseException;
import com.zaki.grunttp.constant.Global;

import java.util.Map;

public class ResponseParser {

    private final ServerResponse response;
    private final String clientResponse;

    public ResponseParser(ServerResponse response, String clientResponse) {
        this.response = response;
        this.clientResponse = clientResponse;
    }

    /**
     * Parses the client response
     * @return Pair of formatted response and action { forward or redirect }
     */
    public Map.Entry<String, String> parseResponse() {

        if (response == null || clientResponse == null) {
            throw new InvalidResponseException("Response " + clientResponse + " is invalid!");
        }

        String result;
        String type = Global.FORWARD;
        if (!clientResponse.contains(":")) {
            result = clientResponse.trim();
        } else {
            int colonIdx = clientResponse.indexOf(":");
            type = clientResponse.substring(0, colonIdx).trim();
            result = clientResponse.substring(colonIdx + 1).trim();
        }
        if (type.equals(Global.REDIRECT)) {
            response.setStatusCode(StatusCode.PERMANENT_REDIRECT);
            // TODO to move permanent to http if not provided
            response.addHeader(Global.HEADER_LOCATION, "http://localhost:5557/" + filterResult(result)); // TODO with the host
            return Map.entry(type, result);
        }
        if (!type.equals(Global.FORWARD)) {
            throw new InvalidResponseException("'" + type + "' is not valid property, please refer to one of these: { forward, redirect }");
        }
        return Map.entry(type, result);
    }

    /**
     * Removes the slash if the response uri contains such
     * @param result client response
     * @return formatted uri
     */
    private String filterResult(String result) {

        if (result.startsWith("/")) {
            return result.substring(1);
        }
        return result;
    }
}
