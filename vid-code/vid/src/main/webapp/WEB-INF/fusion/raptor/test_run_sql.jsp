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
<%@ page import="java.util.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.AppUtils" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.AppConstants" %>

<script language="JavaScript">
  
   function postSQL() {
	 document.formb.<%= AppConstants.RI_FORMATTED_SQL %>.value=window.opener.forma.reportSQL.value;
	 document.formb.submit();
   }

</script>
<body onLoad="postSQL()">
<form name="formb" action="<%= AppUtils.getBaseURL() %>" method="post">
  <input type="hidden" name="action" value="raptor">
  <input type="hidden" name="r_action" value="report.popup.testrun.sql">
  <input type="hidden" name="<%= AppConstants.RI_FORMATTED_SQL %>" value="">
</form>
</body>
