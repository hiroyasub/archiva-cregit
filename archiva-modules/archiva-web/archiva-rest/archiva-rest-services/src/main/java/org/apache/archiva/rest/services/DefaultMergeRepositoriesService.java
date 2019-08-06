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
name|common
operator|.
name|utils
operator|.
name|VersionUtil
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
name|maven2
operator|.
name|model
operator|.
name|Artifact
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
name|repository
operator|.
name|ReleaseScheme
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
name|MergeRepositoriesService
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
name|archiva
operator|.
name|stagerepository
operator|.
name|merge
operator|.
name|RepositoryMergerException
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"mergeRepositoriesService#rest"
argument_list|)
specifier|public
class|class
name|DefaultMergeRepositoriesService
extends|extends
name|AbstractRestService
implements|implements
name|MergeRepositoriesService
block|{
comment|// FIXME check archiva-merge-repository to sourceRepoId
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
name|Override
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|getMergeConflictedArtifacts
parameter_list|(
name|String
name|sourceRepositoryId
parameter_list|,
name|String
name|targetRepositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
name|RepositorySession
name|repositorySession
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repositorySession
operator|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
try|try
block|{
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactMetadatas
init|=
name|repositoryMerger
operator|.
name|getConflictingArtifacts
argument_list|(
name|repositorySession
operator|.
name|getRepository
argument_list|()
argument_list|,
name|sourceRepositoryId
argument_list|,
name|targetRepositoryId
argument_list|)
decl_stmt|;
return|return
name|buildArtifacts
argument_list|(
name|artifactMetadatas
argument_list|,
name|sourceRepositoryId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryMergerException
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|mergeRepositories
parameter_list|(
name|String
name|sourceRepositoryId
parameter_list|,
name|String
name|targetRepositoryId
parameter_list|,
name|boolean
name|skipConflicts
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
if|if
condition|(
name|skipConflicts
condition|)
block|{
name|mergeBySkippingConflicts
argument_list|(
name|sourceRepositoryId
argument_list|,
name|targetRepositoryId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doMerge
argument_list|(
name|sourceRepositoryId
argument_list|,
name|targetRepositoryId
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryMergerException
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
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|doMerge
parameter_list|(
name|String
name|sourceRepositoryId
parameter_list|,
name|String
name|targetRepositoryId
parameter_list|)
throws|throws
name|RepositoryMergerException
throws|,
name|ArchivaRestServiceException
block|{
name|RepositorySession
name|repositorySession
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repositorySession
operator|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
try|try
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
name|managedRepo
init|=
name|repositoryRegistry
operator|.
name|getManagedRepository
argument_list|(
name|targetRepositoryId
argument_list|)
decl_stmt|;
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
name|repositorySession
argument_list|,
name|sourceRepositoryId
argument_list|)
decl_stmt|;
if|if
condition|(
name|managedRepo
operator|.
name|getActiveReleaseSchemes
argument_list|()
operator|.
name|contains
argument_list|(
name|ReleaseScheme
operator|.
name|RELEASE
argument_list|)
operator|&&
operator|!
name|managedRepo
operator|.
name|getActiveReleaseSchemes
argument_list|()
operator|.
name|contains
argument_list|(
name|ReleaseScheme
operator|.
name|SNAPSHOT
argument_list|)
condition|)
block|{
name|mergeWithOutSnapshots
argument_list|(
name|metadataRepository
argument_list|,
name|sourceArtifacts
argument_list|,
name|sourceRepositoryId
argument_list|,
name|targetRepositoryId
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
name|sourceRepositoryId
argument_list|,
name|targetRepositoryId
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
name|targetRepositoryId
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
name|doScanRepository
argument_list|(
name|targetRepositoryId
argument_list|,
literal|false
argument_list|)
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
name|ArchivaRestServiceException
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
block|}
specifier|public
name|void
name|mergeBySkippingConflicts
parameter_list|(
name|String
name|sourceRepositoryId
parameter_list|,
name|String
name|targetRepositoryId
parameter_list|)
throws|throws
name|RepositoryMergerException
throws|,
name|ArchivaRestServiceException
block|{
name|RepositorySession
name|repositorySession
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repositorySession
operator|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
try|try
block|{
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|conflictSourceArtifacts
init|=
name|repositoryMerger
operator|.
name|getConflictingArtifacts
argument_list|(
name|repositorySession
operator|.
name|getRepository
argument_list|()
argument_list|,
name|sourceRepositoryId
argument_list|,
name|targetRepositoryId
argument_list|)
decl_stmt|;
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
name|repositorySession
argument_list|,
name|sourceRepositoryId
argument_list|)
decl_stmt|;
name|sourceArtifacts
operator|.
name|removeAll
argument_list|(
name|conflictSourceArtifacts
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
name|managedRepo
init|=
name|repositoryRegistry
operator|.
name|getManagedRepository
argument_list|(
name|targetRepositoryId
argument_list|)
decl_stmt|;
if|if
condition|(
name|managedRepo
operator|.
name|getActiveReleaseSchemes
argument_list|()
operator|.
name|contains
argument_list|(
name|ReleaseScheme
operator|.
name|RELEASE
argument_list|)
operator|&&
operator|!
name|managedRepo
operator|.
name|getActiveReleaseSchemes
argument_list|()
operator|.
name|contains
argument_list|(
name|ReleaseScheme
operator|.
name|SNAPSHOT
argument_list|)
condition|)
block|{
name|mergeWithOutSnapshots
argument_list|(
name|metadataRepository
argument_list|,
name|sourceArtifacts
argument_list|,
name|sourceRepositoryId
argument_list|,
name|targetRepositoryId
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
name|sourceRepositoryId
argument_list|,
name|targetRepositoryId
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
name|targetRepositoryId
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
name|doScanRepository
argument_list|(
name|targetRepositoryId
argument_list|,
literal|false
argument_list|)
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
name|ArchivaRestServiceException
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
name|RepositoryMergerException
block|{
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactsWithOutSnapshots
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|metadata
operator|.
name|getProjectVersion
argument_list|()
argument_list|)
condition|)
comment|//if ( metadata.getProjectVersion().contains( VersionUtil.SNAPSHOT ) )
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
block|}
end_class

end_unit

