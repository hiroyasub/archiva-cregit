begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webdav
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
name|ManagedRepository
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
name|security
operator|.
name|ServletAuthenticator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavLocatorFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavMethods
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavResourceFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavServletResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|DavSessionProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|WebdavRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|WebdavRequestImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|WebdavResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|WebdavResponseImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|webdav
operator|.
name|server
operator|.
name|AbstractWebdavServlet
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
name|ConfigurationEvent
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
name|ConfigurationListener
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
name|context
operator|.
name|ConfigurableApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|context
operator|.
name|WebApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|context
operator|.
name|support
operator|.
name|WebApplicationContextUtils
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
name|Map
import|;
end_import

begin_comment
comment|/**  * RepositoryServlet  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServlet
extends|extends
name|AbstractWebdavServlet
implements|implements
name|ConfigurationListener
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RepositoryServlet
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepository
argument_list|>
name|repositoryMap
decl_stmt|;
specifier|private
name|DavLocatorFactory
name|locatorFactory
decl_stmt|;
specifier|private
name|DavResourceFactory
name|resourceFactory
decl_stmt|;
specifier|private
name|DavSessionProvider
name|sessionProvider
decl_stmt|;
specifier|private
specifier|final
name|Object
name|reloadLock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|ServletException
block|{
name|super
operator|.
name|init
argument_list|(
name|servletConfig
argument_list|)
expr_stmt|;
try|try
block|{
name|initServers
argument_list|(
name|servletConfig
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Service the given request. This method has been overridden and copy/pasted to allow better exception handling and      * to support different realms      *      * @param request      * @param response      * @throws ServletException      * @throws java.io.IOException      */
annotation|@
name|Override
specifier|protected
name|void
name|service
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|WebdavRequest
name|webdavRequest
init|=
operator|new
name|WebdavRequestImpl
argument_list|(
name|request
argument_list|,
name|getLocatorFactory
argument_list|()
argument_list|)
decl_stmt|;
comment|// DeltaV requires 'Cache-Control' header for all methods except 'VERSION-CONTROL' and 'REPORT'.
name|int
name|methodCode
init|=
name|DavMethods
operator|.
name|getMethodCode
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|noCache
init|=
name|DavMethods
operator|.
name|isDeltaVMethod
argument_list|(
name|webdavRequest
argument_list|)
operator|&&
operator|!
operator|(
name|DavMethods
operator|.
name|DAV_VERSION_CONTROL
operator|==
name|methodCode
operator|||
name|DavMethods
operator|.
name|DAV_REPORT
operator|==
name|methodCode
operator|)
decl_stmt|;
name|WebdavResponse
name|webdavResponse
init|=
operator|new
name|WebdavResponseImpl
argument_list|(
name|response
argument_list|,
name|noCache
argument_list|)
decl_stmt|;
name|DavResource
name|resource
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// make sure there is a authenticated user
if|if
condition|(
operator|!
name|getDavSessionProvider
argument_list|()
operator|.
name|attachSession
argument_list|(
name|webdavRequest
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// check matching if=header for lock-token relevant operations
name|resource
operator|=
name|getResourceFactory
argument_list|()
operator|.
name|createResource
argument_list|(
name|webdavRequest
operator|.
name|getRequestLocator
argument_list|()
argument_list|,
name|webdavRequest
argument_list|,
name|webdavResponse
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isPreconditionValid
argument_list|(
name|webdavRequest
argument_list|,
name|resource
argument_list|)
condition|)
block|{
name|webdavResponse
operator|.
name|sendError
argument_list|(
name|DavServletResponse
operator|.
name|SC_PRECONDITION_FAILED
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|execute
argument_list|(
name|webdavRequest
argument_list|,
name|webdavResponse
argument_list|,
name|methodCode
argument_list|,
name|resource
argument_list|)
condition|)
block|{
name|super
operator|.
name|service
argument_list|(
name|request
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|UnauthorizedDavException
name|e
parameter_list|)
block|{
name|webdavResponse
operator|.
name|setHeader
argument_list|(
literal|"WWW-Authenticate"
argument_list|,
name|getAuthenticateHeaderValue
argument_list|(
name|e
operator|.
name|getRepositoryName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|webdavResponse
operator|.
name|sendError
argument_list|(
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|,
name|e
operator|.
name|getStatusPhrase
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BrowserRedirectException
name|e
parameter_list|)
block|{
name|response
operator|.
name|sendRedirect
argument_list|(
name|e
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DavException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getErrorCode
argument_list|()
operator|==
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
condition|)
block|{
specifier|final
name|String
name|msg
init|=
literal|"Should throw "
operator|+
name|UnauthorizedDavException
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|webdavResponse
operator|.
name|sendError
argument_list|(
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|webdavResponse
operator|.
name|sendError
argument_list|(
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|webdavResponse
operator|.
name|sendError
argument_list|(
name|e
operator|.
name|getErrorCode
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|getDavSessionProvider
argument_list|()
operator|.
name|releaseSession
argument_list|(
name|webdavRequest
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|initServers
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
name|WebApplicationContext
name|wac
init|=
name|WebApplicationContextUtils
operator|.
name|getRequiredWebApplicationContext
argument_list|(
name|servletConfig
operator|.
name|getServletContext
argument_list|()
argument_list|)
decl_stmt|;
name|configuration
operator|=
name|wac
operator|.
name|getBean
argument_list|(
literal|"archivaConfiguration#default"
argument_list|,
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|=
name|wac
operator|.
name|getBean
argument_list|(
name|ManagedRepositoryAdmin
operator|.
name|class
argument_list|)
expr_stmt|;
name|repositoryMap
operator|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
expr_stmt|;
for|for
control|(
name|ManagedRepository
name|repo
range|:
name|repositoryMap
operator|.
name|values
argument_list|()
control|)
block|{
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
name|repo
operator|.
name|getLocation
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
name|repo
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
name|resourceFactory
operator|=
name|wac
operator|.
name|getBean
argument_list|(
literal|"davResourceFactory#archiva"
argument_list|,
name|DavResourceFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|locatorFactory
operator|=
operator|new
name|ArchivaDavLocatorFactory
argument_list|()
expr_stmt|;
name|ServletAuthenticator
name|servletAuth
init|=
name|wac
operator|.
name|getBean
argument_list|(
name|ServletAuthenticator
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpAuthenticator
name|httpAuth
init|=
name|wac
operator|.
name|getBean
argument_list|(
literal|"httpAuthenticator#basic"
argument_list|,
name|HttpAuthenticator
operator|.
name|class
argument_list|)
decl_stmt|;
name|sessionProvider
operator|=
operator|new
name|ArchivaDavSessionProvider
argument_list|(
name|servletAuth
argument_list|,
name|httpAuth
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"initServers done"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|configurationEvent
parameter_list|(
name|ConfigurationEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|ConfigurationEvent
operator|.
name|SAVED
condition|)
block|{
try|try
block|{
name|initRepositories
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|void
name|initRepositories
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
synchronized|synchronized
init|(
name|repositoryMap
init|)
block|{
name|repositoryMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|repositoryMap
operator|.
name|putAll
argument_list|(
name|managedRepositoryAdmin
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|reloadLock
init|)
block|{
name|initServers
argument_list|(
name|getServletConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|synchronized
name|ManagedRepository
name|getRepository
parameter_list|(
name|String
name|prefix
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
if|if
condition|(
name|repositoryMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|repositoryMap
operator|.
name|putAll
argument_list|(
name|managedRepositoryAdmin
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|repositoryMap
operator|.
name|get
argument_list|(
name|prefix
argument_list|)
return|;
block|}
name|ArchivaConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
specifier|protected
name|boolean
name|isPreconditionValid
parameter_list|(
specifier|final
name|WebdavRequest
name|request
parameter_list|,
specifier|final
name|DavResource
name|davResource
parameter_list|)
block|{
comment|// check for read or write access to the resource when resource-based permission is implemented
return|return
literal|true
return|;
block|}
specifier|public
name|DavSessionProvider
name|getDavSessionProvider
parameter_list|()
block|{
return|return
name|sessionProvider
return|;
block|}
specifier|public
name|void
name|setDavSessionProvider
parameter_list|(
specifier|final
name|DavSessionProvider
name|davSessionProvider
parameter_list|)
block|{
name|this
operator|.
name|sessionProvider
operator|=
name|davSessionProvider
expr_stmt|;
block|}
specifier|public
name|DavLocatorFactory
name|getLocatorFactory
parameter_list|()
block|{
return|return
name|locatorFactory
return|;
block|}
specifier|public
name|void
name|setLocatorFactory
parameter_list|(
specifier|final
name|DavLocatorFactory
name|davLocatorFactory
parameter_list|)
block|{
name|locatorFactory
operator|=
name|davLocatorFactory
expr_stmt|;
block|}
specifier|public
name|DavResourceFactory
name|getResourceFactory
parameter_list|()
block|{
return|return
name|resourceFactory
return|;
block|}
specifier|public
name|void
name|setResourceFactory
parameter_list|(
specifier|final
name|DavResourceFactory
name|davResourceFactory
parameter_list|)
block|{
name|resourceFactory
operator|=
name|davResourceFactory
expr_stmt|;
block|}
specifier|public
name|String
name|getAuthenticateHeaderValue
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|String
name|getAuthenticateHeaderValue
parameter_list|(
name|String
name|repository
parameter_list|)
block|{
return|return
literal|"Basic realm=\"Repository Archiva Managed "
operator|+
name|repository
operator|+
literal|" Repository\""
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|configuration
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|resourceFactory
operator|=
literal|null
expr_stmt|;
name|configuration
operator|=
literal|null
expr_stmt|;
name|locatorFactory
operator|=
literal|null
expr_stmt|;
name|sessionProvider
operator|=
literal|null
expr_stmt|;
name|repositoryMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|repositoryMap
operator|=
literal|null
expr_stmt|;
name|WebApplicationContext
name|wac
init|=
name|WebApplicationContextUtils
operator|.
name|getRequiredWebApplicationContext
argument_list|(
name|getServletContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|wac
operator|instanceof
name|ConfigurableApplicationContext
condition|)
block|{
operator|(
operator|(
name|ConfigurableApplicationContext
operator|)
name|wac
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

