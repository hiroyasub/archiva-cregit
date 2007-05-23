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
name|collections
operator|.
name|Closure
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
name|IfClosure
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
name|ConfigurationNames
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
name|configuration
operator|.
name|functors
operator|.
name|LocalRepositoryPredicate
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
name|RepositoryConfigurationToMapClosure
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
name|model
operator|.
name|RepositoryURL
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
name|security
operator|.
name|ArchivaRoleConstants
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
name|authentication
operator|.
name|AuthenticationException
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
name|authentication
operator|.
name|AuthenticationResult
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
name|authorization
operator|.
name|AuthorizationException
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
name|authorization
operator|.
name|AuthorizationResult
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
name|policy
operator|.
name|AccountLockedException
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
name|policy
operator|.
name|MustChangePasswordException
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
name|system
operator|.
name|SecuritySession
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
name|system
operator|.
name|SecuritySystem
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
name|xwork
operator|.
name|filter
operator|.
name|authentication
operator|.
name|HttpAuthenticator
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
name|RegistryListener
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
name|servlet
operator|.
name|multiplexed
operator|.
name|MultiplexedWebDavServlet
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
name|HttpServletRequest
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

begin_comment
comment|/**  * RepositoryServlet  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServlet
extends|extends
name|MultiplexedWebDavServlet
implements|implements
name|RegistryListener
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|SecuritySystem
name|securitySystem
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="basic"      */
specifier|private
name|HttpAuthenticator
name|httpAuth
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|AuditLog
name|audit
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
name|Map
name|repositoryMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|public
name|void
name|initComponents
parameter_list|()
throws|throws
name|ServletException
block|{
name|super
operator|.
name|initComponents
argument_list|()
expr_stmt|;
name|securitySystem
operator|=
operator|(
name|SecuritySystem
operator|)
name|lookup
argument_list|(
name|SecuritySystem
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|httpAuth
operator|=
operator|(
name|HttpAuthenticator
operator|)
name|lookup
argument_list|(
name|HttpAuthenticator
operator|.
name|ROLE
argument_list|,
literal|"basic"
argument_list|)
expr_stmt|;
name|audit
operator|=
operator|(
name|AuditLog
operator|)
name|lookup
argument_list|(
name|AuditLog
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|configuration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|updateRepositoryMap
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|initServers
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|DavServerException
block|{
name|List
name|repositories
init|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|Iterator
name|itrepos
init|=
name|repositories
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itrepos
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|RepositoryConfiguration
name|repo
init|=
operator|(
name|RepositoryConfiguration
operator|)
name|itrepos
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|repo
operator|.
name|isManaged
argument_list|()
condition|)
block|{
comment|// Skip non-managed.
continue|continue;
block|}
name|RepositoryURL
name|url
init|=
operator|new
name|RepositoryURL
argument_list|(
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|repoDir
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|repoDir
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
comment|// Skip invalid directories.
name|log
argument_list|(
literal|"Unable to create missing directory for "
operator|+
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
name|DavServerComponent
name|server
init|=
name|createServer
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
name|repoDir
argument_list|,
name|servletConfig
argument_list|)
decl_stmt|;
name|server
operator|.
name|addListener
argument_list|(
name|audit
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|RepositoryConfiguration
name|getRepository
parameter_list|(
name|DavServerRequest
name|request
parameter_list|)
block|{
return|return
operator|(
name|RepositoryConfiguration
operator|)
name|repositoryMap
operator|.
name|get
argument_list|(
name|request
operator|.
name|getPrefix
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getRepositoryName
parameter_list|(
name|DavServerRequest
name|request
parameter_list|)
block|{
name|RepositoryConfiguration
name|repoConfig
init|=
name|getRepository
argument_list|(
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|repoConfig
operator|==
literal|null
condition|)
block|{
return|return
literal|"Unknown"
return|;
block|}
return|return
name|repoConfig
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|private
name|void
name|updateRepositoryMap
parameter_list|()
block|{
name|RepositoryConfigurationToMapClosure
name|repoMapClosure
init|=
operator|new
name|RepositoryConfigurationToMapClosure
argument_list|()
decl_stmt|;
name|Closure
name|localRepoMap
init|=
name|IfClosure
operator|.
name|getInstance
argument_list|(
name|LocalRepositoryPredicate
operator|.
name|getInstance
argument_list|()
argument_list|,
name|repoMapClosure
argument_list|)
decl_stmt|;
name|CollectionUtils
operator|.
name|forAllDo
argument_list|(
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositories
argument_list|()
argument_list|,
name|localRepoMap
argument_list|)
expr_stmt|;
name|this
operator|.
name|repositoryMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|repositoryMap
operator|.
name|putAll
argument_list|(
name|repoMapClosure
operator|.
name|getMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAuthenticated
parameter_list|(
name|DavServerRequest
name|davRequest
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|HttpServletRequest
name|request
init|=
name|davRequest
operator|.
name|getRequest
argument_list|()
decl_stmt|;
comment|// Authentication Tests.
try|try
block|{
name|AuthenticationResult
name|result
init|=
name|httpAuth
operator|.
name|getAuthenticationResult
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|result
operator|!=
literal|null
operator|)
operator|&&
operator|!
name|result
operator|.
name|isAuthenticated
argument_list|()
condition|)
block|{
comment|// Must Authenticate.
name|httpAuth
operator|.
name|challenge
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
literal|"Repository "
operator|+
name|getRepositoryName
argument_list|(
name|davRequest
argument_list|)
argument_list|,
operator|new
name|AuthenticationException
argument_list|(
literal|"User Credentials Invalid"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|AuthenticationException
name|e
parameter_list|)
block|{
name|log
argument_list|(
literal|"Fatal Http Authentication Error."
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Fatal Http Authentication Error."
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|AccountLockedException
name|e
parameter_list|)
block|{
name|httpAuth
operator|.
name|challenge
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
literal|"Repository "
operator|+
name|getRepositoryName
argument_list|(
name|davRequest
argument_list|)
argument_list|,
operator|new
name|AuthenticationException
argument_list|(
literal|"User account is locked"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MustChangePasswordException
name|e
parameter_list|)
block|{
name|httpAuth
operator|.
name|challenge
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
literal|"Repository "
operator|+
name|getRepositoryName
argument_list|(
name|davRequest
argument_list|)
argument_list|,
operator|new
name|AuthenticationException
argument_list|(
literal|"You must change your password."
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|DavServerRequest
name|davRequest
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
comment|// Authorization Tests.
name|HttpServletRequest
name|request
init|=
name|davRequest
operator|.
name|getRequest
argument_list|()
decl_stmt|;
name|boolean
name|isWriteRequest
init|=
name|WebdavMethodUtil
operator|.
name|isWriteMethod
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
decl_stmt|;
name|SecuritySession
name|securitySession
init|=
name|httpAuth
operator|.
name|getSecuritySession
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|permission
init|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
decl_stmt|;
if|if
condition|(
name|isWriteRequest
condition|)
block|{
name|permission
operator|=
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_UPLOAD
expr_stmt|;
block|}
name|AuthorizationResult
name|authzResult
init|=
name|securitySystem
operator|.
name|authorize
argument_list|(
name|securitySession
argument_list|,
name|permission
argument_list|,
name|davRequest
operator|.
name|getPrefix
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|authzResult
operator|.
name|isAuthorized
argument_list|()
condition|)
block|{
if|if
condition|(
name|authzResult
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|log
argument_list|(
literal|"Authorization Denied [ip="
operator|+
name|request
operator|.
name|getRemoteAddr
argument_list|()
operator|+
literal|",isWriteRequest="
operator|+
name|isWriteRequest
operator|+
literal|",permission="
operator|+
name|permission
operator|+
literal|",repo="
operator|+
name|davRequest
operator|.
name|getPrefix
argument_list|()
operator|+
literal|"] : "
operator|+
name|authzResult
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Issue HTTP Challenge.
name|httpAuth
operator|.
name|challenge
argument_list|(
name|request
argument_list|,
name|response
argument_list|,
literal|"Repository "
operator|+
name|getRepositoryName
argument_list|(
name|davRequest
argument_list|)
argument_list|,
operator|new
name|AuthenticationException
argument_list|(
literal|"Authorization Denied."
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|AuthorizationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Fatal Authorization Subsystem Error."
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|// nothing to do
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|ConfigurationNames
operator|.
name|isRepositories
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|updateRepositoryMap
argument_list|()
expr_stmt|;
name|getDavManager
argument_list|()
operator|.
name|removeAllServers
argument_list|()
expr_stmt|;
try|try
block|{
name|initServers
argument_list|(
name|getServletConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DavServerException
name|e
parameter_list|)
block|{
name|log
argument_list|(
literal|"Error restarting WebDAV server after configuration change - service disabled: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

