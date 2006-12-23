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
name|ConfigurationChangeException
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
name|ConfigurationChangeListener
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
name|ConfigurationStore
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
name|ConfigurationStoreException
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
name|ConfiguredRepositoryFactory
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
name|ProxiedRepositoryConfiguration
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
name|Proxy
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
name|RepositoryConfiguration
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|ResourceDoesNotExistException
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
name|proxy
operator|.
name|ProxyInfo
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
name|logging
operator|.
name|AbstractLogEnabled
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
comment|/**  * Default implementation of the proxy manager that bridges the repository configuration classes to the proxy API. This  * class is not thread safe (due to the request handler being a non-thread safe requirement).  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @todo we should be able to configure "views" that sit in front of this (ie, prefix = /legacy, appears as layout maven-1.x, path gets translated before being passed on)  * @plexus.component instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultProxyManager
extends|extends
name|AbstractLogEnabled
implements|implements
name|ProxyManager
implements|,
name|ConfigurationChangeListener
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfigurationStore
name|configurationStore
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.proxy.ProxyRequestHandler"      * @todo seems to be a bug in qdox that the role above is required      */
specifier|private
name|ProxyRequestHandler
name|requestHandler
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfiguredRepositoryFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * The proxy groups for each managed repository.      */
specifier|private
specifier|static
name|Map
comment|/*<String,ProxiedRepositoryGroup>*/
name|proxyGroups
decl_stmt|;
comment|/**      * The default proxy group/managed repository.      */
specifier|private
specifier|static
name|ProxiedRepositoryGroup
name|defaultProxyGroup
decl_stmt|;
specifier|public
name|File
name|get
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
block|{
assert|assert
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
assert|;
name|Map
name|groups
init|=
name|getProxyGroups
argument_list|()
decl_stmt|;
name|ProxiedRepositoryGroup
name|proxyGroup
init|=
name|parseRepositoryId
argument_list|(
name|path
argument_list|,
name|groups
argument_list|)
decl_stmt|;
name|String
name|repositoryPath
init|=
name|path
decl_stmt|;
if|if
condition|(
name|proxyGroup
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|defaultProxyGroup
operator|!=
literal|null
condition|)
block|{
name|proxyGroup
operator|=
name|defaultProxyGroup
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ResourceDoesNotExistException
argument_list|(
literal|"No repositories exist under the path: "
operator|+
name|path
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|String
name|id
init|=
name|proxyGroup
operator|.
name|getManagedRepository
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"requesting "
operator|+
name|repositoryPath
operator|+
literal|" from repository '"
operator|+
name|id
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|repositoryPath
operator|=
name|repositoryPath
operator|.
name|substring
argument_list|(
name|id
operator|.
name|length
argument_list|()
operator|+
literal|2
argument_list|)
expr_stmt|;
block|}
return|return
name|requestHandler
operator|.
name|get
argument_list|(
name|repositoryPath
argument_list|,
name|proxyGroup
operator|.
name|getProxiedRepositories
argument_list|()
argument_list|,
name|proxyGroup
operator|.
name|getManagedRepository
argument_list|()
argument_list|,
name|proxyGroup
operator|.
name|getWagonProxy
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|File
name|getAlways
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
block|{
assert|assert
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
assert|;
name|Map
name|groups
init|=
name|getProxyGroups
argument_list|()
decl_stmt|;
name|ProxiedRepositoryGroup
name|proxyGroup
init|=
name|parseRepositoryId
argument_list|(
name|path
argument_list|,
name|groups
argument_list|)
decl_stmt|;
name|String
name|repositoryPath
init|=
name|path
decl_stmt|;
if|if
condition|(
name|proxyGroup
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|defaultProxyGroup
operator|!=
literal|null
condition|)
block|{
name|proxyGroup
operator|=
name|defaultProxyGroup
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|ResourceDoesNotExistException
argument_list|(
literal|"No repositories exist under the path: "
operator|+
name|path
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|repositoryPath
operator|=
name|repositoryPath
operator|.
name|substring
argument_list|(
name|proxyGroup
operator|.
name|getManagedRepository
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|length
argument_list|()
operator|+
literal|2
argument_list|)
expr_stmt|;
block|}
return|return
name|requestHandler
operator|.
name|getAlways
argument_list|(
name|repositoryPath
argument_list|,
name|proxyGroup
operator|.
name|getProxiedRepositories
argument_list|()
argument_list|,
name|proxyGroup
operator|.
name|getManagedRepository
argument_list|()
argument_list|,
name|proxyGroup
operator|.
name|getWagonProxy
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Configuration
name|getConfiguration
parameter_list|()
throws|throws
name|ProxyException
block|{
name|Configuration
name|configuration
decl_stmt|;
try|try
block|{
name|configuration
operator|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
expr_stmt|;
name|configurationStore
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationStoreException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProxyException
argument_list|(
literal|"Error reading configuration, unable to proxy any requests: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|configuration
return|;
block|}
specifier|private
name|Map
name|getProxyGroups
parameter_list|()
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
block|{
if|if
condition|(
name|proxyGroups
operator|==
literal|null
condition|)
block|{
name|Map
name|groups
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|Configuration
name|configuration
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
name|ProxyInfo
name|wagonProxy
init|=
name|createWagonProxy
argument_list|(
name|configuration
operator|.
name|getProxy
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|configuration
operator|.
name|getRepositories
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|RepositoryConfiguration
name|repository
init|=
operator|(
name|RepositoryConfiguration
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|ArtifactRepository
name|managedRepository
init|=
name|repositoryFactory
operator|.
name|createRepository
argument_list|(
name|repository
argument_list|)
decl_stmt|;
name|List
name|proxiedRepositories
init|=
name|getProxiedRepositoriesForManagedRepository
argument_list|(
name|configuration
operator|.
name|getProxiedRepositories
argument_list|()
argument_list|,
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|proxiedRepositories
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|groups
operator|.
name|put
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|ProxiedRepositoryGroup
argument_list|(
name|proxiedRepositories
argument_list|,
name|managedRepository
argument_list|,
name|wagonProxy
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO: ability to configure default proxy separately!
if|if
condition|(
name|groups
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|defaultProxyGroup
operator|=
operator|(
name|ProxiedRepositoryGroup
operator|)
name|groups
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|proxyGroups
operator|=
name|groups
expr_stmt|;
block|}
return|return
name|proxyGroups
return|;
block|}
specifier|private
name|List
name|getProxiedRepositoriesForManagedRepository
parameter_list|(
name|List
name|proxiedRepositories
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|List
name|repositories
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|proxiedRepositories
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ProxiedRepositoryConfiguration
name|config
init|=
operator|(
name|ProxiedRepositoryConfiguration
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getManagedRepository
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|repositories
operator|.
name|add
argument_list|(
name|repositoryFactory
operator|.
name|createProxiedRepository
argument_list|(
name|config
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|repositories
return|;
block|}
specifier|private
specifier|static
name|ProxiedRepositoryGroup
name|parseRepositoryId
parameter_list|(
name|String
name|path
parameter_list|,
name|Map
name|groups
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
block|{
name|ProxiedRepositoryGroup
name|group
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|groups
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
operator|&&
name|group
operator|==
literal|null
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|"/"
argument_list|)
condition|)
block|{
name|group
operator|=
operator|(
name|ProxiedRepositoryGroup
operator|)
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|group
return|;
block|}
specifier|private
specifier|static
name|ProxyInfo
name|createWagonProxy
parameter_list|(
name|Proxy
name|proxy
parameter_list|)
block|{
name|ProxyInfo
name|proxyInfo
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|proxy
operator|!=
literal|null
operator|&&
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|proxy
operator|.
name|getHost
argument_list|()
argument_list|)
condition|)
block|{
name|proxyInfo
operator|=
operator|new
name|ProxyInfo
argument_list|()
expr_stmt|;
name|proxyInfo
operator|.
name|setHost
argument_list|(
name|proxy
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|proxyInfo
operator|.
name|setPort
argument_list|(
name|proxy
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|proxyInfo
operator|.
name|setUserName
argument_list|(
name|proxy
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|proxyInfo
operator|.
name|setPassword
argument_list|(
name|proxy
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|proxyInfo
operator|.
name|setNonProxyHosts
argument_list|(
name|proxy
operator|.
name|getNonProxyHosts
argument_list|()
argument_list|)
expr_stmt|;
name|proxyInfo
operator|.
name|setType
argument_list|(
name|proxy
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|proxyInfo
return|;
block|}
specifier|public
name|void
name|notifyOfConfigurationChange
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|InvalidConfigurationException
throws|,
name|ConfigurationChangeException
block|{
comment|// reinit
name|proxyGroups
operator|=
literal|null
expr_stmt|;
name|defaultProxyGroup
operator|=
literal|null
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Re-initialising proxy configuration"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

