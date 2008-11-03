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
name|DavServletRequest
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
name|maven
operator|.
name|archiva
operator|.
name|security
operator|.
name|ArchivaXworkUser
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
name|ServletAuthenticator
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
name|webdav
operator|.
name|util
operator|.
name|RepositoryPathUtil
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
name|webdav
operator|.
name|util
operator|.
name|WebdavMethodUtil
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
name|UnauthorizedException
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

begin_comment
comment|/**  * @author<a href="mailto:james@atlassian.com">James William Dumay</a>  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaDavSessionProvider
implements|implements
name|DavSessionProvider
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ArchivaDavSessionProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|ServletAuthenticator
name|servletAuth
decl_stmt|;
specifier|private
name|HttpAuthenticator
name|httpAuth
decl_stmt|;
specifier|private
name|ArchivaXworkUser
name|archivaXworkUser
decl_stmt|;
specifier|public
name|ArchivaDavSessionProvider
parameter_list|(
name|ServletAuthenticator
name|servletAuth
parameter_list|,
name|HttpAuthenticator
name|httpAuth
parameter_list|,
name|ArchivaXworkUser
name|archivaXworkUser
parameter_list|)
block|{
name|this
operator|.
name|servletAuth
operator|=
name|servletAuth
expr_stmt|;
name|this
operator|.
name|httpAuth
operator|=
name|httpAuth
expr_stmt|;
name|this
operator|.
name|archivaXworkUser
operator|=
name|archivaXworkUser
expr_stmt|;
block|}
specifier|public
name|boolean
name|attachSession
parameter_list|(
name|WebdavRequest
name|request
parameter_list|)
throws|throws
name|DavException
block|{
specifier|final
name|String
name|repositoryId
init|=
name|RepositoryPathUtil
operator|.
name|getRepositoryName
argument_list|(
name|removeContextPath
argument_list|(
name|request
argument_list|)
argument_list|)
decl_stmt|;
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
literal|null
argument_list|)
decl_stmt|;
comment|//Create a dav session
name|request
operator|.
name|setDavSession
argument_list|(
operator|new
name|ArchivaDavSession
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|servletAuth
operator|.
name|isAuthenticated
argument_list|(
name|request
argument_list|,
name|result
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|AuthenticationException
name|e
parameter_list|)
block|{
name|boolean
name|isPut
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
comment|// safety check for MRM-911
name|String
name|guest
init|=
name|archivaXworkUser
operator|.
name|getGuest
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|servletAuth
operator|.
name|isAuthorized
argument_list|(
name|guest
argument_list|,
operator|(
operator|(
name|ArchivaDavResourceLocator
operator|)
name|request
operator|.
name|getRequestLocator
argument_list|()
operator|)
operator|.
name|getRepositoryId
argument_list|()
argument_list|,
name|isPut
argument_list|)
condition|)
block|{
name|request
operator|.
name|setDavSession
argument_list|(
operator|new
name|ArchivaDavSession
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|UnauthorizedException
name|ae
parameter_list|)
block|{
throw|throw
operator|new
name|UnauthorizedDavException
argument_list|(
name|repositoryId
argument_list|,
literal|"You are not authenticated and authorized to access any repository."
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|UnauthorizedDavException
argument_list|(
name|repositoryId
argument_list|,
literal|"You are not authenticated."
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|MustChangePasswordException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnauthorizedDavException
argument_list|(
name|repositoryId
argument_list|,
literal|"You must change your password."
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|AccountLockedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|UnauthorizedDavException
argument_list|(
name|repositoryId
argument_list|,
literal|"User account is locked."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|releaseSession
parameter_list|(
name|WebdavRequest
name|request
parameter_list|)
block|{
name|request
operator|.
name|setDavSession
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|removeContextPath
parameter_list|(
specifier|final
name|DavServletRequest
name|request
parameter_list|)
block|{
name|String
name|path
init|=
name|request
operator|.
name|getRequestURI
argument_list|()
decl_stmt|;
name|String
name|ctx
init|=
name|request
operator|.
name|getContextPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
name|ctx
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|ctx
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|path
return|;
block|}
block|}
end_class

end_unit

