package com.et.common.dubbo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shatao on 13/6/2017.
 */
public class ApplicationServices {
    private String applicationName;
    private List<String> servicesName = new ArrayList();
    private String xmlPath;

    public ApplicationServices() {
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public List<String> getServicesName() {
        return this.servicesName;
    }

    public void setServicesName(List servicesName) {
        this.servicesName = servicesName;
    }

    public String getXmlPath() {
        return this.xmlPath;
    }

    public void setXmlPath(String xmlPath) {
        this.xmlPath = xmlPath;
    }
}

