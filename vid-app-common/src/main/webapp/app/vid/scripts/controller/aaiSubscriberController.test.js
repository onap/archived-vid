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
        const testScope = new Object();
        testScope.toggle = jestMock.fn();
        // when
        $scope.toggle(testScope);
        // then
        expect(testScope.toggle).toHaveBeenCalled();
    });

    test('Verify remove calls remove in given scope', () => {
        // given
        const testScope = new Object();
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

    test('Verify newSubItem pushes new item in tu given scope', () => {
        // given
        const testScope = new Object();
        const testModel = new Object();

        testModel.id = 2;
        testModel.nodes = [];
        testModel.title = 'testObject';

        const expectedResult = {
            id: 20,
            title: 'testObject.1',
            nodes: []
        };

        testModel.nodes = [];
        testScope.$modelValue = testModel;

        // when
        $scope.newSubItem(testScope);
        // then
        expect(testModel.nodes.length).toBe(1);
        expect(testModel.nodes[0]).toMatchObject(expectedResult);
    });
});