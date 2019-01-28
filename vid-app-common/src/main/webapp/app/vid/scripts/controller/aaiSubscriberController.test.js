require('./aaiSubscriberController');

describe('TreeCtrl testing', () => {
  beforeEach(
    angular.mock.module('app')
  );

  var $scope;

  beforeEach(angular.mock.inject(function(_$controller_){
    $scope={};
    _$controller_('TreeCtrl', {
      $scope: $scope,
    });
  }));

  test('adds 1 + 2 to equal 3', () => {
    expect($scope.sum(1, 2)).toBe(3);
  });

});

