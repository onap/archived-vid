/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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

require('./report-modal-request.controller');
const jestMock = require('jest-mock');

describe('Testing error report creation', () => {
    const mockAPI = "testAPI";

    let $notNeeded;
    let $controller;

    let mockHttp;

    let mockModalInstance;
    let mockWindow;
    let mockReportService;
    let testErrorMsg;
    let mockDataService;

    let correctResponse;
    let failResponse;

    let mockInfo;

    beforeEach(
        angular.mock.module('app')
    );

    beforeEach(inject(function (_$controller_) {
        $notNeeded = jestMock.fn();
        mockHttp = jestMock.fn();
        mockDataService = jestMock.fn();

        mockDataService.getMsoRequestParametersTestApi = function() {
            return mockAPI;
        };

        mockModalInstance = {};
        mockWindow = {
            webkitURL: {
                createObjectURL: function (blob) {
                    return blob;
                }
            }
        };

        correctResponse = {data:{report:"test-error-report",status:202}};
        failResponse = {data:{report:"test-fail-report",status:404}};

        mockReportService = {

            getReportData: function(info) {
                return Promise.resolve(correctResponse);
            },
            getReportTimeStamp: function () {
                return "testTime";
            }
        };

        testErrorMsg = 'testing message';

        $controller = _$controller_('reportModalInstanceController',{
            $uibModalInstance: mockModalInstance,
            $scope: $notNeeded,
            $window: mockWindow,
            ReportService: mockReportService,
            DataService: mockDataService,
            errorMsg: testErrorMsg,
            requestInfo: mockInfo
        });
    }));

    test('Verify close will call close in modal instance', () => {
        mockModalInstance.close = jestMock.fn();

        $controller.close();

        expect(mockModalInstance.close).toHaveBeenCalled();
    });

    test('Verify report was constructed properly', () => {

        $controller.saveReportData(correctResponse);

        expect($controller.report).toEqual(
            "Selected test API: \n" + mockAPI
            + "\n\n Data from GUI:\n" + testErrorMsg
            + "\n\n Collected data from API:\n" + JSON.stringify(correctResponse.data,  null, "\t"));
        expect($controller.downloadEnable).toBeTruthy();
        expect($controller.download).toEqual(new Blob([ $controller.report ], { type : 'text/plain' }));
    });

    test('Verify report contains error if API did not respond', () => {

        $controller.printReportFail(failResponse);

        expect($controller.report).toEqual(testErrorMsg + "\n\n API error:\n" + JSON.stringify(failResponse.data,  null, "\t"));
        expect($controller.downloadEnable).toBeFalsy();
    });

});
