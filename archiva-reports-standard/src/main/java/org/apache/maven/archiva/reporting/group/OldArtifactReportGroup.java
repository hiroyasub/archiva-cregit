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
operator|.
name|group
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
comment|/**  * The report set for finding old artifacts (both snapshot and release)  *  * @plexus.component role="org.apache.maven.archiva.reporting.group.ReportGroup" role-hint="old-artifact"  */
end_comment

begin_class
specifier|public
class|class
name|OldArtifactReportGroup
extends|extends
name|AbstractReportGroup
block|{
comment|/**      * Role hints of the reports to include in this set.      *      * @todo implement these report processors!      */
specifier|private
specifier|static
specifier|final
name|Map
name|reports
init|=
operator|new
name|LinkedHashMap
argument_list|()
decl_stmt|;
static|static
block|{
name|reports
operator|.
name|put
argument_list|(
literal|"old-artifact"
argument_list|,
literal|"Old Artifacts"
argument_list|)
expr_stmt|;
name|reports
operator|.
name|put
argument_list|(
literal|"old-snapshot-artifact"
argument_list|,
literal|"Old Snapshot Artifacts"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|includeReport
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|reports
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|Map
name|getReports
parameter_list|()
block|{
return|return
name|reports
return|;
block|}
specifier|public
name|String
name|getFilename
parameter_list|()
block|{
return|return
literal|"old-artifacts-report.xml"
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"Old Artifacts"
return|;
block|}
block|}
end_class

end_unit

