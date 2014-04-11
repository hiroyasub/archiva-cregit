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
name|admin
operator|.
name|model
operator|.
name|admin
operator|.
name|ArchivaAdministration
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
name|admin
operator|.
name|model
operator|.
name|beans
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
name|admin
operator|.
name|model
operator|.
name|beans
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|NetworkConfiguration
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
name|admin
operator|.
name|model
operator|.
name|beans
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|UiConfiguration
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
name|repository
operator|.
name|ManagedRepositoryContent
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
name|scanner
operator|.
name|RepositoryContentConsumers
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
name|AdminRepositoryConsumer
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
name|services
operator|.
name|ArchivaAdministrationService
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
name|services
operator|.
name|ArchivaRestServiceException
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
name|services
operator|.
name|utils
operator|.
name|AddAdminRepoConsumerClosure
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
name|services
operator|.
name|utils
operator|.
name|AdminRepositoryConsumerComparator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|CollectionUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|Response
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaAdministrationService#default"
argument_list|)
specifier|public
class|class
name|DefaultArchivaAdministrationService
extends|extends
name|AbstractRestService
implements|implements
name|ArchivaAdministrationService
block|{
annotation|@
name|Inject
specifier|private
name|ArchivaAdministration
name|archivaAdministration
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"managedRepositoryContent#legacy"
argument_list|)
specifier|private
name|ManagedRepositoryContent
name|repositoryContent
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoryContentConsumers
name|repoConsumerUtil
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|LegacyArtifactPath
argument_list|>
name|getLegacyArtifactPaths
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|archivaAdministration
operator|.
name|getLegacyArtifactPaths
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addLegacyArtifactPath
parameter_list|(
name|LegacyArtifactPath
name|legacyArtifactPath
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
comment|// Check the proposed Artifact matches the path
name|ArtifactReference
name|artifact
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setGroupId
argument_list|(
name|legacyArtifactPath
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|legacyArtifactPath
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setClassifier
argument_list|(
name|legacyArtifactPath
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
name|legacyArtifactPath
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setType
argument_list|(
name|legacyArtifactPath
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|repositoryContent
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|path
argument_list|,
name|legacyArtifactPath
operator|.
name|getPath
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
literal|"artifact path reference '"
operator|+
name|legacyArtifactPath
operator|.
name|getPath
argument_list|()
operator|+
literal|"' does not match the initial path: '"
operator|+
name|path
operator|+
literal|"'"
argument_list|,
name|Response
operator|.
name|Status
operator|.
name|BAD_REQUEST
operator|.
name|getStatusCode
argument_list|()
argument_list|,
literal|null
argument_list|)
throw|;
block|}
try|try
block|{
name|archivaAdministration
operator|.
name|addLegacyArtifactPath
argument_list|(
name|legacyArtifactPath
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|deleteLegacyArtifactPath
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|deleteLegacyArtifactPath
argument_list|(
name|path
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|addFileTypePattern
parameter_list|(
name|String
name|fileTypeId
parameter_list|,
name|String
name|pattern
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|addFileTypePattern
argument_list|(
name|fileTypeId
argument_list|,
name|pattern
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|removeFileTypePattern
parameter_list|(
name|String
name|fileTypeId
parameter_list|,
name|String
name|pattern
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|removeFileTypePattern
argument_list|(
name|fileTypeId
argument_list|,
name|pattern
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|FileType
name|getFileType
parameter_list|(
name|String
name|fileTypeId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|archivaAdministration
operator|.
name|getFileType
argument_list|(
name|fileTypeId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|addFileType
parameter_list|(
name|FileType
name|fileType
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|addFileType
argument_list|(
name|fileType
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|removeFileType
parameter_list|(
name|String
name|fileTypeId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|removeFileType
argument_list|(
name|fileTypeId
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|enabledKnownContentConsumer
parameter_list|(
name|String
name|knownContentConsumer
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|addKnownContentConsumer
argument_list|(
name|knownContentConsumer
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|enabledKnownContentConsumers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|knownContentConsumers
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|setKnownContentConsumers
argument_list|(
name|knownContentConsumers
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|disabledKnownContentConsumer
parameter_list|(
name|String
name|knownContentConsumer
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|removeKnownContentConsumer
argument_list|(
name|knownContentConsumer
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|enabledInvalidContentConsumer
parameter_list|(
name|String
name|invalidContentConsumer
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|addInvalidContentConsumer
argument_list|(
name|invalidContentConsumer
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|enabledInvalidContentConsumers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|invalidContentConsumers
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|setInvalidContentConsumers
argument_list|(
name|invalidContentConsumers
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|disabledInvalidContentConsumer
parameter_list|(
name|String
name|invalidContentConsumer
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|removeInvalidContentConsumer
argument_list|(
name|invalidContentConsumer
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|FileType
argument_list|>
name|getFileTypes
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|List
argument_list|<
name|FileType
argument_list|>
name|modelfileTypes
init|=
name|archivaAdministration
operator|.
name|getFileTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|modelfileTypes
operator|==
literal|null
operator|||
name|modelfileTypes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
name|modelfileTypes
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getKnownContentConsumers
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|archivaAdministration
operator|.
name|getKnownContentConsumers
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getInvalidContentConsumers
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|archivaAdministration
operator|.
name|getInvalidContentConsumers
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|OrganisationInformation
name|getOrganisationInformation
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|archivaAdministration
operator|.
name|getOrganisationInformation
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setOrganisationInformation
parameter_list|(
name|OrganisationInformation
name|organisationInformation
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|setOrganisationInformation
argument_list|(
name|organisationInformation
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|registrationDisabled
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
return|return
name|getUiConfiguration
argument_list|()
operator|.
name|isDisableRegistration
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|UiConfiguration
name|getUiConfiguration
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|archivaAdministration
operator|.
name|getUiConfiguration
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setUiConfiguration
parameter_list|(
name|UiConfiguration
name|uiConfiguration
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
comment|// fix for MRM-1757
comment|// strip any trailing '/' at the end of the url so it won't affect url/link calculations in UI
name|uiConfiguration
operator|.
name|setApplicationUrl
argument_list|(
name|StringUtils
operator|.
name|stripEnd
argument_list|(
name|uiConfiguration
operator|.
name|getApplicationUrl
argument_list|()
argument_list|,
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
name|archivaAdministration
operator|.
name|updateUiConfiguration
argument_list|(
name|uiConfiguration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getApplicationUrl
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|archivaAdministration
operator|.
name|getUiConfiguration
argument_list|()
operator|.
name|getApplicationUrl
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|NetworkConfiguration
name|getNetworkConfiguration
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
return|return
name|archivaAdministration
operator|.
name|getNetworkConfiguration
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setNetworkConfiguration
parameter_list|(
name|NetworkConfiguration
name|networkConfiguration
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|archivaAdministration
operator|.
name|setNetworkConfiguration
argument_list|(
name|networkConfiguration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|AdminRepositoryConsumer
argument_list|>
name|getKnownContentAdminRepositoryConsumers
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|AddAdminRepoConsumerClosure
name|addAdminRepoConsumer
init|=
operator|new
name|AddAdminRepoConsumerClosure
argument_list|(
name|archivaAdministration
operator|.
name|getKnownContentConsumers
argument_list|()
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|repoConsumerUtil
operator|.
name|getAvailableKnownConsumers
argument_list|()
argument_list|,
name|addAdminRepoConsumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AdminRepositoryConsumer
argument_list|>
name|knownContentConsumers
init|=
name|addAdminRepoConsumer
operator|.
name|getList
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|knownContentConsumers
argument_list|,
name|AdminRepositoryConsumerComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|knownContentConsumers
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|AdminRepositoryConsumer
argument_list|>
name|getInvalidContentAdminRepositoryConsumers
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|AddAdminRepoConsumerClosure
name|addAdminRepoConsumer
init|=
operator|new
name|AddAdminRepoConsumerClosure
argument_list|(
name|archivaAdministration
operator|.
name|getInvalidContentConsumers
argument_list|()
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|repoConsumerUtil
operator|.
name|getAvailableInvalidConsumers
argument_list|()
argument_list|,
name|addAdminRepoConsumer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AdminRepositoryConsumer
argument_list|>
name|invalidContentConsumers
init|=
name|addAdminRepoConsumer
operator|.
name|getList
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|invalidContentConsumers
argument_list|,
name|AdminRepositoryConsumerComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|invalidContentConsumers
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

