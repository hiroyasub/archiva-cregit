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
name|proxyconnector
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
name|proxyconnector
operator|.
name|ProxyConnectorOrderComparator
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
name|archiva
operator|.
name|repository
operator|.
name|RepositoryRegistry
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
name|collections4
operator|.
name|IterableUtils
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
name|lang3
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"proxyConnectorAdmin#default"
argument_list|)
specifier|public
class|class
name|DefaultProxyConnectorAdmin
extends|extends
name|AbstractRepositoryAdmin
implements|implements
name|ProxyConnectorAdmin
block|{
annotation|@
name|Inject
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|getProxyConnectors
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|proxyConnectorConfigurations
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|proxyConnectors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|proxyConnectorConfigurations
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ProxyConnectorConfiguration
name|configuration
range|:
name|proxyConnectorConfigurations
control|)
block|{
name|proxyConnectors
operator|.
name|add
argument_list|(
name|getProxyConnector
argument_list|(
name|configuration
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|proxyConnectors
argument_list|,
name|ProxyConnectorOrderComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|proxyConnectors
return|;
block|}
annotation|@
name|Override
specifier|public
name|ProxyConnector
name|getProxyConnector
parameter_list|(
name|String
name|sourceRepoId
parameter_list|,
name|String
name|targetRepoId
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
for|for
control|(
name|ProxyConnector
name|proxyConnector
range|:
name|getProxyConnectors
argument_list|()
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|sourceRepoId
argument_list|,
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|equals
argument_list|(
name|targetRepoId
argument_list|,
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|proxyConnector
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|addProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
if|if
condition|(
name|getProxyConnector
argument_list|(
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
argument_list|,
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"Unable to add proxy connector, as one already exists with source repository id ["
operator|+
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
operator|+
literal|"] and target repository id ["
operator|+
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
operator|+
literal|"]."
argument_list|)
throw|;
block|}
name|validateProxyConnector
argument_list|(
name|proxyConnector
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setBlackListPatterns
argument_list|(
name|unescapePatterns
argument_list|(
name|proxyConnector
operator|.
name|getBlackListPatterns
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setWhiteListPatterns
argument_list|(
name|unescapePatterns
argument_list|(
name|proxyConnector
operator|.
name|getWhiteListPatterns
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|ProxyConnectorConfiguration
name|proxyConnectorConfiguration
init|=
name|getProxyConnectorConfiguration
argument_list|(
name|proxyConnector
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|addProxyConnector
argument_list|(
name|proxyConnectorConfiguration
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
operator|+
literal|"-"
operator|+
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|,
literal|null
argument_list|,
name|AuditEvent
operator|.
name|ADD_PROXY_CONNECTOR
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
comment|// FIXME take care of proxyConnectorRules !
annotation|@
name|Override
specifier|public
name|Boolean
name|deleteProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
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
name|ProxyConnectorConfiguration
name|proxyConnectorConfiguration
init|=
name|findProxyConnector
argument_list|(
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
argument_list|,
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxyConnectorConfiguration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"unable to find ProxyConnector with source "
operator|+
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
operator|+
literal|" and target "
operator|+
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|removeProxyConnector
argument_list|(
name|proxyConnectorConfiguration
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|triggerAuditEvent
argument_list|(
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
operator|+
literal|"-"
operator|+
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|,
literal|null
argument_list|,
name|AuditEvent
operator|.
name|DELETE_PROXY_CONNECTOR
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
comment|// FIXME care take of proxyConnectorRules !
annotation|@
name|Override
specifier|public
name|Boolean
name|updateProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
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
name|ProxyConnectorConfiguration
name|proxyConnectorConfiguration
init|=
name|findProxyConnector
argument_list|(
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
argument_list|,
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|removeProxyConnector
argument_list|(
name|proxyConnectorConfiguration
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addProxyConnector
argument_list|(
name|getProxyConnectorConfiguration
argument_list|(
name|proxyConnector
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
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
operator|+
literal|"-"
operator|+
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|,
literal|null
argument_list|,
name|AuditEvent
operator|.
name|MODIFY_PROXY_CONNECTOR
argument_list|,
name|auditInformation
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|unescapePatterns
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|patterns
parameter_list|)
block|{
if|if
condition|(
name|patterns
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|rawPatterns
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|patterns
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
name|rawPatterns
operator|.
name|add
argument_list|(
name|StringUtils
operator|.
name|replace
argument_list|(
name|pattern
argument_list|,
literal|"\\\\"
argument_list|,
literal|"\\"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|rawPatterns
return|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|>
name|getProxyConnectorAsMap
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|>
name|proxyConnectorMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|ProxyConnector
argument_list|>
name|it
init|=
name|getProxyConnectors
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProxyConnector
name|proxyConfig
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|proxyConfig
operator|.
name|getSourceRepoId
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|connectors
init|=
name|proxyConnectorMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectors
operator|==
literal|null
condition|)
block|{
name|connectors
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|proxyConnectorMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|connectors
argument_list|)
expr_stmt|;
block|}
name|connectors
operator|.
name|add
argument_list|(
name|proxyConfig
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|connectors
argument_list|,
name|ProxyConnectorOrderComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|proxyConnectorMap
return|;
block|}
specifier|private
name|ProxyConnectorConfiguration
name|findProxyConnector
parameter_list|(
name|String
name|sourceId
parameter_list|,
name|String
name|targetId
parameter_list|,
name|Configuration
name|configuration
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
name|IterableUtils
operator|.
name|find
argument_list|(
name|configuration
operator|.
name|getProxyConnectors
argument_list|()
argument_list|,
name|selectedProxy
argument_list|)
return|;
block|}
specifier|protected
name|ProxyConnectorConfiguration
name|getProxyConnectorConfiguration
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|)
block|{
return|return
name|proxyConnector
operator|==
literal|null
condition|?
literal|null
else|:
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|proxyConnector
argument_list|,
name|ProxyConnectorConfiguration
operator|.
name|class
argument_list|)
return|;
block|}
specifier|protected
name|ProxyConnector
name|getProxyConnector
parameter_list|(
name|ProxyConnectorConfiguration
name|proxyConnectorConfiguration
parameter_list|)
block|{
return|return
name|proxyConnectorConfiguration
operator|==
literal|null
condition|?
literal|null
else|:
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|proxyConnectorConfiguration
argument_list|,
name|ProxyConnector
operator|.
name|class
argument_list|)
return|;
block|}
specifier|protected
name|void
name|validateProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
comment|// validate source a Managed target a Remote
if|if
condition|(
name|repositoryRegistry
operator|.
name|getManagedRepository
argument_list|(
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"non valid ProxyConnector sourceRepo with id "
operator|+
name|proxyConnector
operator|.
name|getSourceRepoId
argument_list|()
operator|+
literal|" is not a ManagedRepository"
argument_list|)
throw|;
block|}
if|if
condition|(
name|repositoryRegistry
operator|.
name|getRemoteRepository
argument_list|(
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryAdminException
argument_list|(
literal|"non valid ProxyConnector sourceRepo with id "
operator|+
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
operator|+
literal|" is not a RemoteRepository"
argument_list|)
throw|;
block|}
comment|// FIXME validate NetworkProxyConfiguration too when available
block|}
block|}
end_class

end_unit

