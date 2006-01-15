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

<%@ taglib uri="webwork" prefix="ww" %>
<html>
<head>
  <title>Maven Repository Manager</title>
</head>

<body>

<h1>Maven Repository Manager</h1>

<form action="search.action">
  <input name="packageName" type="text"/> <input type="submit" value="Search"/>
</form>

<table>
  <tr>
    <th>Group ID</th>
    <th>Artifact ID</th>
    <th>Version</th>
  </tr>
  <ww:iterator value="artifacts">
    <tr>
      <td><ww:property value="groupId"/></td>
      <td><ww:property value="artifactId"/></td>
      <td><ww:property value="version"/></td>
    </tr>
  </ww:iterator>
</table>

</body>
</html>
