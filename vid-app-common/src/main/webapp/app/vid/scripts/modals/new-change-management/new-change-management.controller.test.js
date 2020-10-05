/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Copyright (C) 2020 Nokia Intellectual Property. All rights reserved.
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
  let $featureFlags;

  beforeEach(
      angular.mock.module('app')
  );

  beforeEach(inject(function (_$controller_) {
    $notNeeded = jestMock.fn();
    let lodash = require('lodash')

    // mock ChangeManagementService
    $changeManagementService = jestMock.fn();
    $changeManagementService.getAllSDCServices = jestMock.fn(() => Promise.resolve([]));

    // mock q
    $q = jestMock.fn();
    $defer = jestMock.fn();
    $flags = jestMock.fn();
    $flags.FEATURE_FLAGS = {FLAG_HANDLE_SO_WORKFLOWS: ''};
    $featureFlags = jestMock.fn();
    $featureFlags.isOn = jestMock.fn(() => true);
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
      _: lodash,
      COMPONENT: $flags,
      VIDCONFIGURATION: $notNeeded,
      DataService: $notNeeded,
      featureFlags: $featureFlags,
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

  test('Verify load workflows wont load parameters from local service', () => {
    // given
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["workflow 0"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data":{
        "parameterDefinitions": [
          {
            "id": 1,
            "name": "Configuration Parameters",
            "required": true,
            "type": "text",
            "pattern": ".*",
            "msgOnPatternError": null,
            "msgOnContentError": null,
            "acceptableFileType": null
          }
        ],
      }});
    let getSOWorkflowsPromiseStub = Promise.resolve({"data":[{

        "id": "ab6478e4-ea33-3346-ac12-ab121484a333",
        "workflowName": "inPlaceSoftwareUpdate",
        "name": "inPlaceSoftwareUpdate",
        "source": "sdc",
        "workflowInputParameters": [
            {
                "label": "New Software Version",
                "inputType": "text",
                "required": true,
                "soFieldName": "new_software_version",
                "soPayloadLocation": "userParams",
                "validation":[]
            }
        ]
    }]
    });

    $controller.changeManagement.vnfNames = [{modelVersionId: 'test1', name:'test'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    // when
    return $controller.loadWorkFlows().then(() => {
      expect($controller.workflows).toContain('inPlaceSoftwareUpdate');
      expect($controller.localWorkflowsParameters).toEqual(new Map());
    });
  });

  test('Verify load workflows will set workflows and parameters', () => {
    // given
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["workflow 0"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data": {}});
    let getSOWorkflowsPromiseStub = Promise.resolve({"data":[{

            "id": "ab6478e4-ea33-3346-ac12-ab121484a333",
            "workflowName": "inPlaceSoftwareUpdate",
            "name": "inPlaceSoftwareUpdate",
            "source": "sdc",
          "workflowInputParameters": [
            {
              "label": "New Software Version",
              "inputType": "text",
              "required": true,
              "soFieldName": "new_software_version",
              "soPayloadLocation": "userParams",
                "validation":[]
            }
          ]
        }]
      });
    $controller.changeManagement.vnfNames = [{modelVersionId: 'test1', name:'test'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    $changeManagementService.getSOWorkflowParameter = () =>  getSOWorkflowsParametersPromiseStub;
    // when
    return $controller.loadWorkFlows()
    .then(() => {
      expect($controller.workflows).toEqual(["inPlaceSoftwareUpdate"]);
      expect($controller.remoteWorkflowsParameters).toEqual(new Map([["inPlaceSoftwareUpdate",
        [{
          "name": "New Software Version",
          "required": true,
          "id": "new_software_version",
          "soFieldName": "new_software_version",
          "maxLength": '500',
          "pattern": '.*',
          "type": "text"
        }]]
      ]));
    });
  });

  test('Verify load workflows wont load workflows parameters from SO if feature flag is disabled', () => {
    // given
    $featureFlags.isOn = jestMock.fn(() => false);
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["workflow 0"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data": {}});
    let getSOWorkflowsPromiseStub = Promise.resolve({"data": [{"id": "1", "name": "workflow 0"}]});
    let getSOWorkflowsParametersPromiseStub = Promise.resolve({"data":{"parameterDefinitions": [
          {"id": 1, "name": "parameter 1", "required": true, "type": "text", "pattern": "[0-9]*"},
          {"id": 2, "name": "parameter 2", "required": true, "type": "text", "pattern": ".*"},
          {"id": 3, "name": "parameter 3", "required": false, "type": "text", "pattern": "[0-9]*"}]}});

    $controller.changeManagement.vnfNames = [{name: 'test1'}, {name: "test2"}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    $changeManagementService.getSOWorkflowParameter = () =>  getSOWorkflowsParametersPromiseStub;
    // when
    return $controller.loadWorkFlows()
    .then(() => {
      expect($controller.workflows).toEqual(["workflow 0"]);
      expect($controller.remoteWorkflowsParameters).toEqual(new Map());
    });
  });

  test('Verify load workflows will call load workflows parameters from local service', () => {
    // given
    $featureFlags.isOn = jestMock.fn(() => false);
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["VNF Scale Out"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data":{
        "parameterDefinitions": [
          {
            "id": 1,
            "name": "Configuration Parameters",
            "required": true,
            "type": "text",
            "pattern": ".*",
            "msgOnPatternError": null,
            "msgOnContentError": null,
            "acceptableFileType": null
          }
        ],
      }});

    $controller.changeManagement.vnfNames = [{name: 'test1'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    // when

    let result = new Map();
    const scaleOutResult = [
      {
        "acceptableFileType": null,
        "id": 1,
        "msgOnContentError": null,
        "msgOnPatternError": null,
        "name": "Configuration Parameters",
        "pattern": ".*",
        "required": true,
        "type": "text",
      }
    ];
    result.set("VNF Scale Out", scaleOutResult);

    return $controller.loadWorkFlows()
    .then(() => {
      expect($controller.localWorkflowsParameters).toEqual(result);
    });
  });

  test('Verify broken SO workflows will return empty list of workflows', () => {
    // given
    let getSOWorkflowsPromiseStub = Promise.reject(new Error("Broken SO workflows service."));

    $controller.changeManagement.vnfNames = [{name:"any"}];
    $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;
    // when
    $controller.loadWorkFlows()
    .then(() => {
      expect($controller.workflows).toEqual([]);
    });
  });

  test('Verify get internal workflow parameters should return an empty list if not such workflow exist', () => {
  // given
    $featureFlags.isOn = jestMock.fn(() => false);
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["VNF Scale Out"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data":{
        "parameterDefinitions": [
          {
            "id": 1,
            "name": "Configuration Parameters",
            "required": true,
            "type": "text",
            "pattern": ".*",
            "msgOnPatternError": null,
            "msgOnContentError": null,
            "acceptableFileType": null
          }
        ],
      }});

    $controller.changeManagement.vnfNames = [{name: 'test1'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    // when
    return $controller.loadWorkFlows()
    .then(() => {
      let internalWorkFlowParameters = $controller.getInternalWorkFlowParameters("NON-EXISTENT WF", "text");
      expect(internalWorkFlowParameters).toEqual([]);
    });
  });

  test('Verify get internal workflow parameters should return an empty list if not such type exist', () => {
    // given
    $featureFlags.isOn = jestMock.fn(() => false);
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["VNF Scale Out"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data":{
        "parameterDefinitions": [
          {
            "id": 1,
            "name": "Configuration Parameters",
            "required": true,
            "type": "text",
            "pattern": ".*",
            "msgOnPatternError": null,
            "msgOnContentError": null,
            "acceptableFileType": null
          }
        ],
      }});

    $controller.changeManagement.vnfNames = [{name: 'test1'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    // when
    return $controller.loadWorkFlows()
    .then(() => {
      let internalWorkFlowParameters = $controller.getInternalWorkFlowParameters("VNF Scale Out", "FILE");
      expect(internalWorkFlowParameters).toEqual([]);
    });
  });

  test('Verify get internal workflow parameters should return an empty list if type exist but mapped to undefined', () => {
    // given
    $featureFlags.isOn = jestMock.fn(() => false);
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["VNF Scale Out"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data": undefined});

    $controller.changeManagement.vnfNames = [{name: 'test1'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
    // when
    return $controller.loadWorkFlows()
    .then(() => {
      let internalWorkFlowParameters = $controller.getInternalWorkFlowParameters("VNF Scale Out", "FILE");
      expect(internalWorkFlowParameters).toEqual([]);
    });
  });

  test('Verify get internal workflow parameters should return a list if such workflow and type exist', () => {
    // given
    $featureFlags.isOn = jestMock.fn(() => false);
    let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["VNF Scale Out"]}});
    let getLocalWorkflowsParametersStub = Promise.resolve({"data":{
        "parameterDefinitions": [
          {
            "id": 1,
            "name": "Configuration Parameters",
            "required": true,
            "type": "text",
            "pattern": ".*",
            "msgOnPatternError": null,
            "msgOnContentError": null,
            "acceptableFileType": null
          }
        ],
      }});
    $controller.changeManagement.vnfNames = [{name: 'test1'}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
    $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;

    let result = [{
        "acceptableFileType": null,
        "id": 1,
        "msgOnContentError": null,
        "msgOnPatternError": null,
        "name": "Configuration Parameters",
        "pattern": ".*",
        "required": true,
        "type": "text",
        }];
    // when
    return $controller.loadWorkFlows()
    .then(() => {
      let internalWorkFlowParameters = $controller.getInternalWorkFlowParameters("VNF Scale Out", "text");
      expect(internalWorkFlowParameters).toEqual(result);
    });
  });

  test('Verify get remote workflow should call internal service for params when workflow is native', () =>{
      let getWorkflowsStub = Promise.resolve({"data": {"workflows": ["workflow 0"]}});
      let getLocalWorkflowsParametersStub = Promise.resolve({"data":{
              "parameterDefinitions": [
                  {
                      "id": 1,
                      "name": "Configuration Parameters",
                      "required": true,
                      "type": "text",
                      "pattern": ".*",
                      "msgOnPatternError": null,
                      "msgOnContentError": null,
                      "acceptableFileType": null
                  }
              ],
          }});
      let getSOWorkflowsPromiseStub = Promise.resolve({"data":[{

        "id": "ab6478e4-ea33-3346-ac12-ab121484a333",
        "workflowName": "inPlaceSoftwareUpdate",
        "name": "inPlaceSoftwareUpdate",
        "source": "native",
        "workflowInputParameters": [
        ]
    }]
  });

  $controller.changeManagement.vnfNames = [{modelVersionId: 'test1', name:'test'}];
  $changeManagementService.getWorkflows = () => getWorkflowsStub;
  $changeManagementService.getLocalWorkflowParameter = () => getLocalWorkflowsParametersStub;
  $changeManagementService.getSOWorkflows = () =>  getSOWorkflowsPromiseStub;

  return $controller.loadWorkFlows().then(() => {
    expect($controller.workflows).toContain('inPlaceSoftwareUpdate');
    expect($controller.localWorkflowsParameters.get('inPlaceSoftwareUpdate')).toEqual([{
        "id": 1,
        "name": "Configuration Parameters",
        "required": true,
        "type": "text",
        "pattern": ".*",
        "msgOnPatternError": null,
        "msgOnContentError": null,
        "acceptableFileType": null
    }]);
  });
});

    test('Verify that vm.searchVNFs return only generic-vnfs with relation to vserver', () => {
        // given
        $controller.changeManagement.serviceType = [];
        let getVnfsByCustomerIdAndServiceType = Promise.resolve({"data":
                { "results" : [
                        { "id": "1",
                            "node-type": "generic-vnf",
                            "properties": {
                                "nf-role": "vLB"
                            },
                            "related-to": [
                                { "id": "11",
                                    "node-type": "vf-module"
                                },
                                { "id": "12",
                                    "node-type": "tenant"
                                }
                            ]
                        },
                        { "id": "2",
                            "node-type": "generic-vnf",
                            "properties": {
                                "nf-role": "vLB"
                            },
                            "related-to": [
                                { "id": "21",
                                    "node-type": "tenant"
                                }
                            ]
                        },
                        { "id": "3",
                            "node-type": "generic-vnf",
                            "properties": {
                                "nf-role": "vLB"
                            },
                            "related-to": [
                                { "id": "31",
                                    "node-type": "vf-module"
                                },
                                { "id": "32",
                                    "node-type": "tenant"
                                },
                                { "id": "33",
                                    "node-type": "vserver"
                                }
                            ]
                        },
                        { "id": "11",
                            "node-type": "vf-module",
                            "related-to": [
                                { "id": "111",
                                    "node-type": "vserver"
                                }
                            ]
                        },
                        { "id": "31",
                            "node-type": "vf-module",
                            "related-to": [
                                { "id": "311",
                                    "node-type": "vserver"
                                }
                            ]
                        }
                    ]
                }
        });
        let expectedVnfs = [
            {
                "id": "1",
                "node-type": "generic-vnf",
                "properties": {"nf-role": "vLB"},
                "related-to": [
                    {"id": "11", "node-type": "vf-module"},
                    {"id": "12", "node-type": "tenant"}]},
            {
                "id": "3",
                "node-type": "generic-vnf",
                "properties": {"nf-role": "vLB"},
                "related-to": [
                    {"id": "31", "node-type": "vf-module"},
                    {"id": "32", "node-type": "tenant"},
                    {"id": "33", "node-type": "vserver"}
                ]}];
        $aaiService.getVnfsByCustomerIdAndServiceType = () => getVnfsByCustomerIdAndServiceType;

        // when
        $controller.searchVNFs().then(() => {
            expect($controller.vnfs).toHaveLength(2);
            expect($controller.vnfs).toEqual(expectedVnfs);
        });
    });
});
