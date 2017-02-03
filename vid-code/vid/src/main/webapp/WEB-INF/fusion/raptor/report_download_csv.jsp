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
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.*" %><%@ page import="java.text.*" %><%@ page import="java.io.*" %><%@ page import="org.openecomp.portalsdk.analytics.model.*" %><%@ page import="org.openecomp.portalsdk.analytics.model.runtime.*" %><%@ page import="org.openecomp.portalsdk.analytics.view.*" %><%@ page import="org.openecomp.portalsdk.analytics.system.*" %><%@ page import="org.openecomp.portalsdk.analytics.util.*" %><%
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
    String formattedReportName = new HtmlStripper().stripSpecialCharacters(rr.getReportName());
    String formattedDate = new SimpleDateFormat("MMddyyyyHHmm").format(new Date());
	String fName = formattedReportName+formattedDate+AppUtils.getUserID(request);
	boolean raw = AppUtils.getRequestFlag(request, "raw");
	if(true && !raw) 
		response.setContentType("application/octet-stream");
	else
		response.setContentType("application/csv");
	String fileName = fName+".csv";
	String sql_whole = (String) request.getAttribute(AppConstants.RI_REPORT_SQL_WHOLE);
	if(true && !raw) 
	 	response.setHeader("Content-disposition","attachment;filename="+fName+".zip");
	else
		 response.setHeader("Content-disposition","attachment;filename="+fName+".csv");
	try {(new ReportHandler()).createCSVFileContent(out, rd, rr, sql_whole, request,fName);
	//out.flush();
	//out.close();
	    if(true) {
	    //	response.reset();
	    	ServletOutputStream outS = response.getOutputStream();
			java.io.File file = null;
			if(true && !raw) {
				response.setContentType("application/octet-stream");
				response.setHeader("Content-disposition","attachment;filename="+fName+".zip");
				file = new java.io.File(AppUtils.getTempFolderPath()+""+fName+".zip");
			} else {
				response.setContentType("application/csv");
				response.setHeader("Content-disposition","attachment;filename="+fName+".csv");
				file = new java.io.File(AppUtils.getTempFolderPath()+""+fName+".csv");
			}
	    	FileInputStream fileIn = new FileInputStream(file);
	    	int c;
			while((c=fileIn.read()) != -1){
	    		outS.write(c);
	    		}
	    		outS.flush();
	    		outS.close();
	    		fileIn.close();

	    	 
	    	/*byte[] outputByte = new byte[4096];
	    	//copy binary contect to output stream
	    	while(fileIn.read(outputByte, 0, 4096) != -1) {
	    		outS.write(outputByte, 0, 4096);
	    	}
	    	fileIn.close();
	    	outS.flush();
	    	outS.close();*/
	    }
	} catch(Exception e) {
		e.printStackTrace();
		Log.write("Fatal error [report_download_csv.jsp]: "+e.getMessage());
	}
%>
<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } %>
