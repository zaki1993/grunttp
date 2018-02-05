package com.zaki.grunttp.server.concurrent;

import com.zaki.grunttp.config.log.ServerLogger;
import com.zaki.grunttp.memory.MemoryPool;
import com.zaki.grunttp.server.HttpServerImpl;
import com.zaki.grunttp.util.exceptions.runtime.ServerRuntimeException;

public class SessionInvalidator extends Thread {

    private boolean active;

    public SessionInvalidator() {
        super("Session Invalidator");
        this.setDaemon(true);
        this.active = true;
        ServerLogger.info("Session invalidator initialized..!");
    }

    @Override
    public void run() {

        while (active) {
            synchronized (this) {
                while (MemoryPool.getSessionMap().isEmpty() || hasOnlyEndlessSessions()) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new ServerRuntimeException(e);
                    }
                }
            }
            clearExpiredSessions();
            sleepInvalidator();
        }
    }

    /**
     * Sleeps the invalidator for 1 second
     */
    private void sleepInvalidator() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            ServerLogger.severe(e);
        }
    }

    /**
     * Used to prevent high cpu usage when the map has infinite sessions
     * Optimized to return false if any of the sessions in the map is not endless
     * @return the sessions map contains only endless sessions
     */
    private boolean hasOnlyEndlessSessions() {
        return !MemoryPool.getSessionMap()
                          .entrySet()
                          .stream()
                          .anyMatch(session -> !session.getValue().isEndless());
    }

    /**
     * Stops invalidating sessions
     */
    public void stopInvalidator() {
        this.active = false;
    }

    /**
     * Method that is called periodically to clear invalid sessions
     */
    private void clearExpiredSessions() {

        MemoryPool.getSessionMap()
                  .entrySet()
                  .removeIf(session -> !session.getValue().isValid());
    }
}
