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
name|repository
operator|.
name|RepositoryAdminException
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
name|RepositoryGroup
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
name|security
operator|.
name|common
operator|.
name|ArchivaRoleConstants
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
name|Consumes
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
name|POST
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

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/repositoryGroupService/"
argument_list|)
specifier|public
interface|interface
name|RepositoryGroupService
block|{
annotation|@
name|Path
argument_list|(
literal|"getRepositoriesGroups"
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
block|,
name|MediaType
operator|.
name|TEXT_PLAIN
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|List
argument_list|<
name|RepositoryGroup
argument_list|>
name|getRepositoriesGroups
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"getRepositoryGroup/{repositoryGroupId}"
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
block|,
name|MediaType
operator|.
name|TEXT_PLAIN
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|RepositoryGroup
name|getRepositoryGroup
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"repositoryGroupId"
argument_list|)
name|String
name|repositoryGroupId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"addRepositoryGroup"
argument_list|)
annotation|@
name|POST
annotation|@
name|Consumes
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
block|,
name|MediaType
operator|.
name|TEXT_PLAIN
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|Boolean
name|addRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"updateRepositoryGroup"
argument_list|)
annotation|@
name|POST
annotation|@
name|Consumes
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
block|,
name|MediaType
operator|.
name|TEXT_PLAIN
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|Boolean
name|updateRepositoryGroup
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"deleteRepositoryGroup/{repositoryGroupId}"
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
block|,
name|MediaType
operator|.
name|TEXT_PLAIN
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|Boolean
name|deleteRepositoryGroup
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"repositoryGroupId"
argument_list|)
name|String
name|repositoryGroupId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"addRepositoryToGroup"
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
block|,
name|MediaType
operator|.
name|TEXT_PLAIN
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|Boolean
name|addRepositoryToGroup
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryGroupId"
argument_list|)
name|String
name|repositoryGroupId
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
name|RepositoryAdminException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"addRepositoryToGroup"
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
block|,
name|MediaType
operator|.
name|TEXT_PLAIN
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|Boolean
name|deleteRepositoryFromGroup
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryGroupId"
argument_list|)
name|String
name|repositoryGroupId
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
name|RepositoryAdminException
function_decl|;
block|}
end_interface

end_unit

