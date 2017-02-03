angular.module("modalServices",[]).service('modalService', ['$modal', function ($modal) {
	
														
        	this.showSuccess = function(heading, messageBody){
        		var modalInstance = $modal.open({
        	        templateUrl: 'modal_informative.html',
        	        controller: 'modalpopupController',
        	        resolve: {
        	           message: function () {
        	        		$(".overlayed").css("display","none");
                		  	$(".loadingId").css("display","none");
        	        	   var message = {
        	        			   title:    heading,
                              		text:     messageBody
                              	};
     			          return message;
   			        	}
        	        }
        	      }); 
        	};
        	this.showFailure = function(heading, messageBody){
        	var modalInstance = $modal.open({
        	        templateUrl: 'modal_warning.html',
        	        controller: 'modalpopupController',
        	        resolve: {
         	           message: function () {
         	        	  var message = {
                               	 title:    heading,
                               	 text:     messageBody
                               	};
      			          return message;
    			        	}
         	        }
        	      }); 
        	};
        	
        	this.showWarning = function(heading, messageBody){
            	var modalInstance = $modal.open({
            	        templateUrl: 'modal_warning_message.html',
            	        controller: 'modalpopupController',
            	        resolve: {
             	           message: function () {
             	        	  var message = {
                                   	 title:    heading,
                                   	 text:     messageBody
                                   	};
          			          return message;
        			        	}
             	        }
            	      }); 
            	};
        	
        	this.popupConfirmWin = function(title, msgBody, callback,dismissCallback){
   			 var modalInstance = $modal.open({
   	 	        templateUrl: 'confirmation_informative.html',
   	 	        controller: 'modalpopupController',
   	 	        resolve: {
   	 	           message: function () {
   	 	        	   var message = {
   	 	        			   title:    title,
   	                       		text:    msgBody
   	                       	};
   				          return message;
   			        	}
   	 	        }
   	 	      }); 
   			 var args = Array.prototype.slice.call( arguments, 0 );
   		     args.splice( 0, 3); 
   	 		modalInstance.result.then(function(){
   		 			callback.apply(null, args);
   	 			}, function() {
   	 		  })['finally'](function(){
   	 		   modalInstance = undefined;
   	 		  });
   			
   		};
   		this.popupConfirmWinWithCancel = function(title, msgBody, callback,dismissCallback){
  			 var modalInstance = $modal.open({
  	 	        templateUrl: 'confirmation_informative.html',
  	 	        controller: 'modalpopupController',
  	 	        resolve: {
  	 	           message: function () {
  	 	        	   var message = {
  	 	        			   title:    title,
  	                       		text:    msgBody
  	                       	};
  				          return message;
  			        	}
  	 	        }
  	 	      }); 
  			 var args = Array.prototype.slice.call( arguments, 0 );
  		     args.splice( 0, 3); 
  	 		modalInstance.result.then(function(){
  		 			callback.apply(null, args);
  	 			}, function() {
  	 			  dismissCallback();
  	 		  })['finally'](function(){
  	 		   modalInstance = undefined;
  	 		  });
  			
  		};
        this.popupDeleteConfirmWin = function(title, msgBody, callback, argForCallBack){
   			 var modalInstance = $modal.open({
   	 	        templateUrl: 'confirmation_for_delete.html',
   	 	        controller: 'modalpopupController',
   	 	        resolve: {
   	 	           message: function () {
   	 	        	   var message = {
   	 	        			   title:    title,
   	                       		text:    msgBody
   	                       	};
   				          return message;
   			        	}
   	 	        }
   	 	      }); 
   			 
   	 		modalInstance.result.then(function(){
   		 			callback(argForCallBack);
   	 			}, function() {
   	 		  })['finally'](function(){
   	 		   modalInstance = undefined;
   	 		  });
   			
   		};		
   		
   	 this.popupSuccessRedirectWin = function(title, msgBody, redirectUrl){
   		var modalInstance = $modal.open({
	        templateUrl: 'modal_informative.html',
	        controller: 'modalpopupController',
	        resolve: {
	           message: function () {
	        	   var message = {
	        			   title:    title,
	                       text:    msgBody
                      	};
			          return message;
		        	}
	        }
	      }); 
		modalInstance.result.then(function() {
		  }, function() {
			  window.location.href=redirectUrl;
		  })['finally'](function(){
		   modalInstance = undefined;
		  });	
   	};	
   	
   	this.popupWarningRedirectWin = function(title, msgBody, redirectUrl){
   		var modalInstance = $modal.open({
   			templateUrl: 'modal_warning_message.html',
	        controller: 'modalpopupController',
	        resolve: {
	           message: function () {
	        	   var message = {
	        			   title:    title,
	                       text:    msgBody
                      	};
			          return message;
		        	}
	        }
	      }); 
		modalInstance.result.then(function() {
		  }, function() {
			  window.location.href=redirectUrl;
		  })['finally'](function(){
		   modalInstance = undefined;
		  });	
   	};	
 }]);