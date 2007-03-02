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
name|repository
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
name|archiva
operator|.
name|proxy
operator|.
name|ProxyException
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
name|proxy
operator|.
name|ProxyRequestHandler
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
name|webdav
operator|.
name|AbstractDavServerComponent
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
name|webdav
operator|.
name|DavServerComponent
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
name|webdav
operator|.
name|DavServerException
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
name|webdav
operator|.
name|servlet
operator|.
name|DavServerRequest
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
name|webdav
operator|.
name|util
operator|.
name|WebdavMethodUtil
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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

begin_comment
comment|/**  * ProxiedDavServer  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.codehaus.plexus.webdav.DavServerComponent"  * role-hint="proxied"  * instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ProxiedDavServer
extends|extends
name|AbstractDavServerComponent
block|{
comment|/**      * @plexus.requirement role-hint="simple"      */
specifier|private
name|DavServerComponent
name|davServer
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.proxy.ProxyRequestHandler"      * @todo seems to be a bug in qdox that the role above is required      */
specifier|private
name|ProxyRequestHandler
name|proxyRequestHandler
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfiguredRepositoryFactory
name|repositoryFactory
decl_stmt|;
specifier|private
name|RepositoryConfiguration
name|repositoryConfiguration
decl_stmt|;
specifier|private
name|ArtifactRepository
name|managedRepository
decl_stmt|;
specifier|private
name|List
comment|/*<ArtifactRepository>*/
name|proxiedRepositories
decl_stmt|;
specifier|private
name|ProxyInfo
name|wagonProxy
decl_stmt|;
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|davServer
operator|.
name|getPrefix
argument_list|()
return|;
block|}
specifier|public
name|File
name|getRootDirectory
parameter_list|()
block|{
return|return
name|davServer
operator|.
name|getRootDirectory
argument_list|()
return|;
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|davServer
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setRootDirectory
parameter_list|(
name|File
name|rootDirectory
parameter_list|)
block|{
name|davServer
operator|.
name|setRootDirectory
argument_list|(
name|rootDirectory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|DavServerException
block|{
name|davServer
operator|.
name|init
argument_list|(
name|servletConfig
argument_list|)
expr_stmt|;
name|proxiedRepositories
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|wagonProxy
operator|=
name|createWagonProxy
argument_list|(
name|config
operator|.
name|getProxy
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryConfiguration
operator|=
name|config
operator|.
name|getRepositoryByUrlName
argument_list|(
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|managedRepository
operator|=
name|repositoryFactory
operator|.
name|createRepository
argument_list|(
name|repositoryConfiguration
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|config
operator|.
name|getProxiedRepositories
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
name|ProxiedRepositoryConfiguration
name|proxiedRepoConfig
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
name|proxiedRepoConfig
operator|.
name|getManagedRepository
argument_list|()
operator|.
name|equals
argument_list|(
name|repositoryConfiguration
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|proxiedRepositories
operator|.
name|add
argument_list|(
name|repositoryFactory
operator|.
name|createProxiedRepository
argument_list|(
name|proxiedRepoConfig
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|process
parameter_list|(
name|DavServerRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|DavServerException
throws|,
name|ServletException
throws|,
name|IOException
block|{
if|if
condition|(
name|WebdavMethodUtil
operator|.
name|isReadMethod
argument_list|(
name|request
operator|.
name|getRequest
argument_list|()
operator|.
name|getMethod
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|hasResource
argument_list|(
name|request
operator|.
name|getLogicalResource
argument_list|()
argument_list|)
condition|)
block|{
name|fetchContentFromProxies
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
block|}
name|davServer
operator|.
name|process
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|fetchContentFromProxies
parameter_list|(
name|DavServerRequest
name|request
parameter_list|)
throws|throws
name|ServletException
block|{
try|try
block|{
name|proxyRequestHandler
operator|.
name|get
argument_list|(
name|request
operator|.
name|getLogicalResource
argument_list|()
argument_list|,
name|this
operator|.
name|proxiedRepositories
argument_list|,
name|this
operator|.
name|managedRepository
argument_list|,
name|this
operator|.
name|wagonProxy
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceDoesNotExistException
name|e
parameter_list|)
block|{
comment|// TODO: getLogger().info( "Unable to fetch resource, it does not exist.", e );
comment|// return an HTTP 404 instead of HTTP 500 error.
return|return;
block|}
catch|catch
parameter_list|(
name|ProxyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Unable to fetch resource."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
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
name|RepositoryConfiguration
name|getRepositoryConfiguration
parameter_list|()
block|{
return|return
name|repositoryConfiguration
return|;
block|}
block|}
end_class

end_unit

