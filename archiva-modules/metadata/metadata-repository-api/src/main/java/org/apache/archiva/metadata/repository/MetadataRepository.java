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
name|Date
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

begin_interface
specifier|public
interface|interface
name|MetadataRepository
block|{
comment|/**      * Update metadata for a particular project in the metadata repository, or create it if it does not already exist.      *      * @param repositoryId the repository the project is in      * @param project      the project metadata to create or update      */
name|void
name|updateProject
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|ProjectMetadata
name|project
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|void
name|updateArtifact
parameter_list|(
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
name|void
name|updateProjectVersion
parameter_list|(
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
comment|/**      * create the namespace in the repository. (if not exist)      * @param repositoryId      * @param namespace      * @throws MetadataRepositoryException      */
name|void
name|updateNamespace
parameter_list|(
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
name|String
argument_list|>
name|getMetadataFacets
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      *      * @param repositoryId      * @param facetId      * @return true if the repository datas for this facetId      * @since 1.4-M4      * @throws MetadataRepositoryException      */
name|boolean
name|hasMetadataFacet
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|MetadataFacet
name|getMetadataFacet
parameter_list|(
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
name|void
name|addMetadataFacet
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|MetadataFacet
name|metadataFacet
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|void
name|removeMetadataFacets
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|void
name|removeMetadataFacet
parameter_list|(
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
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByDateRange
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|Date
name|startTime
parameter_list|,
name|Date
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|// TODO: remove from API, just use configuration
name|Collection
argument_list|<
name|String
argument_list|>
name|getRepositories
parameter_list|()
throws|throws
name|MetadataRepositoryException
function_decl|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByChecksum
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|checksum
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|void
name|removeArtifact
parameter_list|(
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
comment|/**      * used for deleting timestamped version of SNAPSHOT artifacts      *      * @param artifactMetadata the artifactMetadata with the timestamped version (2.0-20120618.214135-2)      * @param baseVersion      the base version of the snapshot (2.0-SNAPSHOT)      * @throws MetadataRepositoryException      * @since 1.4-M3      */
name|void
name|removeArtifact
parameter_list|(
name|ArtifactMetadata
name|artifactMetadata
parameter_list|,
name|String
name|baseVersion
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * @param repositoryId      * @param namespace      * @param project      * @param version      * @param metadataFacet will remove artifacts which have this {@link MetadataFacet} using equals      * @throws MetadataRepositoryException      * @since 1.4-M3      */
name|void
name|removeArtifact
parameter_list|(
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
comment|/**      * Delete a repository's metadata. This includes all associated metadata facets.      *      * @param repositoryId the repository to delete      */
name|void
name|removeRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      * @param repositoryId      * @param namespace    (groupId for maven )      * @throws MetadataRepositoryException      * @since 1.4-M3      */
name|void
name|removeNamespace
parameter_list|(
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
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|ProjectMetadata
name|getProject
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
throws|throws
name|MetadataResolutionException
function_decl|;
name|ProjectVersionMetadata
name|getProjectVersion
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
comment|/**      * Retrieve project references from the metadata repository. Note that this is not built into the content model for      * a project version as a reference may be present (due to reverse-lookup of dependencies) before the actual      * project is, and we want to avoid adding a stub model to the content repository.      *      * @param repoId         the repository ID to look within      * @param namespace      the namespace of the project to get references to      * @param projectId      the identifier of the project to get references to      * @param projectVersion the version of the project to get references to      * @return a list of project references      */
name|Collection
argument_list|<
name|ProjectVersionReference
argument_list|>
name|getProjectReferences
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
name|String
name|repoId
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
name|Collection
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjects
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjectVersions
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
throws|throws
name|MetadataResolutionException
function_decl|;
comment|/**      * @param repoId      * @param namespace      * @param projectId      * @param projectVersion      * @throws MetadataResolutionException      * @since 1.4-M4      */
name|void
name|removeProjectVersion
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
name|String
name|projectVersion
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifacts
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
name|String
name|projectVersion
parameter_list|)
throws|throws
name|MetadataResolutionException
function_decl|;
comment|/**      * remove a project      * @param repositoryId      * @param namespace      * @param projectId      * @throws MetadataRepositoryException      * @since 1.4-M4      */
name|void
name|removeProject
parameter_list|(
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
comment|/**      *<b>implementations can throw RuntimeException</b>      */
name|void
name|save
parameter_list|()
function_decl|;
name|void
name|close
parameter_list|()
throws|throws
name|MetadataRepositoryException
function_decl|;
comment|/**      *<b>implementations can throw RuntimeException</b>      */
name|void
name|revert
parameter_list|()
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
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
throws|throws
name|MetadataRepositoryException
function_decl|;
block|}
end_interface

end_unit

