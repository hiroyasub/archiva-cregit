<%--
  ~ Copyright 2005-2006 The Apache Software Foundation.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<%@ taglib prefix="ww" uri="/webwork" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Adminsitrator Registration Page</title>
  <ww:head/>
</head>

<body>

<div id="contentArea">
  <div id="searchBox">
    <p>
      <ww:actionmessage/>
      <ww:actionerror/>
    </p>

    <h2>Setup an Administrator Account</h2>
    <ww:form action="registerAdminAccount" method="post" namespace="/admin">
      <%@ include file="/WEB-INF/jsp/admin/include/registerUserForm.jspf" %>       
      <ww:submit value="Register"/>
    </ww:form>

  </div>
</div>


<div class="clear">
  <hr/>
</div>

</body>

</html>
