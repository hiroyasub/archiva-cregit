begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|reporting
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * DefaultReportingManager   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.reporting.ReportingManager"  *                   role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultReportingManager
extends|extends
name|AbstractLogEnabled
implements|implements
name|ReportingManager
block|{
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.reporting.DynamicReportSource"      */
specifier|private
name|Map
name|reportSourceMap
decl_stmt|;
specifier|public
name|DynamicReportSource
name|getReport
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
operator|(
name|DynamicReportSource
operator|)
name|reportSourceMap
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|public
name|Map
name|getAvailableReports
parameter_list|()
block|{
return|return
name|reportSourceMap
return|;
block|}
block|}
end_class

end_unit

