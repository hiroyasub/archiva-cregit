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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|proxy
operator|.
name|configuration
operator|.
name|ProxyConfiguration
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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * Class used to bridge the servlet to the repository proxy implementation.  *  * @author Edwin Punzalan  * @todo the names get() and getRemoteFile() are confusing [!]  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProxyManager
block|{
name|String
name|ROLE
init|=
name|ProxyManager
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Used to retrieve a cached path or retrieve one if the cache does not contain it yet.      *      * @param path the expected repository path      * @return File object referencing the requested path in the cache      * @throws ProxyException                when an exception occurred during the retrieval of the requested path      * @throws ResourceDoesNotExistException when the requested object can't be found in any of the      *                                       configured repositories      */
name|File
name|get
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
function_decl|;
comment|/**      * Used to force remote download of the requested path from any the configured repositories.  This method will      * only bypass the cache for searching but the requested path will still be cached.      *      * @param path the expected repository path      * @return File object referencing the requested path in the cache      * @throws ProxyException                when an exception occurred during the retrieval of the requested path      * @throws ResourceDoesNotExistException when the requested object can't be found in any of the      *                                       configured repositories      */
name|File
name|getRemoteFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ProxyException
throws|,
name|ResourceDoesNotExistException
function_decl|;
comment|/**      * Used by the factory to set the configuration of the proxy      *      * @param config the ProxyConfiguration to set the behavior of the proxy      */
name|void
name|setConfiguration
parameter_list|(
name|ProxyConfiguration
name|config
parameter_list|)
function_decl|;
comment|/**      * Used to retrieve the configuration describing the behavior of the proxy      *      * @return the ProxyConfiguration of this proxy      */
name|ProxyConfiguration
name|getConfiguration
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

