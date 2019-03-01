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

    test('Verify newSubItem pushes new item in too given scope', () => {
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

    let mockedFIELD = {
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

    let mockedCOMPONENT = {
        SHOW_COMPONENT_DETAILS: 'testComponentDetails',
        VNF: 'testComponentVNF',
        WELCOME_PATH: 'http://test/welcome/',
        CREATE_INSTANCE_PATH: 'testInstancePath',
    };

    let mockedAaiService = {
        getSubscriptionServiceTypeList(customerId,successFunction,failFunction){},
        getServiceModelsByServiceType(queryId,customerId,serviceType,successFunction,failFunction){},
    };

    let mockPropertyService = {
        retrieveMsoMaxPollingIntervalMsec(){return 1000},
        setMsoMaxPollingIntervalMsec(msecs){},
        retrieveMsoMaxPolls(){return 1000},
        setMsoMaxPolls(polls){},
    };

    let mockDataService = {
        customerId: '',
        serviceListId: '',
        subscriberName: '',
        subscribers: '',
        setVnfInstanceId(id){},
        setInventoryItem(item){},
        setGlobalCustomerId(id){this.customerId = id},
        getGlobalCustomerId(){return this.customerId},
        setServiceIdList(id){this.serviceListId=id},
        getServiceIdList(){return this.serviceListId},
        setSubscriberName(name){this.subscriberName=name},
        getSubscriberName(){return this.subscriberName},
        setSubscribers(subscribers){this.subscribers = subscribers},
        getSubscribers(){return this.subscribers},
    };

    let mockLocation = {
        path(path){},
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

    beforeEach(inject(function (_$controller_) {
        $scope = {
            $on(request,toDoFunction){}
        };
        $any = {};

        _$controller_('aaiSubscriberController', {
            $scope: $scope,
            COMPONENT: mockedCOMPONENT,
            FIELD: mockedFIELD,
            PARAMETER: $any,
            DataService: mockDataService,
            PropertyService: mockPropertyService,
            $http: $any,
            $timeout: $any,
            $location: mockLocation,
            $log: $any,
            $route: $any,
            $uibModal: $any,
            VIDCONFIGURATION: $any,
            UtilityService: $any,
            vidService: $any,
            AaiService: mockedAaiService,
            MsoService: $any,
            OwningEntityService: $any,
            AsdcService: $any,
            featureFlags: $any,
            $q: $any,
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
        expect(broadcast).toHaveBeenCalledWith(mockedCOMPONENT.SHOW_COMPONENT_DETAILS, { componentId: mockedCOMPONENT.VNF,callbackFunction: expect.any(Function) } );
    });

    test('Verify getSubs will call fetchSubs and fetchServices and gets gets customer list from AaiService on success', () => {
        // given
        mockedAaiService.getSubList = (successFunction,failFunction) => {
            successFunction(['testCustomer1', 'testCustomer2']);
        };
        mockedAaiService.getServices2 = (successFunction,failFunction) => {
            successFunction('testListId');
        };

        // when
        $scope.getSubs();

        // then
        expect( $scope.customerList ).toContain('testCustomer1','testCustomer2');
        expect( mockDataService.getServiceIdList() ).toEqual('testListId');
    });

    test('Verify getSubs will call fetchSubs and fetchServices and return error message from AaiService on fail', () => {
        // given
        mockedAaiService.getSubList = (successFunction,failFunction) => {
            failFunction({status: 404, data: 'getSubListTestErrorMessage'} );
        };
        mockedAaiService.getServices2 = (successFunction,failFunction) => {
            failFunction({status: 404, data: 'getServices02TestErrorMessage'} );
        };

        // when
        $scope.getSubs();

        // then
        expect( $scope.errorDetails ).toEqual('getServices02TestErrorMessage');
    });

    test('Verify refreshServiceTypes will call getServiceTypesList and gets service type list from AaiService, with proper customerID ', () => {
        // given
        mockedAaiService.getSubscriptionServiceTypeList = (customerId, successFunction,failFunction) => {
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
        mockedAaiService.getSubscriptionServiceTypeList = (customerId, successFunction,failFunction) => {
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
        expect( alert ).toHaveBeenCalledWith(mockedFIELD.ERROR.SELECT);
    });

    test('Verify getAaiServiceModels will set correct location ', () => {
        // given
        mockLocation.path = jestMock.fn();

        // when
        $scope.getAaiServiceModels('testServiceType','testSubName');

        // then
        expect(mockLocation.path).toHaveBeenCalledWith(mockedCOMPONENT.CREATE_INSTANCE_PATH);
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

        mockedAaiService.getServiceModelsByServiceType = (queryId,customerId,serviceType,successFunction,failFunction) => {
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

        mockDataService.setServiceIdList(['testService1','testService2','testService3','testService4']);
        mockDataService.setSubscribers([{subscriberName:'testSubscriber1'},{subscriberName:'testSubscriber2'},{subscriberName:'testSubscriber3'},{subscriberName:'testSubscriber4'}]);
        mockDataService.setGlobalCustomerId(2);
        mockDataService.setSubscriberName('testSubscriber1');

        // when
        $scope.getAaiServiceModelsList();

        // then
        expect($scope.services[0]).toEqual(1.546);
        expect($scope.serviceType).toEqual('testServiceType');
    });

    test('Verify getAaiServiceModelsList will call AaiService getServiceModelsByServiceType and will return error data on fail ', () => {
        // given

        mockedAaiService.getServiceModelsByServiceType = (queryId,customerId,serviceType,successFunction,failFunction) => {
            failFunction( {status: 404, data: 'testErrorMessage'})
        };

        // when
        $scope.getAaiServiceModelsList();

        // then
        expect($scope.errorDetails).toEqual('testErrorMessage');
    });

    test('Verify getAaiServiceModelsList will call AaiService getServiceModelsByServiceType and will return error data if respose data is empty ', () => {
        // given
        mockedAaiService.getServiceModelsByServiceType = (queryId,customerId,serviceType,successFunction,failFunction) => {
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

});