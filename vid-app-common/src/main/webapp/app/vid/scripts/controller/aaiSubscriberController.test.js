require('./aaiSubscriberController');
const jest = require('jest-mock');

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
    const broadcast = jest.fn();
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

});

