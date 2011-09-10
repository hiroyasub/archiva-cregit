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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|FileType
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
name|LegacyArtifactPath
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
name|OrganisationInformation
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
literal|"/archivaAdministrationService/"
argument_list|)
specifier|public
interface|interface
name|ArchivaAdministrationService
block|{
annotation|@
name|Path
argument_list|(
literal|"getLegacyArtifactPaths"
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
name|LegacyArtifactPath
argument_list|>
name|getLegacyArtifactPaths
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"addLegacyArtifactPath"
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
name|void
name|addLegacyArtifactPath
parameter_list|(
name|LegacyArtifactPath
name|legacyArtifactPath
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"deleteLegacyArtifactPath"
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
name|deleteLegacyArtifactPath
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"path"
argument_list|)
name|String
name|path
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"addFileTypePattern"
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
name|addFileTypePattern
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"fileTypeId"
argument_list|)
name|String
name|fileTypeId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"pattern"
argument_list|)
name|String
name|pattern
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"removeFileTypePattern"
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
name|removeFileTypePattern
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"fileTypeId"
argument_list|)
name|String
name|fileTypeId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"pattern"
argument_list|)
name|String
name|pattern
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"getFileType"
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
name|FileType
name|getFileType
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"fileTypeId"
argument_list|)
name|String
name|fileTypeId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"addFileType"
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
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|void
name|addFileType
parameter_list|(
name|FileType
name|fileType
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"removeFileType"
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
name|removeFileType
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"fileTypeId"
argument_list|)
name|String
name|fileTypeId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"addKnownContentConsumer"
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
name|addKnownContentConsumer
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"knownContentConsumer"
argument_list|)
name|String
name|knownContentConsumer
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"setKnownContentConsumers"
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
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|void
name|setKnownContentConsumers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|knownContentConsumers
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"removeKnownContentConsumer"
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
name|removeKnownContentConsumer
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"knownContentConsumer"
argument_list|)
name|String
name|knownContentConsumer
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"addInvalidContentConsumer"
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
name|addInvalidContentConsumer
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"invalidContentConsumer"
argument_list|)
name|String
name|invalidContentConsumer
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"setInvalidContentConsumers"
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
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|void
name|setInvalidContentConsumers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|invalidContentConsumers
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"removeInvalidContentConsumer"
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
name|removeInvalidContentConsumer
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"invalidContentConsumer"
argument_list|)
name|String
name|invalidContentConsumer
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"getFileTypes"
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
name|FileType
argument_list|>
name|getFileTypes
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"getKnownContentConsumers"
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
name|String
argument_list|>
name|getKnownContentConsumers
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"getInvalidContentConsumers"
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
name|String
argument_list|>
name|getInvalidContentConsumers
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"getOrganisationInformation"
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
name|OrganisationInformation
name|getOrganisationInformation
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"setOrganisationInformation"
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
name|RedbackAuthorization
argument_list|(
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|)
name|void
name|setOrganisationInformation
parameter_list|(
name|OrganisationInformation
name|organisationInformation
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
block|}
end_interface

end_unit

