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
name|rss
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|Map
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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rss
operator|.
name|RssFeedGenerator
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
name|codehaus
operator|.
name|plexus
operator|.
name|spring
operator|.
name|PlexusToSpringUtils
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

begin_comment
comment|/**  * Servlet for handling rss feed requests.  *   * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  * @version  */
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
literal|"application/xml; charset=UTF-8"
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
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RssFeedGenerator
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
name|log
operator|.
name|info
argument_list|(
literal|"Request URL: "
operator|+
name|req
operator|.
name|getRequestURL
argument_list|()
argument_list|)
expr_stmt|;
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
name|req
operator|.
name|getParameter
argument_list|(
literal|"repoId"
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isAuthorized
argument_list|()
condition|)
block|{
comment|// new artifacts in repo feed request
name|processor
operator|=
operator|(
name|RssFeedProcessor
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|RssFeedProcessor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"new-artifacts"
argument_list|)
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
name|req
operator|.
name|getParameter
argument_list|(
literal|"repoId"
argument_list|)
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
name|SC_UNAUTHORIZED
argument_list|,
literal|"Request is not authorized."
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
if|else if
condition|(
operator|(
name|req
operator|.
name|getParameter
argument_list|(
literal|"groupId"
argument_list|)
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|req
operator|.
name|getParameter
argument_list|(
literal|"artifactId"
argument_list|)
operator|!=
literal|null
operator|)
condition|)
block|{
if|if
condition|(
name|isAuthorized
argument_list|()
condition|)
block|{
comment|// new versions of artifact feed request
name|processor
operator|=
operator|(
name|RssFeedProcessor
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|RssFeedProcessor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"new-versions"
argument_list|)
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
name|req
operator|.
name|getParameter
argument_list|(
literal|"groupId"
argument_list|)
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
name|req
operator|.
name|getParameter
argument_list|(
literal|"artifactId"
argument_list|)
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
name|SC_UNAUTHORIZED
argument_list|,
literal|"Request is not authorized."
argument_list|)
expr_stmt|;
return|return;
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
name|SC_BAD_REQUEST
argument_list|,
literal|"Required fields not found in request."
argument_list|)
expr_stmt|;
return|return;
block|}
name|feed
operator|=
name|processor
operator|.
name|process
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|res
operator|.
name|setContentType
argument_list|(
name|MIME_TYPE
argument_list|)
expr_stmt|;
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
name|FeedException
name|ex
parameter_list|)
block|{
name|String
name|msg
init|=
name|COULD_NOT_GENERATE_FEED_ERROR
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|msg
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
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isAuthorized
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

