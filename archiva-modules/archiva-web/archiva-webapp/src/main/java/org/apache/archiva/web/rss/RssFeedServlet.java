begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|rss
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndFeed
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|io
operator|.
name|FeedException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|io
operator|.
name|SyndFeedOutput
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
name|repository
operator|.
name|RepositorySession
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
name|repository
operator|.
name|RepositorySessionFactory
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
name|rss
operator|.
name|processor
operator|.
name|RssFeedProcessor
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
name|codec
operator|.
name|Decoder
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
name|codec
operator|.
name|DecoderException
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
name|codec
operator|.
name|binary
operator|.
name|Base64
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
name|archiva
operator|.
name|security
operator|.
name|AccessDeniedException
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
name|ArchivaRoleConstants
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
name|ArchivaSecurityException
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
name|PrincipalNotFoundException
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
name|security
operator|.
name|UserRepositories
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
name|users
operator|.
name|UserManager
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
name|users
operator|.
name|UserNotFoundException
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
name|HttpServlet
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
comment|/**  * Servlet for handling rss feed requests.  */
end_comment

begin_class
specifier|public
class|class
name|RssFeedServlet
extends|extends
name|HttpServlet
block|{
specifier|public
specifier|static
specifier|final
name|String
name|MIME_TYPE
init|=
literal|"application/rss+xml; charset=UTF-8"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COULD_NOT_GENERATE_FEED_ERROR
init|=
literal|"Could not generate feed"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|COULD_NOT_AUTHENTICATE_USER
init|=
literal|"Could not authenticate user"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USER_NOT_AUTHORIZED
init|=
literal|"User not authorized to access feed."
decl_stmt|;
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RssFeedServlet
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|RssFeedProcessor
name|processor
decl_stmt|;
specifier|private
name|WebApplicationContext
name|wac
decl_stmt|;
specifier|private
name|UserRepositories
name|userRepositories
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
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
specifier|public
name|void
name|init
parameter_list|(
name|javax
operator|.
name|servlet
operator|.
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
name|wac
operator|=
name|WebApplicationContextUtils
operator|.
name|getRequiredWebApplicationContext
argument_list|(
name|servletConfig
operator|.
name|getServletContext
argument_list|()
argument_list|)
expr_stmt|;
name|userRepositories
operator|=
name|wac
operator|.
name|getBean
argument_list|(
name|UserRepositories
operator|.
name|class
argument_list|)
expr_stmt|;
name|servletAuth
operator|=
name|wac
operator|.
name|getBean
argument_list|(
name|ServletAuthenticator
operator|.
name|class
argument_list|)
expr_stmt|;
name|httpAuth
operator|=
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
expr_stmt|;
comment|// TODO: what if there are other types?
name|repositorySessionFactory
operator|=
name|wac
operator|.
name|getBean
argument_list|(
literal|"repositorySessionFactory"
argument_list|,
name|RepositorySessionFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|String
name|repoId
init|=
literal|null
decl_stmt|;
name|String
name|groupId
init|=
literal|null
decl_stmt|;
name|String
name|artifactId
init|=
literal|null
decl_stmt|;
name|String
name|url
init|=
name|StringUtils
operator|.
name|removeEnd
argument_list|(
name|req
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|countMatches
argument_list|(
name|StringUtils
operator|.
name|substringAfter
argument_list|(
name|url
argument_list|,
literal|"feeds/"
argument_list|)
argument_list|,
literal|"/"
argument_list|)
operator|>
literal|0
condition|)
block|{
name|artifactId
operator|=
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|url
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|groupId
operator|=
name|StringUtils
operator|.
name|substringBeforeLast
argument_list|(
name|StringUtils
operator|.
name|substringAfter
argument_list|(
name|url
argument_list|,
literal|"feeds/"
argument_list|)
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|groupId
operator|=
name|StringUtils
operator|.
name|replaceChars
argument_list|(
name|groupId
argument_list|,
literal|'/'
argument_list|,
literal|'.'
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|StringUtils
operator|.
name|countMatches
argument_list|(
name|StringUtils
operator|.
name|substringAfter
argument_list|(
name|url
argument_list|,
literal|"feeds/"
argument_list|)
argument_list|,
literal|"/"
argument_list|)
operator|==
literal|0
condition|)
block|{
name|repoId
operator|=
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|url
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|res
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_BAD_REQUEST
argument_list|,
literal|"Invalid request url."
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|SyndFeed
name|feed
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isAllowed
argument_list|(
name|req
argument_list|,
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|)
condition|)
block|{
if|if
condition|(
name|repoId
operator|!=
literal|null
condition|)
block|{
comment|// new artifacts in repo feed request
name|processor
operator|=
name|wac
operator|.
name|getBean
argument_list|(
literal|"rssFeedProcessor#new-artifacts"
argument_list|,
name|RssFeedProcessor
operator|.
name|class
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RssFeedProcessor
operator|.
name|KEY_REPO_ID
argument_list|,
name|repoId
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
operator|(
name|groupId
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|artifactId
operator|!=
literal|null
operator|)
condition|)
block|{
comment|// TODO: this only works for guest - we could pass in the list of repos
comment|// new versions of artifact feed request
name|processor
operator|=
name|wac
operator|.
name|getBean
argument_list|(
literal|"rssFeedProcessor#new-versions"
argument_list|,
name|RssFeedProcessor
operator|.
name|class
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RssFeedProcessor
operator|.
name|KEY_GROUP_ID
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RssFeedProcessor
operator|.
name|KEY_ARTIFACT_ID
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|res
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|,
name|USER_NOT_AUTHORIZED
argument_list|)
expr_stmt|;
return|return;
block|}
name|RepositorySession
name|repositorySession
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
decl_stmt|;
try|try
block|{
name|feed
operator|=
name|processor
operator|.
name|process
argument_list|(
name|map
argument_list|,
name|repositorySession
operator|.
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|feed
operator|==
literal|null
condition|)
block|{
name|res
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_NO_CONTENT
argument_list|,
literal|"No information available."
argument_list|)
expr_stmt|;
return|return;
block|}
name|res
operator|.
name|setContentType
argument_list|(
name|MIME_TYPE
argument_list|)
expr_stmt|;
if|if
condition|(
name|repoId
operator|!=
literal|null
condition|)
block|{
name|feed
operator|.
name|setLink
argument_list|(
name|req
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
operator|(
name|groupId
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|artifactId
operator|!=
literal|null
operator|)
condition|)
block|{
name|feed
operator|.
name|setLink
argument_list|(
name|req
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|SyndFeedOutput
name|output
init|=
operator|new
name|SyndFeedOutput
argument_list|()
decl_stmt|;
name|output
operator|.
name|output
argument_list|(
name|feed
argument_list|,
name|res
operator|.
name|getWriter
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UserNotFoundException
name|unfe
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|COULD_NOT_AUTHENTICATE_USER
argument_list|,
name|unfe
argument_list|)
expr_stmt|;
name|res
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|,
name|COULD_NOT_AUTHENTICATE_USER
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccountLockedException
name|acce
parameter_list|)
block|{
name|res
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|,
name|COULD_NOT_AUTHENTICATE_USER
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthenticationException
name|authe
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|COULD_NOT_AUTHENTICATE_USER
argument_list|,
name|authe
argument_list|)
expr_stmt|;
name|res
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|,
name|COULD_NOT_AUTHENTICATE_USER
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FeedException
name|ex
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|COULD_NOT_GENERATE_FEED_ERROR
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|res
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_INTERNAL_SERVER_ERROR
argument_list|,
name|COULD_NOT_GENERATE_FEED_ERROR
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MustChangePasswordException
name|e
parameter_list|)
block|{
name|res
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|,
name|COULD_NOT_AUTHENTICATE_USER
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnauthorizedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|repoId
operator|!=
literal|null
condition|)
block|{
name|res
operator|.
name|setHeader
argument_list|(
literal|"WWW-Authenticate"
argument_list|,
literal|"Basic realm=\"Repository Archiva Managed "
operator|+
name|repoId
operator|+
literal|" Repository"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|res
operator|.
name|setHeader
argument_list|(
literal|"WWW-Authenticate"
argument_list|,
literal|"Basic realm=\"Artifact "
operator|+
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
argument_list|)
expr_stmt|;
block|}
name|res
operator|.
name|sendError
argument_list|(
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|,
name|USER_NOT_AUTHORIZED
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Basic authentication.      *      * @param req      * @param repositoryId TODO      * @param groupId      TODO      * @param artifactId   TODO      * @return      */
specifier|private
name|boolean
name|isAllowed
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
throws|throws
name|UserNotFoundException
throws|,
name|AccountLockedException
throws|,
name|AuthenticationException
throws|,
name|MustChangePasswordException
throws|,
name|UnauthorizedException
block|{
name|String
name|auth
init|=
name|req
operator|.
name|getHeader
argument_list|(
literal|"Authorization"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|repoIds
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|repositoryId
operator|!=
literal|null
condition|)
block|{
name|repoIds
operator|.
name|add
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|artifactId
operator|!=
literal|null
operator|&&
name|groupId
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|auth
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|auth
operator|.
name|toUpperCase
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"BASIC "
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Decoder
name|dec
init|=
operator|new
name|Base64
argument_list|()
decl_stmt|;
name|String
name|usernamePassword
init|=
literal|""
decl_stmt|;
try|try
block|{
name|usernamePassword
operator|=
operator|new
name|String
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|dec
operator|.
name|decode
argument_list|(
name|auth
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DecoderException
name|ie
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error decoding username and password."
argument_list|,
name|ie
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|usernamePassword
operator|==
literal|null
operator|||
name|usernamePassword
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|repoIds
operator|=
name|getObservableRepos
argument_list|(
name|UserManager
operator|.
name|GUEST_USERNAME
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
index|[]
name|userCredentials
init|=
name|usernamePassword
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|repoIds
operator|=
name|getObservableRepos
argument_list|(
name|userCredentials
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|repoIds
operator|=
name|getObservableRepos
argument_list|(
name|UserManager
operator|.
name|GUEST_USERNAME
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|String
name|repoId
range|:
name|repoIds
control|)
block|{
try|try
block|{
name|AuthenticationResult
name|result
init|=
name|httpAuth
operator|.
name|getAuthenticationResult
argument_list|(
name|req
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SecuritySession
name|securitySession
init|=
name|httpAuth
operator|.
name|getSecuritySession
argument_list|(
name|req
operator|.
name|getSession
argument_list|(
literal|true
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|servletAuth
operator|.
name|isAuthenticated
argument_list|(
name|req
argument_list|,
name|result
argument_list|)
operator|&&
name|servletAuth
operator|.
name|isAuthorized
argument_list|(
name|req
argument_list|,
name|securitySession
argument_list|,
name|repoId
argument_list|,
name|ArchivaRoleConstants
operator|.
name|OPERATION_REPOSITORY_ACCESS
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|AuthorizationException
name|e
parameter_list|)
block|{
block|}
catch|catch
parameter_list|(
name|UnauthorizedException
name|e
parameter_list|)
block|{
block|}
block|}
throw|throw
operator|new
name|UnauthorizedException
argument_list|(
literal|"Access denied."
argument_list|)
throw|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getObservableRepos
parameter_list|(
name|String
name|principal
parameter_list|)
block|{
try|try
block|{
return|return
name|userRepositories
operator|.
name|getObservableRepositoryIds
argument_list|(
name|principal
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PrincipalNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AccessDeniedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaSecurityException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
end_class

end_unit

