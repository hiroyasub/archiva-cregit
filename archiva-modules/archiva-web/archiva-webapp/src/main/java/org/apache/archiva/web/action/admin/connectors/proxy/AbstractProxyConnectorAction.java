begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|beans
operator|.
name|ProxyConnector
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
name|admin
operator|.
name|model
operator|.
name|proxyconnector
operator|.
name|ProxyConnectorAdmin
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
name|remote
operator|.
name|RemoteRepositoryAdmin
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
name|apache
operator|.
name|archiva
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
name|apache
operator|.
name|archiva
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
name|apache
operator|.
name|archiva
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
name|apache
operator|.
name|archiva
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

begin_comment
comment|/**  * AbstractProxyConnectorAction  *  *  */
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
annotation|@
name|Inject
specifier|private
name|ProxyConnectorAdmin
name|proxyConnectorAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|RemoteRepositoryAdmin
name|remoteRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
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
specifier|protected
name|void
name|addProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|getProxyConnectorAdmin
argument_list|()
operator|.
name|addProxyConnector
argument_list|(
name|proxyConnector
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ProxyConnector
name|findProxyConnector
parameter_list|(
name|String
name|sourceId
parameter_list|,
name|String
name|targetId
parameter_list|)
throws|throws
name|RepositoryAdminException
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
return|return
name|getProxyConnectorAdmin
argument_list|()
operator|.
name|getProxyConnector
argument_list|(
name|sourceId
argument_list|,
name|targetId
argument_list|)
return|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|>
name|createProxyConnectorMap
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
return|return
name|getProxyConnectorAdmin
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
throws|throws
name|RepositoryAdminException
block|{
name|ProxyConnector
name|proxyConnector
init|=
name|findProxyConnector
argument_list|(
name|sourceId
argument_list|,
name|targetId
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxyConnector
operator|!=
literal|null
condition|)
block|{
name|getProxyConnectorAdmin
argument_list|()
operator|.
name|deleteProxyConnector
argument_list|(
name|proxyConnector
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|removeProxyConnector
parameter_list|(
name|ProxyConnector
name|connector
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|getProxyConnectorAdmin
argument_list|()
operator|.
name|deleteProxyConnector
argument_list|(
name|connector
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ProxyConnectorAdmin
name|getProxyConnectorAdmin
parameter_list|()
block|{
return|return
name|proxyConnectorAdmin
return|;
block|}
specifier|public
name|void
name|setProxyConnectorAdmin
parameter_list|(
name|ProxyConnectorAdmin
name|proxyConnectorAdmin
parameter_list|)
block|{
name|this
operator|.
name|proxyConnectorAdmin
operator|=
name|proxyConnectorAdmin
expr_stmt|;
block|}
specifier|public
name|RemoteRepositoryAdmin
name|getRemoteRepositoryAdmin
parameter_list|()
block|{
return|return
name|remoteRepositoryAdmin
return|;
block|}
specifier|public
name|void
name|setRemoteRepositoryAdmin
parameter_list|(
name|RemoteRepositoryAdmin
name|remoteRepositoryAdmin
parameter_list|)
block|{
name|this
operator|.
name|remoteRepositoryAdmin
operator|=
name|remoteRepositoryAdmin
expr_stmt|;
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

