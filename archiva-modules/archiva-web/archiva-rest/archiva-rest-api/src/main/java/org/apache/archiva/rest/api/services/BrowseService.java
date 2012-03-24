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
name|api
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
name|admin
operator|.
name|model
operator|.
name|beans
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
name|rest
operator|.
name|api
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|BrowseResult
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
name|model
operator|.
name|TreeEntry
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
name|model
operator|.
name|VersionsList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|authorization
operator|.
name|RedbackAuthorization
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|DELETE
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PUT
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|QueryParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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
name|Map
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/browseService/"
argument_list|)
specifier|public
interface|interface
name|BrowseService
block|{
annotation|@
name|Path
argument_list|(
literal|"rootGroups"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
name|BrowseResult
name|getRootGroups
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"browseGroupId/{groupId}"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
name|BrowseResult
name|browseGroupId
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"groupId"
argument_list|)
name|String
name|groupId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"versionsList/{g}/{a}"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
name|VersionsList
name|getVersionsList
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"g"
argument_list|)
name|String
name|groupId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"a"
argument_list|)
name|String
name|artifactId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"projectVersionMetadata/{g}/{a}"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
name|ProjectVersionMetadata
name|getProjectVersionMetadata
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"g"
argument_list|)
name|String
name|groupId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"a"
argument_list|)
name|String
name|artifactId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"projectVersionMetadata/{g}/{a}/{v}"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
name|ProjectVersionMetadata
name|getProjectMetadata
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"g"
argument_list|)
name|String
name|groupId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"a"
argument_list|)
name|String
name|artifactId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"v"
argument_list|)
name|String
name|version
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"userRepositories"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|getUserRepositories
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"treeEntries/{g}/{a}/{v}"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|TreeEntry
argument_list|>
name|getTreeEntries
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"g"
argument_list|)
name|String
name|groupId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"a"
argument_list|)
name|String
name|artifactId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"v"
argument_list|)
name|String
name|version
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"dependees/{g}/{a}/{v}"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|Artifact
argument_list|>
name|getDependees
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"g"
argument_list|)
name|String
name|groupId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"a"
argument_list|)
name|String
name|artifactId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"v"
argument_list|)
name|String
name|version
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"metadatas/{g}/{a}/{v}"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMetadatas
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"g"
argument_list|)
name|String
name|groupId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"a"
argument_list|)
name|String
name|artifactId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"v"
argument_list|)
name|String
name|version
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"metadata/{g}/{a}/{v}/{key}/{value}"
argument_list|)
annotation|@
name|PUT
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|false
argument_list|,
name|noRestriction
operator|=
literal|false
argument_list|,
name|permissions
operator|=
literal|"archiva-add-metadata"
argument_list|)
name|Boolean
name|addMetadata
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"g"
argument_list|)
name|String
name|groupId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"a"
argument_list|)
name|String
name|artifactId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"v"
argument_list|)
name|String
name|version
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"key"
argument_list|)
name|String
name|key
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"value"
argument_list|)
name|String
name|value
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"metadata/{g}/{a}/{v}/{key}"
argument_list|)
annotation|@
name|DELETE
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|false
argument_list|,
name|noRestriction
operator|=
literal|false
argument_list|,
name|permissions
operator|=
literal|"archiva-add-metadata"
argument_list|)
name|Boolean
name|deleteMetadata
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"g"
argument_list|)
name|String
name|groupId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"a"
argument_list|)
name|String
name|artifactId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"v"
argument_list|)
name|String
name|version
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"key"
argument_list|)
name|String
name|key
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
block|}
end_interface

end_unit

