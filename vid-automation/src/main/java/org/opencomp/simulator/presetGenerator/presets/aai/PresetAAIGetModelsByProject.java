package org.opencomp.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;
import org.opencomp.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PresetAAIGetModelsByProject extends BaseAAIPreset {
    private String projectName;
    private String serviceInstanceId;

    public PresetAAIGetModelsByProject(String projectName) {
        this.projectName = projectName;
        ImmutableMap servicesPerProject = ImmutableMap.of ("x1","7e4f8130-5dee-47c4-8770-1abc5f5ded83",
                "yyy1","13695dfb-db99-4c2f-905e-fe7bf2fc7b9f");
        this.serviceInstanceId = (servicesPerProject.containsKey(projectName)) ? servicesPerProject.get(projectName).toString(): UUID.randomUUID().toString();
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/business/projects";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of(
                "project-name", Collections.singletonList(projectName)
        );
    }

    @Override
    public Object getResponseBody() {
        return "{" +
                "  \"project\": [" +
                "    {" +
                "      \"project-name\": \"" + projectName + "\"," +
                "      \"resource-version\": \"1527026201826\"," +
                "      \"relationship-list\": {" +
                "        \"relationship\": [" +
                "          {" +
                "            \"related-to\": \"service-instance\"," +
                "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "            \"related-link\": \"/aai/v12/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Mobility/service-instances/service-instance/3f826016-3ac9-4928-9561-beee75fd91d5\"," +
                "            \"relationship-data\": [" +
                "              {" +
                "                \"relationship-key\": \"customer.global-customer-id\"," +
                "                \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"" +
                "              }," +
                "              {" +
                "                \"relationship-key\": \"service-subscription.service-type\"," +
                "                \"relationship-value\": \"Mobility\"" +
                "              }," +
                "              {" +
                "                \"relationship-key\": \"service-instance.service-instance-id\"," +
                "                \"relationship-value\": \"3f826016-3ac9-4928-9561-beee75fd91d5\"" +
                "              }" +
                "            ]," +
                "            \"related-to-property\": [" +
                "              {" +
                "                \"property-key\": \"service-instance.service-instance-name\"," +
                "                \"property-value\": \"Lital_SRIOV2_001\"" +
                "              }" +
                "            ]" +
                "          }," +
                "          {" +
                "            \"related-to\": \"service-instance\"," +
                "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "            \"related-link\": \"/aai/v12/business/customers/customer/DHV1707-TestSubscriber-2/service-subscriptions/service-subscription/HNGATEWAY/service-instances/service-instance/45713f81-04b8-4fd0-b824-64536d493984\"," +
                "            \"relationship-data\": [" +
                "              {" +
                "                \"relationship-key\": \"customer.global-customer-id\"," +
                "                \"relationship-value\": \"DHV1707-TestSubscriber-2\"" +
                "              }," +
                "              {" +
                "                \"relationship-key\": \"service-subscription.service-type\"," +
                "                \"relationship-value\": \"HNGATEWAY\"" +
                "              }," +
                "              {" +
                "                \"relationship-key\": \"service-instance.service-instance-id\"," +
                "                \"relationship-value\": \"45713f81-04b8-4fd0-b824-64536d493984\"" +
                "              }" +
                "            ]," +
                "            \"related-to-property\": [" +
                "              {" +
                "                \"property-key\": \"service-instance.service-instance-name\"," +
                "                \"property-value\": \"kkkk\"" +
                "              }" +
                "            ]" +
                "          }," +
                "          {" +
                "            \"related-to\": \"service-instance\"," +
                "            \"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\"," +
                "            \"related-link\": \"/aai/v12/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/vMOG/service-instances/service-instance/" + serviceInstanceId + "\"," +
                "            \"relationship-data\": [" +
                "              {" +
                "                \"relationship-key\": \"customer.global-customer-id\"," +
                "                \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"" +
                "              }," +
                "              {" +
                "                \"relationship-key\": \"service-subscription.service-type\"," +
                "                \"relationship-value\": \"vMOG\"" +
                "              }," +
                "              {" +
                "                \"relationship-key\": \"service-instance.service-instance-id\"," +
                "                \"relationship-value\": \"" + serviceInstanceId + "\"" +
                "              }" +
                "            ]," +
                "            \"related-to-property\": [" +
                "              {" +
                "                \"property-key\": \"service-instance.service-instance-name\"," +
                "                \"property-value\": \"FIRSTNET_DEMO\"" +
                "              }" +
                "            ]" +
                "          }," +
                "          {" +
                "            \"related-to\": \"service-instance\"," +
                "            \"relationship-label\": \"org.onap.relationships.inventory.Uses\"," +
                "            \"related-link\": \"/aai/v12/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Mobility/service-instances/service-instance/ff2d9326-1ef5-4760-aba0-0eaf372ae675\"," +
                "            \"relationship-data\": [" +
                "              {" +
                "                \"relationship-key\": \"customer.global-customer-id\"," +
                "                \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"" +
                "              }," +
                "              {" +
                "                \"relationship-key\": \"service-subscription.service-type\"," +
                "                \"relationship-value\": \"Mobility\"" +
                "              }," +
                "              {" +
                "                \"relationship-key\": \"service-instance.service-instance-id\"," +
                "                \"relationship-value\": \"ff2d9326-1ef5-4760-aba0-0eaf372ae675\"" +
                "              }" +
                "            ]," +
                "            \"related-to-property\": [" +
                "              {" +
                "                \"property-key\": \"service-instance.service-instance-name\"," +
                "                \"property-value\": \"VNF_INSTANCE_DEMO_THREE\"" +
                "              }" +
                "            ]" +
                "          }" +
                "        ]" +
                "      }" +
                "    }" +
                "  ]" +
                "}";
    }


}