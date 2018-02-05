package com.zaki.grunttp.util.exceptions.base;

import com.zaki.grunttp.config.exceptions.InvalidConfigurationException;
import com.zaki.grunttp.constant.RequestMethod;

public class DublicateHandlerException extends InvalidConfigurationException {
    public DublicateHandlerException(RequestMethod typeValue, String className) {
        super(new StringBuilder("Request type ").append(typeValue)
                                                .append(" for ")
                                                .append(className)
                                                .append(" already exists")
                                                .toString());
    }
}
