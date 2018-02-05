package com.zaki.grunttp.server.session;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ServerSession implements Session {

    private static long defaultSessionLife = 30L;
    private Map<String, Object> attributes;
    private LocalDateTime sessionEnd;
    private long sessionLife;
    private boolean isNew;
    private boolean endless;

    public ServerSession() {
        this(defaultSessionLife);
    }

    public ServerSession(long seconds) {
        this.attributes = new HashMap<>();
        this.sessionLife = seconds;
        this.isNew = true;
        this.endless = (seconds <= 0);
        if (!endless) {
            findSessionEndTime();
        }
    }

    @Override
    public boolean isValid() {
        return endless || LocalDateTime.now().isBefore(this.sessionEnd);
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
    public void setAttribute(String attribute, Object value) {
        this.attributes.put(attribute, value);
    }

    @Override
    public Object getAttribute(String attribute) {
        return this.attributes.get(attribute);
    }

    @Override
    public Map<String, Object> getSessionAttributes() {
        return this.attributes;
    }

    @Override
    public void invalidate() {
        this.sessionLife = 0L;
        this.endless = false;
        findSessionEndTime();
    }

    @Override
    public boolean isEndless() {
        return this.endless;
    }

    /**
     * Sets the default session life time, used if when creating session the time is not provided
     * @param life session lifetime
     */
    public static void setDefaultSessionLife(long life) {
        ServerSession.defaultSessionLife = life;
    }

    /**
     * Revalidate the session
     */
    public void revalidate() {
        if (!endless) {
            findSessionEndTime();
        }
        this.isNew = false;
    }

    /**
     * Helper method to find when is the session life end
     */
    private void findSessionEndTime() {
        this.sessionEnd = LocalDateTime.now().plusSeconds(this.sessionLife);
    }
}
