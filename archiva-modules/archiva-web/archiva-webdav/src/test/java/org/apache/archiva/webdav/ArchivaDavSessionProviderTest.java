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
name|apache
operator|.
name|archiva
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
name|system
operator|.
name|SecuritySession
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
name|test
operator|.
name|utils
operator|.
name|ArchivaBlockJUnit4ClassRunner
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
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mock
operator|.
name|web
operator|.
name|MockHttpServletRequest
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
name|IOException
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaBlockJUnit4ClassRunner
operator|.
name|class
argument_list|)
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
annotation|@
name|Before
specifier|public
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
name|MockHttpServletRequest
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
annotation|@
name|Test
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
comment|/*     private class HttpServletRequestMock         implements HttpServletRequest     {          @Override         public long getContentLengthLong()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String changeSessionId()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public<T extends HttpUpgradeHandler> T upgrade( Class<T> handlerClass )             throws IOException, ServletException         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public boolean authenticate( HttpServletResponse httpServletResponse )             throws IOException, ServletException         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public void login( String s, String s1 )             throws ServletException         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public void logout()             throws ServletException         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public Collection<Part> getParts()             throws IOException, ServletException         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public Part getPart( String s )             throws IOException, ServletException         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public ServletContext getServletContext()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public AsyncContext startAsync()             throws IllegalStateException         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public AsyncContext startAsync( ServletRequest servletRequest, ServletResponse servletResponse )             throws IllegalStateException         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public boolean isAsyncStarted()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public boolean isAsyncSupported()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public AsyncContext getAsyncContext()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public DispatcherType getDispatcherType()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public Object getAttribute( String arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public Enumeration getAttributeNames()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getCharacterEncoding()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public int getContentLength()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getContentType()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public ServletInputStream getInputStream()             throws IOException         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getLocalAddr()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getLocalName()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public int getLocalPort()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public Locale getLocale()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public Enumeration getLocales()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getParameter( String arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public Map getParameterMap()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public Enumeration getParameterNames()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String[] getParameterValues( String arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getProtocol()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public BufferedReader getReader()             throws IOException         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getRealPath( String arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getRemoteAddr()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getRemoteHost()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public int getRemotePort()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public RequestDispatcher getRequestDispatcher( String arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getScheme()         {             return "";         }          @Override         public String getServerName()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public int getServerPort()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public boolean isSecure()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public void removeAttribute( String arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public void setAttribute( String arg0, Object arg1 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public void setCharacterEncoding( String arg0 )             throws UnsupportedEncodingException         {             throw new UnsupportedOperationException( "Not supported yet." );         }           @Override         public String getAuthType()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getContextPath()         {             return "/";         }          @Override         public Cookie[] getCookies()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public long getDateHeader( String arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getHeader( String arg0 )         {             return "";         }          @Override         public Enumeration getHeaderNames()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public Enumeration getHeaders( String arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public int getIntHeader( String arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getMethod()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getPathInfo()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getPathTranslated()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getQueryString()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getRemoteUser()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getRequestURI()         {             return "/";         }          @Override         public StringBuffer getRequestURL()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getRequestedSessionId()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public String getServletPath()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public HttpSession getSession( boolean arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public HttpSession getSession()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public Principal getUserPrincipal()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public boolean isRequestedSessionIdFromCookie()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public boolean isRequestedSessionIdFromURL()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public boolean isRequestedSessionIdFromUrl()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public boolean isRequestedSessionIdValid()         {             throw new UnsupportedOperationException( "Not supported yet." );         }          @Override         public boolean isUserInRole( String arg0 )         {             throw new UnsupportedOperationException( "Not supported yet." );         }     }      */
specifier|private
class|class
name|ServletAuthenticatorMock
implements|implements
name|ServletAuthenticator
block|{
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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

