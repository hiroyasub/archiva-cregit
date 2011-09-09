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
name|repository
operator|.
name|RepositoryAdminException
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
specifier|public
interface|interface
name|ArchivaAdministration
block|{
name|List
argument_list|<
name|LegacyArtifactPath
argument_list|>
name|getLegacyArtifactPaths
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
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
function_decl|;
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
function_decl|;
name|RepositoryScanning
name|getRepositoryScanning
parameter_list|()
throws|throws
name|RepositoryAdminException
function_decl|;
name|void
name|updateRepositoryScanning
parameter_list|(
name|RepositoryScanning
name|repositoryScanning
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
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
function_decl|;
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
function_decl|;
name|FileType
name|getFileType
parameter_list|(
name|String
name|fileTypeId
parameter_list|)
throws|throws
name|RepositoryAdminException
function_decl|;
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
function_decl|;
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
function_decl|;
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
function_decl|;
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
function_decl|;
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
function_decl|;
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
function_decl|;
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
function_decl|;
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
function_decl|;
block|}
end_interface

end_unit

