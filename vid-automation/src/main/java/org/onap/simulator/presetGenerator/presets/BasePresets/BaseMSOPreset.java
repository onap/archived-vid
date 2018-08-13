package org.onap.simulator.presetGenerator.presets.BasePresets;

import vid.automation.test.infra.Features;

/**
 * Created by itzikliderman on 27/12/2017.
 */
public abstract class BaseMSOPreset extends BasePreset {

    public static String getRequestBodyWithTestApiOnly() {
        if (Features.FLAG_ADD_MSO_TESTAPI_FIELD.isActive()) {
            return "" +
                    "{" +
                    "  \"requestDetails\": { " +
                    "    \"requestParameters\": { " +
                    "      \"testApi\": \"GR_API\" " +
                    "    } " +
                    "  } " +
                    "} " +
                    "";
        } else {
            return null;
        }
    }

    @Override
    protected String getRootPath() {
        return "/mso";
    }
}
