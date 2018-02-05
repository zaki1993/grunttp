package com.zaki.grunttp.handler.anotations;

import com.zaki.grunttp.constant.ServiceState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Handler {
    String url();
    ServiceState state() default ServiceState.STATEFUL;
}
