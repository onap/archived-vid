package org.onap.vid.controllers;

import org.onap.vid.properties.Features;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("flags")
public class FeatureTogglingController extends RestrictedBaseController {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Map<String,Boolean> getFeatureToggles()
    {
        HashMap<String,Boolean> flags = new HashMap <String, Boolean>();
        for(Features flag : Features.values()){
            flags.put(flag.name(), flag.isActive());
        }
        return flags;


    }
}
