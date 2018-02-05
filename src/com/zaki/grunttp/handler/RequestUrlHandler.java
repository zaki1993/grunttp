package com.zaki.grunttp.handler;

import com.zaki.grunttp.constant.RequestMethod;
import com.zaki.grunttp.constant.ServiceState;
import com.zaki.grunttp.handler.anotations.RequestMapping;
import com.zaki.grunttp.handler.excepions.InvalidHandlerException;
import com.zaki.grunttp.server.request.ServerRequest;
import com.zaki.grunttp.server.response.ServerResponse;
import com.zaki.grunttp.util.exceptions.base.DublicateHandlerException;
import com.zaki.grunttp.util.exceptions.base.InvalidHandlerUrlException;
import com.zaki.grunttp.util.exceptions.runtime.response.CallHandlerException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public final class RequestUrlHandler {

	private static final String REQUEST_METHOD = "method";

	private String url;
	private String className;
	private ServiceState state;
	private Object instance;
	private Class<?> handlerClass;
	private Map<RequestMethod, Method> methods;

	public RequestUrlHandler(String path, String className, ServiceState state) throws InvalidHandlerUrlException, DublicateHandlerException, InvalidHandlerException {

		this.url = path;
		if ("".equals(this.url.trim())) {
			throw new InvalidHandlerUrlException(className);
		}
		this.className = className;
		this.handlerClass = findHandlerClass();
		this.state = state;
		if (this.state == ServiceState.STATEFUL) {
			this.instance = newInstance();
		}
		this.methods = findAnnotatedMethods();
	}

	/**
	 * @return instance of the url handler
	 */
	public Object getInstance() {
		if (state == ServiceState.STATELESS) {
			return newInstance();
		}
		return instance;
	}

	/**
	 * Finds all methods that are annotated with @RequestMapping annotation
	 * @return Map of request method and method
	 * @throws DublicateHandlerException if two class methods have the same request method
	 * @throws InvalidHandlerException if the handler is not valid
	 */
	private Map<RequestMethod, Method> findAnnotatedMethods() throws DublicateHandlerException, InvalidHandlerException {

		Map<RequestMethod, Method> result = new HashMap<>();
		Method[] classMethods = handlerClass.getDeclaredMethods();
		for (Method method : classMethods) {
			Annotation annotation = method.getAnnotation(RequestMapping.class);
			if (annotation != null) {
                validateHandlerMethodReturnType(method.getReturnType(), method.getName());
                validateHandlerMethodParametersTypeAndCount(method.getParameterTypes(), method.getName());
				Class annotationType = annotation.annotationType();
				Method annotationMethod = getAnnotatedRequestMethod(annotationType);
				RequestMethod typeValue = getRequestMethodAnnotation(annotationMethod, annotation);
				if (result.containsKey(typeValue)) {
					throw new DublicateHandlerException(typeValue, className);
				} else {
					result.put(typeValue, method);
				}
			}
		}
		return result;
	}

	/**
	 * Calls the annotation method to get the requestm ethod
	 * @param annotationMethod annotation method
	 * @param annotation annotation
	 * @return request method
	 */
    private RequestMethod getRequestMethodAnnotation(Method annotationMethod, Annotation annotation) {

	    try {
            return (RequestMethod) annotationMethod.invoke(annotation, (Object[]) null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CallHandlerException(e);
        }
    }

	/**
	 *
	 * @param annotationType annotation type
	 * @return annotation method
	 */
	private Method getAnnotatedRequestMethod(Class<?> annotationType) {

        try {
            return annotationType.getMethod(REQUEST_METHOD);
        } catch (NoSuchMethodException e) {
            throw new CallHandlerException(e);
        }
    }

	/**
	 * Validates if the parameters of the method are ServerRequest.class and ServerResponse.class
	 * @param parameters handler parameter 1
	 * @param methodName handler parameter 2
	 * @throws InvalidHandlerException if the handler is not valid
	 */
    private void validateHandlerMethodParametersTypeAndCount(Class[] parameters, String methodName) throws InvalidHandlerException {

	    if (parameters.length != 2) {
	        throw new InvalidHandlerException("Wrong number of parameters for method " + methodName + " in handler " + getClassName() + ", expected 2 but got " + parameters.length);
        }
        if (parameters[0] != ServerRequest.class ||
           (parameters[1] != ServerResponse.class)) {
            throw new InvalidHandlerException("Invalid parameters type for method " + methodName + " in handler " + getClassName());
        }
    }

	/**
	 * Validates if the return type is String.class
	 * @param returnType method return type
	 * @param methodName method name
	 * @throws InvalidHandlerException if the handler is not valid
	 */
    private void validateHandlerMethodReturnType(Class<?> returnType, String methodName) throws InvalidHandlerException {
        if (returnType != String.class) {
            throw new InvalidHandlerException("The return type of method " + methodName + " is not " + String.class);
        }
    }

	/**
	 * @return url handler class
	 * @throws InvalidHandlerException if the handler is not valid
	 */
	private Class<?> findHandlerClass() throws InvalidHandlerException {
	    try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new InvalidHandlerException(e);
        }
    }

	/**
	 * @return new instance of the handler class
	 */
	private Object newInstance() {

		Object instance;
		try {
			Constructor<?> constructor = handlerClass.getConstructor();
			instance = constructor.newInstance();
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new CallHandlerException(e);
		}
		return instance;
	}

	/**
	 * @return handler url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return handler class name
	 */
	public String getClassName() {
		return handlerClass.getCanonicalName();
	}

	/**
	 * @param methodType request method
	 * @return handler method that handles this request method
	 */
	public Method getMethod(RequestMethod methodType) {
		return methods.get(methodType);
	}

	@Override
	public String toString() {

		StringBuilder handlerMethods = new StringBuilder(50);
		if (!methods.isEmpty()) {
			for (Map.Entry<RequestMethod, Method> method : methods.entrySet()) {
				handlerMethods.append("Request method: ")
							  .append(method.getKey())
                              .append(", ")
							  .append("Handler method: ")
							  .append(method.getValue().getName())
							  .append("\n");
			}
		}
		String current = handlerMethods.toString();
		return new StringBuilder(100).append("Handler found: \n")
                                            .append("\'")
                                            .append(getClassName())
                                            .append("\' is handling \'")
                                            .append(url)
                                            .append("\'")
                                            .append("\nState: ")
                                            .append(state)
                                            .append("\n")
                                            .append(current)
                                            .toString();
	}
}
