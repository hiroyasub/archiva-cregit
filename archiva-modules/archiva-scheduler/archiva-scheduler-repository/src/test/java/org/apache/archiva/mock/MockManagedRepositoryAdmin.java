begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|mock
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
name|AuditInformation
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
name|beans
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
name|index
operator|.
name|context
operator|.
name|IndexingContext
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|MockManagedRepositoryAdmin
implements|implements
name|ManagedRepositoryAdmin
block|{
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|public
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|getManagedRepositories
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
name|managedRepoConfigs
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
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
name|managedRepoConfigs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ManagedRepositoryConfiguration
name|repoConfig
range|:
name|managedRepoConfigs
control|)
block|{
comment|// TODO add staging repo information back too
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
name|repoConfig
operator|.
name|getRefreshCronExpression
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getIndexDir
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|isScanned
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getDaysOlder
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getRetentionCount
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|,
literal|true
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
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepository
argument_list|>
name|getManagedRepositoriesAsMap
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ManagedRepository
name|getManagedRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
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
name|repositoryId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|,
name|boolean
name|deleteContent
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|addManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|,
name|boolean
name|needStageRepo
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|updateManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|,
name|boolean
name|needStageRepo
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|,
name|boolean
name|resetStats
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ArchivaConfiguration
name|getArchivaConfiguration
parameter_list|()
block|{
return|return
name|archivaConfiguration
return|;
block|}
specifier|public
name|void
name|setArchivaConfiguration
parameter_list|(
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|)
block|{
name|this
operator|.
name|archivaConfiguration
operator|=
name|archivaConfiguration
expr_stmt|;
block|}
specifier|public
name|IndexingContext
name|createIndexContext
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

