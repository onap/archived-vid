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

require('./new-change-management.controller');
const jestMock = require('jest-mock');

describe('Testing workFlows from SO', () => {
  let $notNeeded;
  let $controller;
  let $changeManagementService;
  beforeEach(
      angular.mock.module('app')
  );

  beforeEach(inject(function (_$controller_) {
    $notNeeded = jestMock.fn();
    // mock ChangeManagementService
    $changeManagementService = jestMock.fn();
    $changeManagementService.getAllSDCServices = jestMock.fn(() => Promise.resolve([]));

    // mock q
    $q = jestMock.fn();
    $defer = jestMock.fn();
    $q.defer = jestMock.fn(() => $defer);
    $defer.promise = Promise.resolve({});
    // mock AaiService
    $aaiService = jestMock.fn();
    $aaiService.getLoggedInUserID = jestMock.fn();
    $aaiService.getSubscribers = jestMock.fn();
    $controller = _$controller_('newChangeManagementModalController', {
      $uibModalInstance: $notNeeded,
      $uibModal: $notNeeded,
      $q: $q,
      AaiService: $aaiService,
      changeManagementService: $changeManagementService,
      Upload: $notNeeded,
      $log: $notNeeded,
      _: $notNeeded,
      COMPONENT: $notNeeded,
      VIDCONFIGURATION: $notNeeded,
      DataService: $notNeeded,
      featureFlags: $notNeeded,
      $scope: $notNeeded,
    });
  }));

  test('Verify load workflows from SO will call getSOWorkflow and return only names of workflows', () => {
    // given
    $controller.changeManagement.vnfNames = [{name: 'test1'}, {name: "test2"}];
    let getSOWorkflowsPromiseStub = Promise.resolve({"data": [{"id": "1", "name": "workflow 1"}, {"id": "2", "name": "workflow 2"}]});
    $changeManagementService.getSOWorkflows = () => getSOWorkflowsPromiseStub;
    $controller.workflows = [];
    // when
    return $controller.loadRemoteWorkFlows()
    .then(() => {
           remoteWorkflows = $controller.remoteWorkflows.map(item => item.name);
           expect(remoteWorkflows).toContain('workflow 1');
           expect(remoteWorkflows).toContain('workflow 2');
        }
     );
  });

  test('Verify load workflows will call load from SO and join workflow lists', () => {
    // given
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["workflow 0"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data": {}});
    let getSOWorkflowsPromiseStub = Promise.resolve({"data": [{"id": "1", "name": "workflow 1"}, {"id": "2", "name": "workflow 2"}]});
    let getSOWorkflowsParametersPromiseStub = Promise.resolve({"data":{"parameterDefinitions": []}});

    $controller.changeManagement.vnfNames = [{name: 'test1'}, {name: "test2"}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    $changeManagementService.getSOWorkflowParameter = () =>  getSOWorkflowsParametersPromiseStub;
    // when
    return $controller.loadWorkFlows().then(() => {
      expect($controller.workflows).toContain('workflow 0');
      expect($controller.workflows).toContain('workflow 1');
      expect($controller.workflows).toContain('workflow 2');
    });
  });

  test('Verify load workflows will call load workflows parameters from SO', () => {
    // given
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["workflow 0"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data": {}});
    let getSOWorkflowsPromiseStub = Promise.resolve({"data": [{"id": "1", "name": "workflow 0"}]});
    let getSOWorkflowsParametersPromiseStub = Promise.resolve({"data":{"parameterDefinitions": [
          {"id": 1, "name": "parameter 1", "required": true, "type": "STRING", "pattern": "[0-9]*"},
          {"id": 2, "name": "parameter 2", "required": true, "type": "STRING", "pattern": ".*"},
          {"id": 3, "name": "parameter 3", "required": false, "type": "STRING", "pattern": "[0-9]*"}]}});

    $controller.changeManagement.vnfNames = [{name: 'test1'}, {name: "test2"}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    $changeManagementService.getSOWorkflowParameter = () =>  getSOWorkflowsParametersPromiseStub;
    // when
    return $controller.loadWorkFlows()
    .then(() => {
      expect($controller.remoteWorkflowsParameters).toEqual(new Map([["workflow 0",
        [{"id": 1, "name": "parameter 1", "pattern": "[0-9]*", "required": true, "type": "STRING"},
         {"id": 2, "name": "parameter 2", "pattern": ".*", "required": true, "type": "STRING"},
         {"id": 3, "name": "parameter 3", "pattern": "[0-9]*", "required": false, "type": "STRING"}]]]));
    });
  });

  test('Verify load workflows will call load workflows parameters from local service', () => {
    // given
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["VNF Scale Out"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data":{
        "parameterDefinitions": [
          {
            "id": 1,
            "name": "Configuration Parameters",
            "required": true,
            "type": "STRING",
            "pattern": ".*",
            "msgOnPatternError": null,
            "msgOnContentError": null,
            "acceptableFileType": null
          }
        ],
      }});
    let getSOWorkflowsPromiseStub = Promise.resolve({"data": [{}]});
    let getSOWorkflowsParametersPromiseStub = Promise.resolve({"data":{}});

    $controller.changeManagement.vnfNames = [{name: 'test1'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    $changeManagementService.getSOWorkflowParameter = () =>  getSOWorkflowsParametersPromiseStub;
    // when

    let result = new Map();
    const scaleOutResult = new Map();
    scaleOutResult.set("FILE", []);
    scaleOutResult.set("STRING", [
      {
        "acceptableFileType": null,
        "id": 1,
        "msgOnContentError": null,
        "msgOnPatternError": null,
        "name": "Configuration Parameters",
        "pattern": ".*",
        "required": true,
        "type": "STRING",
      }
    ]);
    result.set("VNF Scale Out", scaleOutResult);

    return $controller.loadWorkFlows()
    .then(() => {
      expect($controller.localWorkflowsParameters).toEqual(result);
    });
  });

  test('Verify broken SO workflows wont change content of local workflows', () => {
    // given
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["workflow 0"]}});
    let getSOWorkflowsPromiseStub = Promise.reject(new Error("Broken SO workflows service."));

    $controller.changeManagement.vnfNames = "any";
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    // when
    $controller.loadWorkFlows()
    .then(() => {
      expect($controller.workflows).toEqual(['workflow 0']);
    });
  });

  test('Verify get internal workflow parameters should return an empty list if not such workflow exist', () => {
  // given
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["VNF Scale Out"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data":{
        "parameterDefinitions": [
          {
            "id": 1,
            "name": "Configuration Parameters",
            "required": true,
            "type": "STRING",
            "pattern": ".*",
            "msgOnPatternError": null,
            "msgOnContentError": null,
            "acceptableFileType": null
          }
        ],
      }});
    let getSOWorkflowsPromiseStub = Promise.resolve({"data": [{}]});
    let getSOWorkflowsParametersPromiseStub = Promise.resolve({"data":{}});

    $controller.changeManagement.vnfNames = [{name: 'test1'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    $changeManagementService.getSOWorkflowParameter = () =>  getSOWorkflowsParametersPromiseStub;
    // when
    return $controller.loadWorkFlows()
    .then(() => {
      let internalWorkFlowParameters = $controller.getInternalWorkFlowParameters("NON-EXISTENT WF", "STRING");
      expect(internalWorkFlowParameters).toEqual([]);
    });
  });

  test('Verify get internal workflow parameters should return an empty list if not such type exist', () => {
    // given
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["VNF Scale Out"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data":{
        "parameterDefinitions": [
          {
            "id": 1,
            "name": "Configuration Parameters",
            "required": true,
            "type": "STRING",
            "pattern": ".*",
            "msgOnPatternError": null,
            "msgOnContentError": null,
            "acceptableFileType": null
          }
        ],
      }});
    let getSOWorkflowsPromiseStub = Promise.resolve({"data": [{}]});
    let getSOWorkflowsParametersPromiseStub = Promise.resolve({"data":{}});

    $controller.changeManagement.vnfNames = [{name: 'test1'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    $changeManagementService.getSOWorkflowParameter = () =>  getSOWorkflowsParametersPromiseStub;
    // when
    return $controller.loadWorkFlows()
    .then(() => {
      let internalWorkFlowParameters = $controller.getInternalWorkFlowParameters("VNF Scale Out", "FILE");
      expect(internalWorkFlowParameters).toEqual([]);
    });
  });

  test('Verify get internal workflow parameters should return a list if such workflow and type exist', () => {
    // given
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["VNF Scale Out"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data":{
        "parameterDefinitions": [
          {
            "id": 1,
            "name": "Configuration Parameters",
            "required": true,
            "type": "STRING",
            "pattern": ".*",
            "msgOnPatternError": null,
            "msgOnContentError": null,
            "acceptableFileType": null
          }
        ],
      }});
    let getSOWorkflowsPromiseStub = Promise.resolve({"data": [{}]});
    let getSOWorkflowsParametersPromiseStub = Promise.resolve({"data":{}});

    $controller.changeManagement.vnfNames = [{name: 'test1'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    $changeManagementService.getSOWorkflowParameter = () =>  getSOWorkflowsParametersPromiseStub;

    let result = [{
        "acceptableFileType": null,
        "id": 1,
        "msgOnContentError": null,
        "msgOnPatternError": null,
        "name": "Configuration Parameters",
        "pattern": ".*",
        "required": true,
        "type": "STRING",
        }];
    // when
    return $controller.loadWorkFlows()
    .then(() => {
      let internalWorkFlowParameters = $controller.getInternalWorkFlowParameters("VNF Scale Out", "STRING");
      expect(internalWorkFlowParameters).toEqual(result);
    });
  });
});
