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
<%--
 Name: wizard_schedule_formfield_include.jsp
 Use : Shows formfield of the report in the schedule page. 
 
 Change Log
 ==========
 
 28-Aug-2009 : Version 8.5.1 (Sundar); Checkbox and Radio button are also handled. 
 18-Aug-2009 : Version 8.5.1 (Sundar); 
 				
				a) ajax.js is loaded in startup for AJAX functionality.
				b) showArgPopupNew is modified as per report_form.jsp
				c) Ajax function is added very similiar to report_form.jsp
				d) "auto" bug is resolved.

14-Jul-2009 : Version 8.4 (Sundar); 
 				
 				<UL> 
 				<LI> Shows the form field of the first Dashboard report in schedule page if the report is dashboard.</LI>
 				</UL>
--%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.runtime.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.system.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.*" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.ReportDefinition" %>
<%@ page import="org.openecomp.portalsdk.analytics.model.ReportHandler" %>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.JavascriptItemType"%>
<%@ page import="java.util.regex.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.ReportSchedule" %>


<%	
    ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
    ReportHandler rh = new ReportHandler();
    ReportRuntime rr = rh.loadReportRuntime(request, rdef.getReportID());
    request.getSession().setAttribute(AppConstants.SI_REPORT_RUNTIME, rr);
    boolean isDashboard =  rr.isDashboardType();
    ReportFormFields rff = rr.getReportFormFields();
	ReportFormFields rff1 = (ReportFormFields) rff.clone();
	ReportFormFields rff2 = (ReportFormFields) rff.clone();
	ReportFormFields rff5 = (ReportFormFields) rff.clone();
	boolean isFirstTime = nvl(request.getParameter("refresh")).toUpperCase().startsWith("Y");
	ReportSchedule reportSchedule = (ReportSchedule) request.getSession().getAttribute(AppConstants.SI_REPORT_SCHEDULE);

    int dashboardFlag = 0;
	ReportRuntime rr1 = null;
	java.text.SimpleDateFormat sdf = null;

%>

<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/raptor.js"></script>
<script language="JavaScript" src="<%= AppUtils.getBaseFolderURL() %>js/ajax.js"></script>
<%--=(rr.getJavascriptElement()!=null && rr.getJavascriptElement().length()>0)?rr.getJavascriptElement().replaceAll("formd","forma"):""--%>
<script language="JavaScript">
<!--
function showArgPopup(fieldName, jsTargetField) {
	var url = "<%= AppUtils.getRaptorActionURL() %>report.popup.field&<%= AppConstants.RI_FIELD_NAME %>="
						+ fieldName+ "&<%= AppConstants.RI_JS_TARGET_FIELD %>="+jsTargetField;
	var w = window.open(url, "formLookup", "width=440,height=400,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	w.focus();
	w.opener = this;
}   // showArgPopup

function showArgPopupNew(fld, targetField) {
	var newElementCreated = true;

	var oldTarget = null;
	var oldRAction = null;
	var frm = document.forma;
	var w = window.open("<%= AppUtils.getBaseFolderURL() %>loading.jsp", "formLookup", "width=440,height=400,location=no,menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes");
	
	oldTarget = frm.target ;
	oldRAction = frm.r_action.value ;
	frm.target = "formLookup";
	frm.r_action.value = "report.popup.field";
	if(document.getElementById("<%= AppConstants.RI_FIELD_NAME %>")) {
		frm.removeChild(document.getElementById("<%= AppConstants.RI_FIELD_NAME %>"));
		frm.removeChild(document.getElementById("<%= AppConstants.RI_JS_TARGET_FIELD %>"));
	}
	var fld1 = document.createElement("input");
	fld1.setAttribute("type", "hidden");
	fld1.setAttribute("name", "<%= AppConstants.RI_FIELD_NAME %>");
	fld1.setAttribute("id", "<%= AppConstants.RI_FIELD_NAME %>");
	fld1.setAttribute("value", fld);

	var fld2 = document.createElement("input");
	fld2.setAttribute("type", "hidden");
	fld2.setAttribute("name", "<%= AppConstants.RI_JS_TARGET_FIELD %>");
	fld2.setAttribute("id", "<%= AppConstants.RI_JS_TARGET_FIELD %>");
	fld2.setAttribute("value", targetField);
	
	frm.appendChild(fld1);	
	frm.appendChild(fld2);	
	frm.submit();
	w.focus();
	w.opener = this;
	
	frm.target = oldTarget ;
	frm.r_action.value = oldRAction ;
	
	
}   // showArgPopupNew

function validateForm() {
   //if(document.getElementById("schedEnabled").checked && document.getElementById("schedEnabled").value == 'Y') {
	<%for(rff.resetNext(); rff.hasNext(); ) { %>
		<%= rff.getNext().getValidateJavaScript().replaceAll("formd","forma") %>
	<%}%>
	
	var formfields_value="";
	
	for(i=0; i<document.forma.elements.length;i++) {	
		if((document.forma.elements[i].name).substring(0,2)=='ff') {
			//alert("i-" + i+ " "+ document.forma.elements[i].name + " " + document.forma.elements[i].type + " " + document.forma.elements[i].value);
			var obj = document.forma.elements[i];
			if ((document.forma.elements[i].tagName == "SELECT") /*&& !(obj.name.match('_Hr') || obj.name.match('_Min') || obj.name.match('_Sec'))*/ ){
				var selString = "";
					for (var intLoop=0; intLoop < obj.length; intLoop++) {
					         if (obj[intLoop].selected) {
					            formfields_value += "&"+document.forma.elements[i].name+"=" + escape(obj[intLoop].value);
				            
				         }
				}
			}
			else{
				if(escape(document.forma.elements[i].value).charAt(0) == '[' ) {
					var nameObj = document.forma.elements[i].name;
					if(nameObj.indexOf("_")!=-1 && (nameObj.match('_Hr') || nameObj.match('_Min') || nameObj.match('_Sec')) ) {
					} else {
			formfields_value += "&"+document.forma.elements[i].name+"="+escape(document.forma.elements[i].value);
	}
				} else {
					var opt = document.forma.elements[i].name;
					var optValue = "";
					//alert('not in select' + opt);
					var auto_incr = '<%=Boolean.toString(Globals.isScheduleDateParamAutoIncr())%>';
					//alert(auto_incr);
					//alert(document.getElementById(opt+"_auto") + " " + (opt+"_auto"));
					if(document.getElementById(opt+"_auto")/* || opt.lastIndexOf("_auto")>= 0 */) {
							for(k=0; k<document.forma.elements.length;k++) {	
								if(document.forma.elements[k].name == opt) {
									optValue = document.forma.elements[k].value;
								}
							}
							//alert('opt info  ' +opt + " "+ optValue);
							
							 if(auto_incr == 'true') {
								 formfields_value += "&"+document.forma.elements[i].name+"_auto="+escape(optValue);
							 } else if (document.getElementById(opt).checked) {
								formfields_value += "&"+document.forma.elements[i].name+"_auto="+escape(optValue);
							 } /*else 
								 formfields_value += "&"+document.forma.elements[i].name+"="+escape(document.forma.elements[i].value);*/
					} else if (opt.lastIndexOf("_auto") < 0){
                        if(document.forma.elements[i].type == 'checkbox' || document.forma.elements[i].type == 'radio') {
                        	if (document.forma.elements[i].checked)
								formfields_value += "&"+document.forma.elements[i].name+"="+escape(document.forma.elements[i].value);
                        } else {
							formfields_value += "&"+document.forma.elements[i].name+"="+escape(document.forma.elements[i].value);
                        }
					}
				}
	        }
                    }
	}
	<% 
	   String  sessionParams[] = Globals.getSessionParamsForScheduling().split(",");
	   String formfields = "";
	   for (int i=0; i<sessionParams.length; i++) {
		   if(sessionParams[i].equals("login_id"))
				   formfields += "&"+java.net.URLEncoder.encode("login_id","UTF-8")+"="+ java.net.URLEncoder.encode(AppUtils.getUserBackdoorLoginId(request),"UTF-8");
		   else {
		   	if(session.getAttribute(sessionParams[i])!=null)
		   				formfields += "&"+ java.net.URLEncoder.encode(sessionParams[i], "UTF-8") + "="+ java.net.URLEncoder.encode((String)session.getAttribute(sessionParams[i]),"UTF-8");
		   }
	   }
	%>
	formfields_value += '&<%= AppConstants.RI_NEXT_PAGE %>=-1';
	formfields_value += '<%=formfields%>';
	//alert(formfields_value);
	document.getElementById("formFields").value = formfields_value;
	   var javascriptText = "<%=getCallableJavascriptForSubmit(rr)%>";
	    javascriptText = javascriptText.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	    if(javascriptText.length > 0) {
			  if(<%=(getCallableJavascriptForSubmit(rr).trim().length())>0?getCallableJavascriptForSubmit(rr).replaceAll("formd","forma"):"false"%>)
		  return true;
			  else
				  return false;
		  } 
     // }	
	return true;
}	// validateForm
function systemDateTime() {
  <%
  sdf = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
  sdf.setTimeZone(TimeZone.getTimeZone(Globals.getTimeZone()));
  String sysDateTime = sdf.format(new java.util.Date());
  %>
  var sysdate = "<%=sysDateTime%>";
  return sysdate;
}

function systemDate() {
  <%
  sdf = new java.text.SimpleDateFormat("MM/dd/yyyy");
  sdf.setTimeZone(TimeZone.getTimeZone(Globals.getTimeZone()));
  String sysDate = sdf.format(new java.util.Date());
  %>
  var sysdate = "<%=sysDate%>";
  return sysdate;
}
//-->
</script>
	<%
  if(rr.getReportType().equals(AppConstants.RT_DASHBOARD)) {
	  dashboardFlag = 1;
      String strHTML = rr.getDashboardLayoutHTML(); //getdashboardLayoutHTML();
      String rep_id = parseAndGetFirstReportID(strHTML);
      ReportHandler rh1 = new ReportHandler();
	  rr1 = null;
	  int requestFlag = 1;
	  try {
			rr1 = rh1.loadReportRuntime(request, rep_id, true, requestFlag);
	  } catch(Exception e) {
      }
	  rff = rr1.getReportFormFields();
  }
  %>	
	<%if((dashboardFlag == 0 && rr.needFormInput()) || (dashboardFlag == 1 && rr1.needFormInput())) { %>
	<tr>
		
		<td class=rbg3 align="center" width="50%" valign="middle" colspan=2><font class=rtabletext>
			Please input values into the Form Fields for email attachment. Note those fields user must provide value can not leave as blank.
			</font>
			<table width="94%" border="0" cellspacing="1" cellpadding="3" align=center>
				<%
				int colidx = 0;
				java.util.HashMap paramsMap = Globals.getRequestParamtersMap(request, false);
				java.util.HashMap getScheduleMap = getFormFieldsHashMap (request, reportSchedule.getFormFields());
				java.util.HashMap paramsScheduleMap = Globals.getRequestParametersMap(request, getScheduleMap); 
				for (int i = 0; i < rff.size(); i ++){
					FormField ff = (FormField) rff.get(i);
					ff.setDbInfo(rr.getDbInfo());
					ff.setUserId(AppUtils.getUserID(request));					
					if(ff.getFieldType().equals(FormField.FFT_HIDDEN)) {
						%>
									<%
									  if(nvl(reportSchedule.getFormFields()).length() <= 0)
									  	out.println(ff.getHtml(rr.getParamValue(ff.getFieldName()), paramsMap, rr, true));
									  else
										out.println(ff.getHtml(getParameterString(request, ff.getFieldName(), getScheduleMap), paramsScheduleMap, rr, true).replaceAll("formd","forma"));  
									  	
									  
									%>
						<%		}					
					if(!ff.getFieldType().equals(FormField.FFT_HIDDEN) && ff.isVisible()) {

 				%>
		 		<%if (colidx == 0){%><tr><%}%>
					<td class=rbg3 width="10%" align="right" nowrap='yes'>
						<font class=rtabletext>
							<%if (!ff.getFieldType().equals(FormField.FFT_BLANK)){%> 
								<%= ff.getDisplayNameHtml() %>:
							<%}%>
						</font>
					</td> 
					<td class=rbg3 width="15%" align="left" nowrap='yes'>
						<%-- ff.getHtml(rr.getParamValue(ff.getFieldName()), paramsMap,rr, true).replaceAll("formd","forma") --%>
						<%
									  if(nvl(reportSchedule.getFormFields()).length() <= 0)
									  	out.println(ff.getHtml(rr.getParamValue(ff.getFieldName()), paramsMap, rr, true).replaceAll("formd","forma"));
									  else
										out.println(ff.getHtml(getParameterString(request, ff.getFieldName(), getScheduleMap), paramsScheduleMap, rr, true).replaceAll("formd","forma"));  
						%>
					</td>
					<td ></td>
					<%colidx++;%>
				<%if (colidx == rr.getNumFormColsAsInt()){%></tr><%colidx=0;}%>
			<%    }	
				}   //for  %>
			</table>
		</td>
	</tr> 		
<% 	} //if(rr.needFormInput()) %>

<input type='hidden' name='formFields' value=''/>

<script>
<% if(!isDashboard) {  %>
var map = new Object();
var defaultValues = new Object();
<% rff = rr.getReportFormFields();
int idx = 0;
int row = 0;
	
java.util.Map fNameMap = new java.util.HashMap();
for(rff.resetNext(); rff.hasNext(); idx++) { 
	FormField ff = rff.getNext();
	fNameMap.put(ff.getFieldName(), ff.getFieldDisplayName());
}


idx = 0;
row = 0;

for(rff.resetNext(); rff.hasNext(); idx++) { 
	FormField ff = rff.getNext();
	if(!(ff.getFieldType().equals(FormField.FFT_BLANK)) 
			&& ff.isVisible() && !(ff.getFieldType().equals(FormField.FFT_TEXT_W_POPUP))) {
	%>
	
	
	 <%
	
	  if( ff.getDependsOn() != null && ff.getDependsOn().trim().length()>0 ) {
	  	%> // <%=ff.getFieldName()%>	<%=ff.getDependsOn()%>
	     if(document.forma.<%=ff.getFieldName()%>) {
				document.forma.<%=ff.getFieldName()%>.onchange = updateDropDown;
		 }
	  		map['<%=ff.getDependsOn()%>'] = '<%=ff.getFieldName()%>';
	  		// Made double quotes as it conflicts with query.
	  		defaultValues['<%=ff.getFieldName()%>'] = "<%=rr.getParamValue(ff.getFieldName())%>" ;
	  	<%
	  }
     }
}
%>

var oldTarget = null;
var oldr_action = null;
	var ajax = new Array();

	function getOpts(oSelect) {
		var opt, i = 0, selVals = new Array();
		//alert(oSelect.length + " " + oSelect.options);
		if(oSelect && oSelect.options) { 
		while (opt = oSelect[i++]) { 
			if (opt.selected) {
			  selVals[selVals.length] = encodeURIComponent(opt.value);
			  //alert(opt.value)
		}
		}
		//return selVals;
		} else {
			if(oSelect && oSelect.value)
				selVals[0] = encodeURIComponent(oSelect.value);
			//return selVals;
		}
		return selVals;
	}

	function trim(str) {
		return str.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
	}
		
	function updateDropDown(srcElem_) {

		var srcElem = null;
		if(srcElem_ == null) { 
			if (event != null)  
				srcElem = event.srcElement; 
		} else {
		 	srcElem = srcElem_; 
		}
		//alert(srcElem.name);
		//var parentFormField = srcElem.options[srcElem.selectedIndex].value;
		if(srcElem !=null) {
		var parentFormField = getOpts(srcElem);
		var parentFieldName = srcElem.name;
		
		//alert(parentFormField);
		var parentFieldValue = "";
		for (i=0;i<=parentFormField.length-1;i++) {
			if(parentFormField[i] != 'undefined')
			parentFieldValue = parentFieldValue+srcElem.name+'='+parentFormField[i];
			if(i<parentFormField.length-1)
				parentFieldValue = parentFieldValue + "&";
		}
		//alert("ParentFieldValue " +  parentFieldValue + " srcElem.name " + srcElem.name); 
    		
	if(srcElem != null && srcElem.name!=null) {
	<%
		FormField ff =  null;
		String javaScriptText = "";
		String javaScriptAfterChainingText = "";
	%> 
	<%		
		for(rff5.resetNext(); rff5.hasNext();idx++) { 
			ff = rff5.getNext();
	%>		
		    if(srcElem && srcElem.name && srcElem.name == "<%=ff.getFieldName()%>") {
		    	 <% javaScriptText = nvl(ff.getCallableOnChangeJavascript(ff.getFieldName(), rr)); 
                    javaScriptAfterChainingText = nvl(ff.getCallableAfterChainingJavascript(ff.getFieldName(), rr)); 
		    	    if(javaScriptText.length() > 0 ) { %>
						<%=javaScriptText+";"%>
						//document.forma.<%--=ff.getFieldName()--%>//.onchange = 
	 		     <% } %>
		    }						    
	 <%	} %>
	 <%	for(rff5.resetNext(); rff5.hasNext();idx++) { 
	%> 
	<%		
			ff = rff5.getNext();
		%>
		    //alert (" In For " + "<%=ff.getFieldName()%> <%=rff.hasNext()%>");
		<%	
			if(!(ff.getFieldType().equals(FormField.FFT_BLANK))) { 
				/*&& ff.isVisible()*/ /*&& !(ff.getFieldType().equals(FormField.FFT_TEXT_W_POPUP))*/
	%>
				<% if( ff.getDependsOn() != null && !ff.getDependsOn().equals("") ) { 
				     if(!(ff.getFieldType().equals(FormField.FFT_TEXT_W_POPUP))) { %>
					<% if(!ff.getFieldType().equals(FormField.FFT_HIDDEN)) { %>
					if(srcElem && srcElem.name && srcElem.name == "<%=ff.getFieldName()%>") {
						parentFieldValue = parentFieldValue + '&parentFieldName=<%=ff.getFieldName()%>';					 
						//if(document.forma.<%=ff.getFieldName()%> && document.forma.<%=ff.getFieldName()%>.style) document.forma.<%=ff.getFieldName()%>.style.display='none';
					 //if(document.forma.<%=ff.getFieldName()%> && document.forma.<%=ff.getFieldName()%>.style) document.forma.<%=ff.getFieldName()%>.style.display='none';
         					 if(document.getElementById("<%=ff.getFieldName()%>_content") && document.getElementById("<%=ff.getFieldName()%>_content").style ) document.getElementById('<%=ff.getFieldName()+"_content"%>').style.display='block';
					}
					  	
				    <% } %>
				    <% } %>
				var otherFieldValue = "";

		<%
			    for(rff1.resetNext(); rff1.hasNext();) {
			    	FormField ff1 = rff1.getNext(); 
						if( ff1.getDependsOn() != null && !ff1.getDependsOn().equals("") ) { %> 
					    //alert (srcElem.name + " <%=ff1.getFieldName()%>");  
					    if(srcElem && srcElem.name && srcElem.name != '<%=ff1.getFieldName()%>') {
						    var otherFormFields = getOpts(document.forma.<%=ff1.getFieldName()%>);
							for (i=0;i<=otherFormFields.length-1;i++) {
								if(otherFormFields[i] != 'undefined')
								otherFieldValue = otherFieldValue+'<%=ff1.getFieldName()%>'+'='+escape(otherFormFields[i])+'&';
								if(i<otherFormFields.length-1)
									otherFieldValue = otherFieldValue + "&";
							}
						     
					    }
					    <% } 
				    }
      %>	    		
                 //alert("IF OPTIONS 0 AFTER <%=ff.getFieldName()%>" + parentFormField);
                
				//if(parentFormField.length>0) {
				//if(srcElem.name == "<%=ff.getFieldName()%>") {
					if(document.forma.<%=ff.getFieldName()%> && document.forma.<%=ff.getFieldName()%>.options) 
						document.forma.<%=ff.getFieldName()%>.options.length = 0;
				  try { 
					var index = ajax.length;
					ajax[index] = new sack("raptor.htm");
					//alert("P" + parentFieldValue);
					//alert("O" + otherFieldValue);
					ajax[index].method='POST';
					//alert('raptor.htm?action=raptor&r_action=report.childDropDown&parentFieldName='+srcElem.name+'&fieldName=<%=ff.getFieldName()%>&'+parentFieldValue+'&'+otherFieldValue);
						ajax[index].setVar("action","raptor");
						ajax[index].setVar("r_action","report.childDropDown");
						ajax[index].setVar("fieldName","<%=ff.getFieldName()%>");
						ajax[index].setVar("inSchedule","Y");
						//alert("parentFieldValue " + parentFieldValue);
						var parentFieldTokens = parentFieldValue.split( "&" );
						var parentNameValuePair = "";
						var nameValuePairTokens;
						for (var i = 0; i < parentFieldTokens.length; i++) {
							parentNameValuePair = parentFieldTokens[i];
							parentNameValuePairTokens = parentNameValuePair.split("=");
							if (parentNameValuePairTokens.length>0) {
								//alert("ParentNameValuePairTokens " + parentNameValuePairTokens[0]+ " " + decodeURIComponent(parentNameValuePairTokens[1]));
								ajax[index].setVar(parentNameValuePairTokens[0], decodeURIComponent(parentNameValuePairTokens[1]), i);
							}
						}
						//alert("otherFieldValue " + otherFieldValue + " " );
						if(otherFieldValue && otherFieldValue.length>0) {
							if (otherFieldValue.substr(otherFieldValue.length-1) == '&')
								otherFieldValue = otherFieldValue.substr(0,otherFieldValue.length-1); 
						}
						//alert("otherFieldValue1 " + otherFieldValue + " " );
						
						var otherFieldTokens = otherFieldValue.split( "&" );
						var otherNameValuePair = "";
						var otherNameValuePairTokens;
						for (var k = 0; k < otherFieldTokens.length; k++) {
							otherNameValuePair = otherFieldTokens[k];
							otherNameValuePairTokens = otherNameValuePair.split("=");
							if (otherNameValuePairTokens.length>0) {
								//alert("otherNameValuePairTokens " + otherNameValuePairTokens[0]+ " " + decodeURIComponent(otherNameValuePairTokens[1]));
								ajax[index].setVar(otherNameValuePairTokens[0], decodeURIComponent(otherNameValuePairTokens[1]), k);
							}
						}
						//ajax[index].requestFile = 'raptor.htm?action=raptor&r_action=report.childDropDown&fieldName=<%=ff.getFieldName()%>&'+parentFieldValue+'&'+otherFieldValue;	// Specifying which file to get
					ajax[index].onCompletion = function(){ createChildFormField(index, document.forma.<%=ff.getFieldName()%>) };	// Specify function that will be executed after file has been found
					ajax[index].onError 	= function() {bringBacktoNormal(index, document.forma.<%=ff.getFieldName()%>);}
					ajax[index].onFail	 	= function() {bringBacktoNormal(index, document.forma.<%=ff.getFieldName()%>);}
					//ajax[index].onCompletion = function(){ var obj = null; obj = document.forma.<%=ff.getFieldName()%>;eval(ajax[index].response);document.getElementById(obj.name+"_content").style.display='none';obj.style.display='block';  };	// Specify function that will be executed after file has been found
					ajax[index].runAJAX(null,false,100);		// Execute AJAX function
					//alert(ajax[index].readyState);
					//while (ajax[index].xmlhttp.readyState == 4 || ajax[index].xmlhttp.readyState == "complete" ) {
					//}
					//while(ajax[index].xmlhttp.readyState ==4){
					//	 alert("ready State " + ajax[index].xmlhttp.readyState);
					//}
					//setTimeout("Func1()", 5000);
				    } catch (err) {
				    	if(err=="session_exp") {
					    	document.write("session has been expired.");
				    	}
				    }					
					
				//} else {
					<% if(!ff.getFieldType().equals(FormField.FFT_HIDDEN)) { %>
						if(document.getElementById("<%=ff.getFieldName()%>_content") && document.getElementById("<%=ff.getFieldName()%>_content").style ) document.getElementById("<%=ff.getFieldName()%>_content").style.display='none';
						//if(document.forma.<%=ff.getFieldName()%> && document.forma.<%=ff.getFieldName()%>.style) document.forma.<%=ff.getFieldName()%>.style.display='block';
					<% } else  {%>	
						if(document.getElementById("<%=ff.getFieldName()%>_content") && document.getElementById("<%=ff.getFieldName()%>_content").style ) document.getElementById("<%=ff.getFieldName()%>_content").style.display='none';
						//if(document.forma.<%=ff.getFieldName()%> && document.forma.<%=ff.getFieldName()%>.style) document.forma.<%=ff.getFieldName()%>.style.display='none';
					<% } %>	
				//}
                //} // if src.Elem
				<% } %>
	   <% } %>
	                   
	<% } %>	
	         <%
			for(rff.resetNext(); rff.hasNext();idx++) { 
				ff = rff.getNext();
				%>
			    if(srcElem && srcElem.name && srcElem.name == "<%=ff.getFieldName()%>") {
				<%    
                   javaScriptAfterChainingText = nvl(ff.getCallableAfterChainingJavascript(ff.getFieldName(), rr));
				%>
				<%
	           if(javaScriptAfterChainingText.length()>0 && javaScriptAfterChainingText.trim().startsWith("afterchaining")) {
	        	   javaScriptAfterChainingText = Utils.replaceInString(javaScriptAfterChainingText,"afterchaining","");
	        	   javaScriptAfterChainingText = Utils.replaceInString(javaScriptAfterChainingText,"\"","");
	        	   javaScriptAfterChainingText = Utils.replaceInString(javaScriptAfterChainingText,"=","");
	        	 %>
	        	<%   
	          %>
	              <%=javaScriptAfterChainingText%>
	          <%	   
	           }
	         %>
			    }						    
	<% } %>	
	         
	              
      }  
      }  
   }

	function dummyFunction(){
		//alert("Server operation timed out");
	}
	
	function bringBacktoNormal(index, obj) {
		if(obj) {
			//alert('bringing to normal');
			var field_name = obj.name;
			//alert("'"+field_name+"_content"+"'");
			if(document.getElementById(field_name+"_content") && document.getElementById(field_name+"_content").style ) document.getElementById(field_name+"_content").style.display='none';
			//if(obj.style) obj.style.display='block';
		}
	}
	function createChildFormField(index, obj) {
	    //alert('create Cities');
		//var obj = document.getElementById('dhtmlgoodies_city');
		var obj = obj;
		var resp = trim(ajax[index].response);
	    //alert(index + " " + ajax[index]);
	    //alert('response ' + ajax[index].response);
	    if(resp.length > 0 && resp.indexOf("<script type=")!=-1) {
		    document.write(resp);
			if(obj) {
				//alert('obj.name ' + obj.name);
				var field_name = obj.name;
				//alert("'"+field_name+"_content"+"'");
				if(document.getElementById(field_name+"_content") && document.getElementById(field_name+"_content").style ) document.getElementById(field_name+"_content").style.display='none';
				//if(obj.style) obj.style.display='block';
			}
		    throw "session_exp";
	    }
		if(obj) {
	    	if(resp.length > 0)
				eval(resp);	// Executing the response from Ajax as Javascript code
			//alert('obj.name ' + obj.name);
			var field_name = obj.name;
			//alert("'"+field_name+"_content"+"'");
			if(document.getElementById(field_name+"_content") && document.getElementById(field_name+"_content").style ) document.getElementById(field_name+"_content").style.display='none';
			//if(obj.style) obj.style.display='block';
		}
	}
function changeCombo(data){
	var list = data;
	for (var i = 0; i < list.length; i ++){
		var fieldName = list[i].substr(0, list[i].indexOf(":"));
		var selectVal = list[i].substr(list[i].indexOf(":") + 1, list[i].length);
		
		var elem = document.getElementsByName(fieldName);
		var j = 0;
		while (elem[j].tagName == 'INPUT' && elem[j].type=='hidden'){
			j ++;
		}
		
		var oldVal = elem[j].value
		elem[j].parentNode.innerHTML = selectVal;
		elem[j].value=oldVal;
		elem[j].onchange = updateDropDown;
	}	
	document.getElementById('childFieldpopup').style.display='none';
	


	
}
<% } %>
/*
	
	<input type="hidden" name="action" value="raptor">
	<input type="hidden" name="source_page" value="report_run">
	<input type="hidden" name="r_action" value="report.download.pdf">

*/
</script>
<% /* if(request.getAttribute(AppConstants.RI_REPORT_DATA) == null){  */ %>
<script>
 if(<%=isFirstTime%>) {
	 
		<%
		if(!isDashboard) {
		outFor:
	    for(rff2.resetNext(); rff2.hasNext();) {
	    	FormField ff1 = rff2.getNext(); 
				if( ff1.getDependsOn() != null && !ff1.getDependsOn().equals("") ) { %>
				<% if (!ff1.getFieldType().equals(FormField.FFT_HIDDEN))  { %>
				//alert("in First Time <%=ff1.getFieldName()%>");
					if(document.forma.<%=ff1.getFieldName()%>)
						updateDropDown(document.forma.<%=ff1.getFieldName()%>);
                <% } %>
				<% break outFor;
				} 
			} 
		}
			%>		 
  }
</script>

<%!	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
	private String getCallableJavascriptForSubmit(ReportRuntime rr) {
		JavascriptItemType javascriptItemType = null;
		StringBuffer callJavascriptText = new StringBuffer("");
		if(rr.getJavascriptList()!=null) {
			for (Iterator iter = rr.getJavascriptList().getJavascriptItem().iterator(); iter.hasNext();) {
				javascriptItemType  = (JavascriptItemType)iter.next();
				if(javascriptItemType.getFieldId().equals("os1")) {
					callJavascriptText.append(" "+javascriptItemType.getCallText());
					break;
				}
			}
		}
		return callJavascriptText.toString();
	}    

	private HashMap getFormFieldsHashMap (HttpServletRequest request, String formFieldsString) {
		String splitName[] = null;
		ArrayList keyValue = new ArrayList();
		HashMap keyValueMap = new HashMap();
        String newValue = "";
        //System.out.println("Request Str "+ formFieldsString);
        StringTokenizer st = null;
        StringTokenizer st2 = null;
        String key1 = "";
        String value = "";
        
		if(formFieldsString.length() > 0) {
			st = new StringTokenizer(formFieldsString, "&");
			while (st.hasMoreTokens()) {
				keyValue.add(st.nextToken());
			}
			if(keyValue.size() > 0) {
				
				for (int num = 0; num < keyValue.size(); num++) {
					st2 = new StringTokenizer((String) keyValue.get(num), "=");
					while(st2.hasMoreTokens()) {
						key1 = ""; value = "";
						key1 = st2.nextToken();
						key1 = Utils.replaceInString(key1, "_auto", "");
						try {
							value = st2.nextToken();
						}catch (NoSuchElementException ex) { value  = "";}
							if(!keyValueMap.containsKey(key1)) 
								keyValueMap.put(key1,value);
							else {
								String value1 = (String) keyValueMap.get(key1);
								value = value+"|"+value1;
								keyValueMap.put(key1,value);
							}
					}
				}
		
	        }
		}
		return keyValueMap;
	}
	
	private String getParameterString (HttpServletRequest request, String key, HashMap keyValueMap) {
        String newValue = "";
				if(keyValueMap.containsKey(key)) {
					//System.out.println("VALUE IN MAP IS " +key+ " "+ (String) keyValueMap.get(key));
					newValue = XSSFilter.filterRequestOnlyScript((String) keyValueMap.get(key));
					if(nvl(newValue).length()<=0) {
						newValue = XSSFilter.filterRequestOnlyScript((String) keyValueMap.get(key+"_auto"));
					}
				}
		return newValue;
	}
	private String parseAndGetFirstReportID(String strHTML) {
		  String sourcestring = strHTML;
		  //System.out.println("String HTML1 " + strHTML);
		  Pattern re = Pattern.compile("\\[(.*?)\\]");   //\\[(.*?)\\]
		  Matcher m = re.matcher(sourcestring);
		  int mIdx = 0;
		    while (m.find()){
		      for( int groupIdx = 0; groupIdx < m.groupCount(); groupIdx++ ){
		    	String str = m.group(groupIdx);
		    	//System.out.println("REP ID1 " + str.substring(str.indexOf("#")+1, str.length()-1)) ;
              return str.substring(str.indexOf("#")+1, str.length()-1);
		      }
		      mIdx++;
		      
		    }return "";
	}	
%>
