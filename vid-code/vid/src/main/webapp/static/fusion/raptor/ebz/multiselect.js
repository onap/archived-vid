'use strict';

angular.module('quantum').directive('dropdownMultiselect', ['$document', function($document){
   return {
       restrict: 'E',
       scope:{           
            model: '=ngModel',
            options: '='
       },
       template: "<div class='btn-group ng-isolate-scope buttons-dropdown--small' data-ng-class='{open: open}'>"+
        "<a class='button btn buttons-dropdown__split button--secondary'  data-ng-click='open=!open;' style='border-right:none;'>Multi-Select</a>"+
                "<a class='button buttons-dropdown__drop dropdown-toggle button--secondary' data-ng-click='open=!open;'  style='border-left:none;color:#067ab4'></a>"+
                "<ul class='dropdown-menu' aria-labelledby='dropdownMenu'>" + 
                    "<li><a data-ng-click='selectAll()'><i class='icon-included-checkmark'></i>  Check All</a></li>" +
                    "<li><a data-ng-click='deselectAll();'><i class='icon-erase'></i>  Uncheck All</a></li>" +                    
                    "<li class='divider'></li>" +
                    "<li data-ng-repeat='option in options'> <a data-ng-click='setSelectedItem()'>{{option.title}}<span data-ng-class='isChecked(option.value)'></span></a></li>" +                                        
                "</ul>" +
            "</div>" ,
       link: function($scope,element,attr){

     	  $scope.$watch("options",function(newValue,oldValue) {
             	$scope.model = [];
    	  });
           
            $scope.selectAll = function () {
                $scope.model = _.pluck($scope.options, 'value');
            };            
            $scope.deselectAll = function() {
                $scope.model=[];
            };
            $scope.setSelectedItem = function(){
                var value = this.option.value;
                if (_.contains($scope.model, value)) {
                    $scope.model = _.without($scope.model, value);
                } else {
                    $scope.model.push(value);
                }
                return false;
            };
            $scope.isChecked = function (value) {                 
                if (_.contains($scope.model, value)) {
                    return 'icon-included-checkmark pull-right';
                }
                return false;
            };
            
            $document.bind('click', function(event){
                var isClickedElementChildOfPopup = element
                  .find(event.target)
                  .length > 0;
                  
                if (isClickedElementChildOfPopup)
                  return;
                  
                $scope.open = false;
                $scope.$apply();
              });

       }
   }; 
}]);