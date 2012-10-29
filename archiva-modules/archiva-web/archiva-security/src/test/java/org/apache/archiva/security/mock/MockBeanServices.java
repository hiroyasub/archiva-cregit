begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|security
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
name|ProjectMetadata
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
name|ProjectVersionMetadata
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
name|storage
operator|.
name|ReadMetadataRequest
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
name|storage
operator|.
name|RepositoryStorage
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
name|storage
operator|.
name|RepositoryStorageMetadataException
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
name|storage
operator|.
name|RepositoryStorageMetadataInvalidException
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
name|storage
operator|.
name|RepositoryStorageMetadataNotFoundException
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
name|storage
operator|.
name|RepositoryStorageRuntimeException
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
name|model
operator|.
name|ArtifactReference
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
name|policies
operator|.
name|ProxyDownloadException
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
name|redback
operator|.
name|components
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
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepositoryContent
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
name|events
operator|.
name|RepositoryListener
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
name|model
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
name|model
operator|.
name|RepositoryTask
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|MockBeanServices
implements|implements
name|RepositoryStorage
implements|,
name|RepositoryListener
implements|,
name|RepositoryArchivaTaskScheduler
block|{
specifier|public
name|ProjectMetadata
name|readProjectMetadata
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ProjectVersionMetadata
name|readProjectVersionMetadata
parameter_list|(
name|ReadMetadataRequest
name|readMetadataRequest
parameter_list|)
throws|throws
name|RepositoryStorageMetadataInvalidException
throws|,
name|RepositoryStorageMetadataNotFoundException
throws|,
name|RepositoryStorageRuntimeException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|listRootNamespaces
parameter_list|(
name|String
name|repoId
parameter_list|,
name|Filter
argument_list|<
name|String
argument_list|>
name|filter
parameter_list|)
throws|throws
name|RepositoryStorageRuntimeException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|listNamespaces
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|Filter
argument_list|<
name|String
argument_list|>
name|filter
parameter_list|)
throws|throws
name|RepositoryStorageRuntimeException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|listProjects
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|Filter
argument_list|<
name|String
argument_list|>
name|filter
parameter_list|)
throws|throws
name|RepositoryStorageRuntimeException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|listProjectVersions
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|Filter
argument_list|<
name|String
argument_list|>
name|filter
parameter_list|)
throws|throws
name|RepositoryStorageRuntimeException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|readArtifactsMetadata
parameter_list|(
name|ReadMetadataRequest
name|readMetadataRequest
parameter_list|)
throws|throws
name|RepositoryStorageRuntimeException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|ArtifactMetadata
name|readArtifactMetadataFromPath
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|RepositoryStorageRuntimeException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|applyServerSideRelocation
parameter_list|(
name|ManagedRepositoryContent
name|managedRepository
parameter_list|,
name|ArtifactReference
name|artifact
parameter_list|)
throws|throws
name|ProxyDownloadException
block|{
block|}
specifier|public
name|void
name|deleteArtifact
parameter_list|(
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|project
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|id
parameter_list|)
block|{
block|}
specifier|public
name|void
name|addArtifact
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|ProjectVersionMetadata
name|metadata
parameter_list|)
block|{
block|}
specifier|public
name|void
name|addArtifactProblem
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|RepositoryStorageMetadataException
name|exception
parameter_list|)
block|{
block|}
specifier|public
name|boolean
name|isProcessingRepositoryTask
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|isProcessingRepositoryTask
parameter_list|(
name|RepositoryTask
name|task
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|queueTask
parameter_list|(
name|RepositoryTask
name|task
parameter_list|)
throws|throws
name|TaskQueueException
block|{
block|}
specifier|public
name|boolean
name|unQueueTask
parameter_list|(
name|RepositoryTask
name|task
parameter_list|)
throws|throws
name|TaskQueueException
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit
