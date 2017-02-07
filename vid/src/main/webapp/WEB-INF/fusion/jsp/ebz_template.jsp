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
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html>
<html ng-app="abs">
    <head>
		<%@ include file="/WEB-INF/fusion/jsp/meta.jsp" %>
		<script src="static/js/jquery-1.10.2.js" type="text/javascript"></script>
	</head>
	<body class="templatebody" style="opacity: 1; background-color: rgb(242, 242, 242); padding: 0px;">
		<div class="applicationWindow">
			<div>
				<tiles:insertAttribute name="header" />
			</div>
			<br>
			<div class="content" id="mContent">
				<div class="body-content-jsp">
					<tiles:insertAttribute name="body" />
				</div>
			</div>
			<br>
			<div>
				<tiles:insertAttribute name="footer" /> 
			</div>
		</div>
	</body>
</html>
