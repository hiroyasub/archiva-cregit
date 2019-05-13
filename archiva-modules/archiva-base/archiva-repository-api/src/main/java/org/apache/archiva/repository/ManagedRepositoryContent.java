begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|ArchivaArtifact
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
name|model
operator|.
name|ProjectReference
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
name|VersionedReference
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
name|StorageAsset
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  * ManagedRepositoryContent interface for interacting with a managed repository in an abstract way,  * without the need for processing based on filesystem paths, or working with the database.  *  * This interface  */
end_comment

begin_interface
specifier|public
interface|interface
name|ManagedRepositoryContent
extends|extends
name|RepositoryContent
block|{
comment|/**      * Delete from the managed repository all files / directories associated with the      * provided version reference.      *      * @param reference the version reference to delete.      * @throws ContentNotFoundException      */
name|void
name|deleteVersion
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
function_decl|;
comment|/**      * delete a specified artifact from the repository      *      * @param artifactReference      * @throws ContentNotFoundException      */
name|void
name|deleteArtifact
parameter_list|(
name|ArtifactReference
name|artifactReference
parameter_list|)
throws|throws
name|ContentNotFoundException
function_decl|;
comment|/**      * @param groupId      * @throws ContentNotFoundException      * @since 1.4-M3      */
name|void
name|deleteGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
throws|throws
name|ContentNotFoundException
function_decl|;
comment|/**      *      * @param namespace groupId for maven      * @param projectId artifactId for maven      * @throws ContentNotFoundException      */
name|void
name|deleteProject
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      *<p>      * Convenience method to get the repository id.      *</p>      *<p>      * Equivalent to calling<code>.getRepository().getId()</code>      *</p>      *      * @return the repository id.      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      *<p>      * Gather up the list of related artifacts to the ArtifactReference provided.      * This typically inclues the pom files, and those things with      * classifiers (such as doc, source code, test libs, etc...)      *</p>      *<p>      *<strong>NOTE:</strong> Some layouts (such as maven 1 "legacy") are not compatible with this query.      *</p>      *      * @param reference the reference to work off of.      * @return the set of ArtifactReferences for related artifacts.      * @throws ContentNotFoundException if the initial artifact reference does not exist within the repository.      */
name|Set
argument_list|<
name|ArtifactReference
argument_list|>
name|getRelatedArtifacts
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
function_decl|;
comment|/**      *<p>      * Convenience method to get the repository (on disk) root directory.      *</p>      *<p>      * Equivalent to calling<code>.getRepository().getLocation()</code>      *</p>      *      * @return the repository (on disk) root directory.      */
name|String
name|getRepoRoot
parameter_list|()
function_decl|;
comment|/**      * Get the repository configuration associated with this      * repository content.      *      * @return the repository that is associated with this repository content.      */
name|ManagedRepository
name|getRepository
parameter_list|()
function_decl|;
comment|/**      * Given a specific {@link ProjectReference}, return the list of available versions for      * that project reference.      *      * @param reference the project reference to work off of.      * @return the list of versions found for that project reference.      * @throws ContentNotFoundException if the project reference does nto exist within the repository.      * @throws LayoutException      */
name|Set
argument_list|<
name|String
argument_list|>
name|getVersions
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
function_decl|;
comment|/**      *<p>      * Given a specific {@link VersionedReference}, return the list of available versions for that      * versioned reference.      *</p>      *<p>      *<strong>NOTE:</strong> This is really only useful when working with SNAPSHOTs.      *</p>      *      * @param reference the versioned reference to work off of.      * @return the set of versions found.      * @throws ContentNotFoundException if the versioned reference does not exist within the repository.      */
name|Set
argument_list|<
name|String
argument_list|>
name|getVersions
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
function_decl|;
comment|/**      * Determines if the artifact referenced exists in the repository.      *      * @param reference the artifact reference to check for.      * @return true if the artifact referenced exists.      */
name|boolean
name|hasContent
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
function_decl|;
comment|/**      * Determines if the project referenced exists in the repository.      *      * @param reference the project reference to check for.      * @return true it the project referenced exists.      */
name|boolean
name|hasContent
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
function_decl|;
comment|/**      * Determines if the version reference exists in the repository.      *      * @param reference the version reference to check for.      * @return true if the version referenced exists.      */
name|boolean
name|hasContent
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
function_decl|;
comment|/**      * Set the repository configuration to associate with this      * repository content.      *      * @param repo the repository to associate with this repository content.      */
name|void
name|setRepository
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
name|repo
parameter_list|)
function_decl|;
comment|/**      * Given an {@link ArtifactReference}, return the file reference to the artifact.      *      * @param reference the artifact reference to use.      * @return the relative path to the artifact.      */
name|Path
name|toFile
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
function_decl|;
comment|/**      * Given an {@link ArchivaArtifact}, return the file reference to the artifact.      *      * @param reference the archiva artifact to use.      * @return the relative path to the artifact.      */
name|Path
name|toFile
parameter_list|(
name|ArchivaArtifact
name|reference
parameter_list|)
function_decl|;
comment|/**      * Given a {@link ProjectReference}, return the path to the metadata for      * the project.      *      * @param reference the reference to use.      * @return the path to the metadata file, or null if no metadata is appropriate.      */
name|String
name|toMetadataPath
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
function_decl|;
comment|/**      * Given a {@link VersionedReference}, return the path to the metadata for      * the specific version of the project.      *      * @param reference the reference to use.      * @return the path to the metadata file, or null if no metadata is appropriate.      */
name|String
name|toMetadataPath
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
function_decl|;
comment|/**      * Given an {@link ArchivaArtifact}, return the relative path to the artifact.      *      * @param reference the archiva artifact to use.      * @return the relative path to the artifact.      */
name|String
name|toPath
parameter_list|(
name|ArchivaArtifact
name|reference
parameter_list|)
function_decl|;
comment|/**      * Returns information about a specific storage asset.      * @param path      * @return      */
name|StorageAsset
name|getAsset
parameter_list|(
name|String
name|path
parameter_list|)
function_decl|;
comment|/**      * Consumes the data and sets a lock for the file during the operation.      *      * @param asset      * @param consumerFunction      * @param readLock      * @throws IOException      */
name|void
name|consumeData
parameter_list|(
name|StorageAsset
name|asset
parameter_list|,
name|Consumer
argument_list|<
name|InputStream
argument_list|>
name|consumerFunction
parameter_list|,
name|boolean
name|readLock
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Adds a new asset to the underlying storage.      * @param path The path to the asset.      * @param container True, if the asset should be a container, false, if it is a file.      * @return      */
name|StorageAsset
name|addAsset
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|container
parameter_list|)
function_decl|;
comment|/**      * Removes the given asset from the storage.      *      * @param asset      * @throws IOException      */
name|void
name|removeAsset
parameter_list|(
name|StorageAsset
name|asset
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Moves the asset to the given location and returns the asset object for the destination.      *      * @param origin      * @param destination      * @return      */
name|StorageAsset
name|moveAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|String
name|destination
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Copies the given asset to the new destination.      *      * @param origin      * @param destination      * @return      * @throws IOException      */
name|StorageAsset
name|copyAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|String
name|destination
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

