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
    let getSOWorkflowsPromiseStub = Promise.resolve({"data": [{"id": "1", "name": "workflow 1"}, {"id": "2", "name": "workflow 2"}]});
    let getSOWorkflowsParametersPromiseStub = Promise.resolve({"data":{"parameterDefinitions": []}});

    $controller.changeManagement.vnfNames = [{name: 'test1'}, {name: "test2"}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
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
    let getSOWorkflowsPromiseStub = Promise.resolve({"data": [{"id": "1", "name": "workflow 0"}]});
    let getSOWorkflowsParametersPromiseStub = Promise.resolve({"data":{"parameterDefinitions": [
          {"id": 1, "name": "parameter 1", "required": true, "type": "STRING", "pattern": "[0-9]*"},
          {"id": 2, "name": "parameter 2", "required": true, "type": "STRING", "pattern": ".*"},
          {"id": 3, "name": "parameter 3", "required": false, "type": "STRING", "pattern": "[0-9]*"}]}});

    $controller.changeManagement.vnfNames = [{name: 'test1'}, {name: "test2"}];
    $changeManagementService.getWorkflows = () => getWorkflowsStub;
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
});

