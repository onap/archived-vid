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
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<%@ page import="java.net.*" %>


<script src='dwr/engine.js'></script>
<script src='dwr/util.js'></script>
<script src='dwr/interface/fusionAjax.js'></script>

<%@ include file="/WEB-INF/fusion/jsp/include.jsp" %>

<%
   String url = request.getParameter("returnUrl");

   if (url != null) {
     request.setAttribute("returnUrl", URLDecoder.decode(url, "UTF-8"));
   }

%>

<BODY height="100%">
<form name="listForm" action="<%=request.getAttribute("returnUrl")%>" method="POST" target="_parent">
  <input type="hidden" name="selected" value=""/>
</form>


<!-- Submit record action functionality -->
<script type="text/javascript">
  function submitAction(actionUrl, task, actionUrlParameters) {

   var formFieldParameters = "";	  
   var actionUrlHtml       = actionUrl;
   
   <!-- Keep track of form field params to ensure the refreshed list matches  -->
   <c:if test="${(param['reset_action'] ne 'Y') && (param['reset_action'] ne 'y')}">
     <c:forEach items="${param}" var="request_param">
 	  <c:if test="${fn:startsWith(request_param.key,\"ff\")}">   
	    <c:if test="${!empty request_param.value}">
	      formFieldParameters += "&${request_param.key}=${v:decodeUrl(request_param.value)}";
	    </c:if>   
	  </c:if> 
   </c:forEach>
 </c:if>   

   if (task != null) {
     actionUrlHtml += "?task=" + task;
   }	     

   if (actionUrlParameters != null) {
    actionUrlHtml += "&" + actionUrlParameters; 
   }	     

   
   document.listForm.action = actionUrlHtml + formFieldParameters; 
   document.listForm.submit();	  
  }
</script>
  
<!-- Select Profile functionality -->
<script type="text/javascript">
  function submitSelection() {
    var selectedProfiles = document.listForm.selected;
    var      allProfiles = document.formd.selected;
    var          numRows = null;

    if (allProfiles) {
      numRows = allProfiles.length;

      if (numRows) {
        for (i=0; i < numRows; i++) {
          if (allProfiles[i].checked) {
            selectedProfiles.value += allProfiles[i].value + ",";
          }
        }

        if (selectedProfiles.value != '') {
          selectedProfiles.value = selectedProfiles.value.substring(0, selectedProfiles.value.length-1);
        }
      }
      else {
        if (allProfiles.checked) {
          selectedProfiles.value += allProfiles.value;
        }
      }
    }

    //alert(selectedProfiles.value);
    document.listForm.submit();
  }
</script>


<!-- Profile Search activation/deactivation functionality -->
<script type="text/javascript">
  function toggleUserActive(element, userId) {
    var user_id = "'"+${user.id}+"'";
	if(user_id.length == 0) user_id = 1;
	else
    fusionAjax.toggleUserActive(${user.id}, userId, toggleUserActiveCB);
    return false;
  }

  function toggleUserActiveCB(data) {
    var element = document.getElementById('user_active_' + data);
    if(element.alt == "Activate") {
      element.src = "static/fusion/images/active.gif";
      element.alt = "Inactivate";
    }
    else {
      element.src = "static/fusion/images/inactive.gif";
      element.alt = "Activate";
    }
  }
</script>
