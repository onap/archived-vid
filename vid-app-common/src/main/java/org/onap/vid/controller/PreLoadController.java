package org.onap.vid.controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(PreLoadController.PRE_LOAD)
public class PreLoadController extends VidRestrictedBaseController{
    public static final String PRE_LOAD = "preload";

    @PostMapping()
    public Boolean postPreload (HttpServletRequest request) {
        return true;
    }
}
