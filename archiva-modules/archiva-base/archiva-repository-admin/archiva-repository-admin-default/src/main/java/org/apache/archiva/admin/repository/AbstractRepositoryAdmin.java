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
name|RepositoryCommonValidator
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
name|audit
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
name|archiva
operator|.
name|audit
operator|.
name|AuditListener
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
name|ArchivaConfiguration
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
name|IndeterminateConfigurationException
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
name|users
operator|.
name|User
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
name|registry
operator|.
name|Registry
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
name|registry
operator|.
name|RegistryException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryAdmin
block|{
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|List
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
init|=
operator|new
name|ArrayList
argument_list|<
name|AuditListener
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RepositoryCommonValidator
name|repositoryCommonValidator
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"commons-configuration"
argument_list|)
specifier|private
name|Registry
name|registry
decl_stmt|;
specifier|protected
name|void
name|triggerAuditEvent
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|resource
parameter_list|,
name|String
name|action
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
block|{
name|User
name|user
init|=
name|auditInformation
operator|==
literal|null
condition|?
literal|null
else|:
name|auditInformation
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|AuditEvent
name|event
init|=
operator|new
name|AuditEvent
argument_list|(
name|repositoryId
argument_list|,
name|user
operator|==
literal|null
condition|?
literal|"null"
else|:
operator|(
name|String
operator|)
name|user
operator|.
name|getPrincipal
argument_list|()
argument_list|,
name|resource
argument_list|,
name|action
argument_list|)
decl_stmt|;
name|event
operator|.
name|setRemoteIP
argument_list|(
name|auditInformation
operator|==
literal|null
condition|?
literal|"null"
else|:
name|auditInformation
operator|.
name|getRemoteAddr
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AuditListener
name|listener
range|:
name|getAuditListeners
argument_list|()
control|)
block|{
name|listener
operator|.
name|auditEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|saveConfiguration
parameter_list|(
name|Configuration
name|config
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
try|try
block|{
name|getArchivaConfiguration
argument_list|()
operator|.
name|save
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Error occurred in the registry: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IndeterminateConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Error occurred while saving the configuration: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|AuditListener
argument_list|>
name|getAuditListeners
parameter_list|()
block|{
return|return
name|auditListeners
return|;
block|}
specifier|public
name|void
name|setAuditListeners
parameter_list|(
name|List
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
parameter_list|)
block|{
name|this
operator|.
name|auditListeners
operator|=
name|auditListeners
expr_stmt|;
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
name|RepositoryCommonValidator
name|getRepositoryCommonValidator
parameter_list|()
block|{
return|return
name|repositoryCommonValidator
return|;
block|}
specifier|public
name|void
name|setRepositoryCommonValidator
parameter_list|(
name|RepositoryCommonValidator
name|repositoryCommonValidator
parameter_list|)
block|{
name|this
operator|.
name|repositoryCommonValidator
operator|=
name|repositoryCommonValidator
expr_stmt|;
block|}
specifier|public
name|Registry
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
specifier|public
name|void
name|setRegistry
parameter_list|(
name|Registry
name|registry
parameter_list|)
block|{
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
block|}
end_class

end_unit

