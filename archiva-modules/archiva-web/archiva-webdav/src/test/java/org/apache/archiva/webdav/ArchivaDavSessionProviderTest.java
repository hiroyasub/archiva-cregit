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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|authentication
operator|.
name|AuthenticationException
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
name|policy
operator|.
name|AccountLockedException
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
name|policy
operator|.
name|MustChangePasswordException
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
name|users
operator|.
name|User
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
name|archiva
operator|.
name|redback
operator|.
name|authentication
operator|.
name|AuthenticationDataSource
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
name|authentication
operator|.
name|AuthenticationResult
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
name|authorization
operator|.
name|AuthorizationException
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
name|javax
operator|.
name|servlet
operator|.
name|RequestDispatcher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletInputStream
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
name|Cookie
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpSession
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
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
name|Locale
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

begin_class
specifier|public
class|class
name|ArchivaDavSessionProviderTest
extends|extends
name|TestCase
block|{
specifier|private
name|DavSessionProvider
name|sessionProvider
decl_stmt|;
specifier|private
name|WebdavRequest
name|request
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|sessionProvider
operator|=
operator|new
name|ArchivaDavSessionProvider
argument_list|(
operator|new
name|ServletAuthenticatorMock
argument_list|()
argument_list|,
operator|new
name|HttpAuthenticatorMock
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|WebdavRequestImpl
argument_list|(
operator|new
name|HttpServletRequestMock
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAttachSession
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|request
operator|.
name|getDavSession
argument_list|()
argument_list|)
expr_stmt|;
name|sessionProvider
operator|.
name|attachSession
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|request
operator|.
name|getDavSession
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReleaseSession
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNull
argument_list|(
name|request
operator|.
name|getDavSession
argument_list|()
argument_list|)
expr_stmt|;
name|sessionProvider
operator|.
name|attachSession
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|request
operator|.
name|getDavSession
argument_list|()
argument_list|)
expr_stmt|;
name|sessionProvider
operator|.
name|releaseSession
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|request
operator|.
name|getDavSession
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
class|class
name|HttpServletRequestMock
implements|implements
name|HttpServletRequest
block|{
specifier|public
name|Object
name|getAttribute
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|Enumeration
name|getAttributeNames
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getCharacterEncoding
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|int
name|getContentLength
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|ServletInputStream
name|getInputStream
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getLocalAddr
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|int
name|getLocalPort
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|Locale
name|getLocale
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|Enumeration
name|getLocales
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getParameter
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|Map
name|getParameterMap
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|Enumeration
name|getParameterNames
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
index|[]
name|getParameterValues
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getProtocol
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|BufferedReader
name|getReader
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getRealPath
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getRemoteAddr
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getRemoteHost
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|int
name|getRemotePort
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|RequestDispatcher
name|getRequestDispatcher
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
literal|""
return|;
block|}
specifier|public
name|String
name|getServerName
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|int
name|getServerPort
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|boolean
name|isSecure
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|void
name|removeAttribute
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setAttribute
parameter_list|(
name|String
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setCharacterEncoding
parameter_list|(
name|String
name|arg0
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getAuthType
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
literal|"/"
return|;
block|}
specifier|public
name|Cookie
index|[]
name|getCookies
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|long
name|getDateHeader
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getHeader
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
specifier|public
name|Enumeration
name|getHeaderNames
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|Enumeration
name|getHeaders
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|int
name|getIntHeader
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getPathInfo
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getPathTranslated
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getQueryString
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getRemoteUser
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getRequestURI
parameter_list|()
block|{
return|return
literal|"/"
return|;
block|}
specifier|public
name|StringBuffer
name|getRequestURL
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getRequestedSessionId
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getServletPath
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|HttpSession
name|getSession
parameter_list|(
name|boolean
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|HttpSession
name|getSession
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|Principal
name|getUserPrincipal
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|boolean
name|isRequestedSessionIdFromCookie
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|boolean
name|isRequestedSessionIdFromURL
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|boolean
name|isRequestedSessionIdFromUrl
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|boolean
name|isRequestedSessionIdValid
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
specifier|public
name|boolean
name|isUserInRole
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported yet."
argument_list|)
throw|;
block|}
block|}
specifier|private
class|class
name|ServletAuthenticatorMock
implements|implements
name|ServletAuthenticator
block|{
specifier|public
name|boolean
name|isAuthenticated
parameter_list|(
name|HttpServletRequest
name|arg0
parameter_list|,
name|AuthenticationResult
name|arg1
parameter_list|)
throws|throws
name|AuthenticationException
throws|,
name|AccountLockedException
throws|,
name|MustChangePasswordException
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|SecuritySession
name|securitySession
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|permission
parameter_list|)
throws|throws
name|AuthorizationException
throws|,
name|UnauthorizedException
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|isAuthorized
parameter_list|(
name|String
name|principal
parameter_list|,
name|String
name|repoId
parameter_list|,
name|String
name|permission
parameter_list|)
throws|throws
name|UnauthorizedException
block|{
return|return
literal|true
return|;
block|}
block|}
specifier|private
class|class
name|HttpAuthenticatorMock
extends|extends
name|HttpAuthenticator
block|{
annotation|@
name|Override
specifier|public
name|void
name|challenge
parameter_list|(
name|HttpServletRequest
name|arg0
parameter_list|,
name|HttpServletResponse
name|arg1
parameter_list|,
name|String
name|arg2
parameter_list|,
name|AuthenticationException
name|arg3
parameter_list|)
throws|throws
name|IOException
block|{
comment|//Do nothing
block|}
annotation|@
name|Override
specifier|public
name|AuthenticationResult
name|getAuthenticationResult
parameter_list|(
name|HttpServletRequest
name|arg0
parameter_list|,
name|HttpServletResponse
name|arg1
parameter_list|)
throws|throws
name|AuthenticationException
throws|,
name|AccountLockedException
throws|,
name|MustChangePasswordException
block|{
return|return
operator|new
name|AuthenticationResult
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|AuthenticationResult
name|authenticate
parameter_list|(
name|AuthenticationDataSource
name|arg0
parameter_list|,
name|HttpSession
name|httpSession
parameter_list|)
throws|throws
name|AuthenticationException
throws|,
name|AccountLockedException
throws|,
name|MustChangePasswordException
block|{
return|return
operator|new
name|AuthenticationResult
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|authenticate
parameter_list|(
name|HttpServletRequest
name|arg0
parameter_list|,
name|HttpServletResponse
name|arg1
parameter_list|)
throws|throws
name|AuthenticationException
block|{
comment|//Do nothing
block|}
annotation|@
name|Override
specifier|public
name|SecuritySession
name|getSecuritySession
parameter_list|(
name|HttpSession
name|httpSession
parameter_list|)
block|{
return|return
name|super
operator|.
name|getSecuritySession
argument_list|(
name|httpSession
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|User
name|getSessionUser
parameter_list|(
name|HttpSession
name|httpSession
parameter_list|)
block|{
return|return
name|super
operator|.
name|getSessionUser
argument_list|(
name|httpSession
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isAlreadyAuthenticated
parameter_list|(
name|HttpSession
name|httpSession
parameter_list|)
block|{
return|return
name|super
operator|.
name|isAlreadyAuthenticated
argument_list|(
name|httpSession
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

