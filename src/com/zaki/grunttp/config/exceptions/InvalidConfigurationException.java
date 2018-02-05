package com.zaki.grunttp.config.exceptions;

import com.zaki.grunttp.util.exceptions.base.ServerBaseException;

public class InvalidConfigurationException extends ServerBaseException {
    public InvalidConfigurationException() {
        super();
    }

    public InvalidConfigurationException(String msg) {
        super(msg);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    public InvalidConfigurationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
