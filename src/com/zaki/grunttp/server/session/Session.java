package com.zaki.grunttp.server.session;

import java.util.Map;

public interface Session {

    /**
     * Returns if the session is valid, i.e. if it's still alive
     * @return
     */
    boolean isValid();

    /**
     * Returns if the session was just created
     * @return
     */
    boolean isNew();

    /**
     * Sets a session attribute
     * @param attr is attribute name
     * @param value is attribute value
     */
    void setAttribute(String attr, Object value);

    /**
     * Gets a session attribute
     * @param attr is attribute name
     * @return
     */
    Object getAttribute(String attr);

    /**
     * @return Map of all session attributes
     */
    Map<String, Object> getSessionAttributes();

    /**
     * Invalidates the session
     */
    void invalidate();

    /**
     * Revalidates the session
     */
    void revalidate();

    /**
     * @return if the session is endless
     */
    boolean isEndless();
}
