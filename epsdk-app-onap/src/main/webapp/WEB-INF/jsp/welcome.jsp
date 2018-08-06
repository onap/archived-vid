<script src="app/vid/external/multiselect/angular-bootstrap-multiselect.min.js"></script>
<script src="app/vid/external/upload-file/ng-file-upload.min.js"></script>
<script src="app/vid/external/angular-feature-flags/featureFlags.min.js"></script>
<script src="app/vid/external/angular-moment/moment.min.js"></script>
<script src="app/vid/external/angular-moment/angular-moment.min.js"></script>
<%@ page import="org.onap.vid.properties.Features"%>
<%@ page import="org.onap.vid.properties.VidProperties" %>
<%@ page import="org.onap.portalsdk.core.util.SystemProperties" %>
<div>
     <h1 class="heading1">ONAP</h1>
     <br>
     <h1 class="heading1"><u>Welcome to VID</u></h1>
     <br>
   The Virtual Infrastructure Deployment (VID) application allows infrastructure service deployment operators 
   to instantiate service instances and their constituent parts for Distributed service models required by the 
   ONAP service operations that manage them, such as Mobility Network Services, etc. 
   The models are defined by ONAP component SDC. The service 
   deployment operator selects the service operations owner and model that they wish to instantiate. After 
   entry of appropriate data, the operator instructs VID to direct another ONAP component, MSO, to instantiate 
   the selected service model. Once the service instance has been instantiated, the service operator can instruct 
   VID to direct MSO to instantiate the service instance's component VNFs, VF Modules, Networks and Volume Groups. 
   The VID user can also search for, and display, existing service instances and direct the instantiation of 
   subsequent instance components.
     <br><br>
  
    <h1 class="heading1"><a href="mailto:portal@lists.onap.org" target="_top">Contact Us</a></h1>
    <a href="mailto:portal@lists.onap.org" target="_top">Please click here to contact us.</a>
    <%
        if (Features.FLAG_ADD_MSO_TESTAPI_FIELD.isActive()) {

            String displayTestApi = SystemProperties.getProperty(VidProperties.MSO_DISPLAY_TEST_API_ON_SCREEN);
            String defaultTestApiValue = SystemProperties.getProperty(VidProperties.MSO_DEFAULT_TEST_API);

            String selectionVisibility = Boolean.parseBoolean(displayTestApi) ? "inherit" : "hidden";
    %>
            <div style="visibility: <%=selectionVisibility%>" id="selectTestApiSection">
                <br/><br/><br/>
                <label>Test API for A-la-carte:</label>
                <select style="width: 20ch" id="selectTestApi" onchange="sessionStorage.setItem('msoRequestParametersTestApiValue',this.value);">
                    <option value="VNF_API">VNF_API (old)</option>
                    <option value="GR_API">GR_API (new)</option>
                </select>
            </div>
            <script type="text/javascript">
                var selectedValue = sessionStorage.getItem('msoRequestParametersTestApiValue') || "<%=defaultTestApiValue%>";
                var element = document.getElementById('selectTestApi');
                if (element) {
                    element.value = selectedValue;
                }
                sessionStorage.setItem('msoRequestParametersTestApiValue', selectedValue);
            </script>
    <%
        }
    %>
<BR>
</div>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>

