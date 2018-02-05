package com.zaki.grunttp.handler;

public interface Bean {

    /**
     * @return if error page was given
     */
    boolean hasErrorPage();

    /**
     * @return configured error page relative path
     */
    String getErrorPage();

    /**
     * Sets error page
     * @param page error page
     */
    void setErrorPage(String page);

    /**
     * Sets the static resources location
     * @param staticLocation
     */
    void setStaticLocation(String staticLocation);

    /**
     * @return the static resources location
     */
    String getStaticLocation();
}
