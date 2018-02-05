package com.zaki.grunttp.memory;

import com.zaki.grunttp.config.log.ServerLogger;
import com.zaki.grunttp.server.concurrent.SessionInvalidator;
import com.zaki.grunttp.server.session.ServerSession;
import com.zaki.grunttp.server.session.Session;

import java.util.Map;

public abstract class MemoryPool {

    private static SessionMemoryTable sessionTable;
    private static SessionInvalidator sessionInvalidator;

    public static void onStart() {

        initSessionTable();
        initInvalidator();
        ServerLogger.info("Memory pool initialized..!");
    }


    public static void onStop() {
        sessionInvalidator.stopInvalidator();
    }

    /**
     * @return SessionMemoryTable which is used to retrieve the session map
     */
    public synchronized static Map<String, Session> getSessionMap() {
        if (sessionInvalidator == null) {
            initInvalidator();
        }
        return MemoryPool.sessionTable.getSessionMap();
    }

    /**
     * @param clientHost is the client for which session will be generated or retrieved
     * @return ServerSession
     */
    public static Session getClientSession(String clientHost) {

        if (sessionTable == null) {
            initSessionTable();
        }
        synchronized (sessionTable) {
            Session result;
            Map<String, Session> sessionMap = MemoryPool.getSessionMap();
            if (sessionMap.containsKey(clientHost)) {
                result = sessionMap.get(clientHost);
            } else {
                result = new ServerSession();
                sessionMap.put(clientHost, result);
            }
            synchronized (sessionInvalidator) {
                sessionInvalidator.notifyAll();
            }
            return result;
        }
    }

    /**
     * Initializes the session memory table
     */
    private static void initSessionTable() {
        sessionTable = SessionMemoryTable.getVirtualMemoryTable();
    }

    /**
     * Initializes the session invalidator
     */
    private static void initInvalidator() {
        sessionInvalidator = new SessionInvalidator();
        sessionInvalidator.start();
    }

}
