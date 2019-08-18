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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|Repository
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
name|RepositoryType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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

begin_interface
specifier|public
interface|interface
name|MetadataResolver
block|{
specifier|default
name|List
argument_list|<
name|RepositoryType
argument_list|>
name|supportsRepositoryTypes
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|RepositoryType
operator|.
name|MAVEN
argument_list|)
return|;
block|}
name|ProjectVersionMetadata
name|resolveProjectVersion
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
comment|/**      * Retrieve project references from the metadata repository. Note that this is not built into the content model for      * a project version as a reference may be present (due to reverse-lookup of dependencies) before the actual      * project is, and we want to avoid adding a stub model to the content repository.      *      * @param repoId         the repository ID to look within      * @param namespace      the namespace of the project to get references to      * @param projectId      the identifier of the project to get references to      * @param projectVersion the version of the project to get references to      * @return a list of project references      */
name|Collection
argument_list|<
name|ProjectVersionReference
argument_list|>
name|resolveProjectReferences
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
name|resolveRootNamespaces
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
name|Collection
argument_list|<
name|String
argument_list|>
name|resolveNamespaces
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
name|Collection
argument_list|<
name|String
argument_list|>
name|resolveProjects
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
name|Collection
argument_list|<
name|String
argument_list|>
name|resolveProjectVersions
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
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|resolveArtifacts
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
block|}
end_interface

end_unit

