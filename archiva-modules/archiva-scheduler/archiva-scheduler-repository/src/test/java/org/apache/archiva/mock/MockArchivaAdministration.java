begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|mock
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
name|admin
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
name|admin
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
name|admin
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ArchivaConfiguration
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
name|List
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|MockArchivaAdministration
implements|implements
name|ArchivaAdministration
block|{
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
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
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
comment|//To change body of implemented methods use File | Settings | File Templates.
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
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
argument_list|<
name|String
argument_list|>
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
comment|//To change body of implemented methods use File | Settings | File Templates.
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
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
argument_list|<
name|String
argument_list|>
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|OrganisationInformation
name|getOrganisationInformation
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|ArchivaConfiguration
name|getArchivaConfiguration
parameter_list|()
block|{
return|return
name|archivaConfiguration
return|;
block|}
specifier|public
name|void
name|setArchivaConfiguration
parameter_list|(
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|)
block|{
name|this
operator|.
name|archivaConfiguration
operator|=
name|archivaConfiguration
expr_stmt|;
block|}
block|}
end_class

end_unit

