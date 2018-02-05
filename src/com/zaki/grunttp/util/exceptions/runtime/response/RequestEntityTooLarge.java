package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class RequestEntityTooLarge extends ServerResponseException {
    public RequestEntityTooLarge() {
        super(StatusCode.REQUEST_ENTITY_TOO_LARGE);
    }
}
