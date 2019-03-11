package org.onap.simulator.presetGenerator.presets.mso;

import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

public class PresetMSOOrchestrationRequestsManyInstanceStatusesGet extends BaseMSOPreset {


    private String instanceIdType;

    private String instanceType;


    public PresetMSOOrchestrationRequestsManyInstanceStatusesGet(String instanceIdType, String instanceType) {
        this.instanceIdType = instanceIdType;
        this.instanceType = instanceType;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/orchestrationRequests/v.";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of("filter", singletonList(instanceIdType+":EQUALS:df305d54-75b4-431b-adb2-eb6b9e5460df"));
    }

    @Override
    public Object getResponseBody() {
        return "" +
                "{ " +
                " \"requestList\": [{ " +
                "   \"request\": { " +
                "    \"requestId\": \"28502bd2-3aff-4a03-9f2b-5a0d1cb1ca24\", " +
                "    \"startTime\": \"Thu, 04 Jun 2009 02:51:59 GMT\", " +
                "    \"instanceReferences\": { " +
                "     \"instanceGroupId\": \"df305d54-75b4-431b-adb2-eb6b9e5460df\" " +
                "    }, " +
                "    \"requestScope\": \""+instanceType+"\", " +
                "    \"requestType\": \"createInstance\", " +
                "    \"requestDetails\": { " +
                "     \"modelInfo\": { " +
                "      \"modelType\": \""+instanceType+"\", " +
                "      \"modelVersionId\": \"ddcbbf3d-f2c1-4ca0-8852-76a807285efc\" " +
                "     }, " +
                "     \"requestInfo\": { " +
                "      \"instanceName\": \"groupTestName\", " +
                "      \"source\": \"VID\", " +
                "      \"suppressRollback\": true, " +
                "      \"requestorId\": \"ah2345\" " +
                "     } " +
                "    }, " +
                "    \"requestStatus\": { " +
                "     \"timestamp\": \"Thu, 04 Jun 2009 02:53:39 GMT\", " +
                "     \"requestState\": \"IN_PROGRESS\", " +
                "     \"statusMessage\": \""+instanceType+" instance creation\", " +
                "     \"percentProgress\": \"50\" " +
                "    } " +
                "   } " +
                "  }, { " +
                "   \"request\": { " +
                "    \"requestId\": \"28502bd2-3aff-4a03-9f2b-5a0d1cb1ca24\", " +
                "    \"startTime\": \"Thu, 04 Jun 2009 02:51:59 GMT\", " +
                "    \"instanceReferences\": { " +
                "     \"instanceGroupId\": \"df305d54-75b4-431b-adb2-eb6b9e5460df\" " +
                "    }, " +
                "    \"requestScope\": \""+instanceType+"\", " +
                "    \"requestType\": \"createInstance\", " +
                "    \"requestDetails\": { " +
                "     \"modelInfo\": { " +
                "      \"modelType\": \""+instanceType+"\", " +
                "      \"modelVersionId\": \"ddcbbf3d-f2c1-4ca0-8852-76a807285efc\" " +
                "     }, " +
                "     \"requestInfo\": { " +
                "      \"instanceName\": \"groupTestName\", " +
                "      \"source\": \"VID\", " +
                "      \"suppressRollback\": true, " +
                "      \"requestorId\": \"ah2345\" " +
                "     } " +
                "    }, " +
                "    \"requestStatus\": { " +
                "     \"timestamp\": \"Thu, 04 Jun 2009 02:53:39 GMT\", " +
                "     \"requestState\": \"COMPLETE\", " +
                "     \"statusMessage\": \""+instanceType+" instance creation\", " +
                "     \"percentProgress\": \"100\" " +
                "    } " +
                "   } " +
                "  }, { " +
                "   \"request\": { " +
                "    \"requestId\": \"f711f0ff-24b6-4d7f-9314-4b4eae15f48c\", " +
                "    \"startTime\": \"Thu, 04 Jun 2009 02:51:59 GMT\", " +
                "    \"instanceReferences\": { " +
                "     \"instanceGroupId\": \"df305d54-75b4-431b-adb2-eb6b9e5460df\" " +
                "    }, " +
                "    \"requestScope\": \""+instanceType+"\", " +
                "    \"requestType\": \"deleteInstance\", " +
                "    \"requestDetails\": { " +
                "     \"modelInfo\": { " +
                "      \"modelType\": \""+instanceType+"\", " +
                "      \"modelVersionId\": \"ddcbbf3d-f2c1-4ca0-8852-76a807285efc\" " +
                "     }, " +
                "     \"requestInfo\": { " +
                "      \"instanceName\": \"groupTestName\", " +
                "      \"source\": \"VID\", " +
                "      \"suppressRollback\": true, " +
                "      \"requestorId\": \"ah2345\" " +
                "     } " +
                "    }, " +
                "    \"requestStatus\": { " +
                "     \"timestamp\": \"Thu, 04 Jun 2009 02:53:39 GMT\", " +
                "     \"requestState\": \"IN_PROGRESS\", " +
                "     \"statusMessage\": \""+instanceType+" instance deletion\", " +
                "     \"percentProgress\": \"50\" " +
                "    } " +
                "   } " +
                "  }, { " +
                "   \"request\": { " +
                "    \"requestId\": \"f711f0ff-24b6-4d7f-9314-4b4eae15f48c\", " +
                "    \"startTime\": \"Thu, 04 Jun 2009 02:51:59 GMT\", " +
                "    \"instanceReferences\": { " +
                "     \"instanceGroupId\": \"df305d54-75b4-431b-adb2-eb6b9e5460df\" " +
                "    }, " +
                "    \"requestScope\": \""+instanceType+"\", " +
                "    \"requestType\": \"deleteInstance\", " +
                "    \"requestDetails\": { " +
                "     \"modelInfo\": { " +
                "      \"modelType\": \""+instanceType+"\", " +
                "      \"modelVersionId\": \"ddcbbf3d-f2c1-4ca0-8852-76a807285efc\" " +
                "     }, " +
                "     \"requestInfo\": { " +
                "      \"instanceName\": \"groupTestName\", " +
                "      \"source\": \"VID\", " +
                "      \"suppressRollback\": true, " +
                "      \"requestorId\": \"ah2345\" " +
                "     } " +
                "    }, " +
                "    \"requestStatus\": { " +
                "     \"timestamp\": \"Thu, 04 Jun 2009 02:53:39 GMT\", " +
                "     \"requestState\": \"COMPLETE\", " +
                "     \"statusMessage\": \""+instanceType+" instance deletion\", " +
                "     \"percentProgress\": \"100\" " +
                "    } " +
                "   } " +
                "  } " +
                " ] " +
                "} ";
    }
}
