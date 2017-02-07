
 
  
 app.directive('afterRender', [ function() {
    var def = {
        restrict : 'A', 
        terminal : true,
        transclude : false,
        link : function(scope, element, attrs) {
            if (attrs) { scope.$eval(attrs.afterRender) }
            scope.$emit('onAfterRender')
        }
    };
    return def;
    }]);
    
 app.directive("search", function() {
	return {
		// replace custom element with html5 markup
	    template: '<div >'    
	    	//+ '<c:set var="zeroidx" value="0" />' 
	    	+ '<div ng-repeat="a in optionsSizeArray track by $index">  ' 
	    	+ '<div id="{{sId}}{{a}}" >    ' 
	    	+ '		<div class="form-field form-field__glued pull-left size-onefourth" style=" width:25%;" >' 
	    	+ '			<div att-search="options" ng-model="valueOptions[a]" placeholder="Select"></div>   '                                                              
	    	+ '      </div> ' 
	    	+ '      <div class="form-field form-field__glued pull-left size-onefourth" style="  width:25%;">   '       
	    	+ '           <div att-search="operators" ng-model="compareOptions[a]" placeholder="Select"></div> ' 
	    	+ '       </div> ' 
	    	+ '      <div class="form-field form-field__glued pull-left size-onefourth" style="  width:25%;">' 
	    	+ '          <input ng-model="searchValue[a]" type="text" placeholder="What are you looking for?" style=" border-radius:0px 5px 5px 0px" >  '                      
	    	+ '      </div>  ' 
	    	+ '      <div class="form-field form-field__glued pull-left size-onefourth" style="  width:25%; padding-left:5px;"> '   
	    	+ '     		<button ng-click="remove(sId,a)" class="myzkBtn"	>-</button>' 
	    	+ '          	<button ng-if="$index==0" ng-click="addSearch(sId,a,optionsSizeArray)" class="myzkBtn" 	>+</button>   '   
	    	+ '			<button ng-if="$index==0" ng-click="updateparent({filter: { valueOptions: valueOptions, compareOptions: compareOptions, searchValue : searchValue }  })" class="myzkBtn" style="background-image:url(static/ebz/images/searchIcon.png);  background-repeat: no-repeat; background-position: center;">&nbsp;</button>'   	
	    	+ '       </div>'  
	    	+ '     </div>  '             
	    	+ '</div>' 
	    	+ '</div> ' ,
	    replace: true,
	    // restrict usage to element only since we use attributes for APIs
	    restrict: 'EA',
	    require: 'ngModel',
	    // new isolate scope
	    scope: {
	    	mSearch : '=ngModel'
    		,updateparent: '&'
    		,jsonForOption : '='
    		,jsonForOperator : '='	
    		,searchId : '='	
	      },
	     link: function(scope, iElement, iAttrs){ 
	    	 scope.optionsSizeArray = [];
	    	 scope.sId = '';
	    	 //scope.options = JSON.parse(JSON.stringify(iAttrs.jsonForOption));
	    	 //scope.operators = JSON.parse(iAttrs.operators);
	    	 
	    	 scope.$watch("jsonForOption", function(newval, oldval) {
	    		 scope.options = JSON.parse(JSON.stringify(oldval));
	    		 console.log('1');
	    		 scope.evalRepeatIndex(scope.options.length);
	    		 console.log('2');

	    	 	});
	    	 
	    	 scope.evalRepeatIndex = function(size){
    		 	for(index = 0; index < size; index++) {
	    		 scope.optionsSizeArray.push(index);
	    	 	}
	    	 };
	    	 
	    	 scope.$watch("jsonForOperator", function(newval, oldval) {
	    		 scope.operators = JSON.parse(JSON.stringify(oldval));
	    	
	    		});
	    	 
	    	 if(getParameterByName("search")!=null && getParameterByName("search")!=''){
	    		 if((getParameterByName("location")!=null && getParameterByName("location")!='')){
	    			 scope.searchValue[0]=JSON.parse(JSON.stringify(getParameterByName("location")));
	    			 scope.valueOptions[0]={index:0,value:'Location Name',title:"Location Name",alias:'Location Name'};
	    			 scope.compareOptions[0]={index:1,value:'Contains',title:"Contains",alias:'Contains'};
	    		 }else if((getParameterByName("userLastName")!=null && getParameterByName("userLastName")!='')){
	    			 scope.searchValue[0]=JSON.parse(JSON.stringify(getParameterByName("userLastName")));
	    			 scope.valueOptions[0]= {index: 0, value: 'Last Name', title: 'Last Name', alias:'Last Name'};
	    			 scope.compareOptions[0]={index:1,value:'Contains',title:"Contains",alias:'Contains'};
	    		 }
	    	 }
	    	 scope.$watch("searchId", function(newval, oldval) {
	    		 scope.sId = oldval;
	    		 scope.hideDiv();
	    		});
	    	 
	    	 scope.showHide = function(div){
	    		if(div != 0){
	    			$("#s1").css('display', 'none');
	    		}
	    		 return true; 
	    	 };
	    	 
	    	 
	    	 scope.hideDiv = function(){
	    		 console.log('3');
	    		 console.log(scope.optionsSizeArray);


	    		 for (i = 1; i < scope.optionsSizeArray.length; i++) {
	    			 var element = "#"+scope.sId+i;
	    		 	$(element).css('display', 'none');
	    		 }

	    		 console.log('4');

	    	};
	    	 
	    	 
	    	 $(function() {
				  	scope.hideDiv();
				});
	    	 
	     	},
	     
	     controller: function ($scope) {
	    	
	    	 $scope.addSearch = function(searchId, index, optionsLength){
	    		 console.log('5');

				  for(var i = 0; i<=optionsLength.length ;i++){
					 var element = "#"+searchId+i;	
					 if($(element).css('display') == 'none'){
						 $(element).css('display', 'inline');	
						 break;
					 }
				  };
		    		 console.log('6');

			};
	    	 
			$scope.remove = function(searchId, index){ 
		    	var remove = "#"+searchId+index;	
		    	if(index!=0)
			   		$(remove).css("display", "none");
			   		var v  ={
			   				index: 0,
			   				value: '', 
			   				title: 'Select', 
			   				alias:''
			   		};
			   	 	$scope.valueOptions[index]=v;
			   		$scope.compareOptions[index]=v;
			   		$scope.searchValue[index]="";
		    };
    	 
		    $scope.search = function() {	
		   		var data = {
		    			valueOptions				: $scope.valueOptions,
		    			compareOptions		 		: $scope.compareOptions,
		    			searchValue					: $scope.searchValue
		    	 };
		    };
		    
		    var data = {
	    			valueOptions				: $scope.valueOptions,
	    			compareOptions		 		: $scope.compareOptions,
	    			searchValue					: $scope.searchValue
	    	 };	
		    
		    $scope.mSearch = data;
		    $scope.valueOptions=[];
			$scope.compareOptions=[];
			$scope.searchValue=[];
	     }
	}
  
  });

  app.directive('phoneNumberMask', [function(){
	 	return {
	 		restrict: 'A',
		  	require: '?ngModel',
		  	scope: {
		  		ngModel : '='
		  	},
		    link: function(scope, el, attrs){
		    	scope.$watch(attrs.phoneNumberMask, function(newValue, oldValue) {
		    		
		    		if(scope.ngModel)
		    			scope.ngModel= scope.ngModel.replace(/(\+1)?(\d{3})(\d{3})(\d{4})/, '$2.$3.$4');
	    		 $(el).mask("999.999.9999");
	            });      
		      scope.$watch("ngModel", function() {
		    	  var current = $(el).val();
		    	  if(scope.ngModel && /^(\+1)?\d{3,}$/.test(scope.ngModel))
		    		scope.ngModel= scope.ngModel.replace(/(\+1)?(\d{3})(\d{3})(\d{4})/, '$2.$3.$4');
	    		
		      });
		    },
		};
	}]);
  
  app.directive('allowOnlyNumber', [function(){
	  return {
		     require: 'ngModel',
		     link: function(scope, element, attrs, modelCtrl) {
		       modelCtrl.$parsers.push(function (inputValue) {
		           if (inputValue == undefined) return '';
		           var transformedInput = inputValue.replace(/[^0-9]/g, ''); 
		           if (transformedInput!=inputValue) {
		              modelCtrl.$setViewValue(transformedInput);
		              modelCtrl.$render();
		           }         

		           return transformedInput;         
		       });
		     }
		   };
	}]);
  
  app.directive('showProcessing', function(){
      return {
          restrict: 'A',
          link: function(scope, elem, attrs) {
        	  elem.bind('click', function() {
        		  	$(".overlayed").css("display","inline");
        		  	$(".loadingId").css("display","inline");
        	  });
          }
       }      
    });
 
app.directive("searchCriteria", function() {
		return {
			// replace custom element with html5 markup
		    template: '<div >'    
		    	+ '<div ng-repeat="a in searchCriterion track by $index" ng-show="isShown[$index]">  ' 
			    + '  	<div class="form-field form-field__glued form-field__square size-onefourth left_round_border">' 
			    + '    		<div att-search="availableOptions[$index]" ng-model="valueOptions[$index]"></div>   '                                                              
		    	+ '      </div> ' 
		    	+ '      <div class="form-field form-field__glued form-field__square size-onefourth">   '       
		    	+ '           <div att-search="availableOperators[$index]" ng-model="compareOptions[$index]" > </div> ' 
		    	+ '       </div> ' 
		    	+ '      <div class="form-field form-field__glued form-field__square size-onefourth right_round_border">' 
		    	+ ' 		<input ng-show="!availableValues[$index] || availableValues[$index].length===0" ng-model="searchValue[$index]" type="text" placeholder="What are you looking for?" class="fn-ebz-text" style="width:100%;" >  '   
		      	+ '           <div ng-show="availableValues[$index] && availableValues[$index].length!==0" att-search="availableValues[$index]" ng-model="searchValue[$index]"></div> ' 
		    	+ '      </div>  ' 
		    	+ '      <div class="form-field form-field__glued form-field__square size-onefourth" style=" padding-left:5px;" > '   
		    	+ '     			<button ng-show="$index!=0" ng-click="removeSearchCriteria($index)" class="myzkBtn">-</button>' 
		    	+ '          	<button ng-show="$index==0" ng-click="addSearchCriteria($index)" class="myzkBtn">+</button>   '   
		    	+ '			<button ng-show="$index==0" ng-click="search()" class="myzkBtn" style="background-image:url(static/ebz/images/searchIcon.png);  background-repeat: no-repeat; background-position: center;">&nbsp;</button>'   	
		    	+ '       </div>'  
		    	+ '     </div>  '             
		    	+ '</div> ' ,
		    replace: true,
		    // restrict usage to element only since we use attributes for APIs
		    restrict: 'EA',
		    require: 'ngModel',
		    // new isolate scope
		    scope: {
		    	mSearch : '=ngModel',
	    		updateparent: '&'
	    		,options : '=jsonForOption'
	    		,operators : '=jsonForOperator'
		      },
		   
		     controller: function ($scope) {
		    	$scope.isShown = [true]; 
		    	$scope.searchCriterion = [];
			    $scope.valueOptions=[];
				$scope.compareOptions=[];
				$scope.searchValue=[]; 
				$scope.availableOperators=[];
				$scope.availableValues=[];
		    	$scope.availableOptions = [];
		    	//init 
		    	$scope.insertEmptyOption = function(arr){
		    		var hasEmpty = false;
		    		 $.each(arr, function(i, a){ 
			    		 if(a.value ===''){
			    			 hasEmpty = true;
			    		 }
			    	 });
		    		 if(!hasEmpty){
			    		 $.each(arr, function(i, a){ 
				    		 a.index +=1;
				    	 });
				    	arr.unshift({index: 0, value: '', title: 'Select', alias:'Select'});
		    		 }
		    	};
		    	$scope.findFirstOption = function(arr){
		    		
		    	};
		    	$scope.insertEmptyOption($scope.options);
		    	$scope.insertEmptyOption($scope.operators);
		    	
		    	 for(var i = 0, l= $scope.options.length; i<l; i++) {
	    			 var option= $scope.options[i];
	    			 var n = option.maxOccurs;
	    			 if(option.value!==''){
		    			 if(n && Number(n)===n && n%1===0){ //maxOccurs is specified
		    				 for(var j=0; j<n; j++){
		    					 $scope.searchCriterion.push("");
		    					 $scope.availableOperators.push($scope.operators);
		    					 var valueArr = [];
		    					 $scope.availableValues.push(valueArr);
		    					 $scope.availableOptions.push($scope.options);
		    					 $scope.isShown.push(false);
		    				 }
		    			 }else{
		    				 $scope.searchCriterion.push("");
		    				 $scope.availableOperators.push($scope.operators);
		    				 var valueArr = [];
	    					 $scope.availableValues.push(valueArr);
	    					 $scope.availableOptions.push($scope.options);
	    					 $scope.isShown.push(false);
		    			 }
	    			 }
		    	 }
		    	 $scope.isShown.pop();
		    	
		    	 $scope.updateAvailableOptions = function(index, isFirst){
		    		 var selectedOptions = [];//{value:'name', occurs: }
		    		 if($scope.options && $scope.options.length > 0){
		    			 $.each($scope.valueOptions, function(i, a){ 
		    				 if(a){
				    			 var v = a.value;
				    			 if(v){
					    			 var s=  $.grep(selectedOptions, function(e){ return e.value === v });
				    			
					    			 if(s.length ===1){
					    				s[0].occurs += 1;
					    			 }else{
					    				 selectedOptions.push({value: v, occurs:1});
					    			 }
				    			 }
				    			 var o=  $.grep($scope.options, function(e){ return e.value === v });
				    			 if(o.length ===1){
				    				if("operators" in o[0]){
				    					$scope.availableOperators[i]=o[0].operators;
				    					$scope.insertEmptyOption($scope.availableOperators[i]);
				    				}else{
				    					$scope.availableOperators[i]=$scope.operators;
				    				}
				    				if("values" in o[0]){
				    					$scope.availableValues[i]=o[0].values;
				    				}else{
				    					$scope.availableValues[i]=[];
				    				}
				    			 }
			    			 }
			 			});
		    			for(var j = 0 , l = $scope.availableOptions.length; j<l ; j++){
			    			 var newOptions = [];
			    			 var aoi = 0;
			    			 $.each($scope.options, function(i, a){ 
				    			 var s=  $.grep(selectedOptions, function(e){ return e.value === a.value });
				    			 var maxOccurs = "maxOccurs" in a ? a.maxOccurs :1;
				    			 if(s.length > 0 && s[0].occurs >= maxOccurs && ($scope.valueOptions[j]  && $scope.valueOptions[j].value !== s[0].value)){// reach limit 
				    			 }else{
				    				 newOptions.push({index: aoi, value: a.value, title: a.title, alias:a.alias});
				    				 aoi += 1;
				    			 }
			    			 });
			    			 $scope.availableOptions[j] = newOptions;
			    			 if(isFirst){
			    				 if($scope.availableValues[j].length>0)
				    				 $scope.searchValue[j]= $scope.availableValues[j][0];
				    			 else
				    				 $scope.searchValue[j]="";
			    				 
			    				 $scope.compareOptions[j]= $scope.availableOperators[j][0];
			    			 }
			 			};
		    			
			    	 }
		    		 if(typeof index !== 'undefined' && !isFirst){
		    			 if($scope.availableValues[index].length>0)
		    				 $scope.searchValue[index]= $scope.availableValues[index][0];
		    			 else
		    				 $scope.searchValue[index]="";
		    		 }
		    	 };
		    	 
		    	 $scope.updateAvailableOptions(undefined, true);
		    	 $.each($scope.availableOptions, function(j, a){ 
		    		 $scope.valueOptions[j]= a[0];
		    	 });
		    	$scope.addSearchCriteria = function(index){
		    		for(var i = 0 , l =$scope.isShown.length; i<l; i++ ){
		    			if(!$scope.isShown[i]){
		    				$scope.isShown[i]= true;
		    				break;
		    			}
		    			
		    		}
		    	};
		    	
		    	$scope.removeSearchCriteria = function(index){
		    		$scope.isShown[index]= false;
		    		
		    		
			   	 	$scope.valueOptions[index]=$scope.availableOptions[index][0];
			   		$scope.compareOptions[index]=$scope.availableOperators[index][0];
			   		$scope.searchValue[index]="";
		    	};
			    $scope.search = function() {
			    	//remove empty criteria
					var vo =[];
		    		var co =[];
		    		var sv =[];
			    	for(var i = 0 , l=$scope.valueOptions.length; i<l ; i++){
			    		if($scope.valueOptions[i].value==='' || $scope.compareOptions[i].value==='' ||(typeof $scope.searchValue[i] ==='string' && $scope.searchValue[i] ==='') || $scope.searchValue[i].value === ''){
			    		}else{
			    			vo.push($scope.valueOptions[i]);
			    			co.push($scope.compareOptions[i]);
			    			sv.push($scope.searchValue[i]);
			    		}
			    	}
			    	$scope.updateparent({filter: { valueOptions: vo, compareOptions: co, searchValue :sv }  });
			    };
			    
			    $scope.$watchCollection("valueOptions", function(collection, oldValue ){
			    	  if(collection) {
			    		  var index;
			    		  for(var i = 0 , l =collection.length; i<l; i++ ){
				    			if(!oldValue[i] || oldValue[i].value!=collection[i].value){
				    				index = i;
				    				break;
				    			}
				    			
				    		}
			    		  $scope.updateAvailableOptions(index);
			    		  }
			    		}, true);
				
		     }
		}
	  
	  });
   
 app.directive("selectUser", function() {
		return {
			// replace custom element with html5 markup
		    template: '<div>		'
		    	+ '<table style="width:660px; margin-left:-12px;" >'
		    	+ '<thead>'
		    	+ '  <tr >'
		    	+ '   <th style="width:325px;">{{availableTitle}}</th> '
		    	+ '    <th style="width:10px;"></th>        '
		    	+ '    <th style="width:325px;">{{userTitle}}</th> '        
		    	+ '</tr>'
		    	+ '</thead>'
		    	+ '<tbody>'
		    	+ '<tr>'
		    	+ ' <td style="width:325px;">'
		    	+ '  <div class="ebz-listbox">'
		    	+ '   <label ng-repeat="canditateId in canditateIds track by canditateId.ociUserId" style="display:block;"> '
		    	+ '       <input type="checkbox" style="margin-top: 10px;" ng-model="canditateId.available" att-checkbox ng-change="checkCanditate($index)"/> {{canditateId.firstName}} {{canditateId.lastName}}({{canditateId.phone}})<br/>'
		    	+ '   </label>'
		    	+ '  </div>'
		    	+ '</td>'
		    	+ '<td valign="middle" width="10px">'
		    	+ '      <img src="static/images/rightarrow_g.png" id="removeBtn" ng-click="chooseSelected()" ng-hide="oneMax && chosenIds.length==1"/>'
		    	+ '		<img src="static/images/leftarrow_g.png" id="chooseBtn"  ng-click="removeSelected()" ng-show="oneMax && chosenIds.length==1"/>'
		    	+ '     <br/><br/> '
		    	+ '     <img src="static/images/leftarrow_g.png" id="chooseBtn"  ng-click="removeSelected()" ng-hide="oneMax"/>'
		    	+ '      <br/><br/>'
		    	+ '      <img  id="chooseAllBtn"  src="static/images/rightrightarrow_g.png"  ng-click="chooseAll()" ng-hide="oneMax"/>'
		    	+ '     <br/><br/>'
		    	+ '    <img style="cursor:pointer" id="removeAllBtn" src="static/images/leftleftarrow_g.png"  ng-click="removeAll()" ng-hide="oneMax"/>'
		    	+ '</td>'
		    	+ '<td style="width:325px;">'
		    	+ '    <div class="ebz-listbox" >'
		    	+ '	   <label ng-repeat="chosenId in chosenIds" style="display:block;">'
		    	+ '	   		 <input type="checkbox" style=" margin-top :10px;" att-checkbox ng-model="chosenId.available"/> {{chosenId.firstName}} {{chosenId.lastName}}({{chosenId.phone}})<br/>'
		    	+ '        </label>'
		    	+ ' 	</div>'
		    	+ '</td> '
		    	+ ' </tr>'
		    	+ '</tbody>'
		    	+ '</table>'	
		    	+ '</div>',
		    replace: true,
		    // restrict usage to element only since we use attributes for APIs
		    restrict: 'EA',
		    // new isolate scope
		    scope: {
		    	chosenIds  : '=assignedUsers'	
	    		,availableUsers : '='	
	    		,userTitle : '='	
	    		,availableTitle : '='	
	    		,oneMax : '=?'
		      },
		     link: function(scope, iElement, attrs){ 
		    	
		    	 scope.$watch("userTitle", function(newval, oldval) {
		    		 scope.userTitle = newval;
		    	 	});
		    	 scope.$watch("availableTitle", function(newval, oldval) {
		    		 scope.availableTitle = newval;
		    	 	});
		    	 
		    	 scope.$watch("availableUsers", function(newval, oldval) {
		    		 scope.availableUsers = newval;
		    		 scope.canditateIds= scope.getArrayRemoved(scope.availableUsers, scope.chosenIds);
		    	 	});
		    	 
		     	},
		     
		     controller: function ($scope) {
		    	 $scope.getArrayRemoved = function(from, removed){
		    			var retArray = (from)? from:[];
		    			if(retArray && retArray.length >0 && removed){
		    				for(var i = 0, l = removed.length; i<l; i++ ){
		    					retArray = $.grep(retArray, function(e){ return e.ociUserId !== removed[i].ociUserId; });
		    				};
		    			}
		    			return retArray;
		    		};
		    	 $scope.canditateIds= $scope.getArrayRemoved($scope.availableUsers, $scope.chosenIds);
		    	 $scope.chooseAll = function() {
		    		 
		    		 var dataFromTableData = $scope.canditateIds;
		    			 for (var i = dataFromTableData.length - 1; i >= 0; i--){	
		    				 if(!$scope.chosenIds)
		    		    		 $scope.chosenIds=[];
		    				  $scope.chosenIds.push(dataFromTableData[i]);
		    			      $scope.canditateIds.splice(i,1);
		    				  
		    			   }
		    		var dataFromChoosonTable = $scope.chosenIds;	
		 				for (var i = dataFromChoosonTable.length - 1; i >= 0; i--){
		 				    if(dataFromChoosonTable[i].available){
		 				    	$scope.chosenIds[i].available=false;
		 				    }
		 				}
		    	 };
		    	
		    	$scope.removeAll = function() {
		    		var dataFromTableData = $scope.chosenIds;
		    		for (var i = dataFromTableData.length - 1; i >= 0; i--){	 
		    			  $scope.canditateIds.push(dataFromTableData[i]);
		    		      $scope.chosenIds.splice(i,1);
		    			  
		    		   } 
		    	var dataFromChoosonTable = $scope.canditateIds;	
		 			for (var i = dataFromChoosonTable.length - 1; i >= 0; i--){
		 			    if(dataFromChoosonTable[i].available){
		 			    	$scope.canditateIds[i].available=false;
		 			    }
		 			}
		    	 };
		    	 
		    	$scope.chooseSelected = function() {
		    		
		    		   	var dataFromTableData = $scope.canditateIds;
		    		   	
		    		   			for (var i = dataFromTableData.length - 1; i >= 0; i--){	 
		    		   	  
		    	    		  if(dataFromTableData[i].available){
		    	    			  if(!$scope.chosenIds)
		    	 		    		 $scope.chosenIds=[];
		    	    			  $scope.chosenIds.push(dataFromTableData[i]);
		    	    			  $scope.canditateIds.splice(i,1);
		    	    			  
		    	    		 } 
		    	    	 }
		    		   	var dataFromChoosonTable = $scope.chosenIds;	
		    		   			for (var i = dataFromChoosonTable.length - 1; i >= 0; i--){
		    		   			    if(dataFromChoosonTable[i].available){
		    		   			    	$scope.chosenIds[i].available=false;
		    		   			    }
		    		   			}
		    	 };
		    	 $scope.removeSelected = function() {
		    			
		    			 var dataFromTableData = $scope.chosenIds;
		    		 	
		    		 		for (var i = dataFromTableData.length - 1; i >= 0; i--){	 
		    		 		  	if(dataFromTableData[i].available){
		    		 			  	$scope.canditateIds.push(dataFromTableData[i]);
		    		 			  	$scope.chosenIds.splice(i,1);
		    		 			
		    			 		 } 
		    			 	 } ;
		    			  var dataFromChoosonTable = $scope.canditateIds;	
		 			 		for (var i = dataFromChoosonTable.length - 1; i >= 0; i--){
		 			 		    if(dataFromChoosonTable[i].available){
		 			 		    	$scope.canditateIds[i].available=false;
		 			 		    }
		 			 		}
		    	 };
		    	 $scope.checkCanditate = function(index) {
		    		 if($scope.oneMax){
		    			 for (var i = $scope.canditateIds.length - 1; i >= 0; i--){	
		    				 if(i != index)
		    			      $scope.canditateIds[i].available=false;
		    			 }
		    		 }
		    	 };
		    	
		    	 
		     }
		}
	});
 app.directive("selectString", function() {
		return {
			// replace custom element with html5 markup
		    template: '<div>		'
		    	+ '<table style="width:660px;">'
		    	+ '<thead>'
		    	+ '  <tr >'
		    	+ '   <th style="width:325px;">{{availableTitle}}</th> '
		    	+ '    <th style="width:10px;"></th>        '
		    	+ '    <th style="width:325px;">{{assignedTitle}}</th> '        
		    	+ '</tr>'
		    	+ '</thead>'
		    	+ '<tbody>'
		    	+ '<tr>'
		    	+ ' <td style="width:325px;">'
		    	+ '  <div class="ebz-listbox">'
		    	+ '   <label ng-repeat="canditateId in canditateIds track by $index" style="display:block;"> '
		    	+ '       <input type="checkbox" style="margin-top: 10px;" ng-model="canditateId.available" att-checkbox /> {{canditateId}}<br/>'
		    	+ '   </label>'
		    	+ '  </div>'
		    	+ '</td>'
		    	+ '<td valign="middle" width="10px">'
		    	+ '      <img src="static/images/rightarrow_g.png" id="removeBtn" ng-click="chooseSelected()" />'
		    	+ '     <br/><br/> '
		    	+ '     <img src="static/images/leftarrow_g.png" id="chooseBtn"  ng-click="removeSelected()" />'
		    	+ '      <br/><br/>'
		    	+ '      <img  id="chooseAllBtn"  src="static/images/rightrightarrow_g.png"  ng-click="chooseAll()" />'
		    	+ '     <br/><br/>'
		    	+ '    <img style="cursor:pointer" id="removeAllBtn" src="static/images/leftleftarrow_g.png"  ng-click="removeAll()" />'
		    	+ '</td>'
		    	+ '<td style="width:325px;">'
		    	+ '    <div class="ebz-listbox" >'
		    	+ '	   <label ng-repeat="chosenId in chosenIds" style="display:block;">'
		    	+ '	   		 <input type="checkbox" style=" margin-top :10px;" att-checkbox ng-model="chosenId.available"/> {{chosenId}}<br/>'
		    	+ '        </label>'
		    	+ ' 	</div>'
		    	+ '</td> '
		    	+ ' </tr>'
		    	+ '</tbody>'
		    	+ '</table>'	
		    	+ '</div>',
		    replace: true,
		    // restrict usage to element only since we use attributes for APIs
		    restrict: 'EA',
		    // new isolate scope
		    scope: {
		    	chosenIds  : '=assignedOptions'	
	    		,availableOptions : '='	
	    		,availableTitle : '@'	
	    		,assignedTitle : '@'	
		      },
		     link: function(scope, iElement, attrs){ 
		    	 scope.$watch("availableOptions", function(newval, oldval) {
		    		 scope.availableOptions = newval;
		    		 scope.canditateIds= scope.getArrayRemoved(scope.availableOptions, scope.chosenIds);
		    	 	});
		    	 
		     	},
		     
		     controller: function ($scope) {
		    	 $scope.getArrayRemoved = function(from, removed){
		    			var retArray = (from)? from:[];
		    			if(retArray && retArray.length >0 && removed){
		    				for(var i = 0, l = removed.length; i<l; i++ ){
		    					retArray = $.grep(retArray, function(e){ return e !== removed[i]; });
		    				};
		    			}
		    			return retArray;
		    		};
		    	 
		    	 $scope.canditateIds= $scope.getArrayRemoved($scope.availableOptions, $scope.chosenIds);
		    	 $scope.chooseAll = function() {
		    		 
		    		 var dataFromTableData = $scope.canditateIds;
		    			 for (var i = dataFromTableData.length - 1; i >= 0; i--){	
		    				 if(!$scope.chosenIds)
		    		    		 $scope.chosenIds=[];
		    				  $scope.chosenIds.push(dataFromTableData[i]);
		    			      $scope.canditateIds.splice(i,1);
		    				  
		    			   } 
		    	 };
		    	
		    	$scope.removeAll = function() {
		    		var dataFromTableData = $scope.chosenIds;
		    		for (var i = dataFromTableData.length - 1; i >= 0; i--){	 
		    			  $scope.canditateIds.push(dataFromTableData[i]);
		    		      $scope.chosenIds.splice(i,1);
		    			  
		    		   } 
		    	 };
		    	 
		    	$scope.chooseSelected = function() {
		    		
		    		   	var dataFromTableData = $scope.canditateIds;
		    		   	
		    		   			for (var i = dataFromTableData.length - 1; i >= 0; i--){	 
		    		   	  
		    	    		  if(dataFromTableData[i].available){
		    	    			  if(!$scope.chosenIds)
		    	 		    		 $scope.chosenIds=[];
		    	    			  $scope.chosenIds.push(dataFromTableData[i]);
		    	    			  $scope.canditateIds.splice(i,1);
		    	    			  
		    	    		 } 
		    	    	 } 
		    	 };
		    	 $scope.removeSelected = function() {
		    			
		    			 var dataFromTableData = $scope.chosenIds;
		    		 	
		    		 		for (var i = dataFromTableData.length - 1; i >= 0; i--){	 
		    		 		  	if(dataFromTableData[i].available){
		    		 			  	$scope.canditateIds.push(dataFromTableData[i]);
		    		 			  	$scope.chosenIds.splice(i,1);
		    		 			
		    			 		 } 
		    			 	 } ;
		    	 };
		    	 
		     }
		}
	});
	function getParameterByName(name) {
		name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
		var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
		    results = regex.exec(location.search);
		return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	};

 app.directive("time", function() {
		return {
			// replace custom element with html5 markup
		    template: '<div style="height:80px; display:inline-block;">		'
		    	+ '         <input type="text" class="fn-ebz-text" ng-model="timeStr"  placeholder="HH:MM" style="width:70px" ng-change="parseValue()"/>'
		    	+ '        <div class="form-field" att-select="ampmOptions" ng-model="ampm" style="width:70px" ng-change="parseValue()"></div>'
		    	+ ' 	</div>',
		    replace: true,
		    // restrict usage to element only since we use attributes for APIs
		    restrict: 'EA',
		    // new isolate scope
		    scope: {
		    	value  : '='
		    	,validTime : '=?'
		      },
		     link: function(scope, iElement, attrs){ 
		     	},
		     
		     controller: function ($scope) {
		    	 $scope.timeStr='';
		    	 $scope.ampm=null;
		    	 var am ={index: 0, value: 'AM', title: 'AM', alias:'Name2'};
		    	 var pm = {index: 1, value: 'PM', title: 'PM', alias:'Name'};
		    	 $scope.ampmOptions=[
		    	                    am,
		    	                    pm,
									]
		    	 $scope.parseTimeStr= function(str){
		    		 if(str){
		    			try{
		    				var date;
		    				if(/^(\d*):(\d*)$/.test(str)){
		    					var hh = Number(str.match(/^(\d+)/)[1]);
		    					var mm = Number(str.match(/:(\d+)/)[1]);
				    			 date = new Date(1970, 0, 1, hh, mm, 0);
		    				}else{
		    					date = new Date(str);
		    				}
			    			  var hours = date.getHours();
			    			  var minutes = date.getMinutes();
			    			  var ampm = hours >= 12 ?pm : am;
			    			  hours = hours % 12;
			    			  hours = hours ? hours : 12; // the hour '0' should be '12'
			    			  minutes = minutes < 10 ? '0'+minutes : minutes;
			    			  hours = hours < 10 ? '0'+hours : hours;
			    			  var strTime = hours + ':' + minutes ;
			    			 
			    			 
			    			   $scope.timeStr =strTime;
			    			   $scope.ampm = ampm;    
			    			   if (typeof $scope.validTime != 'undefined'){
				    				 $scope.validTime=true;
				    			 }
		    			}catch(err){
		    				if (typeof $scope.validTime != 'undefined'){
			    				 $scope.validTime=false;
			    			 }
		    			}
		    		 }else{
		    			$scope.timeStr = "12:00";
		    			$scope.ampm =am;
		    		 }
		    	 };
		    	 $scope.parseTimeStr($scope.value);
		    	$scope.parseValue = function(){
		    		try{
	    				var date;
    					var hh = Number($scope.timeStr.match(/^(\d+)/)[1]);
    					var mm = Number($scope.timeStr.match(/:(\d+)/)[1]);
    					
    					if(hh<=12 && hh>0 && mm>=0 && mm<=59){
    						
		    			 if($scope.ampm.value=='PM'&& hh<12) hh = hh+12;
		    			 if($scope.ampm.value=='AM'&& hh==12) hh = hh-12;
		    			 mm = mm < 10 ? '0'+mm : mm;
		    			 hh = hh < 10 ? '0'+hh : hh;
		    			 $scope.value=	hh+":"+mm; 
		    			 if (typeof $scope.validTime != 'undefined'){
   		    				 $scope.validTime=true;
   		    			 }
    					}else{
    						if (typeof $scope.validTime != 'undefined'){
	   		    				 $scope.validTime=false;
	   		    			 }
    					}
	    				
		    		}catch(err){
		    			if (typeof $scope.validTime != 'undefined'){
		    				 $scope.validTime=false;
		    			 }
	    			}
		    	};
		     }
		}
	});