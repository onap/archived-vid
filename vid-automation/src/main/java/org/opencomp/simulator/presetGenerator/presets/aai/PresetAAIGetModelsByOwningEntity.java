package org.opencomp.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;
import org.opencomp.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAIGetModelsByOwningEntity extends BaseAAIPreset {
    String oeName;

    public PresetAAIGetModelsByOwningEntity(String oeName) {
        this.oeName = oeName;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/business/owning-entities";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of(
                "owning-entity-id", Collections.singletonList(oeName)
        );
    }

    @Override
    public Object getResponseBody() {
        return "{" +
                "      \"owning-entity\": [" +
                "        {" +
                "          \"owning-entity-id\": \"43b8a85a-0421-4265-9069-117dd6526b8a\"," +
                "          \"owning-entity-name\": \"" + oeName + "\"," +
                "          \"resource-version\": \"1527418700853\"," +
                "          \"relationship-list\": {" +
                "            \"relationship\": [" +
                "              {" +
                "                \"related-to\": \"service-instance\"," +
                "                \"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\"," +
                "                \"related-link\": \"/aai/v12/business/customers/customer/MSO_1610_ST/service-subscriptions/service-subscription/MSO-dev-service-type/service-instances/service-instance/af9d52f9-13b2-4657-a198-463677f82dc0\"," +
                "                \"relationship-data\": [" +
                "                  {" +
                "                    \"relationship-key\": \"customer.global-customer-id\"," +
                "                    \"relationship-value\": \"MSO_1610_ST\"" +
                "                  }," +
                "                  {" +
                "                    \"relationship-key\": \"service-subscription.service-type\"," +
                "                    \"relationship-value\": \"MSO-dev-service-type\"" +
                "                  }," +
                "                  {" +
                "                    \"relationship-key\": \"service-instance.service-instance-id\"," +
                "                    \"relationship-value\": \"af9d52f9-13b2-4657-a198-463677f82dc0\"" +
                "                  }" +
                "                ]," +
                "                \"related-to-property\": [" +
                "                  {" +
                "                    \"property-key\": \"service-instance.service-instance-name\"," +
                "                    \"property-value\": \"xbghrftgr_shani\"" +
                "                  }" +
                "                ]" +
                "              }," +
                "              {" +
                "                \"related-to\": \"service-instance\"," +
                "                \"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\"," +
                "                \"related-link\": \"/aai/v12/business/customers/customer/MSO_1610_ST/service-subscriptions/service-subscription/MSO-dev-service-type/service-instances/service-instance/49769492-5def-4c89-8e73-b236f958fa40\"," +
                "                \"relationship-data\": [" +
                "                  {" +
                "                    \"relationship-key\": \"customer.global-customer-id\"," +
                "                    \"relationship-value\": \"MSO_1610_ST\"" +
                "                  }," +
                "                  {" +
                "                    \"relationship-key\": \"service-subscription.service-type\"," +
                "                    \"relationship-value\": \"MSO-dev-service-type\"" +
                "                  }," +
                "                  {" +
                "                    \"relationship-key\": \"service-instance.service-instance-id\"," +
                "                    \"relationship-value\": \"49769492-5def-4c89-8e73-b236f958fa40\"" +
                "                  }" +
                "                ]," +
                "                \"related-to-property\": [" +
                "                  {" +
                "                    \"property-key\": \"service-instance.service-instance-name\"," +
                "                    \"property-value\": \"fghghfhgf\"" +
                "                  }" +
                "                ]" +
                "              }," +
                "              {" +
                "                \"related-to\": \"service-instance\"," +
                "                \"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\"," +
                "                \"related-link\": \"/aai/v12/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/vMOG/service-instances/service-instance/13695dfb-db99-4c2f-905e-fe7bf2fc7b9f\"," +
                "                \"relationship-data\": [" +
                "                  {" +
                "                    \"relationship-key\": \"customer.global-customer-id\"," +
                "                    \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"" +
                "                  }," +
                "                  {" +
                "                    \"relationship-key\": \"service-subscription.service-type\"," +
                "                    \"relationship-value\": \"vMOG\"" +
                "                  }," +
                "                  {" +
                "                    \"relationship-key\": \"service-instance.service-instance-id\"," +
                "                    \"relationship-value\": \"13695dfb-db99-4c2f-905e-fe7bf2fc7b9f\"" +
                "                  }" +
                "                ]," +
                "                \"related-to-property\": [" +
                "                  {" +
                "                    \"property-key\": \"service-instance.service-instance-name\"," +
                "                    \"property-value\": \"FIRSTNET_DEMO\"" +
                "                  }" +
                "                ]" +
                "              }," +
                "              {" +
                "                \"related-to\": \"service-instance\"," +
                "                \"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\"," +
                "                \"related-link\": \"/aai/v12/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Mobility/service-instances/service-instance/7e4f8130-5dee-47c4-8770-1abc5f5ded83\"," +
                "                \"relationship-data\": [" +
                "                  {" +
                "                    \"relationship-key\": \"customer.global-customer-id\"," +
                "                    \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"" +
                "                  }," +
                "                  {" +
                "                    \"relationship-key\": \"service-subscription.service-type\"," +
                "                    \"relationship-value\": \"Mobility\"" +
                "                  }," +
                "                  {" +
                "                    \"relationship-key\": \"service-instance.service-instance-id\"," +
                "                    \"relationship-value\": \"7e4f8130-5dee-47c4-8770-1abc5f5ded83\"" +
                "                  }" +
                "                ]," +
                "                \"related-to-property\": [" +
                "                  {" +
                "                    \"property-key\": \"service-instance.service-instance-name\"," +
                "                    \"property-value\": \"Amir123\"" +
                "                  }" +
                "                ]" +
                "              }," +
                "              {" +
                "                \"related-to\": \"service-instance\"," +
                "                \"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\"," +
                "                \"related-link\": \"/aai/v12/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/Mobility/service-instances/service-instance/d849b312-03f6-4fa3-a923-a469b850ec73\"," +
                "                \"relationship-data\": [" +
                "                  {" +
                "                    \"relationship-key\": \"customer.global-customer-id\"," +
                "                    \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"" +
                "                  }," +
                "                  {" +
                "                    \"relationship-key\": \"service-subscription.service-type\"," +
                "                    \"relationship-value\": \"Mobility\"" +
                "                  }," +
                "                  {" +
                "                    \"relationship-key\": \"service-instance.service-instance-id\"," +
                "                    \"relationship-value\": \"d849b312-03f6-4fa3-a923-a469b850ec73\"" +
                "                  }" +
                "                ]," +
                "                \"related-to-property\": [" +
                "                  {" +
                "                    \"property-key\": \"service-instance.service-instance-name\"," +
                "                    \"property-value\": \"edbh54\"" +
                "                  }" +
                "                ]" +
                "              }" +
                "            ]" +
                "          }" +
                "        }" +
                "      ]" +
                "    }";
    }
}