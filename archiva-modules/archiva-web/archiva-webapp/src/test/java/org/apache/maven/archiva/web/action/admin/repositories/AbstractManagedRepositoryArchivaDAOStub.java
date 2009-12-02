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
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|repositories
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|ArtifactDAO
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
name|RepositoryContentStatisticsDAO
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
name|RepositoryProblemDAO
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
name|SimpleConstraint
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
name|RepositoryContentStatistics
import|;
end_import

begin_comment
comment|/**  * AbstractManagedRepositoryArchivaDAOStub  *   * @version  */
end_comment

begin_class
specifier|public
class|class
name|AbstractManagedRepositoryArchivaDAOStub
implements|implements
name|ArchivaDAO
block|{
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArtifactDAO
name|artifactDAO
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|RepositoryContentStatisticsDAO
name|repoContentStatisticsDAO
decl_stmt|;
specifier|public
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
name|query
parameter_list|(
name|SimpleConstraint
name|constraint
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Object
name|save
parameter_list|(
name|Serializable
name|obj
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"query not implemented for stub"
argument_list|)
throw|;
block|}
specifier|public
name|ArtifactDAO
name|getArtifactDAO
parameter_list|()
block|{
return|return
name|artifactDAO
return|;
block|}
specifier|public
name|RepositoryProblemDAO
name|getRepositoryProblemDAO
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"query not implemented for stub"
argument_list|)
throw|;
block|}
specifier|public
name|RepositoryContentStatisticsDAO
name|getRepositoryContentStatisticsDAO
parameter_list|()
block|{
return|return
name|repoContentStatisticsDAO
return|;
block|}
block|}
end_class

end_unit

