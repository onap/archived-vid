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
    //
    // let mockDataService = {
    //     customerId: '',
    //     serviceListId: '',
    //     serviceName: '',
    //     subscriberName: '',
    //     subscribers: '',
    //     setVnfInstanceId(id){},
    //     setInventoryItem(item){},
    //     setGlobalCustomerId(id){this.customerId = id},
    //     getGlobalCustomerId(){return this.customerId},
    //     setServiceIdList(id){this.serviceListId=id},
    //     getServiceIdList(){return this.serviceListId},
    //     setSubscriberName(name){this.subscriberName=name},
    //     getSubscriberName(){return this.subscriberName},
    //     setSubscribers(subscribers){this.subscribers = subscribers},
    //     getSubscribers(){return this.subscribers},
    //     setServiceName(name){this.serviceName=name},
    //     setModelInfo(type,info){},
    //     setHideServiceFields(){},
    // };

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
    };

    let mockedLog = {};

    beforeEach(inject(function (_$controller_,DataService) {
        $scope = {
            $on(request,toDoFunction){}
        };
        $any = {};
        dataService = DataService;
        _$controller_('aaiSubscriberController', {
            $scope: $scope,
            COMPONENT: mockCOMPONENT,
            FIELD: mockFIELD,
            PARAMETER: $any,
            DataService: DataService,
            PropertyService: mockPropertyService,
            $http: mockHttp,
            $timeout: $any,
            $location: mockLocation,
            $log: mockedLog,
            $route: $any,
            $uibModal: $any,
            VIDCONFIGURATION: $any,
            UtilityService: mockUtilityService,
            vidService: mockVidService,
            AaiService: mockAaiService,
            MsoService: $any,
            OwningEntityService: mockOwningEntityService,
            AsdcService: mockAsdcService,
            featureFlags: $any,
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

        // $scope.refreshSubs = jestMock.fn();
        $scope.refreshSubs = (subscriberId, serviceType, serviceInstanceId) => {
            console.log(subscriberId, serviceType, serviceInstanceId);
            // expect(subscriberId).toEqual('testSubscribern');
            // expect(serviceType).toEqual('testService');
            // expect(serviceInstanceId).toEqual('testInstance');
        };

        let mockedGetPromise = Promise.resolve({data: {service: {name: 'testServiceName' }}});
        mockHttp.get = () => mockedGetPromise;

        // when
        $scope.deployService(service,true);

        // then
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

        // then
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

        // then

    });

    test('Verify getSubDetails catches bad response', () => {
        // given
        let aaiPromise = Promise.reject(
            {data:'testError',status:404});

        mockLocation.url = () => {return ""};

        mockAaiService.searchServiceInstances = (query)=>aaiPromise;

        // when
        $scope.getSubDetails();

        // then

    });

    test('Verify getComponentList returns list of components', () => {
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
        //mockQ.resolve = () => {return Promise.resolve()};

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
        mockAaiService.runNamedQuery = (namedQueryId,globalCustomerId,serviceType,serviceInstanceId,successFunction,failureFunction) => {
                return Promise.resolve("testComponentList");
        };


        // when
        $scope.getComponentList('','').
        then((components)=>{
            expect(components).toEqual("testComponentList")
        });

        // then

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
          if (content === 'testContentError') {
              return true;
          }
          return false;
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

});