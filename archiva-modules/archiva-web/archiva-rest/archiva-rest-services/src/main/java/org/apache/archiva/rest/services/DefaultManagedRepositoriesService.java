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
name|net
operator|.
name|sf
operator|.
name|beanlib
operator|.
name|provider
operator|.
name|replicator
operator|.
name|BeanReplicator
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
name|RepositoryCommonValidator
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
name|RepositoryStatistics
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|ArchivaRepositoryStatistics
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
name|StringEscapeUtils
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
name|List
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
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
annotation|@
name|Inject
specifier|private
name|RepositoryCommonValidator
name|repositoryCommonValidator
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositorySessionFactory"
argument_list|)
specifier|protected
name|RepositorySessionFactory
name|repositorySessionFactory
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
return|return
name|repos
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|ManagedRepository
operator|>
name|emptyList
argument_list|()
else|:
name|repos
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
argument_list|,
name|e
operator|.
name|getFieldName
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|ManagedRepository
name|addManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|boolean
name|res
init|=
name|managedRepositoryAdmin
operator|.
name|addManagedRepository
argument_list|(
name|managedRepository
argument_list|,
name|managedRepository
operator|.
name|isStageRepoNeeded
argument_list|()
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
condition|)
block|{
return|return
name|getManagedRepository
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
literal|"fail to created managed Repository"
argument_list|)
throw|;
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
argument_list|,
name|e
operator|.
name|getFieldName
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
try|try
block|{
return|return
name|managedRepositoryAdmin
operator|.
name|updateManagedRepository
argument_list|(
name|managedRepository
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
argument_list|,
name|e
operator|.
name|getFieldName
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|fileLocationExists
parameter_list|(
name|String
name|fileLocation
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|String
name|location
init|=
name|repositoryCommonValidator
operator|.
name|removeExpressions
argument_list|(
name|fileLocation
argument_list|)
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|location
argument_list|)
operator|.
name|exists
argument_list|()
return|;
block|}
specifier|public
name|ArchivaRepositoryStatistics
name|getManagedRepositoryStatistics
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
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
name|RepositoryStatistics
name|stats
init|=
literal|null
decl_stmt|;
try|try
block|{
name|stats
operator|=
name|repositoryStatisticsManager
operator|.
name|getLastStatistics
argument_list|(
name|metadataRepository
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error retrieving repository statistics: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|stats
operator|!=
literal|null
condition|)
block|{
name|ArchivaRepositoryStatistics
name|archivaRepositoryStatistics
init|=
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|stats
argument_list|,
name|ArchivaRepositoryStatistics
operator|.
name|class
argument_list|)
decl_stmt|;
name|archivaRepositoryStatistics
operator|.
name|setDuration
argument_list|(
name|archivaRepositoryStatistics
operator|.
name|getScanEndTime
argument_list|()
operator|.
name|getTime
argument_list|()
operator|-
name|archivaRepositoryStatistics
operator|.
name|getScanStartTime
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|archivaRepositoryStatistics
return|;
block|}
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|getPomSnippet
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
return|return
name|createSnippet
argument_list|(
name|getManagedRepository
argument_list|(
name|repositoryId
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|String
name|createSnippet
parameter_list|(
name|ManagedRepository
name|repo
parameter_list|)
block|{
name|StringBuilder
name|snippet
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<project>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"  ...\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<distributionManagement>\n"
argument_list|)
expr_stmt|;
name|String
name|distRepoName
init|=
literal|"repository"
decl_stmt|;
if|if
condition|(
name|repo
operator|.
name|isSnapshots
argument_list|()
condition|)
block|{
name|distRepoName
operator|=
literal|"snapshotRepository"
expr_stmt|;
block|}
name|snippet
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|distRepoName
argument_list|)
operator|.
name|append
argument_list|(
literal|">\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<id>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</id>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<url>"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
name|getBaseUrl
argument_list|(
name|httpServletRequest
argument_list|)
operator|+
literal|"repository"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
literal|"</url>\n"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|"default"
operator|.
name|equals
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
condition|)
block|{
name|snippet
operator|.
name|append
argument_list|(
literal|"<layout>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</layout>"
argument_list|)
expr_stmt|;
block|}
name|snippet
operator|.
name|append
argument_list|(
literal|"</"
argument_list|)
operator|.
name|append
argument_list|(
name|distRepoName
argument_list|)
operator|.
name|append
argument_list|(
literal|">\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</distributionManagement>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<repositories>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<repository>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<id>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</id>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<name>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</name>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<url>"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
name|getBaseUrl
argument_list|(
name|httpServletRequest
argument_list|)
operator|+
literal|"repository"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</url>\n"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|"default"
operator|.
name|equals
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
condition|)
block|{
name|snippet
operator|.
name|append
argument_list|(
literal|"<layout>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</layout>\n"
argument_list|)
expr_stmt|;
block|}
name|snippet
operator|.
name|append
argument_list|(
literal|"<releases>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<enabled>"
argument_list|)
operator|.
name|append
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|repo
operator|.
name|isReleases
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"</enabled>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</releases>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<snapshots>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<enabled>"
argument_list|)
operator|.
name|append
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|repo
operator|.
name|isSnapshots
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"</enabled>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</snapshots>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</repository>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</repositories>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<pluginRepositories>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<pluginRepository>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<id>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</id>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<name>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</name>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<url>"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
name|getBaseUrl
argument_list|(
name|httpServletRequest
argument_list|)
operator|+
literal|"repository"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</url>\n"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|"default"
operator|.
name|equals
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
condition|)
block|{
name|snippet
operator|.
name|append
argument_list|(
literal|"<layout>"
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</layout>\n"
argument_list|)
expr_stmt|;
block|}
name|snippet
operator|.
name|append
argument_list|(
literal|"<releases>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<enabled>"
argument_list|)
operator|.
name|append
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|repo
operator|.
name|isReleases
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"</enabled>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</releases>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<snapshots>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"<enabled>"
argument_list|)
operator|.
name|append
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|repo
operator|.
name|isSnapshots
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"</enabled>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</snapshots>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</pluginRepository>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</pluginRepositories>\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"  ...\n"
argument_list|)
expr_stmt|;
name|snippet
operator|.
name|append
argument_list|(
literal|"</project>\n"
argument_list|)
expr_stmt|;
return|return
name|StringEscapeUtils
operator|.
name|escapeXml
argument_list|(
name|snippet
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

