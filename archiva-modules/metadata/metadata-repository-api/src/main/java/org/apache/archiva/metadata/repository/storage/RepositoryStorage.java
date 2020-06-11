begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
name|repository
operator|.
name|content
operator|.
name|ItemSelector
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
name|xml
operator|.
name|XMLException
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
name|Collection
import|;
end_import

begin_comment
comment|// FIXME: we should drop the repositoryId parameters and attach this to an instance of a repository storage
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryStorage
block|{
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
function_decl|;
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
function_decl|;
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
function_decl|;
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
function_decl|;
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
function_decl|;
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
function_decl|;
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
function_decl|;
comment|// FIXME: reconsider this API, do we want to expose storage format in the form of a path?
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
function_decl|;
comment|/**      * A relocation capable client will request the POM prior to the artifact, and will then read meta-data and do      * client side relocation. A simplier client (like maven 1) will only request the artifact and not use the      * metadatas.      *<p>      * For such clients, archiva does server-side relocation by reading itself the&lt;relocation&gt; element in      * metadatas and serving the expected artifact.      * @param managedRepository the used managed repository      * @param selector the artifact reference      * @throws org.apache.archiva.policies.ProxyDownloadException      */
name|ItemSelector
name|applyServerSideRelocation
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|,
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ProxyDownloadException
function_decl|;
comment|/**      * add an other method to evaluate real path as when receiving -SNAPSHOT (for maven storage)      * request redirect to the last build      * @param requestPath the web uri request      * @param managedRepository the used managed repository can be<code>null</code> so last version won't be resolved      * @return the file path      * @since 2.0.0      */
name|String
name|getFilePath
parameter_list|(
name|String
name|requestPath
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
name|managedRepository
parameter_list|)
function_decl|;
name|String
name|getFilePathWithVersion
parameter_list|(
specifier|final
name|String
name|requestPath
parameter_list|,
name|ManagedRepositoryContent
name|managedRepositoryContent
parameter_list|)
throws|throws
name|RelocationException
throws|,
name|XMLException
throws|,
name|IOException
function_decl|;
block|}
end_interface

end_unit

