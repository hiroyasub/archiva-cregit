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
name|configuration
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_comment
comment|/**  * @author Ben Walding  * @author Brett Porter  */
end_comment

begin_class
specifier|public
class|class
name|MavenProxyPropertyLoader
block|{
specifier|private
specifier|static
specifier|final
name|String
name|REPO_LOCAL_STORE
init|=
literal|"repo.local.store"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROXY_LIST
init|=
literal|"proxy.list"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPO_LIST
init|=
literal|"repo.list"
decl_stmt|;
specifier|public
name|void
name|load
parameter_list|(
name|Properties
name|props
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|InvalidConfigurationException
block|{
comment|// set up the managed repository
name|String
name|localCachePath
init|=
name|getMandatoryProperty
argument_list|(
name|props
argument_list|,
name|REPO_LOCAL_STORE
argument_list|)
decl_stmt|;
name|RepositoryConfiguration
name|config
init|=
operator|new
name|RepositoryConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setUrl
argument_list|(
name|toURL
argument_list|(
name|localCachePath
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|setName
argument_list|(
literal|"Imported Maven-Proxy Cache"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setId
argument_list|(
literal|"maven-proxy"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addRepository
argument_list|(
name|config
argument_list|)
expr_stmt|;
comment|// Add the network proxies.
name|String
name|propertyList
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|PROXY_LIST
argument_list|)
decl_stmt|;
if|if
condition|(
name|propertyList
operator|!=
literal|null
condition|)
block|{
name|StringTokenizer
name|tok
init|=
operator|new
name|StringTokenizer
argument_list|(
name|propertyList
argument_list|,
literal|","
argument_list|)
decl_stmt|;
while|while
condition|(
name|tok
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|key
init|=
name|tok
operator|.
name|nextToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|ProxyConfiguration
name|proxy
init|=
operator|new
name|ProxyConfiguration
argument_list|()
decl_stmt|;
name|proxy
operator|.
name|setHost
argument_list|(
name|getMandatoryProperty
argument_list|(
name|props
argument_list|,
literal|"proxy."
operator|+
name|key
operator|+
literal|".host"
argument_list|)
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setPort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|getMandatoryProperty
argument_list|(
name|props
argument_list|,
literal|"proxy."
operator|+
name|key
operator|+
literal|".port"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// the username and password isn't required
name|proxy
operator|.
name|setUsername
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
literal|"proxy."
operator|+
name|key
operator|+
literal|".username"
argument_list|)
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|setPassword
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
literal|"proxy."
operator|+
name|key
operator|+
literal|".password"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addNetworkProxy
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Add the remote repository list
name|String
name|repoList
init|=
name|getMandatoryProperty
argument_list|(
name|props
argument_list|,
name|REPO_LIST
argument_list|)
decl_stmt|;
name|StringTokenizer
name|tok
init|=
operator|new
name|StringTokenizer
argument_list|(
name|repoList
argument_list|,
literal|","
argument_list|)
decl_stmt|;
while|while
condition|(
name|tok
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
name|String
name|key
init|=
name|tok
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|Properties
name|repoProps
init|=
name|getSubset
argument_list|(
name|props
argument_list|,
literal|"repo."
operator|+
name|key
operator|+
literal|"."
argument_list|)
decl_stmt|;
name|String
name|url
init|=
name|getMandatoryProperty
argument_list|(
name|props
argument_list|,
literal|"repo."
operator|+
name|key
operator|+
literal|".url"
argument_list|)
decl_stmt|;
name|String
name|proxyKey
init|=
name|repoProps
operator|.
name|getProperty
argument_list|(
literal|"proxy"
argument_list|)
decl_stmt|;
name|int
name|cachePeriod
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|repoProps
operator|.
name|getProperty
argument_list|(
literal|"cache.period"
argument_list|,
literal|"60"
argument_list|)
argument_list|)
decl_stmt|;
name|RepositoryConfiguration
name|repository
init|=
operator|new
name|RepositoryConfiguration
argument_list|()
decl_stmt|;
name|repository
operator|.
name|setId
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setName
argument_list|(
literal|"Imported Maven-Proxy Remote Proxy"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setLayout
argument_list|(
literal|"legacy"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setIndexed
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setSnapshots
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|RepositoryProxyConnectorConfiguration
name|proxyConnector
init|=
operator|new
name|RepositoryProxyConnectorConfiguration
argument_list|()
decl_stmt|;
name|proxyConnector
operator|.
name|setSourceRepoId
argument_list|(
literal|"maven-proxy"
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setTargetRepoId
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setProxyId
argument_list|(
name|proxyKey
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setFailurePolicy
argument_list|(
name|RepositoryProxyConnectorConfiguration
operator|.
name|NOT_FOUND
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setSnapshotsPolicy
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|cachePeriod
argument_list|)
argument_list|)
expr_stmt|;
name|proxyConnector
operator|.
name|setReleasesPolicy
argument_list|(
name|RepositoryProxyConnectorConfiguration
operator|.
name|NEVER
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addProxyConnector
argument_list|(
name|proxyConnector
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|toURL
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|file
operator|.
name|toURL
argument_list|()
operator|.
name|toExternalForm
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
return|return
literal|"file://"
operator|+
name|StringUtils
operator|.
name|replaceChars
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
block|}
specifier|private
name|Properties
name|getSubset
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|Enumeration
name|keys
init|=
name|props
operator|.
name|keys
argument_list|()
decl_stmt|;
name|Properties
name|result
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|keys
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|String
name|newKey
init|=
name|key
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|setProperty
argument_list|(
name|newKey
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|public
name|void
name|load
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|IOException
throws|,
name|InvalidConfigurationException
block|{
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|load
argument_list|(
name|props
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getMandatoryProperty
parameter_list|(
name|Properties
name|props
parameter_list|,
name|String
name|key
parameter_list|)
throws|throws
name|InvalidConfigurationException
block|{
name|String
name|value
init|=
name|props
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidConfigurationException
argument_list|(
name|key
argument_list|,
literal|"Missing required field: "
operator|+
name|key
argument_list|)
throw|;
block|}
return|return
name|value
return|;
block|}
block|}
end_class

end_unit

