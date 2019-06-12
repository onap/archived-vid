
require('./report-window.controller');
const jestMock = require('jest-mock');

describe('Testing report window controller', () => {

    let $notNeeded;
    let $controller;
    let $mockUibModalInstance;

    beforeEach(
        angular.mock.module('app')
    );

    beforeEach(inject(function (_$controller_) {
        $notNeeded = jestMock.fn();
        $mockUibModalInstance = {};

        $controller = _$controller_('reportWindowController', {
            $uibModalInstance: $mockUibModalInstance,
            $scope: $notNeeded,
        });
    }));

    test('Verify close the window', () => {

        $mockUibModalInstance.close = jestMock.fn();

        $controller.close();

        expect($mockUibModalInstance.close).toBeCalled();
    })

});
