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
name|artifact
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ArchivaDAO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ArchivaDatabaseException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|Constraint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ObjectNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|constraints
operator|.
name|RepositoryProblemByTypeConstraint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|RepositoryProblem
import|;
end_import

begin_import
import|import
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
name|DataLimits
import|;
end_import

begin_import
import|import
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
name|DynamicReportSource
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * LocationArtifactsReport   *  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.reporting.DynamicReportSource"   *                   role-hint="artifact-location"  */
end_comment

begin_class
specifier|public
class|class
name|LocationArtifactsReport
implements|implements
name|DynamicReportSource
argument_list|<
name|RepositoryProblem
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PROBLEM_TYPE_BAD_ARTIFACT_LOCATION
init|=
literal|"bad-artifact-location"
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Artifact Locations Report"      */
specifier|private
name|String
name|name
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
specifier|private
name|Constraint
name|constraint
decl_stmt|;
specifier|public
name|LocationArtifactsReport
parameter_list|()
block|{
name|constraint
operator|=
operator|new
name|RepositoryProblemByTypeConstraint
argument_list|(
name|PROBLEM_TYPE_BAD_ARTIFACT_LOCATION
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|RepositoryProblem
argument_list|>
name|getData
parameter_list|()
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
return|return
name|dao
operator|.
name|getRepositoryProblemDAO
argument_list|()
operator|.
name|queryRepositoryProblems
argument_list|(
name|constraint
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|RepositoryProblem
argument_list|>
name|getData
parameter_list|(
name|DataLimits
name|limits
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
comment|// TODO: implement limits.
return|return
name|dao
operator|.
name|getRepositoryProblemDAO
argument_list|()
operator|.
name|queryRepositoryProblems
argument_list|(
name|constraint
argument_list|)
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

