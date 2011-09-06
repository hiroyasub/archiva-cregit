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
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Validateable
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
name|audit
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
name|audit
operator|.
name|Auditable
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
name|ArtifactMetadata
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
name|filter
operator|.
name|Filter
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
name|filter
operator|.
name|IncludesFilter
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
name|archiva
operator|.
name|stagerepository
operator|.
name|merge
operator|.
name|Maven2RepositoryMerger
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
name|ManagedRepositoryConfiguration
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
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
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
name|Controller
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

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"mergeAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|MergeAction
extends|extends
name|AbstractActionSupport
implements|implements
name|Validateable
implements|,
name|Preparable
implements|,
name|Auditable
block|{
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositoryMerger#maven2"
argument_list|)
specifier|private
name|Maven2RepositoryMerger
name|repositoryMerger
decl_stmt|;
annotation|@
name|Inject
specifier|protected
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
literal|"archivaTaskScheduler#repository"
argument_list|)
specifier|private
name|RepositoryArchivaTaskScheduler
name|repositoryTaskScheduler
decl_stmt|;
specifier|private
name|ManagedRepositoryConfiguration
name|repository
decl_stmt|;
specifier|private
name|String
name|repoid
decl_stmt|;
specifier|private
name|String
name|sourceRepoId
decl_stmt|;
specifier|private
specifier|final
name|String
name|action
init|=
literal|"merge"
decl_stmt|;
specifier|private
specifier|final
name|String
name|hasConflicts
init|=
literal|"CONFLICTS"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|conflictSourceArtifacts
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|conflictSourceArtifactsToBeDisplayed
decl_stmt|;
specifier|public
name|String
name|getConflicts
parameter_list|()
block|{
name|sourceRepoId
operator|=
name|repoid
operator|+
literal|"-stage"
expr_stmt|;
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|ManagedRepositoryConfiguration
name|targetRepoConfig
init|=
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|sourceRepoId
argument_list|)
decl_stmt|;
if|if
condition|(
name|targetRepoConfig
operator|!=
literal|null
condition|)
block|{
return|return
name|hasConflicts
return|;
block|}
else|else
block|{
return|return
name|ERROR
return|;
block|}
block|}
specifier|public
name|String
name|doMerge
parameter_list|()
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
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|sourceArtifacts
init|=
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|sourceRepoId
argument_list|)
decl_stmt|;
if|if
condition|(
name|repository
operator|.
name|isReleases
argument_list|()
operator|&&
operator|!
name|repository
operator|.
name|isSnapshots
argument_list|()
condition|)
block|{
name|mergeWithOutSnapshots
argument_list|(
name|metadataRepository
argument_list|,
name|sourceArtifacts
argument_list|,
name|sourceRepoId
argument_list|,
name|repoid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|repositoryMerger
operator|.
name|merge
argument_list|(
name|metadataRepository
argument_list|,
name|sourceRepoId
argument_list|,
name|repoid
argument_list|)
expr_stmt|;
for|for
control|(
name|ArtifactMetadata
name|metadata
range|:
name|sourceArtifacts
control|)
block|{
name|triggerAuditEvent
argument_list|(
name|repoid
argument_list|,
name|metadata
operator|.
name|getId
argument_list|()
argument_list|,
name|AuditEvent
operator|.
name|MERGING_REPOSITORIES
argument_list|)
expr_stmt|;
block|}
block|}
name|scanRepository
argument_list|()
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Repository '"
operator|+
name|sourceRepoId
operator|+
literal|"' successfully merged to '"
operator|+
name|repoid
operator|+
literal|"'."
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|addActionError
argument_list|(
literal|"Error occurred while merging the repositories: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|mergeBySkippingConflicts
parameter_list|()
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
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|sourceArtifacts
init|=
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|sourceRepoId
argument_list|)
decl_stmt|;
name|sourceArtifacts
operator|.
name|removeAll
argument_list|(
name|conflictSourceArtifacts
argument_list|)
expr_stmt|;
if|if
condition|(
name|repository
operator|.
name|isReleases
argument_list|()
operator|&&
operator|!
name|repository
operator|.
name|isSnapshots
argument_list|()
condition|)
block|{
name|mergeWithOutSnapshots
argument_list|(
name|metadataRepository
argument_list|,
name|sourceArtifacts
argument_list|,
name|sourceRepoId
argument_list|,
name|repoid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Filter
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactsWithOutConflicts
init|=
operator|new
name|IncludesFilter
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|(
name|sourceArtifacts
argument_list|)
decl_stmt|;
name|repositoryMerger
operator|.
name|merge
argument_list|(
name|metadataRepository
argument_list|,
name|sourceRepoId
argument_list|,
name|repoid
argument_list|,
name|artifactsWithOutConflicts
argument_list|)
expr_stmt|;
for|for
control|(
name|ArtifactMetadata
name|metadata
range|:
name|sourceArtifacts
control|)
block|{
name|triggerAuditEvent
argument_list|(
name|repoid
argument_list|,
name|metadata
operator|.
name|getId
argument_list|()
argument_list|,
name|AuditEvent
operator|.
name|MERGING_REPOSITORIES
argument_list|)
expr_stmt|;
block|}
block|}
name|scanRepository
argument_list|()
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Repository '"
operator|+
name|sourceRepoId
operator|+
literal|"' successfully merged to '"
operator|+
name|repoid
operator|+
literal|"'."
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
name|addActionError
argument_list|(
literal|"Error occurred while merging the repositories: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|mergeWithOutConlficts
parameter_list|()
block|{
name|sourceRepoId
operator|=
name|repoid
operator|+
literal|"-stage"
expr_stmt|;
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
name|conflictSourceArtifacts
operator|=
name|repositoryMerger
operator|.
name|getConflictingArtifacts
argument_list|(
name|repositorySession
operator|.
name|getRepository
argument_list|()
argument_list|,
name|sourceRepoId
argument_list|,
name|repoid
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Error occurred while merging the repositories."
argument_list|)
expr_stmt|;
return|return
name|ERROR
return|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|addActionMessage
argument_list|(
literal|"Repository '"
operator|+
name|sourceRepoId
operator|+
literal|"' successfully merged to '"
operator|+
name|repoid
operator|+
literal|"'."
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
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
name|void
name|prepare
parameter_list|()
throws|throws
name|Exception
block|{
name|sourceRepoId
operator|=
name|repoid
operator|+
literal|"-stage"
expr_stmt|;
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
name|conflictSourceArtifacts
operator|=
name|repositoryMerger
operator|.
name|getConflictingArtifacts
argument_list|(
name|repositorySession
operator|.
name|getRepository
argument_list|()
argument_list|,
name|sourceRepoId
argument_list|,
name|repoid
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|this
operator|.
name|repository
operator|=
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|repoid
argument_list|)
expr_stmt|;
name|setConflictSourceArtifactsToBeDisplayed
argument_list|(
name|conflictSourceArtifacts
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getSourceRepoId
parameter_list|()
block|{
return|return
name|sourceRepoId
return|;
block|}
specifier|public
name|void
name|setSourceRepoId
parameter_list|(
name|String
name|sourceRepoId
parameter_list|)
block|{
name|this
operator|.
name|sourceRepoId
operator|=
name|sourceRepoId
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
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getConflictSourceArtifacts
parameter_list|()
block|{
return|return
name|conflictSourceArtifacts
return|;
block|}
specifier|public
name|void
name|setConflictSourceArtifacts
parameter_list|(
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|conflictSourceArtifacts
parameter_list|)
block|{
name|this
operator|.
name|conflictSourceArtifacts
operator|=
name|conflictSourceArtifacts
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getConflictSourceArtifactsToBeDisplayed
parameter_list|()
block|{
return|return
name|conflictSourceArtifactsToBeDisplayed
return|;
block|}
specifier|public
name|void
name|setConflictSourceArtifactsToBeDisplayed
parameter_list|(
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|conflictSourceArtifacts
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|conflictSourceArtifactsToBeDisplayed
operator|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|()
expr_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|ArtifactMetadata
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|ArtifactMetadata
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|metadata
range|:
name|conflictSourceArtifacts
control|)
block|{
name|String
name|metadataId
init|=
name|metadata
operator|.
name|getNamespace
argument_list|()
operator|+
name|metadata
operator|.
name|getProject
argument_list|()
operator|+
name|metadata
operator|.
name|getProjectVersion
argument_list|()
operator|+
name|metadata
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|metadataId
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
name|conflictSourceArtifactsToBeDisplayed
operator|.
name|addAll
argument_list|(
name|map
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|mergeWithOutSnapshots
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|sourceArtifacts
parameter_list|,
name|String
name|sourceRepoId
parameter_list|,
name|String
name|repoid
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactsWithOutSnapshots
init|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|metadata
range|:
name|sourceArtifacts
control|)
block|{
if|if
condition|(
name|metadata
operator|.
name|getProjectVersion
argument_list|()
operator|.
name|contains
argument_list|(
literal|"SNAPSHOT"
argument_list|)
condition|)
block|{
name|artifactsWithOutSnapshots
operator|.
name|add
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|triggerAuditEvent
argument_list|(
name|repoid
argument_list|,
name|metadata
operator|.
name|getId
argument_list|()
argument_list|,
name|AuditEvent
operator|.
name|MERGING_REPOSITORIES
argument_list|)
expr_stmt|;
block|}
block|}
name|sourceArtifacts
operator|.
name|removeAll
argument_list|(
name|artifactsWithOutSnapshots
argument_list|)
expr_stmt|;
name|Filter
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactListWithOutSnapShots
init|=
operator|new
name|IncludesFilter
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|(
name|sourceArtifacts
argument_list|)
decl_stmt|;
name|repositoryMerger
operator|.
name|merge
argument_list|(
name|metadataRepository
argument_list|,
name|sourceRepoId
argument_list|,
name|repoid
argument_list|,
name|artifactListWithOutSnapShots
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|scanRepository
parameter_list|()
block|{
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
name|repoid
argument_list|)
expr_stmt|;
name|task
operator|.
name|setScanAll
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|repositoryTaskScheduler
operator|.
name|isProcessingRepositoryTask
argument_list|(
name|repoid
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Repository ["
operator|+
name|repoid
operator|+
literal|"] task was already queued."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Your request to have repository ["
operator|+
name|repoid
operator|+
literal|"] be indexed has been queued."
argument_list|)
expr_stmt|;
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
name|warn
argument_list|(
literal|"Unable to queue your request to have repository ["
operator|+
name|repoid
operator|+
literal|"] be indexed: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

