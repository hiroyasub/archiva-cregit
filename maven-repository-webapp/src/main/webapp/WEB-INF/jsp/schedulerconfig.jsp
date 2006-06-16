<%--
  ~ Copyright 2006 The Apache Software Foundation.
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
  ~
  --%>

<%@ taglib uri="webwork" prefix="ww" %>

<br>
<p>
<b>SCHEDULER CONFIGURATION:</b>
</p>

<form action="configureScheduler.action" method="post">
  Cron Expression: <input type="text" name="discoveryCronExpression" value="<ww:property value="parameters.discoveryCronExpression"/>"/>
  <input type="submit" value="Update"/>
</form>


