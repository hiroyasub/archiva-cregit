begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|proxy
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *     http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|wagon
operator|.
name|ResourceDoesNotExistException
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
name|wagon
operator|.
name|proxy
operator|.
name|ProxyInfo
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * An individual request handler for the proxy.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProxyRequestHandler
block|{
comment|/**      * The Plexus role of the component.      */
name|String
name|ROLE
init|=
name|ProxyRequestHandler
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Used to retrieve an artifact at a particular path, giving the cached version if it exists.      *      * @param path                the expected repository path      * @param proxiedRepositories the repositories being proxied to      * @param managedRepository   the locally managed repository to cache artifacts in      * @return File object referencing the requested path in the cache      * @throws ProxyException when an exception occurred during the retrieval of the requested path      * @throws org.apache.maven.wagon.ResourceDoesNotExistException      *                        when the requested object can't be found in any of the      *                        configured repositories      */
name|File
name|get
parameter_list|(
name|String
name|path
parameter_list|,
name|List
name|proxiedRepositories
parameter_list|,
name|ArtifactRepository
name|managedRepository
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
function_decl|;
comment|/**      * Used to retrieve an artifact at a particular path, giving the cached version if it exists.      *      * @param path                the expected repository path      * @param proxiedRepositories the repositories being proxied to      * @param managedRepository   the locally managed repository to cache artifacts in      * @param wagonProxy          a network proxy to use when transferring files if needed      * @return File object referencing the requested path in the cache      * @throws ProxyException when an exception occurred during the retrieval of the requested path      * @throws org.apache.maven.wagon.ResourceDoesNotExistException      *                        when the requested object can't be found in any of the      *                        configured repositories      */
name|File
name|get
parameter_list|(
name|String
name|path
parameter_list|,
name|List
name|proxiedRepositories
parameter_list|,
name|ArtifactRepository
name|managedRepository
parameter_list|,
name|ProxyInfo
name|wagonProxy
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
function_decl|;
comment|/**      * Used to force remote download of the requested path from any the configured repositories.  This method will      * only bypass the cache for searching but the requested path will still be cached.      *      * @param path                the expected repository path      * @param proxiedRepositories the repositories being proxied to      * @param managedRepository   the locally managed repository to cache artifacts in      * @return File object referencing the requested path in the cache      * @throws ProxyException when an exception occurred during the retrieval of the requested path      * @throws org.apache.maven.wagon.ResourceDoesNotExistException      *                        when the requested object can't be found in any of the      *                        configured repositories      */
name|File
name|getAlways
parameter_list|(
name|String
name|path
parameter_list|,
name|List
name|proxiedRepositories
parameter_list|,
name|ArtifactRepository
name|managedRepository
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
function_decl|;
comment|/**      * Used to force remote download of the requested path from any the configured repositories.  This method will      * only bypass the cache for searching but the requested path will still be cached.      *      * @param path                the expected repository path      * @param proxiedRepositories the repositories being proxied to      * @param managedRepository   the locally managed repository to cache artifacts in      * @param wagonProxy          a network proxy to use when transferring files if needed      * @return File object referencing the requested path in the cache      * @throws ProxyException when an exception occurred during the retrieval of the requested path      * @throws org.apache.maven.wagon.ResourceDoesNotExistException      *                        when the requested object can't be found in any of the      *                        configured repositories      */
name|File
name|getAlways
parameter_list|(
name|String
name|path
parameter_list|,
name|List
name|proxiedRepositories
parameter_list|,
name|ArtifactRepository
name|managedRepository
parameter_list|,
name|ProxyInfo
name|wagonProxy
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
function_decl|;
block|}
end_interface

end_unit

