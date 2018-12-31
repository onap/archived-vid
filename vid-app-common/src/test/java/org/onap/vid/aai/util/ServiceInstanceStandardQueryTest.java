package org.onap.vid.aai.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.ExceptionWithRequestInfo;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Network;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.ServiceInstance;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Vlan;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Vnf;
import org.onap.vid.utils.Multival;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.onap.vid.utils.Unchecked.toURI;


public class ServiceInstanceStandardQueryTest {
    private static final Logger logger = LogManager.getLogger(ServiceInstanceStandardQueryTest.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final String serviceInstanceId = "9cdd1b2a-43a7-47bc-a88e-759ba2399f0b";
    private final String vnfInstanceId_1 = "c015cc0f-0f37-4488-aabf-53795fd93cd3";
    private final String vnfInstanceId_2 = "0846287b-65bf-45a6-88f6-6a1af4149fac";
    private final String networkInstanceId_1 = "7989a6d2-ba10-4a5d-8f15-4520bc833090";
    private final String networkInstanceId_2 = "82373aaa-c561-4e2b-96f1-7ef6f7f7b0e9";
    private final String vlanTagInstanceId_1 = "701edbb2-37e4-4473-a2a6-405cb3ab2e37";
    private final String vlanTagInstanceId_2 = "531571f4-e133-4780-8ba8-d79e63804084";
    private final String vlanTagInstanceId_3 = "df674e8c-1773-4d39-a9e9-bddd2b339d0a";
    private final String SERVICE_TYPE = "service";
    private final String VNF_TYPE = "vnf";

    @Mock
    AaiClientInterface aaiClient;
    @InjectMocks
    private ServiceInstanceStandardQuery serviceInstanceStandardQuery;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);

        when(aaiClient.typedAaiGet(any(URI.class), any(Class.class)))
                .thenAnswer(invocationOnMock -> {
                    final URI uri = (URI) invocationOnMock.getArguments()[0];
                    final String lastPart = uri.toString().replaceAll(".*/", "");
                    switch (lastPart) {
                        case serviceInstanceId:
                            return getAaiObject(ServiceInstance.class);
                        case vnfInstanceId_1:
                        case vnfInstanceId_2:
                            return getAaiObject(Vnf.class);
                        case networkInstanceId_1:
                        case networkInstanceId_2:
                            return getAaiObject(Network.class);
                        case vlanTagInstanceId_1:
                        case vlanTagInstanceId_2:
                        case vlanTagInstanceId_3:
                            return getAaiObject(Vlan.class);
                        default:
                            throw new ExceptionWithRequestInfo(HttpMethod.GET, uri.toASCIIString(), getAaiObjectString(true), 404,
                                    new HttpClientErrorException(HttpStatus.NOT_FOUND));
                    }
                });
    }

    @Test
    public void pathToObject_serviceInstanceUri_yieldsAaiObject() {
        final ServiceInstance serviceInstance = serviceInstanceStandardQuery.objectByUri(ServiceInstance.class, toURI("/aai/v12/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/vMOG/service-instances/service-instance/" + serviceInstanceId));
        assertThat(serviceInstance.getServiceInstanceId(), is(serviceInstanceId));
        assertThat(serviceInstance.getServiceInstanceName(), is("Network_repl_vMOG_rk"));
    }

    @Test
    public void pathToObject_networkUri_yieldsAaiObject() {
        final Network network = serviceInstanceStandardQuery.objectByUri(Network.class, toURI("/aai/v12/network/l3-networks/l3-network/" + networkInstanceId_1 + ""));
        assertThat(network.getNetworkId(), is(networkInstanceId_1));
        assertThat(network.getNetworkName(), is("APPC-24595-T-IST-02AShared_cor_direct_net_1"));
    }

    @Test
    public void pathToObject_vlanTagUri_yieldsAaiObject() {
        final Vlan vlan = serviceInstanceStandardQuery.objectByUri(Vlan.class, toURI("/this is an invented link/aai/v12/tag/vlan-tags/vlan-tag/" + vlanTagInstanceId_1 + ""));
        assertThat(vlan.getVlanInterface(), is("US-10688-genvnf-vlan-interface1"));
        assertThat(vlan.getVlanIdInner(), is("917"));
    }

    @Test(expectedExceptions = Exception.class)
    public void pathToObject_oneUnknownUri_throwsException() {
        serviceInstanceStandardQuery.objectByUri(ServiceInstance.class, toURI("/aai/v12/non existing path"));
    }

    @Test
    public void vnfsForServiceInstance_noRelatedButManyOthers_emptyResult() {
        final Multival<ServiceInstance, Vnf> vnfs =
                serviceInstanceStandardQuery.fetchRelatedVnfs(getAaiObject(false, ServiceInstance.class));
        assertThat(vnfs.getValues(), is(empty()));
    }

    @Test
    public void vnfsForServiceInstance_2RelatedAndManyOthers_Result2CorrectPath2() {
        final Multival<ServiceInstance, Vnf> vnfs =
                serviceInstanceStandardQuery.fetchRelatedVnfs(getAaiObject(ServiceInstance.class));

        assertThat(vnfs.getValues(), hasSize(2));
    }

    @Test
    public void serviceInstanceToL3Networks_noRelatedButManyOthers_emptyResult() {
        final Multival<ServiceInstance, Network> l3Networks =
                serviceInstanceStandardQuery.fetchRelatedL3Networks(SERVICE_TYPE, getAaiObject(false, ServiceInstance.class));
        assertThat(l3Networks.getValues(), is(empty()));
    }

    @Test
    public void serviceInstanceToL3Networks_2RelatedAndManyOthers_Result2CorrectPath2() {
        final Multival<ServiceInstance, Network> l3Networks =
                serviceInstanceStandardQuery.fetchRelatedL3Networks(SERVICE_TYPE, getAaiObject(ServiceInstance.class));

        assertThat(l3Networks.getValues(), hasSize(2));
    }

    @Test
    public void l3NetworkToVlanTags_noRelatedButManyOthers_emptyResult() {
        final Multival<Network, Vlan> vlanTags =
                serviceInstanceStandardQuery.fetchRelatedVlanTags(getAaiObject(false, Network.class));
        assertThat(vlanTags.getValues(), is(empty()));
    }

    @Test
    public void l3NetworkToVlanTags__2RelatedAndManyOthers_Result2CorrectPath() {
        final Multival<Network, Vlan> vlanTags =
                serviceInstanceStandardQuery.fetchRelatedVlanTags(getAaiObject(Network.class));

        assertThat(vlanTags.getValues(), hasSize(3));
    }

    private <T> T getAaiObject(Class<T> valueType) {
        return getAaiObject(true, valueType);
    }

    private <T> T getAaiObject(boolean withRelated, Class<T> valueType) {
        try {
            return MAPPER.readValue(getAaiObjectString(withRelated), valueType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void integrativeUsageWithLinearInvocation() throws JsonProcessingException {
        final ServiceInstanceStandardQuery srv = this.serviceInstanceStandardQuery;
        final ServiceInstance service = srv.fetchServiceInstance(toURI("path of service/" + serviceInstanceId + ""));
        final Network l3Network = ImmutableList.copyOf(srv.fetchRelatedL3Networks(SERVICE_TYPE, service).getValues()).get(0);
        final Vlan vlanTag = ImmutableList.copyOf(srv.fetchRelatedVlanTags(l3Network).getValues()).get(0);

        assertThat(vlanTag.getVlanInterface(), is("US-10688-genvnf-vlan-interface1"));
        assertThat(vlanTag.getVlanIdInner(), is("917"));
    }

    @Test
    public void integrativeUsageWithGenericAccessors() throws JsonProcessingException {
        final ServiceInstanceStandardQuery srv = this.serviceInstanceStandardQuery;

        final ServiceInstance serviceInstance = srv.fetchServiceInstance(toURI("path of service/" + serviceInstanceId + ""));

        final Multival<ServiceInstance, Network> serviceToNetworks =
                srv.fetchRelatedL3Networks(SERVICE_TYPE, serviceInstance);

        final Multival<ServiceInstance, Multival<Network, Vlan>> serviceToNetworksToVlans =
                serviceToNetworks.mapEachVal(srv::fetchRelatedVlanTags);

        logger.info(MAPPER.writeValueAsString(serviceToNetworksToVlans));

        // check all tags are in place
        final List<Vlan> vlanTags = serviceToNetworksToVlans
                .getValues().stream()
                .flatMap(networkVlanMultival -> networkVlanMultival.getValues().stream())
                .collect(Collectors.toList());

        assertThat(vlanTags, hasSize(6)); // 2 networks, with 3 vlans each
        assertThat(vlanTags, everyItem(hasProperty("vlanInterface", is("US-10688-genvnf-vlan-interface1"))));
        assertThat(vlanTags, everyItem(hasProperty("vlanIdInner", is("917"))));
    }

    private String getAaiObjectString(boolean withRelated) {
        final String relatedToVnfs = "" +
                "      { " +
                "        \"related-to\": \"generic-vnf\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", " +
                "        \"related-link\": \"/aai/v12/network/generic-vnfs/generic-vnf/" + vnfInstanceId_1 + "\", " +
                "        \"relationship-data\": [{ " +
                "            \"relationship-key\": \"generic-vnf.vnf-id\", " +
                "            \"relationship-value\": \"" + vlanTagInstanceId_1 + "\" " +
                "          } " +
                "        ], " +
                "        \"related-to-property\": [{ " +
                "            \"property-key\": \"generic-vnf.vnf-name\", " +
                "            \"property-value\": \"\" " +
                "          } " +
                "        ] " +
                "      }, { " +
                "        \"related-to\": \"generic-vnf\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", " +
                "        \"related-link\": \"/aai/v12/network/generic-vnfs/generic-vnf/" + vnfInstanceId_2 + "\", " +
                "        \"relationship-data\": [{ " +
                "            \"relationship-key\": \"generic-vnf.vnf-id\", " +
                "            \"relationship-value\": \"" + vlanTagInstanceId_2 + "\" " +
                "          } " +
                "        ], " +
                "        \"related-to-property\": [{ " +
                "            \"property-key\": \"generic-vnf.vnf-name\", " +
                "            \"property-value\": \"\" " +
                "          } " +
                "        ] " +
                "      }, ";


        final String relatedToL3Networks = "" +
                "      { " +
                "        \"related-to\": \"l3-network\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", " +
                "        \"related-link\": \"/aai/v12/network/l3-networks/l3-network/" + networkInstanceId_1 + "\", " +
                "        \"relationship-data\": [{ " +
                "            \"relationship-key\": \"l3-network.network-id\", " +
                "            \"relationship-value\": \"" + networkInstanceId_1 + "\" " +
                "          } " +
                "        ], " +
                "        \"related-to-property\": [{ " +
                "            \"property-key\": \"l3-network.network-name\", " +
                "            \"property-value\": \"APPC-24595-T-IST-02AShared_cor_direct_net_1\" " +
                "          } " +
                "        ] " +
                "      }, { " +
                "        \"related-to\": \"l3-network\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", " +
                "        \"related-link\": \"/aai/v12/network/l3-networks/l3-network/" + networkInstanceId_2 + "\", " +
                "        \"relationship-data\": [{ " +
                "            \"relationship-key\": \"l3-network.network-id\", " +
                "            \"relationship-value\": \"" + networkInstanceId_2 + "\" " +
                "          } " +
                "        ], " +
                "        \"related-to-property\": [{ " +
                "            \"property-key\": \"l3-network.network-name\", " +
                "            \"property-value\": \"APPC-24595-T-IST-02AShared_cor_direct_net_1\" " +
                "          } " +
                "        ] " +
                "      }, ";

        final String relatedToVlanTags = "" +
                "      { " +
                "        \"related-to\": \"vlan-tag\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", " +
                "        \"related-link\": \"/this is an invented link/aai/v12/tag/vlan-tags/vlan-tag/" + vlanTagInstanceId_2 + "\", " +
                "        \"relationship-data\": [{ " +
                "            \"relationship-key\": \"vlan-tag.vlan-tag-id\", " +
                "            \"relationship-value\": \"" + vlanTagInstanceId_2 + "\" " +
                "          } " +
                "        ], " +
                "        \"related-to-property\": [{ " +
                "            \"property-key\": \"vlan-tag.vlan-tag-name\", " +
                "            \"property-value\": \"Behram_smeralda_56\" " +
                "          } " +
                "        ] " +
                "      }, { " +
                "        \"related-to\": \"vlan-tag\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", " +
                "        \"related-link\": \"/this is an invented link/aai/v12/tag/vlan-tags/vlan-tag/" + vlanTagInstanceId_3 + "\" " +
                "      }, { " +
                "        \"related-to\": \"vlan-tag\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", " +
                "        \"related-link\": \"/this is an invented link/aai/v12/tag/vlan-tags/vlan-tag/" + vlanTagInstanceId_1 + "\", " +
                "        \"relationship-data\": [{ " +
                "            \"relationship-key\": \"vlan-tag.vlan-tag-id\", " +
                "            \"relationship-value\": \"" + vlanTagInstanceId_1 + "\" " +
                "          } " +
                "        ], " +
                "        \"related-to-property\": [{ " +
                "            \"property-key\": \"vlan-tag.vlan-tag-name\", " +
                "            \"property-value\": \"Alexandra_Liron_3\" " +
                "          } " +
                "        ] " +
                "      }, ";

        return "" +
                "{ " +

                // vlan props
                "  \"vlan-interface\": \"US-10688-genvnf-vlan-interface1\", " +
                "  \"vlan-id-inner\": 917, " +
                "  \"resource-version\": \"1518934744675\", " +
                "  \"in-maint\": false, " +
                "  \"is-ip-unnumbered\": false, " +
                // imaginary vlan props
                "  \"vlan-tag-id\": \"" + vlanTagInstanceId_2 + "\", " +
                "  \"vlan-tag-name\": \"Alexandra_Liron_3\", " +
                "  \"vlan-tag-type\": \"SUNIWOBA\", " +

                // service-instance props
                "  \"service-instance-name\": \"Network_repl_vMOG_rk\", " +
                "  \"service-instance-id\": \"" + serviceInstanceId + "\", " +
                "  \"environment-context\": \"General_Revenue-Bearing\", " +
                "  \"workload-context\": \"Production\", " +
                "  \"model-invariant-id\": \"92a72881-0a97-4d16-8c29-4831062e7e9b\", " +
                "  \"model-version-id\": \"5a3ad576-c01d-4bed-8194-0e72b4a3d020\", " +
                "  \"resource-version\": \"1516045827731\", " +
                "  \"orchestration-status\": \"Active\", " +

                // network props
                "  \"network-id\": \"" + networkInstanceId_1 + "\", " +
                "  \"network-name\": \"APPC-24595-T-IST-02AShared_cor_direct_net_1\", " +
                "  \"network-type\": \"CONTRAIL30_BASIC\", " +
                "  \"network-role\": \"repl\", " +
                "  \"network-technology\": \"contrail\", " +
                "  \"neutron-network-id\": \"66ee6123-1c45-4e71-b6c0-a748ae0fee88\", " +
                "  \"is-bound-to-vpn\": true, " +
                "  \"service-id\": \"db171b8f-115c-4992-a2e3-ee04cae357e0\", " +
                "  \"network-role-instance\": 0, " +
                "  \"resource-version\": \"1516046029762\", " +
                "  \"heat-stack-id\": \"APPC-24595-T-IST-02AShared_cor_direct_net_1/e8b256aa-8ce1-4384-9d99-6606eaca9eac\", " +
                "  \"contrail-network-fqdn\": \"default-domain:APPC-24595-T-IST-02C:APPC-24595-T-IST-02AShared_cor_direct_net_1\", " +
                "  \"physical-network-name\": \"FALSE\", " +
                "  \"is-provider-network\": false, " +
                "  \"is-shared-network\": true, " +
                "  \"is-external-network\": true, " +

                "  \"relationship-list\": { " +
                "    \"relationship\": [{ " +
                "        \"related-to\": \"service-instance\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.ComposedOf\", " +
                "        \"related-link\": \"/aai/v12/business/customers/customer/a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb/service-subscriptions/service-subscription/vMOG/service-instances/service-instance/" + serviceInstanceId + "\", " +
                "        \"relationship-data\": [{ " +
                "            \"relationship-key\": \"customer.global-customer-id\", " +
                "            \"relationship-value\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\" " +
                "          }, { " +
                "            \"relationship-key\": \"service-subscription.service-type\", " +
                "            \"relationship-value\": \"vMOG\" " +
                "          }, { " +
                "            \"relationship-key\": \"service-instance.service-instance-id\", " +
                "            \"relationship-value\": \"" + serviceInstanceId + "\" " +
                "          } " +
                "        ], " +
                "        \"related-to-property\": [{ " +
                "            \"property-key\": \"service-instance.service-instance-name\", " +
                "            \"property-value\": \"Network_repl_vMOG_rk\" " +
                "          } " +
                "        ] " +
                "      }, { " +
                "        \"related-to\": \"vpn-binding\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.Uses\", " +
                "        \"related-link\": \"/aai/v12/network/vpn-bindings/vpn-binding/13e94b71-3ce1-4988-ab0e-61208fc91f1c\", " +
                "        \"relationship-data\": [{ " +
                "            \"relationship-key\": \"vpn-binding.vpn-id\", " +
                "            \"relationship-value\": \"13e94b71-3ce1-4988-ab0e-61208fc91f1c\" " +
                "          } " +
                "        ], " +
                "        \"related-to-property\": [{ " +
                "            \"property-key\": \"vpn-binding.vpn-name\", " +
                "            \"property-value\": \"vMDNS\" " +
                "          }, { " +
                "            \"property-key\": \"vpn-binding.vpn-type\" " +
                "          } " +
                "        ] " +
                "      }, " + (withRelated ? relatedToVlanTags : "") + "{ " +
                "        \"related-to\": \"project\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.Uses\", " +
                "        \"related-link\": \"/aai/v12/business/projects/project/project1\", " +
                "        \"relationship-data\": [{ " +
                "            \"relationship-key\": \"project.project-name\", " +
                "            \"relationship-value\": \"project1\" " +
                "          } " +
                "        ] " +
                "      }, " + (withRelated ? relatedToL3Networks + relatedToVnfs : "") + "{ " +
                "        \"related-to\": \"owning-entity\", " +
                "        \"relationship-label\": \"org.onap.relationships.inventory.BelongsTo\", " +
                "        \"related-link\": \"/aai/v12/business/owning-entities/owning-entity/589fe0db-26c4-45e5-9f4e-a246c74fce76\", " +
                "        \"relationship-data\": [{ " +
                "            \"relationship-key\": \"owning-entity.owning-entity-id\", " +
                "            \"relationship-value\": \"589fe0db-26c4-45e5-9f4e-a246c74fce76\" " +
                "          } " +
                "        ] " +
                "      } " +
                "    ] " +
                "  } " +
                "} ";
    }
}
