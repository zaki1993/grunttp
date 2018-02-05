package com.zaki.grunttp.handler;

public class BeanImpl implements Bean {

    private String errorPage;
    private String staticLocation;

    @Override
    public boolean hasErrorPage() {
        return errorPage != null && !errorPage.trim().isEmpty();
    }

    @Override
    public String getErrorPage() {
        return errorPage;
    }

    @Override
    public void setErrorPage(String page) {

        if (page == null || page.trim().isEmpty()) {
            errorPage = null;
        } else if (page.startsWith("/")) {
                this.errorPage = page.substring(1);
        } else {
            this.errorPage = page;
        }
    }

    @Override
    public void setStaticLocation(String staticLocation) {

        if (staticLocation == null) {
            this.staticLocation = "";
        } else if (staticLocation.startsWith("/")) {
            this.staticLocation = staticLocation.substring(1);
        } else {
            this.staticLocation = staticLocation;
        }
    }

    @Override
    public String getStaticLocation() {
        return staticLocation;
    }
}
