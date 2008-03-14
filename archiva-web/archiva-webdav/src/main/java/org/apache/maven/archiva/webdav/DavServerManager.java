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
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * DavServerManager   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id: DavServerManager.java 6017 2007-03-06 00:39:53Z joakime $  */
end_comment

begin_interface
specifier|public
interface|interface
name|DavServerManager
block|{
comment|/** The Plexus ROLE name. */
specifier|public
specifier|static
specifier|final
name|String
name|ROLE
init|=
name|DavServerManager
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Create a DavServerComponent and start tracking it.       *       * @param prefix the prefix for this component.      * @param rootDirectory the root directory for this component's content.  null to not set a root directory.      * @return the created component, suitable for use.      * @throws DavServerException       */
specifier|public
name|DavServerComponent
name|createServer
parameter_list|(
name|String
name|prefix
parameter_list|,
name|File
name|rootDirectory
parameter_list|)
throws|throws
name|DavServerException
function_decl|;
comment|/**      * Get the collection of tracked servers.      *       * @return Collection of {@link DavServerComponent} objects.      */
specifier|public
name|Collection
name|getServers
parameter_list|()
function_decl|;
comment|/**      * Removes a specific server from the tracked list of servers.      *       * NOTE: This does not remove the associated files on disk, merely the reference being tracked.      *       * @param prefix the prefix to remove.      */
specifier|public
name|void
name|removeServer
parameter_list|(
name|String
name|prefix
parameter_list|)
function_decl|;
comment|/**      * Get the {@link DavServerComponent} associated with the specified prefix.      *       * @param prefix the prefix for the dav server component to use.      * @return the DavServerComponent, or null if not found.      */
specifier|public
name|DavServerComponent
name|getServer
parameter_list|(
name|String
name|prefix
parameter_list|)
function_decl|;
comment|/**      * Remove all servers being tracked by the manager.      */
specifier|public
name|void
name|removeAllServers
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

