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
name|storage
operator|.
name|StorageAsset
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
comment|/**      * Returns the version reference for the given coordinates.      * @param groupId the group id      * @param artifactId the artifact id      * @param version the version number      * @return a version reference      */
name|VersionedReference
name|toVersion
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
function_decl|;
comment|/**      * Return the version reference the artifact is part of.      *      * @param artifactReference The artifact reference      * @return the version reference      */
name|VersionedReference
name|toVersion
parameter_list|(
name|ArtifactReference
name|artifactReference
parameter_list|)
function_decl|;
comment|/**      * Returns a artifact reference for the given coordinates.      * @param groupId the group id      * @param artifactId the artifact id      * @param version the version      * @param type the type      * @param classifier the classifier      * @return a artifact reference object      */
name|ArtifactReference
name|toArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|classifier
parameter_list|)
function_decl|;
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
comment|/**      *<p>      * Gather up the list of related artifacts to the ArtifactReference provided.      * This typically includes the pom files, and those things with      * classifiers (such as doc, source code, test libs, etc...). Even if the classifier      * is set in the artifact reference, it may return artifacts with different classifiers.      *</p>      *<p>      *<strong>NOTE:</strong> Some layouts (such as maven 1 "legacy") are not compatible with this query.      *</p>      *      * @param reference the reference to work off of.      * @return the list of ArtifactReferences for related artifacts, if      * @throws ContentNotFoundException if the initial artifact reference does not exist within the repository.      */
name|List
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
throws|,
name|LayoutException
function_decl|;
comment|/**      * Returns all the assets that belong to a given artifact type. The list returned contain      * all the files that correspond to the given artifact reference.      * This method is the same as {@link #getRelatedArtifacts(ArtifactReference)} but may also return      * e.g. hash files.      *      * @param reference      * @return      */
name|List
argument_list|<
name|StorageAsset
argument_list|>
name|getRelatedAssets
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
function_decl|;
comment|/**      * Returns all artifacts that belong to a given version      * @param reference the version reference      * @return the list of artifacts or a empty list      */
name|List
argument_list|<
name|ArtifactReference
argument_list|>
name|getArtifacts
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
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
name|ManagedRepository
name|repo
parameter_list|)
function_decl|;
comment|/**      * Given an {@link ArtifactReference}, return the file reference to the artifact.      *      * @param reference the artifact reference to use.      * @return the relative path to the artifact.      */
name|StorageAsset
name|toFile
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
function_decl|;
comment|/**      * Given an {@link ArchivaArtifact}, return the file reference to the artifact.      *      * @param reference the archiva artifact to use.      * @return the relative path to the artifact.      */
name|StorageAsset
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
block|}
end_interface

end_unit

