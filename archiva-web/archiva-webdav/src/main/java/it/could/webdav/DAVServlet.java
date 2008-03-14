begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/* ========================================================================== *  *         Copyright (C) 2004-2006, Pier Fumagalli<http://could.it/>         *  *                            All rights reserved.                            *  * ========================================================================== *  *                                                                            *  * Licensed under the  Apache License, Version 2.0  (the "License").  You may *  * not use this file except in compliance with the License.  You may obtain a *  * copy of the License at<http://www.apache.org/licenses/LICENSE-2.0>.       *  *                                                                            *  * Unless  required  by applicable  law or  agreed  to  in writing,  software *  * distributed under the License is distributed on an  "AS IS" BASIS, WITHOUT *  * WARRANTIES OR  CONDITIONS OF ANY KIND, either express or implied.  See the *  * License for the  specific language  governing permissions  and limitations *  * under the License.                                                         *  *                                                                            *  * ========================================================================== */
end_comment

begin_package
package|package
name|it
operator|.
name|could
operator|.
name|webdav
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Servlet
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
name|ServletContext
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
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
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
name|net
operator|.
name|URL
import|;
end_import

begin_comment
comment|/**  *<p>A very simple servlet capable of processing very simple  *<a href="http://www.rfc-editor.org/rfc/rfc2518.txt">WebDAV</a>  * requests.</p>   *  * @author<a href="http://could.it/">Pier Fumagalli</a>  */
end_comment

begin_class
specifier|public
class|class
name|DAVServlet
implements|implements
name|Servlet
implements|,
name|DAVListener
block|{
comment|/**<p>The {@link DAVRepository} configured for this instance.</p> */
specifier|protected
name|DAVRepository
name|repository
init|=
literal|null
decl_stmt|;
comment|/**<p>The {@link DAVLogger} configured for this instance.</p> */
specifier|protected
name|DAVLogger
name|logger
init|=
literal|null
decl_stmt|;
comment|/**<p>The {@link DAVProcessor} configured for this instance.</p> */
specifier|protected
name|DAVProcessor
name|processor
init|=
literal|null
decl_stmt|;
comment|/**<p>The {@link ServletContext} associated with this instance.</p> */
specifier|private
name|ServletContext
name|context
init|=
literal|null
decl_stmt|;
comment|/**<p>The {@link ServletConfig} associated with this instance.</p> */
specifier|private
name|ServletConfig
name|config
init|=
literal|null
decl_stmt|;
comment|/**      *<p>Create a new {@link DAVServlet} instance.</p>      */
specifier|public
name|DAVServlet
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      *<p>Initialize this {@link Servlet} instance.</p>      *       *<p>The only initialization parameter required by this servlet is the      *&quot;<code>rootPath</code>&quot; parameter specifying the path      * of the repository root (either absolute or relative to the configured      * {@link ServletContext}.</p>      *       *<p>If the specified root is relative, it will be considered to      * be relative to the {@link ServletContext} deployment path.</p>      *       *<p>In any case, the specified root must ultimately point to an existing      * directory on a locally-accessible file system.</p>      *       *<p>When set to<code>true</code>, an optional parameter called      *<code>xmlOnly</code> will force this {@link DAVServlet} to use an      * {@link XMLRepository} instead of the default {@link DAVRepository}.</p>      *      *<p>Finally, when set to<code>true</code>, the optional parameter      *<code>debugEnabled</code> will enable logging of method invocation and      * events in the repository.</p>       */
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
comment|/* Remember the configuration instance */
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|config
operator|.
name|getServletContext
argument_list|()
expr_stmt|;
comment|/* Setup logging */
name|boolean
name|debug
init|=
literal|"true"
operator|.
name|equals
argument_list|(
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"debugEnabled"
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|logger
operator|=
operator|new
name|DAVLogger
argument_list|(
name|config
argument_list|,
name|debug
argument_list|)
expr_stmt|;
comment|/* Try to retrieve the WebDAV root path from the configuration */
name|String
name|rootPath
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"rootPath"
argument_list|)
decl_stmt|;
if|if
condition|(
name|rootPath
operator|==
literal|null
condition|)
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Parameter \"rootPath\" not specified"
argument_list|)
throw|;
comment|/* Create repository and processor */
try|try
block|{
name|File
name|root
init|=
operator|new
name|File
argument_list|(
name|rootPath
argument_list|)
decl_stmt|;
comment|// The repository may not be the local filesystem. It may be rooted at "/".
comment|// But then on Windows new File("/").isAbsolute() is false.
name|boolean
name|unixAbsolute
init|=
name|rootPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
name|boolean
name|localAbsolute
init|=
name|root
operator|.
name|isAbsolute
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|unixAbsolute
operator|&&
operator|!
name|localAbsolute
condition|)
block|{
name|URL
name|url
init|=
name|this
operator|.
name|context
operator|.
name|getResource
argument_list|(
literal|"/"
operator|+
name|rootPath
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"file"
operator|.
name|equals
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Invalid root \""
operator|+
name|url
operator|+
literal|"\""
argument_list|)
throw|;
block|}
else|else
block|{
name|root
operator|=
operator|new
name|File
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* Discover the repository implementation at runtime */
name|String
name|repositoryClass
init|=
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"repositoryClass"
argument_list|)
decl_stmt|;
if|if
condition|(
name|repositoryClass
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|repository
operator|=
name|DAVServlet
operator|.
name|newRepository
argument_list|(
name|repositoryClass
argument_list|,
name|root
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// legacy configuration format. keep for now
comment|/* Make sure that we use the correct repository type */
if|if
condition|(
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|config
operator|.
name|getInitParameter
argument_list|(
literal|"xmlOnly"
argument_list|)
argument_list|)
condition|)
block|{
name|this
operator|.
name|repository
operator|=
operator|new
name|XMLRepository
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|repository
operator|=
operator|new
name|DAVRepository
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* Initialize the processor and register ourselves as listeners */
name|this
operator|.
name|processor
operator|=
operator|new
name|DAVProcessor
argument_list|(
name|this
operator|.
name|repository
argument_list|)
expr_stmt|;
name|this
operator|.
name|repository
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|logger
operator|.
name|log
argument_list|(
literal|"Initialized from "
operator|+
name|root
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Can't resolve \""
operator|+
name|rootPath
operator|+
literal|"\""
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Can't initialize repository at \""
operator|+
name|rootPath
operator|+
literal|"\""
decl_stmt|;
throw|throw
operator|new
name|ServletException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|/* Finally, register this repository in the servlet context */
specifier|final
name|String
name|key
init|=
name|getRepositoryKey
argument_list|(
name|config
operator|.
name|getServletName
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|context
operator|.
name|setAttribute
argument_list|(
name|key
argument_list|,
name|this
operator|.
name|repository
argument_list|)
expr_stmt|;
block|}
comment|/**      *<p>Retrieve a {@link DAVRepository} for a given {@link File}.</p>      */
specifier|public
name|DAVRepository
name|getRepository
parameter_list|(
name|File
name|root
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|XMLRepository
argument_list|(
name|root
argument_list|)
return|;
block|}
comment|/**      *<p>Detroy this {@link Servlet} instance.</p>      */
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|this
operator|.
name|repository
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**      *<p>Return the {@link ServletConfig} associated with this instance.</p>      */
specifier|public
name|ServletConfig
name|getServletConfig
parameter_list|()
block|{
return|return
operator|(
name|this
operator|.
name|config
operator|)
return|;
block|}
comment|/**      *<p>Return the {@link ServletContext} associated with this instance.</p>      */
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
operator|(
name|this
operator|.
name|context
operator|)
return|;
block|}
comment|/**      *<p>Return a informative {@link String} about this servlet.</p>      */
specifier|public
name|String
name|getServletInfo
parameter_list|()
block|{
return|return
name|DAVUtilities
operator|.
name|SERVLET_INFORMATION
return|;
block|}
comment|/**      *<p>Execute the current request.</p>      */
specifier|public
name|void
name|service
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|)
throws|throws
name|ServletException
throws|,
name|IOException
block|{
name|HttpServletRequest
name|req
init|=
operator|(
name|HttpServletRequest
operator|)
name|request
decl_stmt|;
name|HttpServletResponse
name|res
init|=
operator|(
name|HttpServletResponse
operator|)
name|response
decl_stmt|;
comment|/* Mark our presence */
name|res
operator|.
name|setHeader
argument_list|(
literal|"Server"
argument_list|,
name|this
operator|.
name|context
operator|.
name|getServerInfo
argument_list|()
operator|+
literal|' '
operator|+
name|DAVUtilities
operator|.
name|SERVLET_SIGNATURE
argument_list|)
expr_stmt|;
comment|/* Normal methods are processed by their individual instances */
name|DAVTransaction
name|transaction
init|=
operator|new
name|DAVTransaction
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
decl_stmt|;
try|try
block|{
name|this
operator|.
name|processor
operator|.
name|process
argument_list|(
name|transaction
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|exception
parameter_list|)
block|{
specifier|final
name|String
name|header
init|=
name|req
operator|.
name|getMethod
argument_list|()
operator|+
literal|' '
operator|+
name|req
operator|.
name|getRequestURI
argument_list|()
operator|+
literal|' '
operator|+
name|req
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
name|this
operator|.
name|context
operator|.
name|log
argument_list|(
literal|"Error processing: "
operator|+
name|header
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|.
name|log
argument_list|(
literal|"Exception processing DAV transaction"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
block|}
comment|/* ====================================================================== */
comment|/* DAV LISTENER INTERFACE IMPLEMENTATION                                  */
comment|/* ====================================================================== */
comment|/**      *<p>Receive notification of an event occurred in a specific      * {@link DAVRepository}.</p>      */
specifier|public
name|void
name|notify
parameter_list|(
name|DAVResource
name|resource
parameter_list|,
name|int
name|event
parameter_list|)
block|{
name|String
name|message
init|=
literal|"Unknown event"
decl_stmt|;
switch|switch
condition|(
name|event
condition|)
block|{
case|case
name|DAVListener
operator|.
name|COLLECTION_CREATED
case|:
name|message
operator|=
literal|"Collection created"
expr_stmt|;
break|break;
case|case
name|DAVListener
operator|.
name|COLLECTION_REMOVED
case|:
name|message
operator|=
literal|"Collection removed"
expr_stmt|;
break|break;
case|case
name|DAVListener
operator|.
name|RESOURCE_CREATED
case|:
name|message
operator|=
literal|"Resource created"
expr_stmt|;
break|break;
case|case
name|DAVListener
operator|.
name|RESOURCE_REMOVED
case|:
name|message
operator|=
literal|"Resource removed"
expr_stmt|;
break|break;
case|case
name|DAVListener
operator|.
name|RESOURCE_MODIFIED
case|:
name|message
operator|=
literal|"Resource modified"
expr_stmt|;
break|break;
block|}
name|this
operator|.
name|logger
operator|.
name|debug
argument_list|(
name|message
operator|+
literal|": \""
operator|+
name|resource
operator|.
name|getRelativePath
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
comment|/* ====================================================================== */
comment|/* CONTEXT METHODS                                                        */
comment|/* ====================================================================== */
comment|/**      *<p>Retrieve the key in the {@link ServletContext} where the instance of      * the {@link DAVRepository} associated with a named {@link DAVServlet}      * can be found.</p>      *       * @param servletName the name of the {@link DAVServlet} as specified in      *                    the<code>web.xml</code> deployment descriptor.</p>      */
specifier|public
specifier|static
name|String
name|getRepositoryKey
parameter_list|(
name|String
name|servletName
parameter_list|)
block|{
if|if
condition|(
name|servletName
operator|==
literal|null
condition|)
throw|throw
operator|new
name|NullPointerException
argument_list|()
throw|;
return|return
name|DAVRepository
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|servletName
return|;
block|}
comment|/** factory for subclasses configured in web.xml       * @param repositoryClass must extend DAVRepository and have a public constructor(File).      *  */
specifier|static
name|DAVRepository
name|newRepository
parameter_list|(
name|String
name|repositoryClass
parameter_list|,
name|File
name|root
parameter_list|)
throws|throws
name|ServletException
block|{
try|try
block|{
name|Class
name|c
init|=
name|Class
operator|.
name|forName
argument_list|(
name|repositoryClass
argument_list|)
decl_stmt|;
name|Constructor
name|ctor
init|=
name|c
operator|.
name|getConstructor
argument_list|(
operator|new
name|Class
index|[]
block|{
name|File
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|DAVRepository
name|repo
init|=
operator|(
name|DAVRepository
operator|)
name|ctor
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
name|root
block|}
argument_list|)
decl_stmt|;
return|return
name|repo
return|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|LinkageError
name|le
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|le
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|ns
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|ns
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|it
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|it
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|ia
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|ia
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|ie
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
name|ie
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

