begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|RepositoryAdminException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|managed
operator|.
name|ManagedRepositoryAdmin
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridge
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|ManagedRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ArchivaRestServiceException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ManagedRepositoriesService
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
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"managedRepositoriesService#rest"
argument_list|)
specifier|public
class|class
name|DefaultManagedRepositoriesService
extends|extends
name|AbstractRestService
implements|implements
name|ManagedRepositoriesService
block|{
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|PlexusSisuBridge
name|plexusSisuBridge
decl_stmt|;
specifier|public
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|getManagedRepositories
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
argument_list|>
name|repos
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|managedRepos
init|=
operator|new
name|ArrayList
argument_list|<
name|ManagedRepository
argument_list|>
argument_list|(
name|repos
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
name|repoConfig
range|:
name|repos
control|)
block|{
comment|// TODO staging repo too
name|ManagedRepository
name|repo
init|=
operator|new
name|ManagedRepository
argument_list|(
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getName
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getLayout
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|isSnapshots
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|isReleases
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|isBlockRedeployments
argument_list|()
argument_list|,
literal|false
argument_list|,
name|repoConfig
operator|.
name|getCronExpression
argument_list|()
argument_list|)
decl_stmt|;
name|managedRepos
operator|.
name|add
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
return|return
name|managedRepos
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|ManagedRepository
name|getManagedRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|repos
init|=
name|getManagedRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|ManagedRepository
name|repo
range|:
name|repos
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryId
argument_list|)
condition|)
block|{
return|return
name|repo
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|deleteManagedRepository
parameter_list|(
name|String
name|repoId
parameter_list|,
name|boolean
name|deleteContent
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|managedRepositoryAdmin
operator|.
name|deleteManagedRepository
argument_list|(
name|repoId
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|,
name|deleteContent
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|addManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
name|repo
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|managedRepository
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setBlockRedeployments
argument_list|(
name|managedRepository
operator|.
name|isBlockRedeployments
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setCronExpression
argument_list|(
name|managedRepository
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLayout
argument_list|(
name|managedRepository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|managedRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setReleases
argument_list|(
name|managedRepository
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setSnapshots
argument_list|(
name|managedRepository
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setIndexDirectory
argument_list|(
name|managedRepository
operator|.
name|getIndexDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setDaysOlder
argument_list|(
name|managedRepository
operator|.
name|getDaysOlder
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
name|managedRepository
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setRetentionCount
argument_list|(
name|managedRepository
operator|.
name|getRetentionCount
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|managedRepositoryAdmin
operator|.
name|addManagedRepository
argument_list|(
name|repo
argument_list|,
name|managedRepository
operator|.
name|isStageRepoNeeded
argument_list|()
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|updateManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
name|repo
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|managedRepository
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setBlockRedeployments
argument_list|(
name|managedRepository
operator|.
name|isBlockRedeployments
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setCronExpression
argument_list|(
name|managedRepository
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLayout
argument_list|(
name|managedRepository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|managedRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setReleases
argument_list|(
name|managedRepository
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setSnapshots
argument_list|(
name|managedRepository
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|managedRepositoryAdmin
operator|.
name|updateManagedRepository
argument_list|(
name|repo
argument_list|,
name|managedRepository
operator|.
name|isStageRepoNeeded
argument_list|()
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|,
name|managedRepository
operator|.
name|isResetStats
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

