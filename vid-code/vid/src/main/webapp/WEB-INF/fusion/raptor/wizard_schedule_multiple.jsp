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
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.base.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.controller.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>

<%@ page errorPage="error_page.jsp" %>
<%! 
class ValueComparator implements Comparator {
	  public int compare(Object o1, Object o2) {
	    Map.Entry e1 = (Map.Entry) o1;
	    Map.Entry e2 = (Map.Entry) o2;
	    Comparable c1 = (Comparable)e1.getValue();
	    Comparable c2 = (Comparable)e2.getValue();
	    return c1.compareTo(c2);
	  }
}
%>
<%
HashMap hashMap = ReportLoader.loadReportsToSchedule(request);
ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
if(rdef ==null) rdef = (ReportDefinition) request.getSession().getAttribute(AppConstants.SI_REPORT_DEFINITION);
Set mapSet = hashMap.entrySet();
List entrylist = new ArrayList(mapSet);
Collections.sort(entrylist, new ValueComparator());
Map.Entry me;
session.removeAttribute(AppConstants.SI_REPORT_SCHEDULE);
session.removeAttribute(AppConstants.SI_REPORT_DEFINITION);
ReportSchedule reportSchedule = (ReportSchedule) session.getAttribute(AppConstants.SI_REPORT_SCHEDULE);
%>
 <jsp:include page="header.jsp" flush="true" />
 <!-- <link rel="stylesheet" type="text/css" href="<%= AppUtils.getBaseFolderURL() %>css/raptor.css">-->
<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/raptor.js"></script>
 

<jsp:include page="error_include.jsp" flush="true" />
<br/><br/><br/>
<% if (rdef == null || request.getSession().getAttribute(AppConstants.SI_REPORT_SCHEDULE) == null) {%>

<form name="formb" action="<%= AppUtils.getBaseURL() %>" method="post">
	<input type="hidden" name="action" value="raptor">
	<input type="hidden" name="<%= AppConstants.RI_ACTION %>" value="report.schedule.multiple">


<table width="94%"  class="tableBorder"  border="0" cellspacing="0" cellpadding="0" align=center valign="center">
<tr><td colspan="2">
	<table width="100%" border="0" cellspacing="0" cellpadding="3">

		<tr class=rbg1>
			<td valign="Middle"><b class=rtableheader><%= "Scheduling Report" %></b></td>
		</tr>
		<% if(request.getAttribute("message")!=null) { %>
		<tr class=rbg1>
			<td valign="Middle"><b class=rtableheader><%= (String) request.getAttribute("message") %></b></td>
		</tr>
		<% } %>
	</table>
</td></tr>
<tr> <td> <font class=rtabletext> Reports: </font></td><td height="30" align="center"> 
  		         <% if (rdef !=null && request.getSession().getAttribute(AppConstants.SI_REPORT_SCHEDULE) != null ) {%>  		       
                   <font class=rtabletext><%= rdef.getReportName()%></font>
                   <% } else { %> 		
  		       <select name="schedule_reports" onChange="document.formb.<%= AppConstants.RI_ACTION %>.value='report.schedule.report.submit'; document.formb.submit();">
  		             <option value="-1" selected> -->select report <-- </option>
         <%      for (Iterator iter = entrylist.iterator(); iter.hasNext();) { 
  		        	      me=(Map.Entry)iter.next();
  		         %>
  		         <% if (rdef !=null && rdef.getReportID().equals((String)me.getKey())) {%>  		         
                    	<option value="<%=(String) me.getKey()%>" selected> <%=(String) me.getValue()%> </option>
                  <%  } else { %>
                    	<option value="<%=(String) me.getKey()%>"> <%=(String) me.getValue()%> </option>
                  <%  } %>  				         
                  <% } %>
  		       </select>
  		       <% } %>
		
</td> </tr>

</table>	
</form>
<% } %>
<% if(reportSchedule!=null)  { %> 
  <jsp:include page="wizard_schedule_only.jsp" flush="true"/>
<% } %>  
 <jsp:include page="footer.jsp" flush="true" />
</div>
</td>
</tr>
</table>
<%--<jsp:include page="disclaimer.jsp" flush="true" />--%>
<script>
	//document.getElementById('loadingMessageDiv').style.display='none';
    //adding the bread crumb on the folder tree
	<%
		String isTreeHidden = "true";
		if(request.getSession().getAttribute("isTreeHidden") != null && ((String)request.getSession().getAttribute("isTreeHidden")).equals("true")){
			isTreeHidden = "true";
		}else{
			isTreeHidden = "false";
		}
	%>
	//if ("<%=isTreeHidden%>" == "true"){
	//	hideTree();
	//}
	//document.getElementById('contentDiv').style.height=document.body.offsetHeight - 50;
</script>
<%!	private String HTMLEncode(String value) {
		StringBuffer sb = new StringBuffer(value);
		
		for(int i=0; i<sb.length(); i++)
			if(sb.charAt(i)=='<')
				sb.replace(i, i+1, "&lt;");
			else if(sb.charAt(i)=='>')
				sb.replace(i, i+1, "&gt;");
			else if(sb.charAt(i)=='"')
				sb.replace(i, i+1, "&quot;");
   		
		return sb.toString();
	}   // HTMLEncode
	
	private String clearSpaces(String value) {
		StringBuffer sb = new StringBuffer(value);
		
		for(int i=0; i<sb.length(); i++)
			if(sb.charAt(i)==' ')
				sb.replace(i, i+1, "&nbsp;");
   		
		return sb.toString();
	}   // clearSpaces
	
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

 
