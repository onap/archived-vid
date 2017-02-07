angular.module('quantum')
  .directive('formBuilder', ['$q', '$parse', '$http', '$templateCache', '$compile', '$document', '$timeout', function ($q, $parse, $http, $templateCache, $compile, $document, $timeout) {
    return {
      restrict: 'E', // supports using directive as element only
      scope:{ 
		ngModel: '=',
		ngFormFields: '=',
		ngNumFormCols: '=',
		ngTriggerMethod: '=',
		ngShowFieldId: '='
		},
      link: function ($scope, element, attrs) {
    	  $scope.element=element;
    	  $scope.datetimeformat = "MM/dd/yyyy hh:mm a";

    	  $scope.buildField = function (field, parentElement) {
            var x = '';
            if(field.visible) {
            	if (field.fieldType === 'LIST_MULTI_SELECT') {
            		x = angular.element('<label><i>'+field.fieldDisplayName+'<span ng-show="ngShowFieldId"> [ '+field.fieldId+' ] </span>:</i></label><br>'+
  			          	'<div class="select2-container ebz-listbox form-field" style="height:100px;" tabindex="0" >'+
  			      			'<label ng-repeat="item in ngModel.'+field.fieldId+'" style="display:block;">'+
  			      			'<input type="checkbox" style="margin-top: 10px;" ng-model="item.defaultValue" ng-change="triggerFormFields('+field.triggerOtherFormFields+')" att-checkbox title="{{item.title}}"/> {{item.title}}<br/>'+
  			      			'</label>'+
  			      		'</div>');
                } else if (field.fieldType === 'LIST_BOX') {
                    	x = angular.element('<label><i>'+field.fieldDisplayName+'<span ng-show="ngShowFieldId"> [ '+field.fieldId+' ] </span>:</i></label><br> <div class="form-field" att-select="formFieldLuValues.'+field.fieldId+'" ng-model="ngModel.'+field.fieldId+'" ng-change="triggerFormFields('+field.triggerOtherFormFields+')"></div>');
                } else if((field.fieldType === 'text' || field.fieldType === 'TEXT') && field.validationType === 'DATE'){
                	x = angular.element('<label><i>'+field.fieldDisplayName+'<span ng-show="ngShowFieldId"> [ '+field.fieldId+' ] </span>:</i></label><br> <input id="'+field.fieldId+'" type="text" ng-model="ngModel.'+field.fieldId+'" tabindex="0" ng-change="triggerFormFields('+field.triggerOtherFormFields+')" att-datepicker>');
                } else if((field.fieldType === 'text' || field.fieldType === 'TEXT') && field.validationType === 'TIMESTAMP_MIN'){
                	x = angular.element('<label><i>'+field.fieldDisplayName+'<span ng-show="ngShowFieldId"> [ '+field.fieldId+' ] </span>:</i></label><br> <input id="'+field.fieldId+'" type="text" date-format="datetimeformat" ng-model="ngModel.'+field.fieldId+'" tabindex="0" ng-change="triggerFormFields('+field.triggerOtherFormFields+')" att-date-time-picker>');
                } else if(field.fieldType === 'text' || field.fieldType === 'TEXT'){
                		x = angular.element('<label><i>'+field.fieldDisplayName+'<span ng-show="ngShowFieldId"> [ '+field.fieldId+' ] </span>:</i></label><br> <input type="text" class="form-field" ng-model="ngModel.'+field.fieldId+'" maxlength="'+field.length+'" ng-blur="triggerFormFields('+field.triggerOtherFormFields+')" />');
                } else if(field.fieldType === 'CHECK_BOX'){
                	x = angular.element('<label><i>'+field.fieldDisplayName+'<span ng-show="ngShowFieldId"> [ '+field.fieldId+' ] </span>:</i></label><br> <input type="checkbox" ng-model="ngModel.'+field.fieldId+'" tabindex="0" ng-change="triggerFormFields('+field.triggerOtherFormFields+')" att-checkbox>');
                }
            }
          	parentElement.append(x);
            $compile(x)($scope);
          };
          $scope.buildForm = function() {
  	        // create elements <table> and a <tbody>
  	        var tbl     = angular.element("<table></table>");
  	        var tblBody = angular.element("<tbody></tbody>");
  	        var row = angular.element("<tr></tr>");
  	        
  	        var ngFormFieldsLength = $scope.ngFormFields.length;

  	        for (var j = 0; j < ngFormFieldsLength; j++) {
  	            var cell = angular.element("<td style='padding: 5px;'></td>");
  	            $scope.buildField($scope.ngFormFields[j],cell); 
  	            row.append(cell);

  	            if((j!=0 && (j+1)%$scope.ngNumFormCols==0) || j==(ngFormFieldsLength-1)){
	    	            tblBody.append(row);
  	            	row = angular.element("<tr></tr>");
  	            }
  	        }
  	        tbl.append(tblBody);
    		angular.element($scope.element).html('');
    		$scope.element.append(tbl);
        };

    	  
  		$scope.formFieldLuValues = {};
		$scope.getEBZFormat = function() {
			if($scope.ngFormFields && $scope.ngFormFields.length>0){
    	    	$scope.ngFormFields.forEach(function(formField) {
    		   		if(formField.fieldType === 'LIST_MULTI_SELECT') {
    		   			$scope.ngModel[formField.fieldId]= [];
    		   			if(formField.formFieldValues && formField.formFieldValues.length>0){
	    		   		 	formField.formFieldValues.forEach(function(entry,i) {
	    		   		 		$scope.ngModel[formField.fieldId].push({ index: i, value: entry.id, title: entry.name, defaultValue: entry.defaultValue});
	    			   		});
    		   			}
    		   		} else if(formField.fieldType==='LIST_BOX') {
    		   			$scope.formFieldLuValues[formField.fieldId]= [];
    		   			if(formField.formFieldValues && formField.formFieldValues.length>0){
	    		   		 	formField.formFieldValues.forEach(function(entry,i) {
	    		   		 		$scope.formFieldLuValues[formField.fieldId].push({ index: i, value: entry.id, title: entry.name});
	    		   		 		if(entry.defaultValue){
	    		   		 			$scope.ngModel[formField.fieldId]={ index: i, value: entry.id, title: entry.name};
	    		   		 		}
	    			   		});
    		   			}
    		   		} else if(formField.fieldType === 'text' || formField.fieldType === 'TEXT' || 
    		   				formField.validationType === 'DATE' || formField.validationType === 'TIMESTAMP_MIN') {
		   		 		if(formField.formFieldValues && formField.formFieldValues.length>0){
		   		 			$scope.ngModel[formField.fieldId]=formField.formFieldValues[0].id;
		   		 		}
                    }
    		   	});
    		}
		};
		
  		$scope.$watch("ngFormFields",function(newValue,oldValue) {
  		  if($scope.ngFormFields){
  	  		$scope.getEBZFormat();
  	  	    $scope.buildForm();
  		  }
  		});
  		
  		$scope.triggerFormFields = function(triggerFlag) {
  			if(triggerFlag){
  	 	  		$scope.element.html('Loading...');
  	 	  		$scope.ngTriggerMethod();
  			}
    	};

      }
    };
  }]);
