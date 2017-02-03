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
<%@ page import="org.openecomp.portalsdk.analytics.model.definition.ReportDefinition" %>
<%@ page import="org.openecomp.portalsdk.analytics.util.AppConstants"%>
<%@ page import="org.openecomp.portalsdk.analytics.controller.WizardSequence"%>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.FormFieldType"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="org.openecomp.portalsdk.analytics.xmlobj.JavascriptItemType"%>
<%@ page import="org.openecomp.portalsdk.analytics.system.AppUtils"%>

<% 
   ReportDefinition rdef = (ReportDefinition) request.getAttribute(AppConstants.SI_REPORT_DEFINITION);
   WizardSequence ws = rdef.getWizardSequence();
   FormFieldType fft = null;
   boolean isFormfieldPresent = (rdef.getFormFieldList()!=null && rdef.getFormFieldList().getFormField().size()<=0);
%>

<table width="100%" class="tableBorder" border="0" cellspacing="1" cellpadding="3" align=center>
	<tr>
		<td class=rbg1 valign="top"><b class=rtableheader>Step <%= ws.getCurrentStepIndex() %> of <%= ws.getStepCount() %> - <%= ws.getCurrentStep() %> Wizard <%= isFormfieldPresent?" <a href=\"javascript:uitmpl_qh('javascript_text'); if(typeof window.parent.resizeWindow == 'function') window.parent.resizeWindow();\" class=\"qh-link\" style=\"position:fixed;\"></a>":""%></b></td>
	</tr>
      <% if(isFormfieldPresent) { %>	  
	   <tr>
	   	<td class="nopad"><!-- quick help text -->
	   	<div id="javascript_text" class="mQH">
	   		<p><span class="label">Form Field Information:<BR></span>
	   		   <table>
	   		 <%
	   		    
	      		for(Iterator iter1=rdef.getFormFieldList().getFormField().iterator(); iter1.hasNext();) { 
	    			fft = (FormFieldType) iter1.next(); 
	   		 %>
	   		        
	   		        <%= "<TR><TD align=\"right\">" +  fft.getFieldName() +":</TD><TD>&nbsp;</TD><TD>document.formd." + fft.getFieldId()+"</TD></TR>" %> 
	   		 <% } 
	   		 %> 
	   		  </table>  
	   		</p>
	   	</div>
	   	<!-- /quick help text --></td>	   	
	</tr>
	   <% } %>
	<tr>
		<td  width="65%" class=rbg3 align="center">
			<textarea name="<%= AppConstants.RI_JAVASCRIPT%>" cols="100" rows="30"><%= (rdef.getJavascriptElement()!=null)?rdef.getJavascriptElement():"" %></textarea> 
		</td>
	</tr> 
    <tr>
		<table class=mTAB width="100%" class="tableBorder" border="3" cellspacing="1" cellpadding="3" align=center>
			<tr class=rbg1>
				<td align="center" valign="Middle" width="7%" height="30"><b class=rtableheader>&nbsp;&nbsp;No&nbsp;&nbsp;</b></td>
				<td align="center" valign="Middle" width="7%"><b class=rtableheader>Form Field</b></td>
				<td align="center" valign="Middle" width="72%"><b class=rtableheader>Calling Javascript</b></td>
				<td align="center" valign="Middle" width="14%">
		                   &nbsp;
                 </td>
			</tr>
            <%
            int iCount = 0;
            String javascriptFieldId = "";
            String id = "";
            String callText = "";
            
            int count = 0;
            if(rdef.getJavascriptList()!=null )  {
      		for(Iterator iter=rdef.getJavascriptList().getJavascriptItem().iterator(); iter.hasNext(); ) { 
      			count++;
      			JavascriptItemType javascriptItem = (JavascriptItemType) iter.next();
      			id = javascriptItem.getId();
      			javascriptFieldId = javascriptItem.getFieldId();
      			callText = javascriptItem.getCallText();
      		%> 
			<tr class=<%=(count%2==0)?"rowalt2":"rowalt1"%>> 
				<td align="center" height="30"><font class=rtabletext><%= ++iCount %></font></td>
				<td align="center" >
                 <select name="javascriptFormField-<%=id %>" style="width: 100px">
     				<option value="-1"> <-- SELECT --> </option>
					<option value="ol1" <%= ("ol1".equals(javascriptFieldId))?" selected":""%>>OnLoad</option>
	                <option value="os1" <%= ("os1".equals(javascriptFieldId))?" selected":""%>>OnSubmit</option>


	              <%
	              if(rdef.getFormFieldList()!=null){
	      		for(Iterator iter1=rdef.getFormFieldList().getFormField().iterator(); iter1.hasNext();) { 
	    			fft = (FormFieldType) iter1.next(); 
	    			%>
					<option value="<%=fft.getFieldId() %>" <%= (fft.getFieldId().equals(javascriptFieldId))?" selected":""%>><%=fft.getFieldName() %></option>
				<%					
	              }
	              }
	             %> 
	              
				</select>
				</td>
				<td align="center">
					<textarea name="callText-<%=id %>" rows="3" cols="100"><%=callText %></textarea>	
				</td>
                <td width="30%"> 
                  <button type="submit" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_SAVE %>'; document.forma.<%=AppConstants.RI_JAVASCRIPT_ITEM_ID %>.value='<%= id %>'; document.forma.submit();" width="12" height="12"   att-button btn-type="primary" size="small" title='Save'>Save</button>
                  <button type="submit" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE %>'; document.forma.<%=AppConstants.RI_JAVASCRIPT_ITEM_ID %>.value='<%= id %>'; document.forma.submit();" width="12" height="12"   att-button btn-type="primary" size="small" title='Delete'>Delete</button>
                </td>
			</tr>
			<% } %>
			<% } 
             
             id = "-1";
             %>
			<tr class=rowalt1>
				<td align="center" height="30"><font class=rtabletext><%= ++iCount %></font></td>
				<td align="center">
                 <select name="javascriptFormField-<%=id %>" style="width: 100px">
    				<option value="-1"> <-- SELECT --> </option>
					<option value="ol1">OnLoad</option>
					<option value="os1">OnSubmit</option>
					
	              <%
	              if(rdef.getFormFieldList()!=null){
	      		for(Iterator iter1=rdef.getFormFieldList().getFormField().iterator(); iter1.hasNext();) { 
	    			fft = (FormFieldType) iter1.next(); 
	    			%>
					<option value="<%=fft.getFieldId() %>"><%=fft.getFieldName() %></option>
				<%					
	              }
	              }
	              %>
				</select>
				</td>
				<td align="center">
					<textarea name="callText-<%=id %>" rows="3" cols="100"></textarea>		
				</td>
                <td width="40%"> 
                  <button type="submit" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_SAVE %>'; document.forma.<%=AppConstants.RI_JAVASCRIPT_ITEM_ID %>.value='<%= id %>'; document.forma.submit();" width="12" height="12"   att-button btn-type="primary" size="small" title='Save'>Save</button>
                  <button type="submit" onClick="document.forma.<%= AppConstants.RI_WIZARD_ACTION %>.value='<%= AppConstants.WA_DELETE %>'; document.forma.<%=AppConstants.RI_JAVASCRIPT_ITEM_ID %>.value='<%= id %>'; document.forma.submit();" width="12" height="12"   att-button btn-type="primary" size="small" title='Delete'>Delete</button>
                </td>
			</tr>
		</table>	
	</tr>  
	
<%!
	private String nvl(String s)                  { return (s==null)?"":s; }
	private String nvl(String s, String sDefault) {	return nvl(s).equals("")?sDefault:s; } 
%>

<script language="JavaScript">
<!--
function dataValidate() {
 return true;
}   // dataValidate
//-->
</script>
