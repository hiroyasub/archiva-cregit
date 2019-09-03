begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
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
name|configuration
operator|.
name|*
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
name|proxy
operator|.
name|model
operator|.
name|NetworkProxy
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
name|proxy
operator|.
name|model
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
name|proxy
operator|.
name|model
operator|.
name|RepositoryProxyHandler
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
name|*
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
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * Default proxy registry implementation. Uses the archiva configuration for accessing and storing the  * proxy information.  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"proxyRegistry#default"
argument_list|)
specifier|public
class|class
name|ArchivaProxyRegistry
implements|implements
name|ProxyRegistry
implements|,
name|ConfigurationListener
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ArchivaProxyRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
name|List
argument_list|<
name|RepositoryProxyHandler
argument_list|>
name|repositoryProxyHandlers
decl_stmt|;
annotation|@
name|Inject
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|NetworkProxy
argument_list|>
name|networkProxyMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|RepositoryType
argument_list|,
name|List
argument_list|<
name|RepositoryProxyHandler
argument_list|>
argument_list|>
name|handlerMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|ProxyConnectorOrderComparator
name|comparator
init|=
name|ProxyConnectorOrderComparator
operator|.
name|getInstance
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|>
name|connectorMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|connectorList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|PostConstruct
specifier|private
name|void
name|init
parameter_list|()
block|{
if|if
condition|(
name|repositoryProxyHandlers
operator|==
literal|null
condition|)
block|{
name|repositoryProxyHandlers
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|updateHandler
argument_list|()
expr_stmt|;
name|updateNetworkProxies
argument_list|()
expr_stmt|;
block|}
specifier|private
name|ArchivaConfiguration
name|getArchivaConfiguration
parameter_list|()
block|{
return|return
name|archivaConfiguration
return|;
block|}
specifier|private
name|void
name|updateNetworkProxies
parameter_list|()
block|{
name|this
operator|.
name|networkProxyMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|NetworkProxyConfiguration
argument_list|>
name|networkProxies
init|=
name|getArchivaConfiguration
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getNetworkProxies
argument_list|()
decl_stmt|;
for|for
control|(
name|NetworkProxyConfiguration
name|networkProxyConfig
range|:
name|networkProxies
control|)
block|{
name|String
name|key
init|=
name|networkProxyConfig
operator|.
name|getId
argument_list|()
decl_stmt|;
name|NetworkProxy
name|proxy
init|=
operator|new
name|NetworkProxy
argument_list|()
decl_stmt|;
name|proxy
operator|.
name|setProtocol
argument_list|(
name|networkProxyConfig
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setHost
argument_list|(
name|networkProxyConfig
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setPort
argument_list|(
name|networkProxyConfig
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setUsername
argument_list|(
name|networkProxyConfig
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setPassword
argument_list|(
name|networkProxyConfig
operator|.
name|getPassword
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setUseNtlm
argument_list|(
name|networkProxyConfig
operator|.
name|isUseNtlm
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|networkProxyMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|proxy
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|RepositoryProxyHandler
name|connectors
range|:
name|repositoryProxyHandlers
control|)
block|{
name|connectors
operator|.
name|setNetworkProxies
argument_list|(
name|this
operator|.
name|networkProxyMap
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateHandler
parameter_list|()
block|{
for|for
control|(
name|RepositoryProxyHandler
name|handler
range|:
name|repositoryProxyHandlers
control|)
block|{
name|List
argument_list|<
name|RepositoryType
argument_list|>
name|types
init|=
name|handler
operator|.
name|supports
argument_list|()
decl_stmt|;
for|for
control|(
name|RepositoryType
name|type
range|:
name|types
control|)
block|{
if|if
condition|(
operator|!
name|handlerMap
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|handlerMap
operator|.
name|put
argument_list|(
name|type
argument_list|,
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|handlerMap
operator|.
name|get
argument_list|(
name|type
argument_list|)
operator|.
name|add
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|updateConnectors
parameter_list|()
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
name|connectorList
operator|=
name|proxyConnectorConfigurations
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|configuration
lambda|->
name|buildProxyConnector
argument_list|(
name|configuration
argument_list|)
argument_list|)
operator|.
name|sorted
argument_list|(
name|comparator
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
expr_stmt|;
name|connectorMap
operator|=
name|connectorList
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|groupingBy
argument_list|(
name|a
lambda|->
name|a
operator|.
name|getSourceRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ProxyConnector
name|buildProxyConnector
parameter_list|(
name|ProxyConnectorConfiguration
name|configuration
parameter_list|)
block|{
name|ProxyConnector
name|proxyConnector
init|=
operator|new
name|ProxyConnector
argument_list|()
decl_stmt|;
name|proxyConnector
operator|.
name|setOrder
argument_list|(
name|configuration
operator|.
name|getOrder
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setBlacklist
argument_list|(
name|configuration
operator|.
name|getBlackListPatterns
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setWhitelist
argument_list|(
name|configuration
operator|.
name|getWhiteListPatterns
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setDisabled
argument_list|(
name|configuration
operator|.
name|isDisabled
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setPolicies
argument_list|(
name|configuration
operator|.
name|getPolicies
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setProperties
argument_list|(
name|configuration
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setProxyId
argument_list|(
name|configuration
operator|.
name|getProxyId
argument_list|()
argument_list|)
expr_stmt|;
name|ManagedRepository
name|srcRepo
init|=
name|repositoryRegistry
operator|.
name|getManagedRepository
argument_list|(
name|configuration
operator|.
name|getSourceRepoId
argument_list|()
argument_list|)
decl_stmt|;
name|proxyConnector
operator|.
name|setSourceRepository
argument_list|(
name|srcRepo
argument_list|)
expr_stmt|;
name|RemoteRepository
name|targetRepo
init|=
name|repositoryRegistry
operator|.
name|getRemoteRepository
argument_list|(
name|configuration
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
decl_stmt|;
name|proxyConnector
operator|.
name|setTargetRepository
argument_list|(
name|targetRepo
argument_list|)
expr_stmt|;
return|return
name|proxyConnector
return|;
block|}
annotation|@
name|Override
specifier|public
name|NetworkProxy
name|getNetworkProxy
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|this
operator|.
name|networkProxyMap
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|RepositoryType
argument_list|,
name|List
argument_list|<
name|RepositoryProxyHandler
argument_list|>
argument_list|>
name|getAllHandler
parameter_list|()
block|{
return|return
name|this
operator|.
name|handlerMap
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|RepositoryProxyHandler
argument_list|>
name|getHandler
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|handlerMap
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|this
operator|.
name|handlerMap
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasHandler
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
block|{
return|return
name|this
operator|.
name|handlerMap
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|configurationEvent
parameter_list|(
name|ConfigurationEvent
name|event
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Config changed updating proxy list"
argument_list|)
expr_stmt|;
name|updateNetworkProxies
argument_list|()
expr_stmt|;
name|updateConnectors
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|getProxyConnectors
parameter_list|()
block|{
return|return
name|connectorList
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
block|{
return|return
name|connectorMap
return|;
block|}
block|}
end_class

end_unit

