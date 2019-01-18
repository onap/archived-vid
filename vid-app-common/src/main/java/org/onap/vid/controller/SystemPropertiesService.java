package org.onap.vid.controller;

import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.stereotype.Service;

@Service
public class SystemPropertiesService {
    public String getProperty(String key) {
        return SystemProperties.getProperty(key);
    }
}
