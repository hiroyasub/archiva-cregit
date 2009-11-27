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
name|ArrayList
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
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|ProjectModelDAO
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
name|database
operator|.
name|constraints
operator|.
name|UniqueArtifactIdConstraint
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
name|UniqueGroupIdConstraint
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
name|UniqueVersionConstraint
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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Stub class for Archiva DAO to avoid having to set up a database for tests.  *  * @todo a mock would be better, but that won't play nicely with Plexus injection.  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaDAOStub
implements|implements
name|ArchivaDAO
block|{
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
name|ArtifactDAO
name|artifactDao
decl_stmt|;
specifier|private
name|ProjectModelDAO
name|projectDao
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|versions
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|groups
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|artifacts
decl_stmt|;
specifier|public
name|List
argument_list|<
name|?
argument_list|>
name|query
parameter_list|(
name|SimpleConstraint
name|constraint
parameter_list|)
block|{
if|if
condition|(
name|constraint
operator|instanceof
name|UniqueVersionConstraint
condition|)
block|{
return|return
name|versions
return|;
block|}
if|else if
condition|(
name|constraint
operator|instanceof
name|UniqueGroupIdConstraint
condition|)
block|{
return|return
name|groups
return|;
block|}
if|else if
condition|(
name|constraint
operator|instanceof
name|UniqueArtifactIdConstraint
condition|)
block|{
return|return
name|artifacts
return|;
block|}
else|else
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|RepositoryContentStatistics
operator|.
name|class
argument_list|,
name|constraint
operator|.
name|getResultClass
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
name|stats
init|=
operator|new
name|ArrayList
argument_list|<
name|RepositoryContentStatistics
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repo
range|:
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|RepositoryContentStatistics
name|statistics
init|=
operator|new
name|RepositoryContentStatistics
argument_list|()
decl_stmt|;
name|statistics
operator|.
name|setRepositoryId
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|stats
operator|.
name|add
argument_list|(
name|statistics
argument_list|)
expr_stmt|;
block|}
return|return
name|stats
return|;
block|}
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
name|artifactDao
return|;
block|}
specifier|public
name|ProjectModelDAO
name|getProjectModelDAO
parameter_list|()
block|{
return|return
name|projectDao
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
literal|"method not implemented for stub"
argument_list|)
throw|;
block|}
specifier|public
name|RepositoryContentStatisticsDAO
name|getRepositoryContentStatisticsDAO
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"method not implemented for stub"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setArtifactDao
parameter_list|(
name|ArtifactDAO
name|artifactDao
parameter_list|)
block|{
name|this
operator|.
name|artifactDao
operator|=
name|artifactDao
expr_stmt|;
block|}
specifier|public
name|void
name|setProjectDao
parameter_list|(
name|ProjectModelDAO
name|projectDao
parameter_list|)
block|{
name|this
operator|.
name|projectDao
operator|=
name|projectDao
expr_stmt|;
block|}
specifier|public
name|void
name|setVersions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|versions
parameter_list|)
block|{
name|this
operator|.
name|versions
operator|=
name|versions
expr_stmt|;
block|}
specifier|public
name|void
name|setGroups
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
block|{
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
block|}
specifier|public
name|void
name|setArtifacts
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|artifacts
parameter_list|)
block|{
name|this
operator|.
name|artifacts
operator|=
name|artifacts
expr_stmt|;
block|}
block|}
end_class

end_unit

