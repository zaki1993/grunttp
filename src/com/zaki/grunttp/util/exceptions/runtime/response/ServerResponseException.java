package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.config.templates.Templates;
import com.zaki.grunttp.constant.StatusCode;

public class ServerResponseException extends RuntimeException {

    private StatusCode status;

    ServerResponseException(StatusCode status, Throwable t) {
        super(t);
        this.status = status;
    }

    public ServerResponseException(StatusCode status) {
        super();
        this.status = status;
    }

    public String getTemplate() {
        return Templates.getTemplateByStatusCode(this.status);
    }

    public StatusCode getStatusCode() {
        return status;
    }
}
