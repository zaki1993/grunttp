package com.zaki.grunttp.util.exceptions.base;

import com.zaki.grunttp.config.exceptions.InvalidConfigurationException;

public class RequestHandlerNotFoundException extends InvalidConfigurationException {

    public RequestHandlerNotFoundException(String className) {
        super(new StringBuilder(50).append("Handler ")
                                   .append(className)
                                   .append(" not found!")
                                   .toString());
    }

}
