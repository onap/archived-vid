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
<%@ page import="org.openecomp.portalsdk.analytics.model.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.view.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>
<%
ReportRuntime rr = null;
ReportData    rd = null;
String parent = "";
int parentFlag = 0;
if(!nvl(request.getParameter("parent"), "").equals("N")) parent = nvl(request.getParameter("parent"), "");
if(parent.startsWith("parent_")) parentFlag = 1;
if(parentFlag == 1) {
	rr = (ReportRuntime) request.getSession().getAttribute(parent+"_rr");
	rd = (ReportData) request.getSession().getAttribute(parent+"_rd");
}
if(rr==null) rr = (ReportRuntime) request.getSession().getAttribute(AppConstants.SI_REPORT_RUNTIME);
if(rd==null) rd = (ReportData)    request.getSession().getAttribute(AppConstants.RI_REPORT_DATA);

	if(rr != null && rr.getReportType().equals(AppConstants.RT_DASHBOARD)) {
		//rr = (ReportRuntime) request.getSession().getAttribute("FirstDashReport");
	} else if (rr == null)
		rr = (ReportRuntime) request.getSession().getAttribute("FirstDashReport");
	//rd = (ReportData)    request.getSession().getAttribute(AppConstants.RI_REPORT_DATA);
	
	//response.setContentType("application/vnd.ms-excel");
	//response.setHeader("Content-disposition","attachment;filename=download_all_"+AppUtils.getUserID(request)+".xls");
	String user_id = AppUtils.getUserID(request);
	try {
/*		if (rr.getReportType().equals(AppConstants.RT_CROSSTAB))  {
			int downloadLimit = (rr.getMaxRowsInExcelDownload()>0)?rr.getMaxRowsInExcelDownload():Globals.getDownloadLimit();
			rd 		= rr.loadReportData(-1, AppUtils.getUserID(request), downloadLimit,request);
		}
*/
		new ReportHandler().createExcelFileContent(out, rd, rr, request, response, user_id, 3); //3 whole
	} catch(Exception e) {
		e.printStackTrace();
		Log.write("Fatal error [report_download_xls.jsp]: "+e.getMessage());
	}
	out.clear();
	out = pageContext.pushBody(); 
%>
<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>

