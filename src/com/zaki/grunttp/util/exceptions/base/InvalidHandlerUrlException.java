package com.zaki.grunttp.util.exceptions.base;

import com.zaki.grunttp.config.exceptions.InvalidConfigurationException;

public class InvalidHandlerUrlException extends InvalidConfigurationException {
    public InvalidHandlerUrlException(String msg) {
        super(msg);
    }
}
