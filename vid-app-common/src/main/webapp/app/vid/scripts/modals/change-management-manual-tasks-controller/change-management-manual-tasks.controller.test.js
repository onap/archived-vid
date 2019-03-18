/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 AT&T Intellectual Property. All rights reserved.
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

require('./change-management-manual-tasks.controller');
const jestMock = require('jest-mock');

describe('changeManagementManualTasksController testing', () => {
    let $controller;
    let $uibModalInstance = jestMock.fn();
    let $MsoService = jestMock.fn();
    let $log = jestMock.fn();

    beforeEach(
        angular.mock.module('app')
    );

    beforeEach(inject(function (_$controller_) {
        $MsoService.getManualTasks = jestMock.fn().mockResolvedValue(
            {data: [manualTaskResponse]});
        $log.error = jestMock.fn();

        $controller = _$controller_('changeManagementManualTasksController', {
            "MsoService": $MsoService,
            "$uibModalInstance": $uibModalInstance,
            "$log": $log,
            "jobInfo": {
                requestState: job.requestStatus.requestState,
                details: job.requestStatus.statusMessage,
                job: job,
            },
            "COMPONENT": {
                MANUAL_TASKS: ["manualTaskName1", "manualTaskName2"]
            },
        });
    }));

    const job = {
        "requestId": "db775fac-d9b5-480e-8b3e-4f0d0ae67890",
        "requestScope": "vnf",
        "requestStatus": {
            "percentProgress": 100.0,
            "requestState": "FAILED",
            "statusMessage": "Error validating request. No valid catalog entry is specified",
            "finishTime": "Thu, 05 Oct 2017 18:58:29 GMT"
        },
        "requestType": "replaceInstance",
        "startTime": "Thu, 05 Oct 2017 18:58:29 GMT",
        "instanceReferences": {
            "serviceInstanceId": "cc8fa0a9-7576-4c39-af31-7ad61d057ac9",
            "vnfInstanceId": "bec0c3d3-09ae-4eb1-b694-057987a10982",
            "requestorId": "pa2396"
        }
    };

    const manualTaskResponse = {
        "taskId": "db775fac-d9b5-480e-8b3e-4f0d0ae67890",
        "validResponses": ["rollback", "abort", "skip", "resume", "retry"]
    };

    test('should populate vm.manualTasks (while init)', () => {
        expect($controller.manualTasks).toEqual(
            manualTaskResponse.validResponses);
    });

    test('should populate vm.MANUAL_TASKS from COMPONENT (while init)', () => {
        expect($controller.MANUAL_TASKS).toEqual(
            ["manualTaskName1", "manualTaskName2"]);
    });

    test('should populate vm.task (while init)', () => {
        expect($controller.task).toEqual(manualTaskResponse);
    });

    test('should find manual task using isTaskAvailable', () => {
        expect($controller.isTaskAvailable('abort')).toBeTruthy();
        expect($controller.isTaskAvailable('resume')).toBeTruthy();

        expect($controller.isTaskAvailable('foo')).toBeFalsy();
        expect($controller.isTaskAvailable(undefined)).toBeFalsy();
    });

    test('should call MsoService upon completeTask', () => {
        $MsoService.completeTask = jestMock.fn().mockResolvedValue({data: {}});

        $controller.completeTask("taskName");
        expect($MsoService.completeTask).toBeCalledWith(
            manualTaskResponse.taskId, "taskName");
    });

    test('should close modal upon completeTask', done => {
        $MsoService.completeTask = jestMock.fn().mockResolvedValue({data: {}});
        $uibModalInstance.close = jestMock.fn(() => {
            done();
        });

        $controller.completeTask("taskName");
    });

    test('should close modal upon failed completeTask', done => {
        $MsoService.completeTask = jestMock.fn().mockRejectedValue();
        $uibModalInstance.close = jestMock.fn(() => {
            done();
        });

        $controller.completeTask("taskName");
    });

});

