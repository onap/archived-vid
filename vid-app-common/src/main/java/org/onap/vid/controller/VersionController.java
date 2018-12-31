package org.onap.vid.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.onap.portalsdk.core.controller.UnRestrictedBaseController;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.substringAfterLast;

@RestController
@RequestMapping("version")
public class VersionController extends UnRestrictedBaseController {

    private final ServletContext servletContext;

    @Inject
    public VersionController(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> getVersionAndFeatures()
    {
        String features = SystemProperties.getProperty("features.set.filename");
        String version;
        try {
            final URL resource = servletContext.getResource("/app/vid/scripts/constants/version.json");
            HashMap<String,String> versionFile = new HashMap <>();
            ObjectMapper mapper = new ObjectMapper();
            versionFile.putAll(mapper.readValue(resource, new TypeReference<HashMap<String, String>>() {}));
            version = versionFile.get("Version");
        } catch (IOException e) {
            version = "Error retrieving build number";
        }
        String majorVersion = getDisplayVersion(features, version);
        return ImmutableMap.of("Features", features, "Build", version, "DisplayVersion", majorVersion);
    }

    String getDisplayVersion(String features, String build) {
        Matcher matcher = Pattern.compile("([^/]+?)(\\.features|$)").matcher(features);
        final String majorByFeatures = matcher.find() ? matcher.group(1) : features;

        final String buildByVersion = StringUtils.defaultIfBlank(substringAfterLast(build, "."), build);

        return StringUtils.join(majorByFeatures, ".", buildByVersion);
    }
}
