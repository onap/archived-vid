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
<iframe id="frame_insert" src="${frame_int}"></iframe>

<script type="text/javascript">

    var frameId = "#frame_insert";

	$(frameId).load(function(){
		iframeLoaded()
	   
	});
  

  function iframeLoaded() {
	  var iFrameRef =  $(frameId);
      if(iFrameRef) {
    	  
    	  var height = iFrameRef.contents().find("input[name='inner_height']");
    	  var width = iFrameRef.contents().find("input[name='inner_width']");
            
    	  iFrameRef.css('height', (height.val() != undefined ? height.val():"500px"));
    	  iFrameRef.css('width',  (width.val() != undefined ? width.val():"500px"));
    	  iFrameRef.css('border','none');
      }   
  }
</script>   
