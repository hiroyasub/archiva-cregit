begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|repositories
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|managed
operator|.
name|ManagedRepositoryAdmin
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
name|Auditable
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
name|apache
operator|.
name|maven
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
name|maven
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|InvalidConfigurationException
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
name|ProxyConnectorConfiguration
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
name|ArchivaRoleConstants
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
name|web
operator|.
name|action
operator|.
name|AbstractActionSupport
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
name|rbac
operator|.
name|Resource
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
name|codehaus
operator|.
name|redback
operator|.
name|integration
operator|.
name|interceptor
operator|.
name|SecureAction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|integration
operator|.
name|interceptor
operator|.
name|SecureActionBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|integration
operator|.
name|interceptor
operator|.
name|SecureActionException
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
name|io
operator|.
name|IOException
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
comment|/**  * Abstract AdminRepositories Action base.  *<p/>  * Base class for all repository administrative functions.  * This should be neutral to the type of action (add/edit/delete) and type of repo (managed/remote)  *  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoriesAdminAction
extends|extends
name|AbstractActionSupport
implements|implements
name|SecureAction
implements|,
name|Auditable
block|{
comment|/**      * plexus.requirement      */
annotation|@
name|Inject
specifier|protected
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
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
name|SecureActionBundle
name|getSecureActionBundle
parameter_list|()
throws|throws
name|SecureActionException
block|{
name|SecureActionBundle
name|bundle
init|=
operator|new
name|SecureActionBundle
argument_list|()
decl_stmt|;
name|bundle
operator|.
name|setRequiresAuthentication
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|bundle
operator|.
name|addRequiredAuthorization
argument_list|(
name|ArchivaRoleConstants
operator|.
name|OPERATION_MANAGE_CONFIGURATION
argument_list|,
name|Resource
operator|.
name|GLOBAL
argument_list|)
expr_stmt|;
return|return
name|bundle
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
comment|/**      * Save the configuration.      *      * @param configuration the configuration to save.      * @return the webwork result code to issue.      * @throws IOException                   thrown if unable to save file to disk.      * @throws InvalidConfigurationException thrown if configuration is invalid.      * @throws RegistryException             thrown if configuration subsystem has a problem saving the configuration to disk.      */
specifier|protected
name|String
name|saveConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
block|{
try|try
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Successfully saved configuration"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndeterminateConfigurationException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Configuration Registry Exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
block|}
return|return
name|SUCCESS
return|;
block|}
comment|/**      * Get the list of ProxyConnectors that are present in the configuration.      *      * @return a new list of ProxyConnectors present in the configuration.      */
specifier|protected
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|getProxyConnectors
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|ManagedRepositoryAdmin
name|getManagedRepositoryAdmin
parameter_list|()
block|{
return|return
name|managedRepositoryAdmin
return|;
block|}
specifier|public
name|void
name|setManagedRepositoryAdmin
parameter_list|(
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
parameter_list|)
block|{
name|this
operator|.
name|managedRepositoryAdmin
operator|=
name|managedRepositoryAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

