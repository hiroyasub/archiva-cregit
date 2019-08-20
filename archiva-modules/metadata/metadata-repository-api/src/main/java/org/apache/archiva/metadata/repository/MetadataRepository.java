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
name|MetadataFacet
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
name|model
operator|.
name|ProjectVersionReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
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
name|stream
operator|.
name|Stream
import|;
end_import

begin_comment
comment|/**  * A Metadata repository provides information about artifact metadata. It does not provide the artifact data itself.  * It may be possible to use the same backend for metadata and storage, but this depends on the backends and they are  * provided by different APIs.  *  * The motivation for this API is to provide fast access to the repository metadata and fulltext search. Also dependencies  * are stored in this repository.  *  * The methods here do not update the artifacts itself. They are only updating the data in the metadata repository.  * That means, if you want to update some artifact, you should make sure to update the artifact itself and the metadata  * repository (either directly or by repository scanning).  *  * Currently we are providing JCR, File based and Cassandra as backend for the metadata.  *  * The metadata repository uses sessions for accessing the data. Please make sure to always close the sessions after using it.  * Best idiom for using the sessions:  *<code>  * try(RepositorySession session = sessionFactory.createSession() {  *     // do your stuff  * }  *</code>  *  * It is implementation dependent, if the sessions are really used by the backend. E.g. the file based implementation ignores  * the sessions completely.  *  * Sessions should be closed immediately after usage. If it is expensive to open a session for a given backend. The backend  * should provide a session pool if possible. There are methods for refreshing a session if needed.  *  * You should avoid stacking sessions, that means, do not create a new session in the same thread, when a session is opened already.  *  * Some backend implementations (JCR) update the metadata in the background, that means update of the metadata is not reflected  * immediately.  *  * The base metadata coordinates are:  *<ul>  *<li>Repository ID: The identifier of the repository, where the artifact resides</li>  *<li>Namespace: This is a hierarchical coordinate for locating the projects. E.g. this corresponds to the groupId in maven.</li>  *<li>Project ID: The project itself</li>  *<li>Version: Each project may have different versions.</li>  *<li>Artifact: Artifacts correspond to files / blob data. Each artifact has additional metadata, like name, version, modification time, ...</li>  *</ul>  *  * As the repository connects to some backend either locally or remote, the access to the repository may fail. The methods capsule the  * backend errors into<code>{@link MetadataRepositoryException}</code>.  *  * Facets are the way to provide additional metadata that is not part of the base API. It depends on the repository type (e.g. Maven, NPM,  * not the metadata backend) what facets are stored in addition to the standard metadata.  * Facets have a specific facet ID that represents the schema for the data stored. For creating specific objects for a given  * facet id the<code>{@link org.apache.archiva.metadata.model.MetadataFacetFactory}</code> is used.  * For each facet id there may exist multiple facet instances on each level. Facet instances are identified by their name, which may be  * a hierarchical path.  * The data in each facet instance is stored in properties (key-value pairs). The properties are converted into / from the specific  * facet object.  *  * Facets can be stored on repository, project, version and artifact level.  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|MetadataRepository
block|{
comment|/**      * Update metadata for a particular project in the metadata repository, or create it, if it does not already exist.      *      * @param session The session used for updating.      * @param repositoryId the repository the project is in      * @param project      the project metadata to create or update      * @throws MetadataRepositoryException if the update fails      */
name|void
name|updateProject
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|ProjectMetadata
name|project
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Update the metadata of a given artifact. If the artifact, namespace, version, project does not exist in the repository it will be created.      *      * @param session The repository session      * @param repositoryId The repository id      * @param namespace The namespace ('.' separated)      * @param projectId The project id      * @param projectVersion The project version      * @param artifactMeta Information about the artifact itself.      * @throws MetadataRepositoryException if something goes wrong during update.      */
name|void
name|updateArtifact
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
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
name|ArtifactMetadata
name|artifactMeta
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Updates the metadata for a specific version of a given project. If the namespace, project, version does not exist,      * it will be created.      *      * @param session The repository session      * @param repositoryId The repository id      * @param namespace The namespace ('.' separated)      * @param projectId The project id      * @param versionMetadata The metadata for the version      * @throws MetadataRepositoryException if something goes wrong during update      */
name|void
name|updateProjectVersion
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|ProjectVersionMetadata
name|versionMetadata
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Create the namespace in the repository, if it does not exist.      * Namespaces do not have specific metadata attached.      *      * @param session The repository session      * @param repositoryId The repository id      * @param namespace The namespace ('.' separated)      * @throws MetadataRepositoryException if something goes wrong during update      */
name|void
name|updateNamespace
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Return the facet names stored for the given facet id on the repository level.      *      * @param session The repository session      * @param repositoryId The repository id      * @param facetId The facet id      * @return The list of facet names, or an empty list, if there are no facets stored on this repository for the given facet id.      * @throws MetadataRepositoryException if something goes wrong      */
name|List
argument_list|<
name|String
argument_list|>
name|getMetadataFacets
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
parameter_list|<
name|T
extends|extends
name|MetadataFacet
parameter_list|>
name|Stream
argument_list|<
name|T
argument_list|>
name|getMetadataFacetStream
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|facetClazz
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
parameter_list|<
name|T
extends|extends
name|MetadataFacet
parameter_list|>
name|Stream
argument_list|<
name|T
argument_list|>
name|getMetadataFacetStream
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|facetClazz
parameter_list|,
name|long
name|offset
parameter_list|,
name|long
name|maxEntries
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Returns true, if there is facet data stored for the given id on the repository. The facet data itself      * may be empty. It's just checking if there is data stored for the given facet id.      *      * @param session The repository session      * @param repositoryId The repository id      * @param facetId The facet id      * @return true if there is data stored this facetId on repository level.      * @throws MetadataRepositoryException if something goes wrong      * @since 1.4-M4      */
name|boolean
name|hasMetadataFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Returns the facet data stored on the repository level. The facet instance is identified by the facet id and the      * facet name. The returned object is a instance created by using<code>{@link org.apache.archiva.metadata.model.MetadataFacetFactory}</code>.      *      * @param session The repository session      * @param repositoryId The repository id      * @param facetId The facet id      * @param name The attribute name      * @return The facet values      * @throws MetadataRepositoryException if something goes wrong.      */
name|MetadataFacet
name|getMetadataFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Returns the facet instance for the given class, which is stored on repository level for the given name.      * If the given name does not point to a instance that can be represented by this class,<code>null</code> will be returned.      * If the facet is not found the method returns<code>null</code>.      *      * @param session The repository session      * @param repositoryId The id of the repository      * @param clazz The facet object class      * @param name The name of the facet (name or path)      * @param<T> The type of the facet object      * @return The facet instance, if it exists.      * @throws MetadataRepositoryException      */
parameter_list|<
name|T
extends|extends
name|MetadataFacet
parameter_list|>
name|T
name|getMetadataFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Adss a facet to the repository level.      *      * @param session The repository session      * @param repositoryId The id of the repository      * @param metadataFacet The facet to add      * @throws MetadataRepositoryException if the facet cannot be stored.      */
name|void
name|addMetadataFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|MetadataFacet
name|metadataFacet
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Removes all facets with the given facetId from the repository level.      *      * @param session The repository session      * @param repositoryId The id of the repository      * @param facetId The facet id      * @throws MetadataRepositoryException if the removal fails      */
name|void
name|removeMetadataFacets
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Removes the given facet from the repository level, if it exists.      *      * @param session The repository session      * @param repositoryId The id of the repository      * @param facetId The facet id      * @param name The facet name or path      */
name|void
name|removeMetadataFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * if startTime or endTime are<code>null</code> they are not used for search      *      *      * @param session      * @param repositoryId      * @param startTime    can be<code>null</code>      * @param endTime      can be<code>null</code>      * @return      * @throws MetadataRepositoryException      */
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByDateRange
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|ZonedDateTime
name|startTime
parameter_list|,
name|ZonedDateTime
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Returns all the artifacts      * @param session      * @param repositoryId      * @param startTime      * @param endTime      * @return      * @throws MetadataRepositoryException      */
name|Stream
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByDateRangeStream
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|ZonedDateTime
name|startTime
parameter_list|,
name|ZonedDateTime
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|Stream
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByDateRangeStream
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|ZonedDateTime
name|startTime
parameter_list|,
name|ZonedDateTime
name|endTime
parameter_list|,
name|long
name|offset
parameter_list|,
name|long
name|maxEntries
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByChecksum
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|checksum
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Get artifacts with a project version metadata key that matches the passed value.      *        *      * @param session      * @param key      * @param value      * @param repositoryId can be null, meaning search in all repositories      * @return a list of artifacts      * @throws MetadataRepositoryException      */
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByProjectVersionMetadata
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Get artifacts with an artifact metadata key that matches the passed value.      *        *      * @param session      * @param key      * @param value      * @param repositoryId can be null, meaning search in all repositories      * @return a list of artifacts      * @throws MetadataRepositoryException      */
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByMetadata
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Get artifacts with a property key that matches the passed value.      * Possible keys are 'scm.url', 'org.name', 'url', 'mailingList.0.name', 'license.0.name',...      *        *      * @param session      * @param key      * @param value      * @param repositoryId can be null, meaning search in all repositories      * @return a list of artifacts      * @throws MetadataRepositoryException      */
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByProperty
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|void
name|removeArtifact
parameter_list|(
name|RepositorySession
name|session
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
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * used for deleting timestamped version of SNAPSHOT artifacts      *      *      * @param session      * @param artifactMetadata the artifactMetadata with the timestamped version (2.0-20120618.214135-2)      * @param baseVersion      the base version of the snapshot (2.0-SNAPSHOT)      * @throws MetadataRepositoryException      * @since 1.4-M3      */
name|void
name|removeArtifact
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|ArtifactMetadata
name|artifactMetadata
parameter_list|,
name|String
name|baseVersion
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * FIXME need a unit test!!!      * Only remove {@link MetadataFacet} for the artifact      *      *      * @param session      * @param repositoryId      * @param namespace      * @param project      * @param version      * @param metadataFacet      * @throws MetadataRepositoryException      * @since 1.4-M3      */
name|void
name|removeArtifact
parameter_list|(
name|RepositorySession
name|session
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
name|MetadataFacet
name|metadataFacet
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Delete a repository's metadata. This includes all associated metadata facets.      *      * @param session      * @param repositoryId the repository to delete      */
name|void
name|removeRepository
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      *      * @param session      * @param repositoryId      * @param namespace    (groupId for maven )      * @throws MetadataRepositoryException      * @since 1.4-M3      */
name|void
name|removeNamespace
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifacts
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * basically just checking it exists not complete data returned      *      *      * @param session      * @param repoId      * @param namespace      * @param projectId      * @return      * @throws MetadataResolutionException      */
name|ProjectMetadata
name|getProject
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
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
name|ProjectVersionMetadata
name|getProjectVersion
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
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
name|Collection
argument_list|<
name|String
argument_list|>
name|getArtifactVersions
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
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
comment|/**      * Retrieve project references from the metadata repository. Note that this is not built into the content model for      * a project version as a reference may be present (due to reverse-lookup of dependencies) before the actual      * project is, and we want to avoid adding a stub model to the content repository.      *      *      * @param session      * @param repoId         the repository ID to look within      * @param namespace      the namespace of the project to get references to      * @param projectId      the identifier of the project to get references to      * @param projectVersion the version of the project to get references to      * @return a list of project references      */
name|Collection
argument_list|<
name|ProjectVersionReference
argument_list|>
name|getProjectReferences
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
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
name|Collection
argument_list|<
name|String
argument_list|>
name|getRootNamespaces
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
comment|/**      *      * @param session      * @param repoId      * @param namespace      * @return {@link Collection} of child namespaces of the namespace argument      * @throws MetadataResolutionException      */
name|Collection
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
comment|/**      *      * @param session      * @param repoId      * @param namespace      * @return      * @throws MetadataResolutionException      */
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjects
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
comment|/**      *      * @param session      * @param repoId      * @param namespace      * @param projectId      * @return      * @throws MetadataResolutionException      */
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjectVersions
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
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
comment|/**      *      * @param session      * @param repoId      * @param namespace      * @param projectId      * @param projectVersion      * @throws MetadataRepositoryException      * @since 1.4-M4      */
name|void
name|removeProjectVersion
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
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      *      * @param session      * @param repoId      * @param namespace      * @param projectId      * @param projectVersion      * @return      * @throws MetadataResolutionException      */
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifacts
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
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
comment|/**      * remove a project      *      *      * @param session      * @param repositoryId      * @param namespace      * @param projectId      * @throws MetadataRepositoryException      * @since 1.4-M4      */
name|void
name|removeProject
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|void
name|close
parameter_list|()
throws|throws
name|MetadataRepositoryException
function_decl|;
name|boolean
name|canObtainAccess
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|)
function_decl|;
parameter_list|<
name|T
parameter_list|>
name|T
name|obtainAccess
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Full text artifacts search.      *        *      * @param session      * @param repositoryId can be null to search in all repositories      * @param text      * @param exact running an exact search, the value must exactly match the text.      * @return a list of artifacts      * @throws MetadataRepositoryException      */
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|searchArtifacts
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|text
parameter_list|,
name|boolean
name|exact
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * Full text artifacts search inside the specified key.      *        *      * @param session      * @param repositoryId can be null to search in all repositories      * @param key search only inside this key      * @param text      * @param exact running an exact search, the value must exactly match the text.      * @return a list of artifacts      * @throws MetadataRepositoryException      */
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|searchArtifacts
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|text
parameter_list|,
name|boolean
name|exact
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
block|}
end_interface

end_unit

