<%--
  ~ Licensed to the Apache Software Foundation (ASF) under one
  ~ or more contributor license agreements.  See the NOTICE file
  ~ distributed with this work for additional information
  ~ regarding copyright ownership.  The ASF licenses this file
  ~ to you under the Apache License, Version 2.0 (the
  ~ "License"); you may not use this file except in compliance
  ~ with the License.  You may obtain a copy of the License at
  ~
  ~   http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing,
  ~ software distributed under the License is distributed on an
  ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  ~ KIND, either express or implied.  See the License for the
  ~ specific language governing permissions and limitations
  ~ under the License.
  --%>

<%@ taglib prefix="ww" uri="/webwork" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
  <title>Browse Repository</title>
  <ww:head/>
</head>

<body>

<h1>Browse Repository</h1>

<div id="contentArea">
  <div id="nameColumn">
    <p>
      <c:set var="cumulativeGroup" value=""/>
      <c:forTokens items="${groupId}" delims="." var="part" varStatus="status">
        <c:choose>
          <c:when test="${empty(cumulativeGroup)}">
            <c:set var="cumulativeGroup" value="${part}"/>
          </c:when>
          <c:otherwise>
            <c:set var="cumulativeGroup" value="${cumulativeGroup}.${part}"/>
          </c:otherwise>
        </c:choose>
        <c:choose>
          <c:when test="${status.last}">
            <strong>${part}</strong>
          </c:when>
          <c:otherwise>
            <c:set var="url">
              <ww:url action="browseGroup" namespace="/">
                <ww:param name="groupId" value="%{'${cumulativeGroup}'}"/>
              </ww:url>
            </c:set>
            <a href="${url}">${part}</a> /
          </c:otherwise>
        </c:choose>
      </c:forTokens>
    </p>

    <ww:set name="groups" value="groups"/>
    <c:if test="${!empty(groups)}">
      <h2>Groups</h2>
      <ul>
        <c:forEach items="${groups}" var="groupId">
          <c:set var="url">
            <ww:url action="browseGroup" namespace="/">
              <ww:param name="groupId" value="%{'${groupId}'}"/>
            </ww:url>
          </c:set>
          <li><a href="${url}">${groupId}/</a></li>
        </c:forEach>
      </ul>
    </c:if>

    <ww:set name="artifactIds" value="artifactIds"/>
    <c:if test="${!empty(artifactIds)}">
      <h2>Artifacts</h2>
      <ul>
        <c:forEach items="${artifactIds}" var="artifactId">
          <c:set var="url">
            <ww:url action="browseArtifact" namespace="/">
              <ww:param name="groupId" value="%{'${groupId}'}"/>
              <ww:param name="artifactId" value="%{'${artifactId}'}"/>
            </ww:url>
          </c:set>
          <li><a href="${url}">${artifactId}/</a></li>
        </c:forEach>
      </ul>
    </c:if>
  </div>
</div>

</body>
</html>
