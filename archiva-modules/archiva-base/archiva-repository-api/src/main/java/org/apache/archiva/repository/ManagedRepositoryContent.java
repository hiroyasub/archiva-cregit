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
name|repository
operator|.
name|content
operator|.
name|ContentItem
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
name|ItemNotFoundException
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
name|repository
operator|.
name|content
operator|.
name|Namespace
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
name|Project
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
name|Version
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
name|stream
operator|.
name|Stream
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
comment|/// *****************   New generation interface **********************
comment|/**      * Removes the specified content item and all content stored under the given item.      *      * @param item the item.      * @throws ItemNotFoundException if the item cannot be found      * @throws ContentAccessException if the deletion was not possible or only partly successful, because the access      *  to the artifacts failed      */
name|void
name|deleteItem
parameter_list|(
name|ContentItem
name|item
parameter_list|)
throws|throws
name|ItemNotFoundException
throws|,
name|ContentAccessException
function_decl|;
comment|/**      * Returns a item for the given selector. The type of the returned item depends on the      * selector.      *      * @param selector the item selector      * @return the content item that matches the given selector      * @throws ContentAccessException if an error occured while accessing the backend      * @throws IllegalArgumentException if the selector does not select a valid content item      */
name|ContentItem
name|getItem
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
function_decl|;
comment|/**      * Returns the namespace for the given selected coordinates. The selector must specify a namespace. All other      * coordinates are ignored.      * The following coordinates must be set at the given selector:      *<ul>      *<li>namespace</li>      *</ul>      * If not, a {@link IllegalArgumentException} will be thrown.      *      * @param namespaceSelector the selectory with the namespace coordinates      * @return the namespace      * @throws ItemNotFoundException if the item does not exist      * @throws ContentAccessException if the item cannot be accessed      * @throws IllegalArgumentException if the selector has no namespace specified      */
name|Namespace
name|getNamespace
parameter_list|(
name|ItemSelector
name|namespaceSelector
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
function_decl|;
comment|/**      * Returns the project for the given coordinates.      * The following coordinates must be set at the given selector:      *<ul>      *<li>namespace</li>      *<li>projectId</li>      *</ul>      * If not, a {@link IllegalArgumentException} will be thrown.      * Additional coordinates will be ignored.      *      * @param projectSelector      * @return the project instance      * @throws ItemNotFoundException if the project does not exist      * @throws ContentAccessException if the item cannot be accessed      * @throws IllegalArgumentException if the selector does not specify the required coordinates      */
name|Project
name|getProject
parameter_list|(
name|ItemSelector
name|projectSelector
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
function_decl|;
comment|/**      * Returns the version for the given coordinates.      * The following coordinates must be set at the given selector:      *<ul>      *<li>namespace</li>      *<li>projectId</li>      *<li>version</li>      *</ul>      * If not, a {@link IllegalArgumentException} will be thrown.      *      * Additional coordinates will be ignored.      *      * @param versionCoordinates      * @return the version object      * @throws ItemNotFoundException      * @throws ContentAccessException      * @throws IllegalArgumentException      */
name|Version
name|getVersion
parameter_list|(
name|ItemSelector
name|versionCoordinates
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
function_decl|;
comment|/**      * Returns the artifact object for the given coordinates.      *      * Normally the following coordinates should be set at the given selector:      *<ul>      *<li>namespace</li>      *<li>artifactVersion and or version</li>      *<li>artifactId or projectId</li>      *</ul>      * If the coordinates do not provide enough information for selecting a artifact, a {@link IllegalArgumentException} will be thrown      * It depends on the repository type, what exactly is deleted for a given set of coordinates. Some repository type      * may have different required and optional coordinates. For further information please check the documentation for the      * type specific implementations.      *      * The following coordinates are optional and may further specify the artifact to delete.      *<ul>      *<li>classifier</li>      *<li>type</li>      *<li>extension</li>      *</ul>      *      * The method always returns a artifact object, if the coordinates are valid. It does not guarantee that the artifact      * exists. To check if there is really a physical representation of the artifact, use the<code>{@link Artifact#exists()}</code>      * method of the artifact.      * For upload and data retrieval use the methods of the {@link StorageAsset} reference returned in the artifact.      *      *      * @param selector the selector with the artifact coordinates      * @return a artifact object      * @throws ItemNotFoundException if the selector coordinates do not specify a artifact      * @throws ContentAccessException if the access to the underlying storage failed      */
name|Artifact
name|getArtifact
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Returns the artifacts that match the given selector. It is up to the repository implementation      * what artifacts are returned for a given set of coordinates.      *      * @param selector the selector for the artifacts      * @return a list of artifacts.      * @throws ItemNotFoundException if the specified coordinates cannot be found in the repository      * @throws ContentAccessException if the access to the underlying storage failed      */
name|List
argument_list|<
name|?
extends|extends
name|Artifact
argument_list|>
name|getAllArtifacts
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Returns the artifacts that match the given selector. It is up to the repository implementation      * what artifacts are returned for a given set of coordinates.      *      * The returned stream is autoclosable and should always closed after using it.      *      * There is no guarantee about the order of the returned artifacts      *      * @param selector the selector for the artifacts      * @return a stream with artifact elements.      * @throws ItemNotFoundException if the specified coordinates cannot be found in the repository      * @throws ContentAccessException if the access to the underlying storage failed      */
name|Stream
argument_list|<
name|?
extends|extends
name|Artifact
argument_list|>
name|getAllArtifactStream
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Return the projects that are part of the given namespace.      *      * @param namespace the namespace      * @return the list of projects or a empty list, if there are no projects for the given namespace.      */
name|List
argument_list|<
name|?
extends|extends
name|Project
argument_list|>
name|getProjects
parameter_list|(
name|Namespace
name|namespace
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Return the existing versions of the given project.      *      * @param project the project      * @return a list of versions or a empty list, if not versions are available for the specified project      */
name|List
argument_list|<
name|?
extends|extends
name|Version
argument_list|>
name|getVersions
parameter_list|(
name|Project
name|project
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Return all the artifacts of a given content item (namespace, project, version)      *      * @param item the item      * @return a list of artifacts or a empty list, if no artifacts are available for the specified item      */
name|List
argument_list|<
name|?
extends|extends
name|Artifact
argument_list|>
name|getArtifacts
parameter_list|(
name|ContentItem
name|item
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Return all the artifacts of a given namespace and all sub namespaces that are defined under the      * given namespace.      *      * @param namespace the namespace, which is the parent namespace      * @param recurse<code>true</code>, if all sub namespaces should be searched too, otherwise<code>false</code>      * @return a list of artifacts or a empty list, if no artifacts are available for the specified namespace      */
name|List
argument_list|<
name|?
extends|extends
name|Artifact
argument_list|>
name|getArtifacts
parameter_list|(
name|Namespace
name|namespace
parameter_list|,
name|boolean
name|recurse
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Return a stream of artifacts that are part of the given content item. The returned stream is      * auto closable. There is no guarantee about the order of returned artifacts.      *      * As the stream may access IO resources, you should always use call this method inside try-with-resources or      * make sure, that the stream is closed after using it.      *      * @param item the item from where the artifacts should be returned      * @return a stream of artifacts. The stream is auto closable. You should always make sure, that the stream      * is closed after use.      */
name|Stream
argument_list|<
name|?
extends|extends
name|Artifact
argument_list|>
name|getArtifactStream
parameter_list|(
name|ContentItem
name|item
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Return a stream of all artifacts that are available for the given namespace and its sub namespaces. The artifacts      * are retrieved recursively. There is no guarantee about the order of returned artifacts.      *      * As the stream may access IO resources, you should always use call this method inside try-with-resources or      * make sure, that the stream is closed after using it.      *      * @param namespace the namespace from where the artifacts should be returned      * @param recurse<code>true</code>, if all sub namespaces should be searched too, otherwise<code>false</code>      * @return a stream of artifacts. The stream is auto closable. You should always make sure, that the stream      * is closed after use.      */
name|Stream
argument_list|<
name|?
extends|extends
name|Artifact
argument_list|>
name|getArtifactStream
parameter_list|(
name|Namespace
name|namespace
parameter_list|,
name|boolean
name|recurse
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Returns true, if the selector coordinates point to a existing item in the repository.      *      * @param selector the item selector      * @return<code>true</code>, if there exists such a item, otherwise<code>false</code>      */
name|boolean
name|hasContent
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
function_decl|;
comment|/**      * Copies the artifact to the given destination coordinates      *      * @param sourceFile the path to the source file      * @param destination the coordinates of the destination      * @throws IllegalArgumentException if the destination is not valid      */
name|void
name|copyArtifact
parameter_list|(
name|Path
name|sourceFile
parameter_list|,
name|ContentItem
name|destination
parameter_list|)
throws|throws
name|IllegalArgumentException
function_decl|;
comment|/**      * Returns the item that matches the given path. The item at the path must not exist.      *      * @param path the path string that points to the item      * @return the content item if the path is a valid item path      * @throws LayoutException if the path is not valid for the repository layout      */
name|ContentItem
name|toItem
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      * Returns the item that matches the given asset path. The asset must not exist.      *      * @param assetPath the path to the artifact or directory      * @return the item, if it is a valid path for the repository layout      * @throws LayoutException if the path is not valid for the repository      */
name|ContentItem
name|toItem
parameter_list|(
name|StorageAsset
name|assetPath
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/// *****************   End of new generation interface **********************
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
comment|/**      * Returns the version reference that represents the generic version, which means that      * snapshot versions are converted to<VERSION>-SNAPSHOT      * @param artifactReference the artifact reference      * @return the generic version      */
name|VersionedReference
name|toGenericVersion
parameter_list|(
name|ArtifactReference
name|artifactReference
parameter_list|)
function_decl|;
comment|/**      * Return the version reference that matches exactly the version string of the artifact      *      * @param artifactReference The artifact reference      * @return the version reference      */
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
throws|,
name|ContentAccessException
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
throws|,
name|ContentAccessException
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
throws|,
name|ContentAccessException
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
name|ContentNotFoundException
throws|,
name|ContentAccessException
function_decl|;
comment|/**      * Deletes a project      * @param reference      */
name|void
name|deleteProject
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|ContentAccessException
function_decl|;
comment|/**      *<p>      * Convenience method to get the repository id.      *</p>      *<p>      * Equivalent to calling<code>.getRepository().getId()</code>      *</p>      *      * @return the repository id.      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      *<p>      * Gather up the list of related artifacts to the ArtifactReference provided.      * If type and / or classifier of the reference is set, this returns only a list of artifacts that is directly      * related to the given artifact, like checksums.      * If type and classifier is<code>null</code> it will return the same artifacts as       * {@link #getRelatedArtifacts(VersionedReference)}      *</p>      *<p>      *<strong>NOTE:</strong> Some layouts (such as maven 1 "legacy") are not compatible with this query.      *</p>      *      * @param reference the reference to work off of.      * @return the list of ArtifactReferences for related artifacts, if      * @throws ContentNotFoundException if the initial artifact reference does not exist within the repository.      * @see #getRelatedArtifacts(VersionedReference)      */
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
throws|,
name|ContentAccessException
function_decl|;
comment|/**      *<p>      * Gather up the list of related artifacts to the ArtifactReference provided.      * This typically includes the pom files, and those things with      * classifiers (such as doc, source code, test libs, etc...). Even if the classifier      * is set in the artifact reference, it may return artifacts with different classifiers.      *</p>      *<p>      *<strong>NOTE:</strong> Some layouts (such as maven 1 "legacy") are not compatible with this query.      *</p>      *      * @param reference the reference to work off of.      * @return the list of ArtifactReferences for related artifacts, if      * @throws ContentNotFoundException if the initial artifact reference does not exist within the repository.      */
name|List
argument_list|<
name|ArtifactReference
argument_list|>
name|getRelatedArtifacts
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentNotFoundException
throws|,
name|LayoutException
throws|,
name|ContentAccessException
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
throws|,
name|ContentAccessException
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
throws|,
name|ContentAccessException
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
throws|,
name|ContentAccessException
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
throws|,
name|ContentAccessException
throws|,
name|LayoutException
function_decl|;
comment|/**      * Determines if the artifact referenced exists in the repository.      *      * @param reference the artifact reference to check for.      * @return true if the artifact referenced exists.      */
name|boolean
name|hasContent
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Determines if the project referenced exists in the repository.      *      * @param reference the project reference to check for.      * @return true it the project referenced exists.      */
name|boolean
name|hasContent
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
throws|throws
name|ContentAccessException
function_decl|;
comment|/**      * Determines if the version reference exists in the repository.      *      * @param reference the version reference to check for.      * @return true if the version referenced exists.      */
name|boolean
name|hasContent
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
throws|throws
name|ContentAccessException
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
name|VersionedReference
name|reference
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

