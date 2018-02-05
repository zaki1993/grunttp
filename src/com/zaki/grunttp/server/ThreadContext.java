package com.zaki.grunttp.server;

import com.zaki.grunttp.server.request.ServerRequest;
import com.zaki.grunttp.server.response.ServerResponse;

public class ThreadContext {

    private static ThreadLocal<ThreadContext> context = new ThreadLocal<>();

    private ServerRequest req;
    private ServerResponse res;

    private ThreadContext(ServerRequest req, ServerResponse res) {
        this.req = req;
        this.res = res;
    }

    /**
     * @return current request
     */
    public ServerRequest getRequest() {
        return req;
    }

    /**
     * @return current response
     */
    public ServerResponse getResponse() {
        return res;
    }

    /**
     * @return current ThreadContext
     */
    public static ThreadContext getCurrent() {
    	return context.get();
    }

    /**
     * Initializes new context and sets the current one to it
     * @param req current request
     * @param res current response
     */
    public static void initContext(ServerRequest req, ServerResponse res) {
    	ThreadContext tc = new ThreadContext(req, res);
    	setAsCurrent(tc);
    }

    /**
     * @param tc ThreadContext which is set to the current one
     */
    public static void setAsCurrent(ThreadContext tc) {

    	if (context == null) {
    		context = new ThreadLocal<>();
    	}
    	context.set(tc);
    }
}
