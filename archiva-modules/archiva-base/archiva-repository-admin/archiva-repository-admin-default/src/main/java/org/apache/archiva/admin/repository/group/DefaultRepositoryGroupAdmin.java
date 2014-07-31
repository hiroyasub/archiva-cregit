begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|repository
operator|.
name|group
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
name|beans
operator|.
name|RepositoryGroup
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
name|group
operator|.
name|RepositoryGroupAdmin
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
name|admin
operator|.
name|repository
operator|.
name|AbstractRepositoryAdmin
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
name|Configuration
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
name|RepositoryGroupConfiguration
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
name|metadata
operator|.
name|model
operator|.
name|facets
operator|.
name|AuditEvent
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
name|scheduler
operator|.
name|MergedRemoteIndexesScheduler
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|annotation
operator|.
name|PostConstruct
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
name|io
operator|.
name|File
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoryGroupAdmin#default"
argument_list|)
specifier|public
class|class
name|DefaultRepositoryGroupAdmin
extends|extends
name|AbstractRepositoryAdmin
implements|implements
name|RepositoryGroupAdmin
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|REPO_GROUP_ID_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[A-Za-z0-9\\._\\-]+"
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|MergedRemoteIndexesScheduler
name|mergedRemoteIndexesScheduler
decl_stmt|;
specifier|private
name|File
name|groupsDirectory
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|String
name|appServerBase
init|=
name|getRegistry
argument_list|()
operator|.
name|getString
argument_list|(
literal|"appserver.base"
argument_list|)
decl_stmt|;
name|groupsDirectory
operator|=
operator|new
name|File
argument_list|(
name|appServerBase
operator|+
name|File
operator|.
name|separatorChar
operator|+
literal|"groups"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|groupsDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
name|groupsDirectory
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
try|try
block|{
for|for
control|(
name|RepositoryGroup
name|repositoryGroup
range|:
name|getRepositoriesGroups
argument_list|()
control|)
block|{
name|mergedRemoteIndexesScheduler
operator|.
name|schedule
argument_list|(
name|repositoryGroup
argument_list|,
name|getMergedIndexDirectory
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// create the directory for each group if not exists
name|File
name|groupPath
init|=
operator|new
name|File
argument_list|(
name|groupsDirectory
argument_list|,
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|groupPath
operator|.
name|exists
argument_list|()
condition|)
block|{
name|groupPath
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"fail to getRepositoriesGroups {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|File
name|getMergedIndexDirectory
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|groupsDirectory
argument_list|,
name|repositoryGroupId
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|RepositoryGroup
argument_list|>
name|getRepositoriesGroups
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|RepositoryGroup
argument_list|>
name|repositoriesGroups
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|RepositoryGroupConfiguration
name|repositoryGroupConfiguration
range|:
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryGroups
argument_list|()
control|)
block|{
name|repositoriesGroups
operator|.
name|add
argument_list|(
operator|new
name|RepositoryGroup
argument_list|(
name|repositoryGroupConfiguration
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|repositoryGroupConfiguration
operator|.
name|getRepositories
argument_list|()
argument_list|)
argument_list|)
operator|.
name|mergedIndexPath
argument_list|(
name|repositoryGroupConfiguration
operator|.
name|getMergedIndexPath
argument_list|()
argument_list|)
operator|.
name|mergedIndexTtl
argument_list|(
name|repositoryGroupConfiguration
operator|.
name|getMergedIndexTtl
argument_list|()
argument_list|)
operator|.
name|cronExpression
argument_list|(
name|repositoryGroupConfiguration
operator|.
name|getCronExpression
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|repositoriesGroups
return|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|getRepositoryGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|RepositoryGroup
argument_list|>
name|repositoriesGroups
init|=
name|getRepositoriesGroups
argument_list|()
decl_stmt|;
for|for
control|(
name|RepositoryGroup
name|repositoryGroup
range|:
name|repositoriesGroups
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|repositoryGroupId
argument_list|,
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|repositoryGroup
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|addRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|validateRepositoryGroup
argument_list|(
name|repositoryGroup
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|validateManagedRepositoriesExists
argument_list|(
name|repositoryGroup
operator|.
name|getRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryGroupConfiguration
name|repositoryGroupConfiguration
init|=
operator|new
name|RepositoryGroupConfiguration
argument_list|()
decl_stmt|;
name|repositoryGroupConfiguration
operator|.
name|setId
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryGroupConfiguration
operator|.
name|setRepositories
argument_list|(
name|repositoryGroup
operator|.
name|getRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryGroupConfiguration
operator|.
name|setMergedIndexPath
argument_list|(
name|repositoryGroup
operator|.
name|getMergedIndexPath
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryGroupConfiguration
operator|.
name|setMergedIndexTtl
argument_list|(
name|repositoryGroup
operator|.
name|getMergedIndexTtl
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryGroupConfiguration
operator|.
name|setCronExpression
argument_list|(
name|repositoryGroup
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|addRepositoryGroup
argument_list|(
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|,
literal|null
argument_list|,
name|AuditEvent
operator|.
name|ADD_REPO_GROUP
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
name|mergedRemoteIndexesScheduler
operator|.
name|schedule
argument_list|(
name|repositoryGroup
argument_list|,
name|getMergedIndexDirectory
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|deleteRepositoryGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|RepositoryGroupConfiguration
name|repositoryGroupConfiguration
init|=
name|configuration
operator|.
name|getRepositoryGroupsAsMap
argument_list|()
operator|.
name|get
argument_list|(
name|repositoryGroupId
argument_list|)
decl_stmt|;
name|mergedRemoteIndexesScheduler
operator|.
name|unschedule
argument_list|(
operator|new
name|RepositoryGroup
argument_list|(
name|repositoryGroupId
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|repositoryGroupConfiguration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"repositoryGroup with id "
operator|+
name|repositoryGroupId
operator|+
literal|" doesn't not exists so cannot remove"
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|removeRepositoryGroup
argument_list|(
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
name|repositoryGroupId
argument_list|,
literal|null
argument_list|,
name|AuditEvent
operator|.
name|DELETE_REPO_GROUP
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|updateRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
name|updateRepositoryGroup
argument_list|(
name|repositoryGroup
argument_list|,
name|auditInformation
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|private
name|Boolean
name|updateRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|,
name|boolean
name|triggerAuditEvent
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|validateRepositoryGroup
argument_list|(
name|repositoryGroup
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|validateManagedRepositoriesExists
argument_list|(
name|repositoryGroup
operator|.
name|getRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|RepositoryGroupConfiguration
name|repositoryGroupConfiguration
init|=
name|configuration
operator|.
name|getRepositoryGroupsAsMap
argument_list|()
operator|.
name|get
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|removeRepositoryGroup
argument_list|(
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
name|repositoryGroupConfiguration
operator|.
name|setRepositories
argument_list|(
name|repositoryGroup
operator|.
name|getRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryGroupConfiguration
operator|.
name|setMergedIndexPath
argument_list|(
name|repositoryGroup
operator|.
name|getMergedIndexPath
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryGroupConfiguration
operator|.
name|setMergedIndexTtl
argument_list|(
name|repositoryGroup
operator|.
name|getMergedIndexTtl
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryGroupConfiguration
operator|.
name|setCronExpression
argument_list|(
name|repositoryGroup
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addRepositoryGroup
argument_list|(
name|repositoryGroupConfiguration
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
if|if
condition|(
name|triggerAuditEvent
condition|)
block|{
name|triggerAuditEvent
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|,
literal|null
argument_list|,
name|AuditEvent
operator|.
name|MODIFY_REPO_GROUP
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
block|}
name|mergedRemoteIndexesScheduler
operator|.
name|unschedule
argument_list|(
name|repositoryGroup
argument_list|)
expr_stmt|;
name|mergedRemoteIndexesScheduler
operator|.
name|schedule
argument_list|(
name|repositoryGroup
argument_list|,
name|getMergedIndexDirectory
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|addRepositoryToGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|RepositoryGroup
name|repositoryGroup
init|=
name|getRepositoryGroup
argument_list|(
name|repositoryGroupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|repositoryGroup
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"repositoryGroup with id "
operator|+
name|repositoryGroupId
operator|+
literal|" doesn't not exists so cannot add repository to it"
argument_list|)
throw|;
block|}
if|if
condition|(
name|repositoryGroup
operator|.
name|getRepositories
argument_list|()
operator|.
name|contains
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"repositoryGroup with id "
operator|+
name|repositoryGroupId
operator|+
literal|" already contain repository with id"
operator|+
name|repositoryId
argument_list|)
throw|;
block|}
name|validateManagedRepositoriesExists
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repositoryId
argument_list|)
argument_list|)
expr_stmt|;
name|repositoryGroup
operator|.
name|addRepository
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
name|updateRepositoryGroup
argument_list|(
name|repositoryGroup
argument_list|,
name|auditInformation
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|,
literal|null
argument_list|,
name|AuditEvent
operator|.
name|ADD_REPO_TO_GROUP
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|deleteRepositoryFromGroup
parameter_list|(
name|String
name|repositoryGroupId
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|RepositoryGroup
name|repositoryGroup
init|=
name|getRepositoryGroup
argument_list|(
name|repositoryGroupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|repositoryGroup
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"repositoryGroup with id "
operator|+
name|repositoryGroupId
operator|+
literal|" doesn't not exists so cannot remove repository from it"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|repositoryGroup
operator|.
name|getRepositories
argument_list|()
operator|.
name|contains
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"repositoryGroup with id "
operator|+
name|repositoryGroupId
operator|+
literal|" doesn't not contains repository with id"
operator|+
name|repositoryId
argument_list|)
throw|;
block|}
name|repositoryGroup
operator|.
name|removeRepository
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
name|updateRepositoryGroup
argument_list|(
name|repositoryGroup
argument_list|,
name|auditInformation
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|,
literal|null
argument_list|,
name|AuditEvent
operator|.
name|DELETE_REPO_FROM_GROUP
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryGroup
argument_list|>
name|getRepositoryGroupsAsMap
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|RepositoryGroup
argument_list|>
name|repositoriesGroups
init|=
name|getRepositoriesGroups
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryGroup
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|repositoriesGroups
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|RepositoryGroup
name|repositoryGroup
range|:
name|repositoriesGroups
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryGroup
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getGroupToRepositoryMap
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ManagedRepository
name|repo
range|:
name|getManagedRepositoryAdmin
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
control|)
block|{
for|for
control|(
name|RepositoryGroup
name|group
range|:
name|getRepositoriesGroups
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|group
operator|.
name|getRepositories
argument_list|()
operator|.
name|contains
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|groupId
init|=
name|group
operator|.
name|getId
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|repos
init|=
name|map
operator|.
name|get
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|repos
operator|==
literal|null
condition|)
block|{
name|repos
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|groupId
argument_list|,
name|repos
argument_list|)
expr_stmt|;
block|}
name|repos
operator|.
name|add
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|map
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getRepositoryToGroupMap
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|RepositoryGroup
name|group
range|:
name|getRepositoriesGroups
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|repositoryId
range|:
name|group
operator|.
name|getRepositories
argument_list|()
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|groups
init|=
name|map
operator|.
name|get
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
if|if
condition|(
name|groups
operator|==
literal|null
condition|)
block|{
name|groups
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|repositoryId
argument_list|,
name|groups
argument_list|)
expr_stmt|;
block|}
name|groups
operator|.
name|add
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
specifier|public
name|Boolean
name|validateRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|,
name|boolean
name|updateMode
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|String
name|repoGroupId
init|=
name|repositoryGroup
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|repoGroupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"repositoryGroup id cannot be empty"
argument_list|)
throw|;
block|}
if|if
condition|(
name|repoGroupId
operator|.
name|length
argument_list|()
operator|>
literal|100
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Identifier ["
operator|+
name|repoGroupId
operator|+
literal|"] is over the maximum limit of 100 characters"
argument_list|)
throw|;
block|}
name|Matcher
name|matcher
init|=
name|REPO_GROUP_ID_PATTERN
operator|.
name|matcher
argument_list|(
name|repoGroupId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Invalid character(s) found in identifier. Only the following characters are allowed: alphanumeric, '.', '-' and '_'"
argument_list|)
throw|;
block|}
if|if
condition|(
name|repositoryGroup
operator|.
name|getMergedIndexTtl
argument_list|()
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Merged Index TTL must be greater than 0."
argument_list|)
throw|;
block|}
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getRepositoryGroupsAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoGroupId
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|updateMode
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Unable to add new repository group with id ["
operator|+
name|repoGroupId
operator|+
literal|"], that id already exists as a repository group."
argument_list|)
throw|;
block|}
block|}
if|else if
condition|(
name|configuration
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoGroupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Unable to add new repository group with id ["
operator|+
name|repoGroupId
operator|+
literal|"], that id already exists as a managed repository."
argument_list|)
throw|;
block|}
if|else if
condition|(
name|configuration
operator|.
name|getRemoteRepositoriesAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoGroupId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Unable to add new repository group with id ["
operator|+
name|repoGroupId
operator|+
literal|"], that id already exists as a remote repository."
argument_list|)
throw|;
block|}
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
specifier|private
name|void
name|validateManagedRepositoriesExists
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|managedRepositoriesIds
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
for|for
control|(
name|String
name|id
range|:
name|managedRepositoriesIds
control|)
block|{
if|if
condition|(
name|getManagedRepositoryAdmin
argument_list|()
operator|.
name|getManagedRepository
argument_list|(
name|id
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"managedRepository with id "
operator|+
name|id
operator|+
literal|" not exists so cannot be used in a repositoryGroup"
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|ManagedRepositoryAdmin
name|getManagedRepositoryAdmin
parameter_list|()
block|{
return|return
name|managedRepositoryAdmin
return|;
block|}
specifier|public
name|void
name|setManagedRepositoryAdmin
parameter_list|(
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
parameter_list|)
block|{
name|this
operator|.
name|managedRepositoryAdmin
operator|=
name|managedRepositoryAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

