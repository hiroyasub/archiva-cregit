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
name|repository
operator|.
name|scanner
operator|.
name|RepositoryScanStatistics
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
name|ArtifactTransferRequest
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

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/repositoriesService/"
argument_list|)
specifier|public
interface|interface
name|RepositoriesService
block|{
annotation|@
name|Path
argument_list|(
literal|"scanRepository"
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
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_RUN_INDEXER
argument_list|)
comment|/**      * index repository      */
name|Boolean
name|scanRepository
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"fullScan"
argument_list|)
name|boolean
name|fullScan
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"scanRepositoryDirectories/{repositoryId}"
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
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_RUN_INDEXER
argument_list|)
comment|/**      * scan directories      * @since 1.4-M3      */
name|RepositoryScanStatistics
name|scanRepositoryDirectories
parameter_list|(
annotation|@
name|PathParam
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
literal|"alreadyScanning/{repositoryId}"
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
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_RUN_INDEXER
argument_list|)
name|Boolean
name|alreadyScanning
parameter_list|(
annotation|@
name|PathParam
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
literal|"removeScanningTaskFromQueue/{repositoryId}"
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
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_RUN_INDEXER
argument_list|)
name|Boolean
name|removeScanningTaskFromQueue
parameter_list|(
annotation|@
name|PathParam
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
literal|"scanRepositoryNow"
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
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_RUN_INDEXER
argument_list|)
name|Boolean
name|scanRepositoryNow
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"fullScan"
argument_list|)
name|boolean
name|fullScan
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"copyArtifact"
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
name|noPermission
operator|=
literal|true
argument_list|)
comment|/**      * permissions are checked in impl      * will copy an artifact from the source repository to the target repository      */
name|Boolean
name|copyArtifact
parameter_list|(
name|ArtifactTransferRequest
name|artifactTransferRequest
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"scheduleDownloadRemoteIndex"
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
name|permissions
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_RUN_INDEXER
argument_list|)
name|Boolean
name|scheduleDownloadRemoteIndex
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"now"
argument_list|)
name|boolean
name|now
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"fullDownload"
argument_list|)
name|boolean
name|fullDownload
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"deleteArtifact"
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
name|noPermission
operator|=
literal|true
argument_list|)
comment|/**      * permissions are checked in impl      * @since 1.4-M2      */
name|Boolean
name|deleteArtifact
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|""
argument_list|)
name|Artifact
name|artifact
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

