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
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<%	ArrayList alErrorList = (ArrayList) request.getAttribute(AppConstants.RI_ERROR_LIST);
	if((alErrorList!=null)&&(alErrorList.size()>0)) { %>
<br>
<table width="94%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align="center">
	<tr class=rbg7>
		<td class=rtabletext colspan=2 align=center>
			<b class=rerrortext>Validation Errors Found</b><br>
			Following errors need to be corrected to continue:
		</td>
	</tr>
<%		for(int i=0; i<alErrorList.size(); i++) {
			String sErrorMsg = (String) alErrorList.get(i);
			if(sErrorMsg!=null&&sErrorMsg.indexOf("|")>=0)
				sErrorMsg = sErrorMsg.substring(sErrorMsg.indexOf("|")+1);
			if((i%2)==0) { %>
	<tr class=rbg6>
<%			} %>
		<td class=rtabletext width=50%>
			<font class=rerrortextsm><li class=rerrortextsm><%= sErrorMsg %></font>
		</td>
<%			if((i%2)==1) { %>
	</tr>
<%			}
		}	// for
%>		
<%		if((alErrorList.size()%2)==1) { %>
	<tr> 
		<td class=rtabletext width=50%>
			<font class=rerrortextsm>&nbsp;</font>
		</td>
	</tr>
<%		} %>
</table>
<%	}	// if 
%>

