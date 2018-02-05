package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class AccessDeniedForbiddenException extends ServerResponseException {

    public AccessDeniedForbiddenException() {
        super(StatusCode.FORBIDDEN);
    }
}
