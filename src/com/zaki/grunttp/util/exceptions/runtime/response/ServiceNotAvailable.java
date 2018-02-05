package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;
import com.zaki.grunttp.util.exceptions.runtime.response.ServerResponseException;

public class ServiceNotAvailable extends ServerResponseException {

    public ServiceNotAvailable() {
        super(StatusCode.SERVICE_NOT_AVAILABLE);
    }
}
