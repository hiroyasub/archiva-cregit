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
name|managed
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
name|repository
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepository
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
name|repository
operator|.
name|MetadataRepositoryException
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
name|repository
operator|.
name|RepositorySession
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
name|repository
operator|.
name|RepositorySessionFactory
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
name|repository
operator|.
name|stats
operator|.
name|RepositoryStatisticsManager
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
name|repository
operator|.
name|RepositoryArchivaTaskScheduler
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
name|repository
operator|.
name|RepositoryTask
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
name|io
operator|.
name|FileUtils
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
name|commons
operator|.
name|validator
operator|.
name|GenericValidator
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
name|IndeterminateConfigurationException
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
name|registry
operator|.
name|Registry
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
name|registry
operator|.
name|RegistryException
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
name|taskqueue
operator|.
name|TaskQueueException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|components
operator|.
name|scheduler
operator|.
name|CronExpressionValidator
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
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
comment|/**  * FIXME remove all generic Exception to have usefull ones  * FIXME review the staging mechanism to have a per user session one  *  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"managedRepositoryAdmin#default"
argument_list|)
specifier|public
class|class
name|DefaultManagedRepositoryAdmin
implements|implements
name|ManagedRepositoryAdmin
block|{
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_ID_VALID_EXPRESSION
init|=
literal|"^[a-zA-Z0-9._-]+$"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_NAME_VALID_EXPRESSION
init|=
literal|"^([a-zA-Z0-9.)/_(-]|\\s)+$"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_LOCATION_VALID_EXPRESSION
init|=
literal|"^[-a-zA-Z0-9._/~:?!&amp;=\\\\]+$"
decl_stmt|;
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
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"commons-configuration"
argument_list|)
specifier|private
name|Registry
name|registry
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaTaskScheduler#repository"
argument_list|)
specifier|private
name|RepositoryArchivaTaskScheduler
name|repositoryTaskScheduler
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
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
name|archivaConfiguration
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
name|repoConfig
operator|.
name|getRefreshCronExpression
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
name|addManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|,
name|boolean
name|needStageRepo
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
name|addManagedRepository
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|managedRepository
operator|.
name|getLayout
argument_list|()
argument_list|,
name|managedRepository
operator|.
name|getName
argument_list|()
argument_list|,
name|managedRepository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|managedRepository
operator|.
name|isBlockRedeployments
argument_list|()
argument_list|,
name|managedRepository
operator|.
name|isReleases
argument_list|()
argument_list|,
name|managedRepository
operator|.
name|isSnapshots
argument_list|()
argument_list|,
name|needStageRepo
argument_list|,
name|managedRepository
operator|.
name|getCronExpression
argument_list|()
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|private
name|ManagedRepositoryConfiguration
name|addManagedRepository
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|layout
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|location
parameter_list|,
name|boolean
name|blockRedeployments
parameter_list|,
name|boolean
name|releasesIncluded
parameter_list|,
name|boolean
name|snapshotsIncluded
parameter_list|,
name|boolean
name|stageRepoNeeded
parameter_list|,
name|String
name|cronExpression
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Unable to add new repository with id ["
operator|+
name|repoId
operator|+
literal|"], that id already exists as a managed repository."
argument_list|)
throw|;
block|}
if|else if
condition|(
name|config
operator|.
name|getRepositoryGroupsAsMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Unable to add new repository with id ["
operator|+
name|repoId
operator|+
literal|"], that id already exists as a repository group."
argument_list|)
throw|;
block|}
comment|// FIXME : olamy can be empty to avoid scheduled scan ?
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|cronExpression
argument_list|)
condition|)
block|{
name|CronExpressionValidator
name|validator
init|=
operator|new
name|CronExpressionValidator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|validator
operator|.
name|validate
argument_list|(
name|cronExpression
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Invalid cron expression."
argument_list|)
throw|;
block|}
block|}
comment|// FIXME checKid non empty
if|if
condition|(
operator|!
name|GenericValidator
operator|.
name|matchRegexp
argument_list|(
name|repoId
argument_list|,
name|REPOSITORY_ID_VALID_EXPRESSION
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Invalid repository ID. Identifier must only contain alphanumeric characters, underscores(_), dots(.), and dashes(-)."
argument_list|)
throw|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"repository name cannot be empty"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|GenericValidator
operator|.
name|matchRegexp
argument_list|(
name|name
argument_list|,
name|REPOSITORY_NAME_VALID_EXPRESSION
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Invalid repository name. Repository Name must only contain alphanumeric characters, white-spaces(' '), "
operator|+
literal|"forward-slashes(/), open-parenthesis('('), close-parenthesis(')'),  underscores(_), dots(.), and dashes(-)."
argument_list|)
throw|;
block|}
name|String
name|repoLocation
init|=
name|removeExpressions
argument_list|(
name|location
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|GenericValidator
operator|.
name|matchRegexp
argument_list|(
name|repoLocation
argument_list|,
name|REPOSITORY_LOCATION_VALID_EXPRESSION
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Invalid repository location. Directory must only contain alphanumeric characters, equals(=), question-marks(?), "
operator|+
literal|"exclamation-points(!), ampersands(&amp;), forward-slashes(/), back-slashes(\\), underscores(_), dots(.), colons(:), tildes(~), and dashes(-)."
argument_list|)
throw|;
block|}
name|ManagedRepositoryConfiguration
name|repository
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repository
operator|.
name|setId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setBlockRedeployments
argument_list|(
name|blockRedeployments
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setReleases
argument_list|(
name|releasesIncluded
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setSnapshots
argument_list|(
name|snapshotsIncluded
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setLocation
argument_list|(
name|repoLocation
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setRefreshCronExpression
argument_list|(
name|cronExpression
argument_list|)
expr_stmt|;
try|try
block|{
name|addRepository
argument_list|(
name|repository
argument_list|,
name|config
argument_list|)
expr_stmt|;
if|if
condition|(
name|stageRepoNeeded
condition|)
block|{
name|ManagedRepositoryConfiguration
name|stagingRepository
init|=
name|getStageRepoConfig
argument_list|(
name|repository
argument_list|)
decl_stmt|;
name|addRepository
argument_list|(
name|stagingRepository
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"failed to add repository "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|saveConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
comment|//MRM-1342 Repository statistics report doesn't appear to be working correctly
comment|//scan repository when adding of repository is successful
try|try
block|{
name|scanRepository
argument_list|(
name|repoId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|stageRepoNeeded
condition|)
block|{
name|ManagedRepositoryConfiguration
name|stagingRepository
init|=
name|getStageRepoConfig
argument_list|(
name|repository
argument_list|)
decl_stmt|;
name|scanRepository
argument_list|(
name|stagingRepository
operator|.
name|getId
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
operator|new
name|StringBuilder
argument_list|(
literal|"Unable to scan repository ["
argument_list|)
operator|.
name|append
argument_list|(
name|repoId
argument_list|)
operator|.
name|append
argument_list|(
literal|"]: "
argument_list|)
operator|.
name|append
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|repository
return|;
block|}
specifier|public
name|Boolean
name|deleteManagedRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
if|if
condition|(
name|repository
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"A repository with that id does not exist"
argument_list|)
throw|;
block|}
name|RepositorySession
name|repositorySession
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
decl_stmt|;
try|try
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|metadataRepository
operator|.
name|removeRepository
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryStatisticsManager
operator|.
name|deleteStatistics
argument_list|(
name|metadataRepository
argument_list|,
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|repositorySession
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|config
operator|.
name|removeManagedRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
try|try
block|{
name|saveConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Error saving configuration for delete action"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
comment|// TODO could be async ? as directory can be huge
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|dir
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Cannot delete repository "
operator|+
name|dir
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|proxyConnectors
init|=
name|config
operator|.
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
name|repository
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
name|repository
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
name|repository
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
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|Boolean
operator|.
name|TRUE
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
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
comment|//--------------------------
comment|// utils methods
comment|//--------------------------
specifier|private
name|String
name|removeExpressions
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
name|String
name|value
init|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|directory
argument_list|,
literal|"${appserver.base}"
argument_list|,
name|registry
operator|.
name|getString
argument_list|(
literal|"appserver.base"
argument_list|,
literal|"${appserver.base}"
argument_list|)
argument_list|)
decl_stmt|;
name|value
operator|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|value
argument_list|,
literal|"${appserver.home}"
argument_list|,
name|registry
operator|.
name|getString
argument_list|(
literal|"appserver.home"
argument_list|,
literal|"${appserver.home}"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|value
return|;
block|}
specifier|private
name|void
name|saveConfiguration
parameter_list|(
name|Configuration
name|config
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
try|try
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Error occurred in the registry."
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IndeterminateConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Error occurred while saving the configuration."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|addRepository
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|RepositoryAdminException
throws|,
name|IOException
block|{
comment|// Normalize the path
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|repository
operator|.
name|setLocation
argument_list|(
name|file
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
operator|||
operator|!
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Unable to add repository - no write access, can not create the root directory: "
operator|+
name|file
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ManagedRepositoryConfiguration
name|getStageRepoConfig
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|)
block|{
name|ManagedRepositoryConfiguration
name|stagingRepository
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|stagingRepository
operator|.
name|setId
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
operator|+
literal|"-stage"
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setLayout
argument_list|(
name|repository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setName
argument_list|(
name|repository
operator|.
name|getName
argument_list|()
operator|+
literal|"-stage"
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setBlockRedeployments
argument_list|(
name|repository
operator|.
name|isBlockRedeployments
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setDaysOlder
argument_list|(
name|repository
operator|.
name|getDaysOlder
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
name|repository
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setIndexDir
argument_list|(
name|repository
operator|.
name|getIndexDir
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|repository
operator|.
name|getLocation
argument_list|()
decl_stmt|;
name|int
name|lastIndex
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|stagingRepository
operator|.
name|setLocation
argument_list|(
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastIndex
argument_list|)
operator|+
literal|"/"
operator|+
name|stagingRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setRefreshCronExpression
argument_list|(
name|repository
operator|.
name|getRefreshCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setReleases
argument_list|(
name|repository
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setRetentionCount
argument_list|(
name|repository
operator|.
name|getRetentionCount
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setScanned
argument_list|(
name|repository
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setSnapshots
argument_list|(
name|repository
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|stagingRepository
return|;
block|}
specifier|public
name|Boolean
name|scanRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|boolean
name|fullScan
parameter_list|)
block|{
if|if
condition|(
name|repositoryTaskScheduler
operator|.
name|isProcessingRepositoryTask
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"scanning of repository with id {} already scheduled"
argument_list|)
expr_stmt|;
block|}
name|RepositoryTask
name|task
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setRepositoryId
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
name|task
operator|.
name|setScanAll
argument_list|(
name|fullScan
argument_list|)
expr_stmt|;
try|try
block|{
name|repositoryTaskScheduler
operator|.
name|queueTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"failed to schedule scanning of repo with id {}"
argument_list|,
name|repositoryId
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

