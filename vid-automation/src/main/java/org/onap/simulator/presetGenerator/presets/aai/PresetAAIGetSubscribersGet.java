package org.onap.simulator.presetGenerator.presets.aai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.onap.simulator.presetGenerator.presets.model.Subscriber;
import org.springframework.http.HttpMethod;

public class PresetAAIGetSubscribersGet extends BaseAAIPreset {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private List<Subscriber> subscriberList;

    public PresetAAIGetSubscribersGet(List<Subscriber> subscriberList) {
        this.subscriberList = subscriberList;
    }

    public PresetAAIGetSubscribersGet() {
        TypeReference<List<Subscriber>> typeRef
                = new TypeReference<List<Subscriber>>() {};

        // like registration_to_simulator/create_new_instance/aai_get_full_subscribers.json
        String subscribersString = "" +
                "[ " +
                "        { " +
                "          \"global-customer-id\":\"CAR_2020_ER\", " +
                "          \"subscriber-name\":\"CAR_2020_ER\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1494001902987\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"21014aa2-526b-11e6-beb8-9e71128cae77\", " +
                "          \"subscriber-name\":\"JULIO ERICKSON\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1494001776295\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"DHV1707-TestSubscriber-2\", " +
                "          \"subscriber-name\":\"DALE BRIDGES\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1498751754450\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"DHV1707-TestSubscriber-1\", " +
                "          \"subscriber-name\":\"LLOYD BRIDGES\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1498595633607\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"jimmy-example\", " +
                "          \"subscriber-name\":\"JimmyExampleCust-20161102\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1494001970063\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"jimmy-example2\", " +
                "          \"subscriber-name\":\"JimmyExampleCust-20161103\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1494001970225\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"ERICA5779-TestSub-PWT-102\", " +
                "          \"subscriber-name\":\"ERICA5779-TestSub-PWT-102\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1508852495058\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"ERICA5779-TestSub-PWT-101\", " +
                "          \"subscriber-name\":\"ERICA5779-TestSub-PWT-101\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1508179953733\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\", " +
                "          \"subscriber-name\":\"Emanuel\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1494001894192\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"ERICA5779-Subscriber-4\", " +
                "          \"subscriber-name\":\"ERICA5779-Subscriber-5\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1509134064531\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"ERICA5779-TestSub-PWT-103\", " +
                "          \"subscriber-name\":\"ERICA5779-TestSub-PWT-103\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1509045503736\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"ERICA5779-Subscriber-2\", " +
                "          \"subscriber-name\":\"ERICA5779-Subscriber-2\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1509122065439\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"e433710f-9217-458d-a79d-1c7aff376d89\", " +
                "          \"subscriber-name\":\"SILVIA ROBBINS\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1494001938080\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"ERICA5779-Subscriber-3\", " +
                "          \"subscriber-name\":\"ERICA5779-Subscriber-3\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1509122759407\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"31739f3e-526b-11e6-beb8-9e71128cae77\", " +
                "          \"subscriber-name\":\"CRAIG/ROBERTS\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1494001772223\" " +
                "        }, " +
                "        { " +
                "          \"global-customer-id\":\"global-customer-id\", " +
                "          \"subscriber-name\":\"global-customer-id\", " +
                "          \"subscriber-type\":\"INFRA\", " +
                "          \"resource-version\":\"1494001772226\", " +
                "          \"relationship-list\":{ " +
                "               \"relationship\":[ " +
                "                   { " +
                "                       \"related-to\":\"vpn-binding\", " +
                "                       \"relationship-label\":\"org.onap.relationships.inventory.Uses\", " +
                "                       \"related-link\":\"/aai/v14/network/vpn-bindings/vpn-binding/3e7834fb-a8e0-4243-a837-5352ccab4602\", " +
                "                       \"relationship-data\":[ " +
                "                           { " +
                "                               \"relationship-key\":\"vpn-binding.vpn-id\", " +
                "                               \"relationship-value\":\"3e7834fb-a8e0-4243-a837-5352ccab4602\" " +
                "                           } " +
                "                       ], " +
                "                       \"related-to-property\":[ " +
                "                           { " +
                "                               \"property-key\":\"vpn-binding.vpn-name\", " +
                "                               \"property-value\":\"LPPVPN\" " +
                "                           }, " +
                "                           { " +
                "                               \"property-key\":\"vpn-binding.vpn-type\", " +
                "                               \"property-value\":\"CUSTOMER\" " +
                "                           } " +
                "                       ] " +
                "                   } " +
                "               ] " +
                "           } " +
                "        }" +
                "      ] ";
        try {
            this.subscriberList = objectMapper.readValue(subscribersString, typeRef);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Subscriber> getSubscriberList() {
        return subscriberList;
    }

    public void setSubscriberList(List<Subscriber> subscriberList) {
        this.subscriberList = subscriberList;
    }

    @Override
    public Object getResponseBody() {
        return ImmutableMap.of("customer", subscriberList);
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of(
                "subscriber-type", Collections.singletonList("INFRA"),
                "depth",  Collections.singletonList("0"));
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/business/customers";
    }

}
