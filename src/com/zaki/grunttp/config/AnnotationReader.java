package com.zaki.grunttp.config;

import com.zaki.grunttp.config.exceptions.*;
import com.zaki.grunttp.config.log.ServerLogger;
import com.zaki.grunttp.constant.Environment;
import com.zaki.grunttp.constant.Global;
import com.zaki.grunttp.constant.ServerHandlers;
import com.zaki.grunttp.constant.ServiceState;
import com.zaki.grunttp.handler.Bean;
import com.zaki.grunttp.handler.BeanImpl;
import com.zaki.grunttp.handler.RequestUrlHandler;
import com.zaki.grunttp.handler.anotations.Handler;
import com.zaki.grunttp.handler.anotations.WebConfig;
import com.zaki.grunttp.util.Utilizer;
import com.zaki.grunttp.util.exceptions.base.RequestHandlerNotFoundException;
import com.zaki.grunttp.util.exceptions.base.ServerBaseException;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

public class AnnotationReader {

    private static final String URL = "url";
    private static final String STATE = "state";
    private Map<String, RequestUrlHandler> handlers = new HashMap<>();
    private Bean grunttpBean;
    private Environment cacheEnvironment;

    /**
     * Read all server and user annotations
     * @return Map of handlers
     * @throws ServerBaseException
     */
    public Map<String, RequestUrlHandler> readHandlers() throws ServerBaseException {

        if (cacheEnvironment == null) {
            cacheEnvironment = Global.ENVIRONMENT;
        }
        if (cacheEnvironment != Environment.TEST) {
            readServerAnnotations();
        }
        readUserAnnotations(Handler.class);
        return handlers;
    }

    /**
     * Reads all user annotated classes
     * @throws ServerBaseException
     */
    private void readUserAnnotations(Class annotation) throws ServerBaseException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = classLoader.getResources("");
            for (Iterator<URL> it = urls.asIterator(); it.hasNext();) {
                File root = new File(it.next().getPath());
                readDir(root.getAbsolutePath(), root, annotation);
            }
        } catch (IOException e) {
            throw new AnnotationParseException(e);
        }
    }

    /**
     * Read all server annotated classes
     * @throws ServerBaseException
     */
    private void readServerAnnotations() throws ServerBaseException {

        List<String> serverHandlers = ServerHandlers.getServerHandlers();
        for (String sh : serverHandlers) {
            scanClassForHandlerAnnotation(sh);
        }
    }

    /**
     * Reads the class and scan it for handler annotation
     * @param baseName the base directory name
     * @param dir the directory to be read
     * @throws ServerBaseException
     */
    private void readDir(String baseName, File dir, Class annotationClass) throws ServerBaseException {

        for (File subObject : dir.listFiles()) {
            if (subObject.isDirectory()) {
                readDir(baseName, subObject, annotationClass);
            } else {
                String absolutePath = subObject.getAbsolutePath();
                String className = Utilizer.getClassNameFromAbsolutePath(baseName, absolutePath);
                readAnnotationByClassName(className, annotationClass);
            }
        }
    }

    /**
     * Reads the annotations by class name(only the ones that the server works with)
     * @param className class to be scanned
     * @param annotationClass annotation class
     * @throws ServerBaseException
     */
    private void readAnnotationByClassName(String className, Class annotationClass) throws ServerBaseException {

        if (annotationClass == Handler.class) {
            scanClassForHandlerAnnotation(className);
        } else if (annotationClass == WebConfig.class) {
            scanClassForWebConfigAnnotation(className);
        }
    }

    /**
     * Reads class for WebConfig.class anotation
     * @param className class to be scanned
     * @throws ServerBaseException
     */
    private void scanClassForWebConfigAnnotation(String className) throws ServerBaseException {

        if (className != null) {
            try {
                Class<?> annotatedClass = Class.forName(className);
                Annotation handler = annotatedClass.getAnnotation(WebConfig.class);
                if (handler != null) {
                    readBeanClass(annotatedClass);
                }
            } catch (ClassNotFoundException e) {
                throw new WebConfigClassNotFoundException(className);
            }
        }
    }

    /**
     * Scans the class for Handler.class annotation
     * @param className the class to be scanned
     * @throws ServerBaseException
     */
    private void scanClassForHandlerAnnotation(String className) throws ServerBaseException {

        if (className != null) {
            try {
                Class<?> annotatedClass = Class.forName(className);
                Annotation handler = annotatedClass.getAnnotation(Handler.class);
                if (handler != null) {
                    RequestUrlHandler ruh = parseHandlerAnnotation(handler, className);
                    String url = ruh.getUrl();
                    if (handlers.containsKey(url)) {
                        throw new DublicateUrlException(url);
                    } else {
                        handlers.put(url, ruh);
                        ServerLogger.info(ruh.toString());
                    }
                }
            } catch (ClassNotFoundException e) {
                throw new RequestHandlerNotFoundException(className);
            }
        }
    }

    /**
     * Parses the handler annotated class
     * @param handler handler annotation
     */
    private RequestUrlHandler parseHandlerAnnotation(Annotation handler, String className) throws ServerBaseException {

        Class<?> annotationType = handler.annotationType();
        String url;
        ServiceState state;
        try {
            url = (String) annotationType.getDeclaredMethod(URL).invoke(handler, (Object[]) null);
            state = (ServiceState) annotationType.getDeclaredMethod(STATE).invoke(handler, (Object[]) null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new ServerBaseException(e);
        }
        return new RequestUrlHandler(url, className, state);
    }

    /**
     * Reads the server bean
     * @param webConfig class with annotation WebConfig
     * @throws ServerBaseException
     */
    private void readBeanClass(Class webConfig) throws ServerBaseException {

        if (grunttpBean != null) {
            throw new DublicateBeanException();
        }
        grunttpBean = new BeanImpl();
        ServerLogger.info("Server bean is: " + webConfig.getName());

        try {
            Constructor webConfigConstructor = webConfig.getConstructor();
            Object instance = webConfigConstructor.newInstance();
            Method getStaticLocation = webConfig.getMethod("getStaticLocation");
            String staticLocation = (String) getStaticLocation.invoke(instance);
            Method getErrorPage = webConfig.getMethod("getErrorPage");
            String errorPage = (String) getErrorPage.invoke(instance);
            grunttpBean.setErrorPage(errorPage);
            grunttpBean.setStaticLocation(staticLocation);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new ServerBaseException(e);
        }
    }

    /**
     * Read grunttp bean, i.e. server bean
     * @return the bean that has been created
     * @throws ServerBaseException
     */
    public Bean readGrunttpBean() throws ServerBaseException {

        readUserAnnotations(WebConfig.class);
        if (grunttpBean == null) {
            throw new GrunttpBeanMissingException();
        }
        return grunttpBean;
    }
}
