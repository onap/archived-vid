/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

require('./aaiSubscriberController');
require('../services/dataService');

const jestMock = require('jest-mock');

describe('TreeCtrl testing', () => {
    var window;

    let $scope;
    beforeEach(
        angular.mock.module('app')
    );

    beforeEach(inject(function (_$controller_) {
        $scope = {};
        _$controller_('TreeCtrl', {
            $scope: $scope
        });
    }));

    test('Verify expandAll calls broadcast with expand-all parameter', () => {
        // given
        const broadcast = jestMock.fn();
        $scope.$broadcast = broadcast;
        FIELD = {
            ID: {
                ANGULAR_UI_TREE_EXPANDALL: "angular-ui-tree:expand-all"
            }
        };
        // when
        $scope.expandAll();
        // then
        expect(broadcast).toHaveBeenCalledWith("angular-ui-tree:expand-all");
    });

    test('Verify collapseAll calls broadcast with collapse-all parameter', () => {
        // given
        const broadcast = jestMock.fn();
        $scope.$broadcast = broadcast;
        FIELD = {
            ID: {
                ANGULAR_UI_TREE_COLLAPSEALL: "angular-ui-tree:collapse-all"
            }
        };
        // when
        $scope.collapseAll();
        // then
        expect(broadcast).toHaveBeenCalledWith("angular-ui-tree:collapse-all");
    });

    test('Verify toggle calls toggle in given scope', () => {
        // given
        const testScope = {};
        testScope.toggle = jestMock.fn();
        // when
        $scope.toggle(testScope);
        // then
        expect(testScope.toggle).toHaveBeenCalled();
    });

    test('Verify remove calls remove in given scope', () => {
        // given
        const testScope = {};
        testScope.remove = jestMock.fn();
        // when
        $scope.remove(testScope);
        // then
        expect(testScope.remove).toHaveBeenCalled();
    });

    test('Verify moveLastToTheBeginning pops last element from data and puts it on the beginning', () => {
        // given
        $scope.data = [ 'a', 'b', 'c' ];
        const expectedResult = [ 'c', 'a', 'b' ];

        // when
        $scope.moveLastToTheBeginning();
        // then
        expect($scope.data).toMatchObject(expectedResult);
    });

    test('Verify newSubItem pushes new item into given scope', () => {
        // given
        const testScope = {};
        const testModel = {};

        testModel.id = 2;
        testModel.nodes = [];
        testModel.title = 'testObject';

        const expectedResult = {
            id: 20,
            title: 'testObject.1',
            nodes: []
        };

        testScope.$modelValue = testModel;

        // when
        $scope.newSubItem(testScope);
        // then
        expect(testModel.nodes.length).toBe(1);
        expect(testModel.nodes[0]).toMatchObject(expectedResult);
    });
});

describe('aaiSubscriberController testing', () => {

    beforeEach(
        angular.mock.module('app')
    );

    let $scope;
    let $any;

    let mockFIELD = {
        PROMPT: {
            SELECT_SERVICE: 'testService'
        },
        NAME: {
            SERVICE_INSTANCE_ID: 'testID',
            SERVICE_INSTANCE_NAME: 'testName'
        },
        ID: {
            INVENTORY_RESPONSE_ITEMS: 0,
            INVENTORY_RESPONSE_ITEM: 0
        },
        STYLE: {
            MSO_CTRL_BTN: 'testButtonStyle',
        },
        STATUS: {
            DONE: 'done',
        },
        ERROR: {
            AAI: 'testAAIError',
            FETCHING_SERVICE_TYPES: 'testServiceType',
            SELECT: 'testAlertError',
        },

    };

    let mockCOMPONENT = {
        SHOW_COMPONENT_DETAILS: 'testComponentDetails',
        VNF: 'testComponentVNF',
        WELCOME_PATH: 'http://test/welcome/',
        CREATE_INSTANCE_PATH: 'testInstancePath',
        FEATURE_FLAGS:{},
    };

    let mockAaiService = {
        getSubscriptionServiceTypeList(customerId,successFunction,failFunction){},
        getServiceModelsByServiceType(queryId,customerId,serviceType,successFunction,failFunction){},
        searchServiceInstances(query){},
    };

    let mockAsdcService = {
        isMacro(item){},
        shouldTakeTheAsyncInstantiationFlow(serviceModel){},
    };

    let mockPropertyService = {
        retrieveMsoMaxPollingIntervalMsec(){return 1000},
        setMsoMaxPollingIntervalMsec(msecs){},
        retrieveMsoMaxPolls(){return 1000},
        setMsoMaxPolls(polls){},
    };

    let mockUtilityService = {
    };

    let mockVidService = {
        setModel(model){},
    };

    let dataService;

    let mockLocation = {
        path(path){},
    };

    let mockHttp = {
        get(){},
    };

    let mockOwningEntityService = {
        getOwningEntityProperties(callBack){}
    };

    let mockQ = {

    };

    $ = (selector) => {return mockSelector};
    let mockSelector = {
        addClass(){return this},
        removeClass(){return this},
        attr(){},
    };

    let mock_ = {
        reduce(service,iterateeFunction,accumulatorFunction){},
        forEach(services,iteratedFunction){},
        includes(array, status){
            return array.includes(status);
        },
        isEmpty(something) {return true;},
    };

    let mockedLog = {};

    let mockFeatureFlags = {};

    let mockVIDCONFIGURATION = {};

    let mockRoute = {};

    let mockUibModal = {};

    let timeout;

    beforeEach(inject(function (_$controller_,DataService,$timeout) {
        $scope = {
            $on(request,toDoFunction){}
        };
        $any = {};
        dataService = DataService;
        timeout = $timeout;
        _$controller_('aaiSubscriberController', {
            $scope: $scope,
            COMPONENT: mockCOMPONENT,
            FIELD: mockFIELD,
            PARAMETER: $any,
            DataService: DataService,
            PropertyService: mockPropertyService,
            $http: mockHttp,
            $timeout: timeout,
            $location: mockLocation,
            $log: mockedLog,
            $route: mockRoute,
            $uibModal: mockUibModal,
            VIDCONFIGURATION: mockVIDCONFIGURATION,
            UtilityService: mockUtilityService,
            vidService: mockVidService,
            AaiService: mockAaiService,
            MsoService: $any,
            OwningEntityService: mockOwningEntityService,
            AsdcService: mockAsdcService,
            featureFlags: mockFeatureFlags,
            $q: mockQ,
            _: mock_
        });
    }));

    test('Verify showVnfDetails calls proper broadcast methots with proper parameters', () => {
        // given
        const broadcast = jestMock.fn();
        $scope.$broadcast = broadcast;

        aaiResult = [[['test']]];

        // when
        $scope.showVnfDetails('testVNF');

        // then
        expect(broadcast).toHaveBeenCalledWith(mockCOMPONENT.SHOW_COMPONENT_DETAILS, { componentId: mockCOMPONENT.VNF,callbackFunction: expect.any(Function) } );
    });

    test('Verify getSubs will call fetchSubs and fetchServices and gets gets customer list from AaiService on success', () => {
        // given
        mockAaiService.getSubList = (successFunction,failFunction) => {
            successFunction(['testCustomer1', 'testCustomer2']);
        };
        mockAaiService.getServices2 = (successFunction,failFunction) => {
            successFunction('testListId');
        };

        // when
        $scope.getSubs();

        // then
        expect( $scope.customerList ).toContain('testCustomer1','testCustomer2');
        expect( dataService.getServiceIdList() ).toEqual('testListId');
    });

    test('Verify getSubs will call fetchSubs and fetchServices and return error message from AaiService on fail', () => {
        // given
        mockAaiService.getSubList = (successFunction,failFunction) => {
            failFunction({status: 404, data: 'getSubListTestErrorMessage'} );
        };
        mockAaiService.getServices2 = (successFunction,failFunction) => {
            failFunction({status: 404, data: 'getServices02TestErrorMessage'} );
        };

        // when
        $scope.getSubs();

        // then
        expect( $scope.errorDetails ).toEqual('getServices02TestErrorMessage');
    });

    test('Verify refreshServiceTypes will call getServiceTypesList and gets service type list from AaiService, with proper customerID ', () => {
        // given
        dataService.setGlobalCustomerId('testCustomerID');
        dataService.setServiceIdList(['testServiceId1','testServiceId2']);

        mockAaiService.getSubscriptionServiceTypeList = (customerId, successFunction,failFunction) => {
            if (customerId === 'testCustomerID'){
                successFunction(['testServiceType1', 'testServiceType2']);
            }
        };

        // when
        $scope.refreshServiceTypes('testCustomerID');

        // then
        expect( $scope.serviceTypeList ).toContain('testServiceType1','testServiceType2');
    });

    test('Verify refreshServiceTypes will call getServiceTypesList and return error message with wrong customerID ', () => {
        // given
        mockAaiService.getSubscriptionServiceTypeList = (customerId, successFunction,failFunction) => {
            if (customerId === 'testWrongCustomerID'){
                failFunction( {status: 404, data: 'testErrorMessage'} );
            }
        };

        // when
        $scope.refreshServiceTypes('testWrongCustomerID');

        // then
        expect( $scope.errorDetails ).toEqual('testErrorMessage');
    });

    test('Verify refreshServiceTypes will call getServiceTypesList and calls alert with no customerID ', () => {
        // given
        alert = jestMock.fn();

        // when
        $scope.refreshServiceTypes('');

        // then
        expect( alert ).toHaveBeenCalledWith(mockFIELD.ERROR.SELECT);
    });

    test('Verify getAaiServiceModels will set correct location ', () => {
        // given
        mockLocation.path = jestMock.fn();

        // when
        $scope.getAaiServiceModels('testServiceType','testSubName');

        // then
        expect(mockLocation.path).toHaveBeenCalledWith(mockCOMPONENT.CREATE_INSTANCE_PATH);
    });

    test('Verify getAaiServiceModels wont set correct location if service type is empty', () => {
        // given
        mockLocation.path = jestMock.fn();

        // when
        $scope.getAaiServiceModels('','testSubName');

        // then
        expect(mockLocation.path).not.toHaveBeenCalled();
    });

    test('Verify getAaiServiceModelsList will call AaiService getServiceModelsByServiceType and will set wholeData ', () => {
        // given

        mockAaiService.getServiceModelsByServiceType = (queryId,customerId,serviceType,successFunction,failFunction) => {
            let response = {};
            response.data = {};
            response.data['inventory-response-item'] = [[],[]];
            response.data['inventory-response-item'][0]['inventory-response-items'] = [];
            response.data['inventory-response-item'][0]['service-subscription'] = [];

            let testItem = [];
            testItem['extra-properties'] = [];
            testItem['extra-properties']['extra-property'] = [[],[],[],[],[],[],[]];
            testItem['extra-properties']['extra-property'][6]["property-value"] = 1.546;

            testItem['extra-properties']['extra-property'][4]['property-value'] = 0;

            response.data['inventory-response-item'][0]['service-subscription']['service-type'] = 'testServiceType';
            response.data['inventory-response-item'][0]['inventory-response-items']['inventory-response-item'] = testItem;


            successFunction(response);
        };

        mock_.reduce = (service,iterateeFunction,accumulatorFunction) => {
            return iterateeFunction([],service);
        };
        mock_.forEach = (service,iterateeFunction) => {
            iterateeFunction(service);
        };
        mock_.maxBy = (item,maxFunction) => {
            return maxFunction( item[0][0] )
        };

        dataService.setServiceIdList(['testService1','testService2','testService3','testService4']);
        dataService.setSubscribers([{subscriberName:'testSubscriber1'},{subscriberName:'testSubscriber2'},{subscriberName:'testSubscriber3'},{subscriberName:'testSubscriber4'}]);
        dataService.setGlobalCustomerId(2);
        dataService.setSubscriberName('testSubscriber1');

        // when
        $scope.getAaiServiceModelsList();

        // then
        expect($scope.services[0]).toEqual(1.546);
        expect($scope.serviceType).toEqual('testServiceType');
    });

    test('Verify getAaiServiceModelsList will call AaiService getServiceModelsByServiceType and will return error data on fail ', () => {
        // given
        dataService.setServiceIdList([['testServiceId1','testServiceId2']]);
        dataService.setSubscribers(['testSubscriber1,testSubscriber2']);

        mockAaiService.getServiceModelsByServiceType = (queryId,customerId,serviceType,successFunction,failFunction) => {
            failFunction( {status: 404, data: 'testErrorMessage'})
        };

        // when
        $scope.getAaiServiceModelsList();

        // then
        expect($scope.errorDetails).toEqual('testErrorMessage');
    });

    test('Verify getAaiServiceModelsList will call AaiService getServiceModelsByServiceType and will return error data if respose data is empty ', () => {
        // given
        dataService.setServiceIdList([['testServiceId1','testServiceId2']]);
        dataService.setSubscribers(['testSubscriber1,testSubscriber2']);

        mockAaiService.getServiceModelsByServiceType = (queryId,customerId,serviceType,successFunction,failFunction) => {
            let response = {};
            response.data = {};
            response.data['inventory-response-item'] = [];
            successFunction(response);
        };

        // when
        $scope.getAaiServiceModelsList();

        // then
        expect($scope.status).toEqual('Failed to get service models from SDC.');
    });

    test('Verify deployService will call http get method to rest model service', () => {
        // given
        mockedLog.error = jestMock.fn();
        mockAsdcService.isMacro = (item) => { return true };
        mockAsdcService.shouldTakeTheAsyncInstantiationFlow = (serviceModel) => {return 'testModel'};
        mockUtilityService.convertModel = (serviceModel)=>{return serviceModel};
        $scope.$broadcast = (broadcastType, broadcastObject) => {broadcastObject.callbackFunction(
            {
                isSuccessful:true,
                control:
                    [
                        {id:"subscriberName",value:"testSubscriber"},
                        {id:"serviceType",value:"testService"}
                        ],
                instanceId:"testInstance"
            })};

        let service = {
            "service-instance":{
                "model-version-id": 101
            }
        };

        $scope.refreshSubs = jestMock.fn();

        let mockedGetPromise = Promise.resolve({data: {service: {name: 'testServiceName' }}});
        mockHttp.get = () => mockedGetPromise;

        // when
        $scope.deployService(service,true);
    });

    test('Verify deployService will log error if get fails ', () => {
        // given

        let mockedGetPromise = Promise.reject({code: 404});
        mockHttp.get = () => mockedGetPromise;

        let service = {
            "service-instance":{
                "model-version-id": 101
            }
        };

        // when
        $scope.deployService(service,false);
    });

    test('Verify refreshSubs fetches Subs and Services', () => {
        // given
        $scope.fetchSubs = jestMock.fn();
        $scope.fetchServices = jestMock.fn();
        $scope.init = jestMock.fn();

        mockFIELD.PROMPT.REFRESH_SUB_LIST = 'testRefreshMock';

        // when
        $scope.refreshSubs();

        // then
        expect($scope.init).toHaveBeenCalled();
        expect($scope.fetchSubs).toHaveBeenCalledWith(mockFIELD.PROMPT.REFRESH_SUB_LIST);
        expect($scope.fetchServices).toHaveBeenCalled();

    });

    test('Verify loadOwningEntity gets owning entity properties', () => {
        // given
        mockOwningEntityService.getOwningEntityProperties = (callBack) => {
          callBack({owningEntity:'testOwner',project:'testProject'});
        };

        // when
        $scope.loadOwningEntity();

        // then
        expect($scope.owningEntities).toEqual('testOwner');
        expect($scope.projects).toEqual('testProject');
    });

    test('Verify getPermitted returns items permission', () => {
        // given
        mockFIELD.ID.IS_PERMITTED = 'testPermission';

        // when
        expect(
            $scope.getPermitted({})
        ).toEqual(undefined);

        expect(
            $scope.getPermitted({isPermitted:true})
        ).toEqual(true);

        expect(
            $scope.getPermitted({isPermitted:false})
        ).toEqual(undefined);

        expect(
            $scope.getPermitted({isPermitted:false,testPermission:true})
        ).toEqual(true);

        expect(
            $scope.getPermitted({testPermission:false,testPermission:false})
        ).toEqual(false);

        expect(
            $scope.getPermitted({isPermitted:true,testPermission:false})
        ).toEqual(true);
    });

    test('Verify getSubDetails calls to aaiService for service instance', () => {
        // given
        let aaiPromise = Promise.resolve(
            {
                displayData:[
                    {globalCustomerId:"testCustomerId01",subscriberName:"testCustomer1"},
                    {globalCustomerId:"testCustomerId02",subscriberName:"testCustomer2"},
                ]
            });

        mockLocation.url = () => {return ""};

        mockAaiService.searchServiceInstances = (query)=>aaiPromise;

        // when
        $scope.getSubDetails();
    });

    test('Verify getSubDetails catches bad response', () => {
        // given
        let aaiPromise = Promise.reject(
            {data:'testError',status:404});

        mockLocation.url = () => {return ""};

        mockAaiService.searchServiceInstances = (query)=>aaiPromise;

        // when
        $scope.getSubDetails();
    });

    test('Verify getComponentList returns list of components if query is correct', () => {
        // given
        mockLocation.search = () => {
            return {
                subscriberId: 'testSubscriberID',
                serviceType: 'testService',
                serviceInstanceId: "testServiceInstanceID",
                subscriberName: "testSubscriber",
                aaiModelVersionId: "testModelVersion"
            }
        };

        mockVidService.getModel = () => {
            return {
                service:{
                    uuid: "testModelVersion",
                },
            }
        };

        mockUtilityService.hasContents = (content) => {
            if (content==="testModelVersion") {
                return true;
            }
            return false;
        };

        mockQ.resolve = (item) => {return Promise.resolve("testModelVersion")};

        $scope.prepareScopeWithModel = () => {return Promise.resolve()};

        mockAaiService.getVlansByNetworksMapping = (globalCustomerId, serviceType, serviceInstanceId, modelServiceUuid) => {
            return Promise.resolve({serviceNetworks:true});
        };

        $scope.service ={
            model:{
                service:{
                    uuid: 'testModelServiceUuid'
                }
            }
        };

        mockedLog.debug = () => {};
        mockUtilityService.isObjectEmpty = () => {
            return false;
        };

        mockFIELD.ID.INVENTORY_RESPONSE_ITEM = "testResponseItems";
        mockFIELD.ID.SERVICE_SUBSCRIPTION = 0;
        mockFIELD.ID.SERVICE_INSTANCES = 0;
        mockFIELD.ID.SERVICE_INSTANCE = 0;
        mockFIELD.ID.SERVICE_INSTANCE_ID = 'testServiceInstanceID';
        mockFIELD.STATUS.ASSIGNED = 'teststatus';

        mockAaiService.runNamedQuery = (namedQueryId,globalCustomerId,serviceType,serviceInstanceId,successFunction,failureFunction) => {
            successFunction({
                data:{
                    testResponseItems:[
                        "testItem1",
                        "testItem2",
                        "testItem3",
                    ]
                },
            });
            return Promise.resolve("testComponentList");
        };

        mockAaiService.getPortMirroringData = (portMirroringConfigurationIds) => {
            return Promise.resolve({data:[8080,9090]});
        };

        mockAaiService.getPortMirroringSourcePorts = (portMirroringConfigurationIds) => {
          return Promise.resolve({data:[8888,9999]})
        };

        mockAaiService.getSubscriberName = (customerId, successFunction) => {
            successFunction({subscriberName:"testSubscriber1",serviceSubscriptions:[[
                    [[[{'testServiceInstanceID':'testServiceInstanceID','orchestration-status':'testStatus'},{'testServiceInstanceID':'','orchestration-status':''}]]],
                    [[[{'testServiceInstanceID':'','orchestration-status':''}]]]
                ]],
            });
        };

        mockAaiService.getSubscriberNameAndServiceInstanceInfo = (customerId, servicInstanceId, servicIdentifierType, successFunction) => {
            successFunction({"results": [{"customer": {	"global-customer-id": "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb",
                        "subscriber-name": "Mobility","subscriber-type": "INFRA","resource-version": "1602518417955",
                        "related-nodes": [	{"service-subscription": {"service-type": "VPMS","resource-version": "1629183620246",
                                "related-nodes": [{"service-instance": {"service-instance-id": "5d942bc7-3acf-4e35-836a-393619ebde66",
                                        "service-instance-name": "dpa2actsf5001v_Port_Mirroring_dpa2a_SVC","service-type": "PORT-MIRROR",
                                        "service-role": "VPROBE",	"environment-context": "General_Revenue-Bearing","workload-context": "Production",
                                        "model-invariant-id": "0757d856-a9c6-450d-b494-e1c0a4aab76f","model-version-id": "a9088517-efe8-4bed-9c54-534462cb08c2",
                                        "resource-version": "1615330529236","selflink": "SOME_SELF_LINK","orchestration-status": "Active"}}]}}]}}]});
        };

        mock_.map = (serviceNetworkVlans, networkId) => {
            return ["aaiNetworkId1","aaiNetworkId2"];
        };

        // when
        return $scope.getComponentList('','').
        then(components =>{
            expect(components).toEqual("testComponentList")
        });

    });

    test('Verify handleServerError sets proper  $scope.error and $scope.status', () => {
        // given
        mockUtilityService.getHttpErrorMessage = (response) => {return response.statusText};
        mockFIELD.ERROR.SYSTEM_ERROR = "testSystemError";
        mockFIELD.STATUS.ERROR = "testStatusError";

        // when
        $scope.handleServerError({statusText:'testStatusError'},'');

        // then
        expect($scope.error).toEqual("testSystemError (testStatusError)");
        expect($scope.status).toEqual("testStatusError");
    });

    test('Verify showContentError sets proper $scope.error and $scope.status if UtilityService has that content', () => {
        // given
        mockFIELD.STATUS.ERROR = "testStatusError";

        mockUtilityService.hasContents = (content) => {
          return content === 'testContentError';

        };

        // when
        $scope.showContentError('testContentError');

        // then
        expect($scope.error).toEqual("System failure (testContentError)");
        expect($scope.status).toEqual("testStatusError");
    });

    test('Verify showContentError sets proper $scope.error and $scope.status if UtilityService has not that content', () => {
        // given
        mockFIELD.ERROR.SYSTEM_ERROR = "testSystemError";
        mockFIELD.STATUS.ERROR = "testStatusError";

        mockUtilityService.hasContents = (content) => {
            return false;
        };

        // when
        $scope.showContentError('testContentError');

        // then
        expect($scope.error).toEqual("testSystemError");
        expect($scope.status).toEqual("testStatusError");
    });

    test('Verify handleInitialResponse shows error for response codes other then 200,201,202', ()  => {
        // given
        let response = {
            data:{
                status:404,
            }
        };

        mockFIELD.ERROR.MSO = "testSystemError";
        mockFIELD.ERROR.AAI_FETCHING_CUST_DATA = "testStatusError:";

        $scope.showError = jestMock.fn();

        // when
        $scope.handleInitialResponse(response);

        // then
        expect($scope.showError).toHaveBeenCalledWith("testSystemError");
        expect($scope.status).toEqual("testStatusError:404");
    });

    test('Verify handleInitialResponse updates customer list with response code 202', ()  => {
        // given
        let customer ={
            'globalCustomerId':'testCustomerID',
            "subscriberName":'testSubscriber',
            "isPermitted":false
        };
        let response = {
            data:{
                status:202,
                customer:[customer],
            }
        };

        mockFIELD.ID.GLOBAL_CUSTOMER_ID = 'globalCustomerId';
        mockFIELD.ID.SUBNAME = 'subscriberName';
        mockFIELD.ID.IS_PERMITTED = 'isPermitted';

        // when
        $scope.handleInitialResponse(response);

        // then
        expect($scope.customerList).toContainEqual(customer);
    });

    test('Verify handleInitialResponse calls showContentError with wrong response ', ()  => {
        // given
        $scope.showContentError = jestMock.fn();

        // when
        $scope.handleInitialResponse(null);

        // then
        expect($scope.showContentError).toHaveBeenCalledWith(expect.objectContaining({message:"Cannot read property 'data' of null"}));
    });

    test('Verify isConfigurationDataAvailiable will return proper response', ()  => {
        // given
        mockedLog.debug = jestMock.fn();
        // when
        expect( $scope.isConfigurationDataAvailiable({configData:{}}) ).toEqual(true);
        expect( $scope.isConfigurationDataAvailiable({configData:{errorDescription:"testerror"}}) ).toEqual(false);
        expect( $scope.isConfigurationDataAvailiable({}) ).toEqual(undefined);

    });

    test('Verify isActivateDeactivateEnabled will return proper response', ()  => {
        // given
        mockedLog.debug = jestMock.fn();

        $scope.serviceOrchestrationStatus = "active";
        mockCOMPONENT.ACTIVATE_SERVICE_STATUSES = ["active","up"];

        // when
        expect( $scope.isActivateDeactivateEnabled("deactivate")).toEqual(true);
        expect( $scope.isActivateDeactivateEnabled("activate")).toEqual(true);

        $scope.serviceOrchestrationStatus = "down";
        mockCOMPONENT.ACTIVATE_SERVICE_STATUSES = ["active","up"];

        expect( $scope.isActivateDeactivateEnabled("deactivate")).toEqual(false);
        expect( $scope.isActivateDeactivateEnabled("activate")).toEqual(false);

        $scope.serviceOrchestrationStatus = null;

        expect( $scope.isActivateDeactivateEnabled(null)).toEqual(false);

    });

    test('Verify isShowVerifyService will return proper response base on feature flag', ()  => {
        // given
        mockCOMPONENT.FEATURE_FLAGS.FLAG_SHOW_VERIFY_SERVICE = 'showVerifyService';
        mockFeatureFlags.isOn = (flag) => {
            if (flag === 'showVerifyService'){return true};
        };

        // when
        expect( $scope.isShowVerifyService()).toEqual(true);
    });

    test('Verify isEnableVerifyService will return false if is not ALaCarte', ()  => {
        // given
        dataService.setALaCarte(false);

        // when
        expect( $scope.isEnableVerifyService()).toEqual(false);
    });

    test('Verify isEnableVerifyService will return verifyButtonEnabled if is ALaCarte', ()  => {
        // given
        dataService.setALaCarte(true);

        // when
        $scope.verifyButtonEnabled = true;
        expect( $scope.isEnableVerifyService()).toEqual(true);

        $scope.verifyButtonEnabled = false;
        expect( $scope.isEnableVerifyService()).toEqual(false);
    });

    test('Verify activateVerifyService will post POMBA verification', ()  => {
        // given
        mockCOMPONENT.VERIFY_SERVICE_URL = "/testURL";

        mockAaiService.postPOMBAverificationRequest = jestMock.fn();

        $scope.serviceInstanceId = "testInstanceID";
        $scope.service = {model:{service:{}},instance:{}};
        $scope.service.model.service.uuid = "testUuid";
        $scope.service.model.service.invariantUuid = "testInvariantUuid";
        $scope.globalCustomerId = "testCustomerId";
        $scope.service.instance.serviceType = "testServiceType";

        // when
        $scope.activateVerifyService();

        // then
        expect(mockAaiService.postPOMBAverificationRequest).toHaveBeenCalledWith(
            "/testURL",
            expect.objectContaining({'serviceInstanceList':[expect.any(Object)]}),
            expect.objectContaining({'headers':expect.any(Object)}));
    });

    test('Verify isShowAssignmentsEnabled will return proper response determine by feature flag', ()  => {
        // given
        mockCOMPONENT.FEATURE_FLAGS.FLAG_SHOW_ASSIGNMENTS = "showAssignment";

        mockFeatureFlags.isOn = (flag) => {
            if (flag === 'showAssignment'){return true};
        };

        // when
        $scope.serviceOrchestrationStatus = "assigned";
        expect( $scope.isShowAssignmentsEnabled() ).toEqual(true);

        $scope.serviceOrchestrationStatus = "notAssigned";
        expect( $scope.isShowAssignmentsEnabled() ).toEqual(false);

        $scope.serviceOrchestrationStatus = null;
        expect( $scope.isShowAssignmentsEnabled() ).toEqual(false);
    });

    test('Verify isActivateFabricConfiguration will return proper response determine by feature flag', ()  => {
        // given
        mockCOMPONENT.FEATURE_FLAGS.FLAG_FABRIC_CONFIGURATION_ASSIGNMENTS = "fabricConfigurationAssignment";
        $scope.hasFabricConfigurations = true;

        mockFeatureFlags.isOn = (flag) => {
            if (flag === 'fabricConfigurationAssignment'){return true};
        };

        // when
        $scope.serviceOrchestrationStatus = "assigned";
        expect( $scope.isActivateFabricConfiguration() ).toEqual(true);

        $scope.serviceOrchestrationStatus = "notAssigned";
        expect( $scope.isActivateFabricConfiguration() ).toEqual(false);

        $scope.serviceOrchestrationStatus = null;
        expect( $scope.isActivateFabricConfiguration() ).toEqual(false);
    });

    test('Verify isResumeShown will return proper response determine by feature flag with disabled ActivateDeactivate', ()  => {
        // given
        $scope.serviceOrchestrationStatus = "assigned";
        $scope.isActivateDeactivateEnabled = () => {return false};

        // when
        expect( $scope.isResumeShown("assigned") ).toEqual(true);
        expect( $scope.isResumeShown("unAssigned") ).toEqual(false);

    });

    test('Verify isResumeShown will return proper response determine by feature flag with enable ActivateDeactivate', ()  => {
        // given
        $scope.serviceOrchestrationStatus = "assigned";
        $scope.isActivateDeactivateEnabled = () => {return true};

        // when
        expect( $scope.isResumeShown("assigned") ).toEqual(false);
        expect( $scope.isResumeShown("unAssigned") ).toEqual(false);

    });

    test('Verify close will call time out cancel and hides pop up window if timer is defined', ()  => {
        // given
        $scope.timer = 1000;
        $scope.isPopupVisible = true;

        timeout.cancel = jestMock.fn();

        // when
        $scope.close();

        // then
        expect(timeout.cancel).toHaveBeenCalledWith(1000);
        expect($scope.isPopupVisible).toEqual(false);
    });

    test('Verify close will hide pop up window if timer is undefined', ()  => {
        // given
        $scope.timer = undefined;
        $scope.isPopupVisible = true;

        // when
        $scope.close();

        // then
        expect($scope.isPopupVisible).toEqual(false);
    });

    test('Verify reloadRoute will call reload on rout', ()  => {
        // given
        mockRoute.reload = jestMock.fn();

        // when
        $scope.reloadRoute();

        // then
        expect(mockRoute.reload).toHaveBeenCalled();
    });

    test('Verify prevPage will decrease currentPage', ()  => {
        // given
        $scope.currentPage = 5;

        // when
        $scope.prevPage();

        // then
        expect($scope.currentPage).toEqual(4);
    });

    test('Verify showAssignmentsSDNC will return proper response base on VIDCONFIGURATION', ()  => {
        // given
        $scope.service = {};
        $scope.service.instance = {};
        $scope.service.instance.id = "testServiceInstanceId";

        mockVIDCONFIGURATION.SDNC_SHOW_ASSIGNMENTS_URL = "test/ulr/to/<SERVICE_INSTANCE_ID>";

        // when
        expect( $scope.showAssignmentsSDNC() ).toEqual("test/ulr/to/testServiceInstanceId");
    });

    test('Verify showAssignmentsSDNC will return null if service instance dos not exist or is null', ()  => {
        // given
        $scope.service = {};

        // when
        expect( $scope.showAssignmentsSDNC() ).toEqual(null);

        $scope.service.instance = null;
        expect( $scope.showAssignmentsSDNC() ).toEqual(null);
    });

    test('Verify activateFabricConfigurationMSO with logged in user, will call uibModal open that will return response ', ()  => {
        // given
        let resopnse = {};

        mockCOMPONENT.MSO_ACTIVATE_FABRIC_CONFIGURATION_REQ = "testMsoActivateType";

        $scope.service = {};
        $scope.service.model = {};
        $scope.service = {};
        $scope.serviceInstanceId= "testServiceInstanceId";

        dataService.setLoggedInUserId("testUserId");

        mockUibModal.open = (testResponse) => {
            resopnse = testResponse.resolve;
        };

        // when
        $scope.activateFabricConfigurationMSO();

        // then
        expect( resopnse.msoType() ).toEqual("testMsoActivateType");
        expect( resopnse.requestParams().serviceInstanceId ).toEqual("testServiceInstanceId");
        expect( resopnse.requestParams().userId ).toEqual("testUserId");
        expect( resopnse.configuration() ).toEqual(undefined);
    });

    test('Verify activateFabricConfigurationMSO without logged in user will first get user id from AaiService , will call uibModal open that will return response ', ()  => {
        // given
        let resopnse = {};

        mockCOMPONENT.MSO_ACTIVATE_FABRIC_CONFIGURATION_REQ = "testMsoActivateType";

        $scope.service = {};
        $scope.service.model = {};
        $scope.service = {};
        $scope.serviceInstanceId= "testServiceInstanceId";


        mockAaiService.getLoggedInUserID = (onSuccess) => {
            onSuccess({data:"testAaiUserId"});
        };

        mockUibModal.open = (testResponse) => {
            resopnse = testResponse.resolve;
        };

        // when
        $scope.activateFabricConfigurationMSO();

        // then
        expect( resopnse.msoType() ).toEqual("testMsoActivateType");
        expect( resopnse.requestParams().serviceInstanceId ).toEqual("testServiceInstanceId");
        expect( resopnse.requestParams().userId ).toEqual("testAaiUserId");
        expect( resopnse.configuration() ).toEqual(undefined);
    });

    test('Verify activateMSOInstance with logged in user, will get aicZone from AaiService and call uibModal open that will return response ', ()  => {
        // given
        let resopnse = {};

        mockCOMPONENT.MSO_ACTIVATE_SERVICE_REQ = "testMsoActivateType";

        $scope.service = {};
        $scope.service.model = {};
        $scope.service.instance = {};

        dataService.setLoggedInUserId("testUserId");

        mockAaiService.getAicZoneForPNF = (globalCustomerId,serviceType,serviceInstanceId,getZoneFunction) => {
            getZoneFunction("testAicZone");
        };

        mockUibModal.open = (testResponse) => {
            resopnse = testResponse.resolve;
        };

        // when
        $scope.activateMSOInstance();

        // then
        expect( resopnse.msoType() ).toEqual("testMsoActivateType");
        expect( resopnse.requestParams().aicZone ).toEqual("testAicZone");
        expect( resopnse.requestParams().userId ).toEqual("testUserId");
        expect( resopnse.configuration() ).toEqual(undefined);
    });

    test('Verify activateMSOInstance without logged in user will first get user id from AaiService , will call uibModal open that will return response ', ()  => {
        // given
        let resopnse = {};

        mockCOMPONENT.MSO_ACTIVATE_SERVICE_REQ = "testMsoActivateType";

        $scope.service = {};
        $scope.service.model = {};
        $scope.service.instance = {};


        mockAaiService.getAicZoneForPNF = (globalCustomerId,serviceType,serviceInstanceId,getZoneFunction) => {
            getZoneFunction("testAicZone");
        };

        mockAaiService.getLoggedInUserID = (onSuccess) => {
            onSuccess({data:"testAaiUserId"});
        };

        mockUibModal.open = (testResponse) => {
            resopnse = testResponse.resolve;
        };

        // when
        $scope.activateMSOInstance();

        // then
        expect( resopnse.msoType() ).toEqual("testMsoActivateType");
        expect( resopnse.requestParams().aicZone ).toEqual("testAicZone");
        expect( resopnse.requestParams().userId ).toEqual("testAaiUserId");
        expect( resopnse.configuration() ).toEqual(undefined);
    });

    test('Verify deactivateMSOInstance will get call uibModal open that will return response ', ()  => {
        // given
        let resopnse = {};

        mockCOMPONENT.MSO_DEACTIVATE_SERVICE_REQ = "testMsoDeactivateType";

        $scope.service = {};
        $scope.service.model = {};
        $scope.service.instance = {};


        mockAaiService.getAicZoneForPNF = (globalCustomerId,serviceType,serviceInstanceId,getZoneFunction) => {
            getZoneFunction("testAicZone");
        };

        mockAaiService.getLoggedInUserID = (onSuccess) => {
            onSuccess({data:"testAaiUserId"});
        };

        mockUibModal.open = (testResponse) => {
            resopnse = testResponse.resolve;
        };

        // when
        $scope.deactivateMSOInstance();

        // then
        expect( resopnse.msoType() ).toEqual("testMsoDeactivateType");
        expect( resopnse.requestParams().aicZone ).toEqual("testAicZone");
        expect( resopnse.requestParams().userId ).toEqual("testAaiUserId");
        expect( resopnse.configuration() ).toEqual(undefined);
    });

    test('Verify deleteConfiguration will get call uibModal open that will return response ', ()  => {
        // given
        let resopnse = {};

        serviceObject = {
            model:{
                service:{
                    invariantUuid:"testInvariantUuid",
                    uuid:"testUuid",
                    name:"testService",
                    version:"testVersion",
                }},
            instance:{
                serviceInstanceId:"testServiceInstanceId",
            }
        };

        configuration = {
            modelInvariantId:"testModelInvariantId",
            modelVersionId:"testModelVersionId",
            modelCustomizationId:"testModelCustomizationId",
            nodeId:"testNodeId",
            DELETE:"testDELETE",
        };

        mockCOMPONENT.MSO_DELETE_CONFIGURATION_REQ = "testMsoDeleteType";

        mockAaiService.getLoggedInUserID = (successFunction) => {
            successFunction( {data:"testLoggedInUserId"} );
        };

        mockUibModal.open = (testResponse) => {
            resopnse = testResponse.resolve;
        };

        // when
        $scope.deleteConfiguration(serviceObject, configuration);

        // then
        expect( resopnse.msoType() ).toEqual("testMsoDeleteType");
        expect( resopnse.requestParams().serviceModel.modelInvariantId ).toEqual("testInvariantUuid");
        expect( resopnse.requestParams().serviceModel.modelVersionId ).toEqual("testUuid");
        expect( resopnse.requestParams().serviceModel.modelName ).toEqual("testService");
        expect( resopnse.requestParams().serviceModel.modelVersion ).toEqual("testVersion");
        expect( resopnse.requestParams().serviceInstanceId ).toEqual("testServiceInstanceId");
        expect( resopnse.requestParams().configurationModel.modelInvariantId ).toEqual("testModelInvariantId");
        expect( resopnse.requestParams().configurationModel.modelVersionId ).toEqual("testModelVersionId");
        expect( resopnse.requestParams().configurationModel.modelCustomizationId ).toEqual("testModelCustomizationId");
        expect( resopnse.requestParams().configurationId ).toEqual("testNodeId");
        expect( resopnse.requestParams().configStatus ).toEqual("testDELETE");
        expect( resopnse.requestParams().userId ).toEqual("testLoggedInUserId");
    });

    test('Verify toggleConfigurationStatus will get call uibModal open that will return response ', ()  => {
        // given
        let resopnse = {};

        serviceObject = {
            model:{
                service:{
                    invariantUuid:"testInvariantUuid",
                    uuid:"testUuid",
                    name:"testService",
                    version:"testVersion",
                }},
            instance:{
                serviceInstanceId:"testServiceInstanceId",
            }
        };

        configuration = {
            modelInvariantId:"testModelInvariantId",
            modelVersionId:"testModelVersionId",
            modelCustomizationId:"testModelCustomizationId",
            nodeId:"testNodeId",
            nodeStatus:"testNodeStatus",
        };

        mockAaiService.getLoggedInUserID = (successFunction) => {
            successFunction( {data:"testLoggedInUserId"} );
        };

        mockUibModal.open = (testResponse) => {
            resopnse = testResponse.resolve;
        };

        mockCOMPONENT.MSO_CHANGE_CONFIG_STATUS_REQ = "testMsoChangeConfig";

        // when
        $scope.toggleConfigurationStatus(serviceObject, configuration);

        // then
        expect( resopnse.msoType() ).toEqual("testMsoChangeConfig");
        expect( resopnse.requestParams().serviceModel.modelInvariantId ).toEqual("testInvariantUuid");
        expect( resopnse.requestParams().serviceModel.modelVersionId ).toEqual("testUuid");
        expect( resopnse.requestParams().serviceModel.modelName ).toEqual("testService");
        expect( resopnse.requestParams().serviceModel.modelVersion ).toEqual("testVersion");
        expect( resopnse.requestParams().serviceInstanceId ).toEqual("testServiceInstanceId");
        expect( resopnse.requestParams().configurationModel.modelInvariantId ).toEqual("testModelInvariantId");
        expect( resopnse.requestParams().configurationModel.modelVersionId ).toEqual("testModelVersionId");
        expect( resopnse.requestParams().configurationModel.modelCustomizationId ).toEqual("testModelCustomizationId");
        expect( resopnse.requestParams().configurationId ).toEqual("testNodeId");
        expect( resopnse.requestParams().configStatus ).toEqual("testNodeStatus");
        expect( resopnse.requestParams().userId ).toEqual("testLoggedInUserId");
    });

    test('Verify togglePortStatus will get call uibModal open that will return response ', ()  => {
        // given
        let resopnse = {};

        let serviceObject = {
            model:{
                service:{
                    invariantUuid:"testInvariantUuid",
                    uuid:"testUuid",
                    name:"testService",
                    version:"testVersion",
                }},
            instance:{
                serviceInstanceId:"testServiceInstanceId",
            }
        };

        let configuration = {
            modelInvariantId:"testModelInvariantId",
            modelVersionId:"testModelVersionId",
            modelCustomizationId:"testModelCustomizationId",
            nodeId:"testNodeId",
        };

        let port = {
            portId:"testPort",
            portStatus:"open",
        };

        mockAaiService.getLoggedInUserID = (successFunction) => {
            successFunction( {data:"testLoggedInUserId"} );
        };

        mockUibModal.open = (testResponse) => {
            resopnse = testResponse.resolve;
        };

        mockCOMPONENT.MSO_CHANGE_PORT_STATUS_REQ = "testMsoPortStatus";

        // when
        $scope.togglePortStatus(serviceObject, configuration,port);

        // then
        expect( resopnse.msoType() ).toEqual("testMsoPortStatus");
        expect( resopnse.requestParams().serviceModel.modelInvariantId ).toEqual("testInvariantUuid");
        expect( resopnse.requestParams().serviceModel.modelVersionId ).toEqual("testUuid");
        expect( resopnse.requestParams().serviceModel.modelName ).toEqual("testService");
        expect( resopnse.requestParams().serviceModel.modelVersion ).toEqual("testVersion");
        expect( resopnse.requestParams().serviceInstanceId ).toEqual("testServiceInstanceId");
        expect( resopnse.requestParams().configurationModel.modelInvariantId ).toEqual("testModelInvariantId");
        expect( resopnse.requestParams().configurationModel.modelVersionId ).toEqual("testModelVersionId");
        expect( resopnse.requestParams().configurationModel.modelCustomizationId ).toEqual("testModelCustomizationId");
        expect( resopnse.requestParams().configurationId ).toEqual("testNodeId");
        expect( resopnse.requestParams().userId ).toEqual("testLoggedInUserId");
        expect( resopnse.requestParams().portId ).toEqual("testPort");
        expect( resopnse.requestParams().portStatus ).toEqual("open");
    });


    test('Verify getServiceInstancesSearchResults will get global customer Id from AaiService with proper service instance', ()  => {
        // given
        let selectedCustomer = 'testCustomer';
        let selectedInstanceIdentifierType = 'testInstanceIdentifierType';
        let selectedServiceInstance = 'testServiceInstance ';
        let selectedProject = "testProject";
        let selectedOwningEntity = "testOwningEntity";

        let globalCustomerId = 'testCustomerIdResponse';

        mockAaiService.getGlobalCustomerIdByInstanceIdentifier = jestMock.fn((serviceInstance, instanceIdentifierType) => {
            if(serviceInstance===selectedServiceInstance && instanceIdentifierType == selectedInstanceIdentifierType){
                return Promise.resolve(globalCustomerId);
            }
            return Promise.reject();
        });

        mockAaiService.getMultipleValueParamQueryString = ( element, subPath) => {
            return subPath + '/ ' + element;
        };

        mockAaiService.getJoinedQueryString = jestMock.fn();

        mock_.map = (element,id) => {
            return element;
        };

        mockCOMPONENT.PROJECT_SUB_PATH = "test/project/sub";
        mockCOMPONENT.OWNING_ENTITY_SUB_PATH = "test/entity/sub";
        mockCOMPONENT.SELECTED_SUBSCRIBER_SUB_PATH = "test/subscriber/sub";
        mockCOMPONENT.SELECTED_SERVICE_INSTANCE_SUB_PATH = "test/service/instance/sub";
        mockCOMPONENT.SELECTED_SERVICE_SUB_PATH = "text/service/sub";

        mockUtilityService.hasContents = (element) => {
          if (  element ===  selectedCustomer ||
                element === selectedServiceInstance ||
                element === globalCustomerId )
              return true;
        };

        window.location = {};

        // when
        $scope.getServiceInstancesSearchResults(selectedCustomer, selectedInstanceIdentifierType, selectedServiceInstance, selectedProject, selectedOwningEntity);

        // then
        expect(mockAaiService.getGlobalCustomerIdByInstanceIdentifier).toHaveBeenCalledWith(selectedServiceInstance,selectedInstanceIdentifierType);
    });

    test('Verify getServiceInstancesSearchResults will alert error if non of parameters is located in UtilityService', ()  => {
        // given
        let selectedCustomer = 'testCustomer';
        let selectedInstanceIdentifierType = 'testInstanceIdentifierType';
        let selectedServiceInstance = 'testServiceInstance ';
        let selectedProject = "testProject";
        let selectedOwningEntity = "testOwningEntity";

        let globalCustomerId = 'testCustomerIdResponse';

        mockAaiService.getGlobalCustomerIdByInstanceIdentifier = (serviceInstance, instanceIdentifierType) => {
            if(serviceInstance===selectedServiceInstance && instanceIdentifierType == selectedInstanceIdentifierType){
                return Promise.resolve(globalCustomerId);
            }
            return Promise.reject();
        };

        mockAaiService.getMultipleValueParamQueryString = ( element, subPath) => {
            return subPath + '/ ' + element;
        };

        mockAaiService.getJoinedQueryString = (queryArray) => {
            let joinedQuery = "";
            queryArray.forEach((element)=>{
                joinedQuery += element + "//"
            });

            return joinedQuery;
        };

        mock_.map = (element,id) => {
            return element;
        };

        mockCOMPONENT.PROJECT_SUB_PATH = "test/project/sub";
        mockCOMPONENT.OWNING_ENTITY_SUB_PATH = "test/entity/sub";
        mockCOMPONENT.SELECTED_SUBSCRIBER_SUB_PATH = "test/subscriber/sub";
        mockCOMPONENT.SELECTED_SERVICE_INSTANCE_SUB_PATH = "test/service/instance/sub";
        mockCOMPONENT.SELECTED_SERVICE_SUB_PATH = "text/service/sub";

        mockUtilityService.hasContents = (element) => {
                return false;
        };

        alert = jestMock.fn();
        mockFIELD.ERROR.SELECT = "testError";

        window.location = {};

        // when
        $scope.getServiceInstancesSearchResults(selectedCustomer, selectedInstanceIdentifierType, selectedServiceInstance, selectedProject, selectedOwningEntity);

        // then
        expect(alert).toHaveBeenCalledWith("testError");
    });

    test('Verify getServiceInstancesSearchResults will navigate to proper page if selected service instance is not present in UtilityService', ()  => {
        // given
        let selectedCustomer = 'testCustomer';
        let selectedInstanceIdentifierType = 'testInstanceIdentifierType';
        let selectedServiceInstance = 'testServiceInstance ';
        let selectedProject = "testProject";
        let selectedOwningEntity = "testOwningEntity";

        let globalCustomerId = 'testCustomerIdResponse';

        mockAaiService.getGlobalCustomerIdByInstanceIdentifier = (serviceInstance, instanceIdentifierType) => {
            if(serviceInstance===selectedServiceInstance && instanceIdentifierType == selectedInstanceIdentifierType){
                return Promise.resolve(globalCustomerId);
            }
            return Promise.reject();
        };

        mockAaiService.getMultipleValueParamQueryString = ( element, subPath) => {
            return subPath + element;
        };

        mockAaiService.getJoinedQueryString = jestMock.fn();

        mock_.map = (element,id) => {
            return element;
        };

        mockCOMPONENT.PROJECT_SUB_PATH = "test/project/sub/";
        mockCOMPONENT.OWNING_ENTITY_SUB_PATH = "test/entity/sub/";
        mockCOMPONENT.SELECTED_SUBSCRIBER_SUB_PATH = "test/subscriber/sub/";
        mockCOMPONENT.SELECTED_SERVICE_INSTANCE_SUB_PATH = "test/service/instance/sub/";
        mockCOMPONENT.SELECTED_SERVICE_SUB_PATH = "text/service/sub/";


        mockUtilityService.hasContents = (element) => {
            return element === selectedCustomer;
        };

        window.location = {};

        // when
        $scope.getServiceInstancesSearchResults(selectedCustomer, selectedInstanceIdentifierType, selectedServiceInstance, selectedProject, selectedOwningEntity);

        // then
        expect(mockAaiService.getJoinedQueryString).toHaveBeenCalledWith(expect.arrayContaining([
            mockCOMPONENT.PROJECT_SUB_PATH+selectedProject,
            mockCOMPONENT.OWNING_ENTITY_SUB_PATH+selectedOwningEntity,
            mockCOMPONENT.SELECTED_SUBSCRIBER_SUB_PATH+selectedCustomer
        ]));
    });


});
