begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
name|admin
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
name|AuditInformation
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
name|admin
operator|.
name|repository
operator|.
name|AbstractRepositoryAdmin
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
name|configuration
operator|.
name|Configuration
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
name|configuration
operator|.
name|UserInterfaceOptions
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
name|configuration
operator|.
name|WebappConfiguration
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
name|facets
operator|.
name|AuditEvent
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
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|conn
operator|.
name|PoolingClientConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|conn
operator|.
name|PoolingHttpClientConnectionManager
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
name|wagon
operator|.
name|providers
operator|.
name|http
operator|.
name|HttpWagon
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
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
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
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaAdministration#default"
argument_list|)
specifier|public
class|class
name|DefaultArchivaAdministration
extends|extends
name|AbstractRepositoryAdmin
implements|implements
name|ArchivaAdministration
block|{
specifier|private
name|PoolingHttpClientConnectionManager
name|poolingClientConnectionManager
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
comment|// setup wagon on start with initial values
name|NetworkConfiguration
name|networkConfiguration
init|=
name|getNetworkConfiguration
argument_list|()
decl_stmt|;
name|setupWagon
argument_list|(
name|networkConfiguration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|poolingClientConnectionManager
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|poolingClientConnectionManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
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
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|LegacyArtifactPath
argument_list|>
name|legacyArtifactPaths
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getLegacyArtifactPaths
argument_list|()
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
name|configuration
operator|.
name|LegacyArtifactPath
name|legacyArtifactPath
range|:
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getLegacyArtifactPaths
argument_list|()
control|)
block|{
name|legacyArtifactPaths
operator|.
name|add
argument_list|(
name|getModelMapper
argument_list|()
operator|.
name|map
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
annotation|@
name|Override
specifier|public
name|void
name|addLegacyArtifactPath
parameter_list|(
name|LegacyArtifactPath
name|legacyArtifactPath
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|addLegacyArtifactPath
argument_list|(
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|legacyArtifactPath
argument_list|,
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|LegacyArtifactPath
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|AuditEvent
operator|.
name|ADD_LEGACY_PATH
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|deleteLegacyArtifactPath
parameter_list|(
name|String
name|path
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|LegacyArtifactPath
name|legacyArtifactPath
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|LegacyArtifactPath
argument_list|()
decl_stmt|;
name|legacyArtifactPath
operator|.
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|removeLegacyArtifactPath
argument_list|(
name|legacyArtifactPath
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|AuditEvent
operator|.
name|REMOVE_LEGACY_PATH
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addFileTypePattern
parameter_list|(
name|String
name|fileTypeId
parameter_list|,
name|String
name|pattern
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|FileType
name|fileType
init|=
name|getFileTypeById
argument_list|(
name|fileTypeId
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|fileType
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|fileType
operator|.
name|getPatterns
argument_list|()
operator|.
name|contains
argument_list|(
name|pattern
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"File type ["
operator|+
name|fileTypeId
operator|+
literal|"] already contains pattern ["
operator|+
name|pattern
operator|+
literal|"]"
argument_list|)
throw|;
block|}
name|fileType
operator|.
name|addPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|AuditEvent
operator|.
name|ADD_PATTERN
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeFileTypePattern
parameter_list|(
name|String
name|fileTypeId
parameter_list|,
name|String
name|pattern
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|FileType
name|fileType
init|=
name|getFileTypeById
argument_list|(
name|fileTypeId
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|fileType
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|fileType
operator|.
name|removePattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|AuditEvent
operator|.
name|REMOVE_PATTERN
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
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
name|RepositoryAdminException
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|FileType
name|fileType
init|=
name|getFileTypeById
argument_list|(
name|fileTypeId
argument_list|,
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
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
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|fileType
argument_list|,
name|FileType
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addFileType
parameter_list|(
name|FileType
name|fileType
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|getFileTypeById
argument_list|(
name|fileType
operator|.
name|getId
argument_list|()
argument_list|,
name|configuration
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"impossible to FileType with id "
operator|+
name|fileType
operator|.
name|getId
argument_list|()
operator|+
literal|" already exists"
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|addFileType
argument_list|(
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|fileType
argument_list|,
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|FileType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeFileType
parameter_list|(
name|String
name|fileTypeId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|FileType
name|fileType
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|FileType
argument_list|()
decl_stmt|;
name|fileType
operator|.
name|setId
argument_list|(
name|fileTypeId
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|removeFileType
argument_list|(
name|fileType
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addKnownContentConsumer
parameter_list|(
name|String
name|knownContentConsumer
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getKnownContentConsumers
argument_list|()
operator|.
name|contains
argument_list|(
name|knownContentConsumer
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"skip adding knownContentConsumer {} as already here"
argument_list|,
name|knownContentConsumer
argument_list|)
expr_stmt|;
return|return;
block|}
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|addKnownContentConsumer
argument_list|(
name|knownContentConsumer
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|AuditEvent
operator|.
name|ENABLE_REPO_CONSUMER
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeKnownContentConsumer
parameter_list|(
name|String
name|knownContentConsumer
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|removeKnownContentConsumer
argument_list|(
name|knownContentConsumer
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|AuditEvent
operator|.
name|DISABLE_REPO_CONSUMER
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addInvalidContentConsumer
parameter_list|(
name|String
name|invalidContentConsumer
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getInvalidContentConsumers
argument_list|()
operator|.
name|contains
argument_list|(
name|invalidContentConsumer
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"skip adding invalidContentConsumer {} as already here"
argument_list|,
name|invalidContentConsumer
argument_list|)
expr_stmt|;
return|return;
block|}
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|addInvalidContentConsumer
argument_list|(
name|invalidContentConsumer
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|AuditEvent
operator|.
name|ENABLE_REPO_CONSUMER
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeInvalidContentConsumer
parameter_list|(
name|String
name|invalidContentConsumer
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|removeInvalidContentConsumer
argument_list|(
name|invalidContentConsumer
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|AuditEvent
operator|.
name|DISABLE_REPO_CONSUMER
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setKnownContentConsumers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|knownContentConsumers
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
if|if
condition|(
name|knownContentConsumers
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|String
name|knowContentConsumer
range|:
name|knownContentConsumers
control|)
block|{
name|addKnownContentConsumer
argument_list|(
name|knowContentConsumer
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setInvalidContentConsumers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|invalidContentConsumers
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
if|if
condition|(
name|invalidContentConsumers
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|String
name|invalidContentConsumer
range|:
name|invalidContentConsumers
control|)
block|{
name|addKnownContentConsumer
argument_list|(
name|invalidContentConsumer
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
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
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|FileType
argument_list|>
name|configFileTypes
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getFileTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|configFileTypes
operator|==
literal|null
operator|||
name|configFileTypes
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
argument_list|<>
argument_list|(
name|configFileTypes
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
name|configuration
operator|.
name|FileType
name|fileType
range|:
name|configFileTypes
control|)
block|{
name|fileTypes
operator|.
name|add
argument_list|(
name|getModelMapper
argument_list|()
operator|.
name|map
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
name|RepositoryAdminException
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getKnownContentConsumers
argument_list|()
argument_list|)
return|;
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
name|RepositoryAdminException
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getInvalidContentConsumers
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|OrganisationInformation
name|getOrganisationInformation
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|OrganisationInformation
name|organisationInformation
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOrganisationInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|organisationInformation
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|organisationInformation
argument_list|,
name|OrganisationInformation
operator|.
name|class
argument_list|)
return|;
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
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|organisationInformation
operator|!=
literal|null
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|OrganisationInformation
name|organisationInformationModel
init|=
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|organisationInformation
argument_list|,
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|OrganisationInformation
operator|.
name|class
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setOrganisationInfo
argument_list|(
name|organisationInformationModel
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setOrganisationInfo
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|UiConfiguration
name|getUiConfiguration
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|WebappConfiguration
name|webappConfiguration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getWebapp
argument_list|()
decl_stmt|;
if|if
condition|(
name|webappConfiguration
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|UserInterfaceOptions
name|userInterfaceOptions
init|=
name|webappConfiguration
operator|.
name|getUi
argument_list|()
decl_stmt|;
if|if
condition|(
name|userInterfaceOptions
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|userInterfaceOptions
argument_list|,
name|UiConfiguration
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateUiConfiguration
parameter_list|(
name|UiConfiguration
name|uiConfiguration
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|uiConfiguration
operator|!=
literal|null
condition|)
block|{
name|UserInterfaceOptions
name|userInterfaceOptions
init|=
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|uiConfiguration
argument_list|,
name|UserInterfaceOptions
operator|.
name|class
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|()
operator|.
name|setUi
argument_list|(
name|userInterfaceOptions
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|getWebapp
argument_list|()
operator|.
name|setUi
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|NetworkConfiguration
name|getNetworkConfiguration
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|NetworkConfiguration
name|networkConfiguration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getNetworkConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|networkConfiguration
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|networkConfiguration
argument_list|,
name|NetworkConfiguration
operator|.
name|class
argument_list|)
return|;
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
name|RepositoryAdminException
block|{
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|networkConfiguration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|.
name|setNetworkConfiguration
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setNetworkConfiguration
argument_list|(
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|networkConfiguration
argument_list|,
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|NetworkConfiguration
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setupWagon
argument_list|(
name|networkConfiguration
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupWagon
parameter_list|(
name|NetworkConfiguration
name|networkConfiguration
parameter_list|)
block|{
if|if
condition|(
name|networkConfiguration
operator|==
literal|null
condition|)
block|{
comment|// back to default values
name|HttpWagon
operator|.
name|setPersistentPool
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|poolingClientConnectionManager
operator|=
operator|new
name|PoolingHttpClientConnectionManager
argument_list|()
expr_stmt|;
name|poolingClientConnectionManager
operator|.
name|setDefaultMaxPerRoute
argument_list|(
literal|30
argument_list|)
expr_stmt|;
name|poolingClientConnectionManager
operator|.
name|setMaxTotal
argument_list|(
literal|30
argument_list|)
expr_stmt|;
name|HttpWagon
operator|.
name|setPoolingHttpClientConnectionManager
argument_list|(
name|poolingClientConnectionManager
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|HttpWagon
operator|.
name|setPersistentPool
argument_list|(
name|networkConfiguration
operator|.
name|isUsePooling
argument_list|()
argument_list|)
expr_stmt|;
name|poolingClientConnectionManager
operator|=
operator|new
name|PoolingHttpClientConnectionManager
argument_list|()
expr_stmt|;
name|poolingClientConnectionManager
operator|.
name|setDefaultMaxPerRoute
argument_list|(
name|networkConfiguration
operator|.
name|getMaxTotalPerHost
argument_list|()
argument_list|)
expr_stmt|;
name|poolingClientConnectionManager
operator|.
name|setMaxTotal
argument_list|(
name|networkConfiguration
operator|.
name|getMaxTotal
argument_list|()
argument_list|)
expr_stmt|;
name|HttpWagon
operator|.
name|setPoolingHttpClientConnectionManager
argument_list|(
name|poolingClientConnectionManager
argument_list|)
expr_stmt|;
block|}
block|}
comment|//-------------------------
comment|//
comment|//-------------------------
specifier|private
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|FileType
name|getFileTypeById
parameter_list|(
name|String
name|id
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
block|{
for|for
control|(
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|FileType
name|fileType
range|:
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getFileTypes
argument_list|()
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|id
argument_list|,
name|fileType
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|fileType
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

