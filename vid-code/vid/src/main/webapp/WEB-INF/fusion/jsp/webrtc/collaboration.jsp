<%--
  ================================================================================
  eCOMP Portal SDK
  ================================================================================
  Copyright (C) 2017 AT&T Intellectual Property
  ================================================================================
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  ================================================================================
  --%>


<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" ng-app="abs">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<link type="text/css" rel="stylesheet" href="app/fusion/styles/layout/layout-default-latest.css" />
    <link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/sandbox/styles/btn.css" >
    <link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/fn-ebz.css" >
		<style type="text/css">
	/*
	 *	NOTE: All CSS is purely cosmetic - it does not affect functionality
	 *  http://layout.jquery-dev.com/demos.cfm
	 */

	/* customize borders to avoid double-borders around inner-layouts */
	.ui-layout-pane {
		border:			0; /* override layout-default-latest.css */
		border-top:		1px solid #BBB;
		border-bottom:	1px solid #BBB;
	}
	.ui-layout-pane-north ,
	.ui-layout-pane-south {
		border:			1px solid #BBB;
		overflow:		auto;
	}
	.ui-layout-pane-west ,
	.ui-layout-pane-east {
	}
	.ui-layout-pane-center	{
		border-left:	0;
		border-right:	0;
		}
		.inner-center {
			border:		1px solid #BBB;
		}

	/* add shading to outer sidebar-panes */
	.outer-west ,
	.outer-east {
		background-color: #EEE;
	}
	.middle-west ,
	.middle-east {
		background-color: #F8F8F8;
	}

	/* remove padding & scrolling from panes that are 'containers' for nested layouts */
	.outer-center ,
	.middle-center {
		border:			0; /* cosmetic */
		padding:		0;
		overflow:		auto;
	}

	/*
	 *	customize borders on panes/resizers to make pretty
	 */
	.ui-layout-pane-west		{ border-right:		0; }
	.ui-layout-resizer-west		{ border-left:		1px solid #BBB; }
	.ui-layout-pane-east		{ border-left:		0; }
	.ui-layout-resizer-east		{ border-right:		1px solid #BBB; }
	.ui-layout-pane-north		{ border-bottom:	0; }
	.ui-layout-resizer-north	{ border-top:		1px solid #BBB; }
	.ui-layout-pane-south		{ border-top:		0; }
	.ui-layout-resizer-south	{ border-bottom: 	1px solid #BBB; }
	/*
	 *	add borders to resizers when pane is 'closed'
	 *
	 *.ui-layout-resizer-closed	{ border:			1px solid #BBB; }
	 */
	/*
	 *	show both borders when the resizer is 'dragging'
	 */
	.ui-layout-resizer-west-dragging ,
	.ui-layout-resizer-east-dragging {
		border-left:		1px solid #BBB;
		border-right:		1px solid #BBB;
	}
	.ui-layout-resizer-north-dragging ,
	.ui-layout-resizer-south-dragging {
		border-top:		1px solid #BBB;
		border-bottom:	1px solid #BBB;
	}
	
	
	/*
	layout toggler background image
	*/
	.ui-layout-toggler-west, .ui-layout-toggler-east {
	border-width: 1px 0;
	background-image: url("static/fusion/images/layout/panel-e-w-toggle.png");
	background-size: 10px 10px;
    background-repeat: no-repeat;
    background-position: center; 
	}
	
	.ui-layout-toggler-north, .ui-layout-toggler-south {
	border-width: 0 1px;
	background-image: url("static/fusion/images/layout/panel-n-s-toggle.png");
	background-size: 10px 10px;
	background-repeat: no-repeat;
	 background-position: center; 
	}
	
	body{background-color:#fff;font-size:14px;font-size:.875rem;margin:0;padding:0px 0 20px;position:relative}

	</style>


	<!-- LAYOUT v 1.3.0 -->
	<script type="text/javascript" src="app/fusion/scripts/layout/jquery-latest.js"></script>
	<script type="text/javascript" src="app/fusion/scripts/layout/jquery-ui-latest.js"></script>
	<script type="text/javascript" src="app/fusion/scripts/layout/jquery.layout-latest.js"></script>
	<script type="text/javascript" src="app/fusion/scripts/webrtc/RTCMultiConnection.js"></script>
    <script type="text/javascript" src="app/fusion/scripts/socket/peerBroadcast.js"></script>
	<script type="text/javascript" src="app/fusion/scripts/layout/debug.js"></script>
	<link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/fn-ebz.css" >
	<link rel="stylesheet" type="text/css" href="app/fusion/external/ebz/sandbox/styles/style.css" >

	<script src= "app/fusion/external/ebz/angular_js/angular.js"></script> 
	<script src= "app/fusion/external/ebz/angular_js/angular-route.min.js"></script>
	<script src= "app/fusion/external/ebz/angular_js/angular-sanitize.js"></script>
	<script src= "app/fusion/external/ebz/angular_js/app.js"></script>
	<script src= "app/fusion/external/ebz/angular_js/gestures.js"></script>
	
	<%@ include file="/WEB-INF/fusion/jsp/popup_modal.html" %>
	<script src="app/fusion/scripts/modalService.js"></script>
	<script src="app/fusion/external/ebz/sandbox/att-abs-tpls.js" type="text/javascript"></script>
	<script src="app/fusion/scripts/att_angular_gridster/ui-gridster-tpls.js"></script>
	<script src="app/fusion/scripts/att_angular_gridster/angular-gridster.js"></script>
	<script src= "app/fusion/external/ebz/angular_js/checklist-model.js"></script>
	<style>
		body{background-color:#fff;font-size:14px;font-size:.875rem;margin:0;padding:0px 0 20px;position:relative}
	</style>

	<script type="text/javascript">

	
	var popupModalService;
	
	app.controller("collaborationController", function ($scope,$http,modalService, $modal) { 
		popupModalService = modalService;
		   
		});
	

	$(document).ready(function () {

		// OUTER-LAYOUT
		panelLayout = $('body').layout({
			center__paneSelector:	".outer-center"
		,	west__paneSelector:		".outer-west"
		,	east__paneSelector:		".outer-east"
		//,	west__size:				800
		//,	east__size:				125
		,	spacing_open:			8  // ALL panes
		,	spacing_closed:			12 // ALL panes
	
		,	center__childOptions: {
			center__paneSelector:	".inner-center"
		,	west__paneSelector:		".inner-west"
		,	east__paneSelector:		".inner-east"
		,	west__size:				75
		,	east__size:				75
		,	spacing_open:			8  // ALL panes
		,	spacing_closed:			8  // ALL panes
		,	west__spacing_closed:	12
		,	east__spacing_closed:	12
		}

		 

		
		});
		
	
		function initializeConnections() {
	    	
	    	var channelId = null;
			channelId = location.href.replace(/\/|:|#|%|\.|\[|\]/g, '');
			var videoChannelId = channelId.concat("video");
			var screenChannelId = channelId.concat("screen");
			
			videoConnection = new RTCMultiConnection(videoChannelId);
			screenConnection = new RTCMultiConnection(screenChannelId);
			
			configConnection(videoConnection,true,true,false,true,false);
		    configConnection(screenConnection,false,false,true,false,true);
	    	
	    };
	    
	    function configConnection(_connection, _audio, _video, _screen, _data, _oneway) {
	    	 _connection.session = {
	                 audio:     _audio, // by default, it is true
	                 video:     _video, // by default, it is true
	                 screen:    _screen,
	                 data:      _data,
	                 oneway:    _oneway,
	                 broadcast: false
	            };
	    	 
	    	 _connection.direction = "one-to-one";
	    	 
	    	 if( _data == true ) {
	    	 _connection.onmessage = function(e) {
	             appendDIV(e.data);

	             console.debug(e.userid, 'posted', e.data);
	             console.log('latency:', e.latency, 'ms');
	         };
	    	 }
	    
	     
	    }; 
	    
	    function assignStreamToDom() {
	    	
	    	
	    	screenConnection.screenbody = document.querySelector('.screenContainer1'); 
	    	screenConnection.videobody = document.querySelector('.videoContainer2'); 
	    	
	    	videoConnection.screenbody = document.querySelector('.screenContainer2'); 
	    	videoConnection.videobody = document.querySelector('.videoContainer1'); 
	    };
	    
		function maximizeLayout() {
	    	
			// open the panes and maximize the window.
		     top.window.resizeTo(screen.availWidth,screen.availHeight);
		     panelLayout.open('west');
		     // panelLayout.open('south'); is not working due to state initialization problem; debug to find out. so replacing the call with work around below - hack.
	    	 $(".ui-layout-toggler-south-closed").first().click();
		     
		 };
	    
	   function minimizeLayout() {
	    	
			// close the panes and minimize the window.
		     top.window.resizeTo(screen.availWidth - 2*screen.availWidth/3, screen.availHeight - screen.availHeight/2);
		     panelLayout.close('west');
		     // panelLayout.close('south'); is not working due to state initialization problem; debug to find out. so replacing the call with work around below - hack.
	    	 $(".ui-layout-toggler-south-opened").first().click();
	    };
	    
	    function emptyContainers() {
	    	 $('.screenContainer1').empty(); 
		     $('.videoContainer2').empty(); 
		    	
		     $('.screenContainer2').empty();  
		     $('.videoContainer1').empty(); 
	    };
	    
	    function appendDIV(div, parent) {
	        if (typeof div === 'string') {
	            var content = div;
	            div = document.createElement('div');
	            div.innerHTML =  content;
	          };
	          
	          var chatOutput = document.getElementById('chat-output'),
	            fileProgress = document.getElementById('file-progress');
	    
	          if (!parent) chatOutput.insertBefore(div, chatOutput.firstChild);
	            else fileProgress.insertBefore(div, fileProgress.firstChild);

	            div.tabIndex = 0;
	            $('#chat-input').focus();
	     };
	     
	     function confirmClose() {
	    	 var message = "Are you sure you want to close the session?";
	    	 
	    	 if(popupModalService != undefined) {
	    		 popupModalService.popupConfirmWin("Confirm", message, function(){ location.reload();});
	    	 }
	    	 
	    	 else if (confirm(message) == true) {
	    		     location.reload();
	    		    //window.opener.location.reload(); // go to the parent window
	    			//close();
	    	    } else {
	    	        // do nothing
	    	    }
	    	 
	     };
	     
	     function notifyOthers() {
	    	 
	    	// var websocket = localStorage.getItem('notifySocket');
	    	 //if( websocket != null) {
	    		 // handling websocket peer broadcast session 
	    		 var currentUser = "${sessionScope.user.orgUserId}";
	    		 var initialPageVisit = "${sessionScope.initialPageVisit}";
	    		 var remoteUser = '';
	    		 
	    		 var userList = location.search.split('chat_id=')[1].split('-');
	    		 for(var i=0;i<userList.length;i++) {
	    			 if(userList[i] !== currentUser) {
	    				 remoteUser = userList[i];
	    				 break;
	    			 }
	    		 }
	    		
	    		socketSetup(initialPageVisit, currentUser, remoteUser,"socketSend");
	    		
	    		 
	    		 
	    		 
	    	
	    	 
	     };
	     
	     function makeChatVisible() {
	    	 
	    	 $('#chat-input').css("visibility", 'visible');    	 
	     };
	    
	    
	    
	     /* on click button enabled*/
	     document.getElementById('share-screen').onclick = function() {
	    
	     emptyContainers();	 
	     videoConnection.close();
		 screenConnection.close();	 
	     
	     maximizeLayout();
	     emptyContainers();
	     makeChatVisible();
	     
	     videoConnection.open();
	     screenConnection.open();
	     
	     
	     notifyOthers();
	     
	    
	    
		};

		 document.getElementById('stop-share-screen').onclick = function() {
		 
		 emptyContainers();
		 
		 videoConnection.close();
		 screenConnection.close();
		 
		 confirmClose();
		 
		};

		 document.getElementById('view-screen').onclick = function() {
		
		 maximizeLayout();
		 emptyContainers();
		 makeChatVisible();
		 
		 // timeout is required for the sharing to properly work
		 setTimeout(function() { 
			 screenConnection.connect();
		 },2000);
		 setTimeout(function() { 
			 videoConnection.connect();
		 },1000);
		
		 
		};
		
		document.getElementById('chat-input').onkeypress = function(e) {
	        if (e.keyCode !== 13 || !this.value) return;
	        var message = "<b>${model.name}</b>: " + this.value;
	        appendDIV(message);

	        // sending text message
	        videoConnection.send(message);

	        this.value = '';
	    };
	    
	    /*
	    document.getElementById('file').onchange = function() {
	    	videoConnection.send(this.files[0]);
        };
		*/
		
		
		//document.querySelector('.screenContainerPane').appenChild(document.querySelector('.screenContainer'));
		//document.querySelector('.videoContainerPane').appendChild(document.querySelector('.videoContainer'));
		
		//panelLayout.bindButton($('#share-screen'), 'open', 'outer-west');
		//panelLayout.bindButton($('#stop-share-screen'), 'close', 'outer-west');
		var videoConnection = null, screenConnection = null;
		initializeConnections();
		assignStreamToDom();
		
		// start the share
		//document.getElementById('share-screen').click();
		//
		});
	

</script>


</head>

<body>
<!--  
	<button id="share-screen" hidden="true" style="display: none;" class="setup">Share Your Screen</button>
    <button id="stop-share-screen" hidden="true" style="display: none;" class="setup">Stop Share Your Screen</button>
    <button id="view-screen" hidden="true" style="display: none;" class="setup">View My Screen</button>   
    
    -->    
 
<div class="outer-center" style="position: absolute; left: 12px; right: 0px; top: 0px;bottom: 0px;">
	<div class="inner-center">
	
	<!-- 	<jsp:include page="/WEB-INF/fusion/zul/chatOne.zul" /> -->
	
	
	
	<table style="width: 100%;">
				<tbody><tr>
                    <td>
                        <button id="share-screen"  class="button button--primary button--small setup">Start Session</button>
    				    <button id="stop-share-screen"  class="button button--primary button--small setup">Stop Session</button>
    					<button id="view-screen" class="button button--primary button--small setup">View</button>       
	
                    </td>
    
                </tr>
            </tbody>
	
                <tbody><tr>
                    <td>
                         <input type="text" id="chat-input" style="font-size: 1.2em;visibility:collapse;" placeholder="type here.."/>
                         <div id="chat-output"></div>
                    </td>
                    <!-- 
                    <td style="background: white;">
                        <input type="file" id="file">
                        <div id="file-progress"></div>
                    </td>
                     -->
                </tr>
            </tbody>
    </table>
	
		
	</div>
	<div id="inner-south" class="ui-layout-south">
		<div class="videoContainer1"></div>
		<div class="videoContainer2"></div>
 		
	</div>
</div>

<div class="outer-west">
   <div class="screenContainer1"></div>
   <div class="screenContainer2"></div>
   <div ng-controller="collaborationController">
   </div>
</div>





</body>
</html>
