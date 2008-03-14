begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|servlet
operator|.
name|DavServerRequest
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

begin_comment
comment|/**  * DavServerComponent   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id: DavServerComponent.java 6000 2007-03-04 22:01:49Z joakime $  */
end_comment

begin_interface
specifier|public
interface|interface
name|DavServerComponent
block|{
comment|/** The Plexus ROLE name */
specifier|public
specifier|static
specifier|final
name|String
name|ROLE
init|=
name|DavServerComponent
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Get the Prefix for this server component.      * @return the prefix associated with this component.      */
specifier|public
name|String
name|getPrefix
parameter_list|()
function_decl|;
comment|/**      * Set the prefix for this server component.      * @param prefix the prefix to use.      */
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
function_decl|;
comment|/**      *<p>      * Flag to indicate how the dav server component should treat a GET request against      * a DAV Collection.      *</p>      *       *<p>      * If true, the collection being requested will be searched for an index.html (or index.htm)       * file to serve back, before it defaults to displaying the collection (directory) contents.      *</p>      *       *<p>      * If false, the collection will always be presented in as a list of contents.      *</p>      *         * @return true to use the index.html instead of directory contents.      */
specifier|public
name|boolean
name|isUseIndexHtml
parameter_list|()
function_decl|;
comment|/**      *<p>      * Flag to indicate how the dav server component should treat a GET request against      * a DAV Collection.      *</p>      *       *<p>      * If true, the collection being requested will be searched for an index.html (or index.htm)       * file to serve back, before it defaults to displaying the collection (directory) contents.      *</p>      *       *<p>      * If false, the collection will always be presented in as a list of contents.      *</p>      *         * @param useIndexHtml true to use the index.html instead of directory contents.      */
specifier|public
name|void
name|setUseIndexHtml
parameter_list|(
name|boolean
name|useIndexHtml
parameter_list|)
function_decl|;
comment|/**      * Get the root directory for this server.      *       * @return the root directory for this server.      */
specifier|public
name|File
name|getRootDirectory
parameter_list|()
function_decl|;
comment|/**      * Set the root directory for this server's content.      *       * @param rootDirectory the root directory for this server's content.      */
specifier|public
name|void
name|setRootDirectory
parameter_list|(
name|File
name|rootDirectory
parameter_list|)
function_decl|;
comment|/**      * Add a Server Listener for this server component.      *       * @param listener the listener to add for this component.      */
specifier|public
name|void
name|addListener
parameter_list|(
name|DavServerListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Remove a server listener for this server component.      *       * @param listener the listener to remove.      */
specifier|public
name|void
name|removeListener
parameter_list|(
name|DavServerListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Perform any initialization needed.      *       * @param servletConfig the servlet config that might be needed.      * @throws DavServerException if there was a problem initializing the server component.      */
specifier|public
name|void
name|init
parameter_list|(
name|ServletConfig
name|servletConfig
parameter_list|)
throws|throws
name|DavServerException
function_decl|;
comment|/**      * Performs a simple filesystem check for the specified resource.      *       * @param resource the resource to check for.      * @return true if the resource exists.      */
specifier|public
name|boolean
name|hasResource
parameter_list|(
name|String
name|resource
parameter_list|)
function_decl|;
comment|/**      * Process incoming request.      *       * @param request the incoming request to process.      * @param response the outgoing response to provide.      */
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
function_decl|;
block|}
end_interface

end_unit

