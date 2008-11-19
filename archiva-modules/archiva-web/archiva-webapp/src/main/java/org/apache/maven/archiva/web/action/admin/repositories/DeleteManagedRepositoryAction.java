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
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Preparable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|Configuration
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
name|ManagedRepositoryConfiguration
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
name|ArtifactsByRepositoryConstraint
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
name|RepositoryContentStatisticsByRepositoryConstraint
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
name|ArchivaArtifact
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
name|ArchivaProjectModel
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
name|ProxyConnectorConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|role
operator|.
name|RoleManagerException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * DeleteManagedRepositoryAction  *   * @version $Id$  * @plexus.component role="com.opensymphony.xwork2.Action" role-hint="deleteManagedRepositoryAction"  */
end_comment

begin_class
specifier|public
class|class
name|DeleteManagedRepositoryAction
extends|extends
name|AbstractManagedRepositoriesAction
implements|implements
name|Preparable
block|{
specifier|private
name|ManagedRepositoryConfiguration
name|repository
decl_stmt|;
specifier|private
name|String
name|repoid
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|archivaDAO
decl_stmt|;
specifier|public
name|void
name|prepare
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|repoid
argument_list|)
condition|)
block|{
name|this
operator|.
name|repository
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findManagedRepositoryById
argument_list|(
name|repoid
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|confirmDelete
parameter_list|()
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|repoid
argument_list|)
condition|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete managed repository: repository id was blank."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
return|return
name|INPUT
return|;
block|}
specifier|public
name|String
name|deleteEntry
parameter_list|()
block|{
return|return
name|deleteRepository
argument_list|(
literal|false
argument_list|)
return|;
block|}
specifier|public
name|String
name|deleteContents
parameter_list|()
block|{
return|return
name|deleteRepository
argument_list|(
literal|true
argument_list|)
return|;
block|}
specifier|private
name|String
name|deleteRepository
parameter_list|(
name|boolean
name|deleteContents
parameter_list|)
block|{
name|ManagedRepositoryConfiguration
name|existingRepository
init|=
name|repository
decl_stmt|;
if|if
condition|(
name|existingRepository
operator|==
literal|null
condition|)
block|{
name|addActionError
argument_list|(
literal|"A repository with that id does not exist"
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
name|String
name|result
init|=
name|SUCCESS
decl_stmt|;
try|try
block|{
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|cleanupRepositoryData
argument_list|(
name|existingRepository
argument_list|)
expr_stmt|;
name|removeRepository
argument_list|(
name|repoid
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|result
operator|=
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|.
name|equals
argument_list|(
name|SUCCESS
argument_list|)
condition|)
block|{
if|if
condition|(
name|deleteContents
condition|)
block|{
name|removeContents
argument_list|(
name|existingRepository
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete repository: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
name|ERROR
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RoleManagerException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete repository: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
name|ERROR
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to delete repositoy: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
name|ERROR
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
name|void
name|cleanupRepositoryData
parameter_list|(
name|ManagedRepositoryConfiguration
name|cleanupRepository
parameter_list|)
throws|throws
name|RoleManagerException
throws|,
name|ArchivaDatabaseException
block|{
name|removeRepositoryRoles
argument_list|(
name|cleanupRepository
argument_list|)
expr_stmt|;
name|cleanupDatabase
argument_list|(
name|cleanupRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|cleanupScanStats
argument_list|(
name|cleanupRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|proxyConnectors
init|=
name|getProxyConnectors
argument_list|()
decl_stmt|;
for|for
control|(
name|ProxyConnectorConfiguration
name|proxyConnector
range|:
name|proxyConnectors
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
argument_list|,
name|cleanupRepository
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|removeProxyConnector
argument_list|(
name|proxyConnector
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|repoToGroupMap
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryToGroupMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|repoToGroupMap
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|repoToGroupMap
operator|.
name|containsKey
argument_list|(
name|cleanupRepository
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|repoGroups
init|=
name|repoToGroupMap
operator|.
name|get
argument_list|(
name|cleanupRepository
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|repoGroup
range|:
name|repoGroups
control|)
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findRepositoryGroupById
argument_list|(
name|repoGroup
argument_list|)
operator|.
name|removeRepository
argument_list|(
name|cleanupRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|cleanupDatabase
parameter_list|(
name|String
name|repoId
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|Constraint
name|constraint
init|=
operator|new
name|ArtifactsByRepositoryConstraint
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|archivaDAO
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|queryArtifacts
argument_list|(
name|constraint
argument_list|)
decl_stmt|;
for|for
control|(
name|ArchivaArtifact
name|artifact
range|:
name|artifacts
control|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Removing artifact "
operator|+
name|artifact
operator|+
literal|" from the database."
argument_list|)
expr_stmt|;
try|try
block|{
name|archivaDAO
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|deleteArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|projectModel
init|=
name|archivaDAO
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|getProjectModel
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|archivaDAO
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|deleteProjectModel
argument_list|(
name|projectModel
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|oe
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Project model of artifact "
operator|+
name|artifact
operator|+
literal|" does not exist in the database. "
operator|+
literal|"Moving on to the next artifact."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|ae
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Unable to delete artifact "
operator|+
name|artifact
operator|+
literal|" from the database. "
operator|+
literal|"Moving on to the next artifact."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|cleanupScanStats
parameter_list|(
name|String
name|repoId
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
name|results
init|=
name|archivaDAO
operator|.
name|getRepositoryContentStatisticsDAO
argument_list|()
operator|.
name|queryRepositoryContentStatistics
argument_list|(
operator|new
name|RepositoryContentStatisticsByRepositoryConstraint
argument_list|(
name|repoId
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|RepositoryContentStatistics
name|stats
range|:
name|results
control|)
block|{
name|archivaDAO
operator|.
name|getRepositoryContentStatisticsDAO
argument_list|()
operator|.
name|deleteRepositoryContentStatistics
argument_list|(
name|stats
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ManagedRepositoryConfiguration
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
specifier|public
name|void
name|setRepository
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
block|}
specifier|public
name|String
name|getRepoid
parameter_list|()
block|{
return|return
name|repoid
return|;
block|}
specifier|public
name|void
name|setRepoid
parameter_list|(
name|String
name|repoid
parameter_list|)
block|{
name|this
operator|.
name|repoid
operator|=
name|repoid
expr_stmt|;
block|}
specifier|public
name|void
name|setArchivaDAO
parameter_list|(
name|ArchivaDAO
name|archivaDAO
parameter_list|)
block|{
name|this
operator|.
name|archivaDAO
operator|=
name|archivaDAO
expr_stmt|;
block|}
specifier|public
name|ArchivaDAO
name|getArchivaDAO
parameter_list|()
block|{
return|return
name|archivaDAO
return|;
block|}
block|}
end_class

end_unit

