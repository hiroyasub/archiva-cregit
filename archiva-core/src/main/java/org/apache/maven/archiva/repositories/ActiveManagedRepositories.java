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
name|repositories
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
name|maven
operator|.
name|archiva
operator|.
name|common
operator|.
name|artifact
operator|.
name|managed
operator|.
name|ManagedArtifact
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
name|RepositoryConfiguration
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
name|artifact
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
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|project
operator|.
name|MavenProject
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
name|project
operator|.
name|ProjectBuildingException
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
comment|/**  * ActiveManagedRepositories  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|ActiveManagedRepositories
block|{
name|String
name|ROLE
init|=
name|ActiveManagedRepositories
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Obtain the ArtifactRepository for the specified Repository ID.      *      * @param id the ID of the repository.      * @return the ArtifactRepository associated with the provided ID, or null if none found.      */
specifier|public
name|ArtifactRepository
name|getArtifactRepository
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Get the List of active managed repositories as a List of {@link ArtifactRepository} objects.      *        * @return the list of ArtifactRepository objects.      */
specifier|public
name|List
comment|/*<ArtifactRepository>*/
name|getAllArtifactRepositories
parameter_list|()
function_decl|;
name|RepositoryConfiguration
name|getRepositoryConfiguration
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Providing only a groupId, artifactId, and version, return the MavenProject that      * is found, in any managed repository.      *       * @param groupId the groupId to search for      * @param artifactId the artifactId to search for      * @param version the version to search for      * @return the MavenProject from the provided parameters.      * @throws ProjectBuildingException if there was a problem building the maven project object.      */
name|MavenProject
name|findProject
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
throws|throws
name|ProjectBuildingException
function_decl|;
name|ManagedArtifact
name|findArtifact
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
throws|throws
name|ProjectBuildingException
function_decl|;
name|ManagedArtifact
name|findArtifact
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
parameter_list|)
function_decl|;
name|ManagedArtifact
name|findArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
function_decl|;
comment|/**      * Obtain the last data refresh timestamp for all Managed Repositories.      *       * @return the last data refresh timestamp.      */
name|long
name|getLastDataRefreshTime
parameter_list|()
function_decl|;
comment|/**      * Tests to see if there needs to be a data refresh performed.      *       * The only valid scenario is if 1 or more repositories have not had their data refreshed ever.       *       * @return true if there needs to be a data refresh.      */
name|boolean
name|needsDataRefresh
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

