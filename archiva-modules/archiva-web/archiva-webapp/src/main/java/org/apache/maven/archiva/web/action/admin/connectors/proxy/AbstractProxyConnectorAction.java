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
name|connectors
operator|.
name|proxy
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
name|collections
operator|.
name|functors
operator|.
name|NotPredicate
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
name|ProxyConnectorConfiguration
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
name|functors
operator|.
name|ProxyConnectorSelectionPredicate
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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_comment
comment|/**  * AbstractProxyConnectorAction   *  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractProxyConnectorAction
extends|extends
name|AbstractActionSupport
implements|implements
name|SecureAction
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DIRECT_CONNECTION
init|=
literal|"(direct connection)"
decl_stmt|;
comment|/**      * plexus.requirement      */
annotation|@
name|Inject
specifier|protected
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
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
specifier|protected
name|void
name|addProxyConnector
parameter_list|(
name|ProxyConnectorConfiguration
name|proxyConnector
parameter_list|)
block|{
name|getConfig
argument_list|()
operator|.
name|addProxyConnector
argument_list|(
name|proxyConnector
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ProxyConnectorConfiguration
name|findProxyConnector
parameter_list|(
name|String
name|sourceId
parameter_list|,
name|String
name|targetId
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|sourceId
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|targetId
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ProxyConnectorSelectionPredicate
name|selectedProxy
init|=
operator|new
name|ProxyConnectorSelectionPredicate
argument_list|(
name|sourceId
argument_list|,
name|targetId
argument_list|)
decl_stmt|;
return|return
operator|(
name|ProxyConnectorConfiguration
operator|)
name|CollectionUtils
operator|.
name|find
argument_list|(
name|getConfig
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
argument_list|,
name|selectedProxy
argument_list|)
return|;
block|}
specifier|protected
name|Configuration
name|getConfig
parameter_list|()
block|{
return|return
name|this
operator|.
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
argument_list|>
name|createProxyConnectorMap
parameter_list|()
block|{
return|return
name|getConfig
argument_list|()
operator|.
name|getProxyConnectorAsMap
argument_list|()
return|;
block|}
specifier|protected
name|void
name|removeConnector
parameter_list|(
name|String
name|sourceId
parameter_list|,
name|String
name|targetId
parameter_list|)
block|{
name|ProxyConnectorSelectionPredicate
name|selectedProxy
init|=
operator|new
name|ProxyConnectorSelectionPredicate
argument_list|(
name|sourceId
argument_list|,
name|targetId
argument_list|)
decl_stmt|;
name|NotPredicate
name|notSelectedProxy
init|=
operator|new
name|NotPredicate
argument_list|(
name|selectedProxy
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|filter
argument_list|(
name|getConfig
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
argument_list|,
name|notSelectedProxy
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|removeProxyConnector
parameter_list|(
name|ProxyConnectorConfiguration
name|connector
parameter_list|)
block|{
name|getConfig
argument_list|()
operator|.
name|removeProxyConnector
argument_list|(
name|connector
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|saveConfiguration
parameter_list|()
block|{
try|try
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|getConfig
argument_list|()
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
name|RegistryException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Unable to save configuration: "
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
return|return
name|SUCCESS
return|;
block|}
block|}
end_class

end_unit

