package vid.automation.test.services;

import com.google.common.collect.ImmutableMap;
import vid.automation.test.Constants;

public class BulkRegistration {

    public static void searchExistingServiceInstance() {
        searchExistingServiceInstance("Active");
    }
    public static void searchExistingServiceInstance(String orchStatus) {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.genericRequest.ECOMP_PORTAL_GET_SESSION_SLOT_CHECK_INTERVAL,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_FULL_SUBSCRIBERS,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SERVICES,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER,
                        Constants.RegisterToSimulator.SearchForServiceInstance.FILTER_SERVICE_INSTANCE_BY_ID,
                        Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT

                }, ImmutableMap.<String, Object>of("<ORCH_STATUS>", orchStatus));
    }

    public static void searchExistingServiceInstance2(String orchStatus) {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_FULL_SUBSCRIBERS,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SERVICES,
                        Constants.RegisterToSimulator.SearchForServiceInstance.GET_SUBSCRIBERS_FOR_CUSTOMER_2,
                        Constants.RegisterToSimulator.SearchForServiceInstance.FILTER_SERVICE_INSTANCE_BY_ID_2,
                        Constants.RegisterToSimulator.SearchForServiceInstance.NAMED_QUERY_VIEW_EDIT_2
                }, ImmutableMap.<String, Object>of("<ORCH_STATUS>", orchStatus));
    }

    public static void associatePnf() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.pProbe.GET_SPECIFIC_PNF,
                        Constants.RegisterToSimulator.pProbe.ADD_PNF_RELATIONSHIP,
                        Constants.RegisterToSimulator.pProbe.GET_ADD_PNF_RELATIONSHIP_ORCH_REQ
                }, ImmutableMap.<String, Object>of());
    }

    public static void searchPnfError() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.pProbe.GET_SPECIFIC_PNF_ERROR
                }, ImmutableMap.<String, Object>of());
    }
    public static void associatePnfError() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.pProbe.GET_SPECIFIC_PNF,
                        Constants.RegisterToSimulator.pProbe.ADD_PNF_RELATIONSHIP_ERROR
                }, ImmutableMap.<String, Object>of());
    }

    public static void dissociatePnf() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.REMOVE_PNF_RELATIONSHIP,
                        Constants.RegisterToSimulator.pProbe.GET_REMOVE_PNF_RELATIONSHIP_ORCH_REQ
                }, ImmutableMap.<String, Object>of());
    }

    public static void getAssociatedPnfs() {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.pProbe.GET_LOGICAL_LINK
                }, ImmutableMap.<String, Object>of());
    }

    public static void activateServiceInstance(String action) {
        SimulatorApi.registerExpectation(
                new String [] {
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.activateDeactivate.ACTIVATE_SERVICE_INSTANCE,
                        Constants.RegisterToSimulator.activateDeactivate.ACTIVATE_SERVICE_INSTANCE_ORCH_REQUEST
                }, ImmutableMap.<String, Object>of("<ACTIVE_ACTION>", action));
    }

    public static void activateServiceInstanceError(String action) {
        SimulatorApi.registerExpectation(
                new String []{
                        Constants.RegisterToSimulator.pProbe.GET_SERVICE_INSTANCE_WITH_LOGICAL_LINKS,
                        Constants.RegisterToSimulator.activateDeactivate.ACTIVATE_SERVICE_INSTANCE_ERROR
                } , ImmutableMap.<String, Object>of("<ACTIVE_ACTION>", action));
    }

}
