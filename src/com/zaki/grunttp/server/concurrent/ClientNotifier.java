package com.zaki.grunttp.server.concurrent;

import java.util.concurrent.atomic.AtomicLong;

public class ClientNotifier {

    private static AtomicLong connectedClients = new AtomicLong(0);

    private ClientNotifier() { }

    /**
     * Increments when client connects
     */
    public static void notifyConnect() {
        connectedClients.incrementAndGet();
    }

    /**
     * Increment when client disconnects
     */
    public static void notifyDisconnect() {
        connectedClients.decrementAndGet();
    }

    /**
     * @return the number of currently connected clients
     */
    public long getConnectedClients() {
        return this.connectedClients.get();
    }
}
