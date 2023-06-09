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
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|tags
operator|.
name|Tag
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
name|maven
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
name|redback
operator|.
name|authorization
operator|.
name|RedbackAuthorization
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
comment|/**  * provide REST services on the top of stage merge repository plugin  *  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/mergeRepositoriesService/"
argument_list|)
annotation|@
name|Tag
argument_list|(
name|name
operator|=
literal|"RepositoryMerge"
argument_list|,
name|description
operator|=
literal|"Merging repositories"
argument_list|)
specifier|public
interface|interface
name|MergeRepositoriesService
block|{
comment|/**      *<b>permissions are checked in impl</b>      * @since 1.4-M3      */
annotation|@
name|Path
argument_list|(
literal|"mergeConflictedArtifacts/{sourceRepositoryId}/{targetRepositoryId}"
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
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MERGE_REPOSITORY
argument_list|,
name|resource
operator|=
literal|"{sourceRepositoryId}"
argument_list|)
name|List
argument_list|<
name|Artifact
argument_list|>
name|getMergeConflictedArtifacts
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"sourceRepositoryId"
argument_list|)
name|String
name|sourceRepositoryId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"targetRepositoryId"
argument_list|)
name|String
name|targetRepositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
comment|/**      *<b>permissions are checked in impl</b>      * @since 1.4-M3      */
annotation|@
name|Path
argument_list|(
literal|"mergeRepositories/{sourceRepositoryId}/{targetRepositoryId}/{skipConflicts}"
argument_list|)
annotation|@
name|GET
annotation|@
name|RedbackAuthorization
argument_list|(
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MERGE_REPOSITORY
argument_list|,
name|resource
operator|=
literal|"{sourceRepositoryId}"
argument_list|)
name|void
name|mergeRepositories
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"sourceRepositoryId"
argument_list|)
name|String
name|sourceRepositoryId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"targetRepositoryId"
argument_list|)
name|String
name|targetRepositoryId
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"skipConflicts"
argument_list|)
name|boolean
name|skipConflicts
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
block|}
end_interface

end_unit

