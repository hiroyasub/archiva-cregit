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
name|net
operator|.
name|sf
operator|.
name|beanlib
operator|.
name|provider
operator|.
name|replicator
operator|.
name|BeanReplicator
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
comment|/**  * @author Olivier Lamy  * @since 1.4  */
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
name|List
argument_list|<
name|LegacyArtifactPath
argument_list|>
name|legacyArtifactPaths
init|=
operator|new
name|ArrayList
argument_list|<
name|LegacyArtifactPath
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
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
name|legacyArtifactPath
range|:
name|archivaAdministration
operator|.
name|getLegacyArtifactPaths
argument_list|()
control|)
block|{
name|legacyArtifactPaths
operator|.
name|add
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|legacyArtifactPath
argument_list|,
name|LegacyArtifactPath
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|legacyArtifactPaths
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
argument_list|)
throw|;
block|}
block|}
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
try|try
block|{
name|archivaAdministration
operator|.
name|addLegacyArtifactPath
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|legacyArtifactPath
argument_list|,
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
operator|.
name|class
argument_list|)
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
argument_list|)
throw|;
block|}
block|}
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
argument_list|)
throw|;
block|}
block|}
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
argument_list|)
throw|;
block|}
block|}
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
argument_list|)
throw|;
block|}
block|}
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
name|fileType
init|=
name|archivaAdministration
operator|.
name|getFileType
argument_list|(
name|fileTypeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|fileType
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|fileType
argument_list|,
name|FileType
operator|.
name|class
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
argument_list|)
throw|;
block|}
block|}
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
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|fileType
argument_list|,
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
operator|.
name|class
argument_list|)
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
argument_list|)
throw|;
block|}
block|}
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
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|addKnownContentConsumer
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
argument_list|)
throw|;
block|}
block|}
specifier|public
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
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|removeKnownContentConsumer
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
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|addInvalidContentConsumer
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
argument_list|)
throw|;
block|}
block|}
specifier|public
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
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Boolean
name|removeInvalidContentConsumer
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
argument_list|)
throw|;
block|}
block|}
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
name|List
argument_list|<
name|FileType
argument_list|>
name|fileTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|FileType
argument_list|>
argument_list|(
name|modelfileTypes
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
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
name|fileType
range|:
name|modelfileTypes
control|)
block|{
name|fileTypes
operator|.
name|add
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|fileType
argument_list|,
name|FileType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|fileTypes
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
argument_list|)
throw|;
block|}
block|}
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
argument_list|)
throw|;
block|}
block|}
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
argument_list|)
throw|;
block|}
block|}
specifier|public
name|OrganisationInformation
name|getOrganisationInformation
parameter_list|()
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
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
name|organisationInformation
init|=
name|archivaAdministration
operator|.
name|getOrganisationInformation
argument_list|()
decl_stmt|;
return|return
name|organisationInformation
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|organisationInformation
argument_list|,
name|OrganisationInformation
operator|.
name|class
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
argument_list|)
throw|;
block|}
block|}
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
if|if
condition|(
name|organisationInformation
operator|==
literal|null
condition|)
block|{
name|archivaAdministration
operator|.
name|setOrganisationInformation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|archivaAdministration
operator|.
name|setOrganisationInformation
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|organisationInformation
argument_list|,
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
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

