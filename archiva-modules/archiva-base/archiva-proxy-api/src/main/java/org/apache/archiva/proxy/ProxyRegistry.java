begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
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
name|archiva
operator|.
name|proxy
operator|.
name|model
operator|.
name|NetworkProxy
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
name|proxy
operator|.
name|model
operator|.
name|ProxyConnector
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
name|proxy
operator|.
name|model
operator|.
name|RepositoryProxyHandler
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
name|repository
operator|.
name|RepositoryType
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
comment|/**  * A proxy registry is central access point for accessing a proxy. It gives access to the proxy handlers  * that are registered for the different repository types.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ProxyRegistry
block|{
comment|/**      * Returns the network proxy that is configured for the given id (repository id).      *      * @param id The proxy id      * @return The network proxy object if defined, otherwise null.      */
name|NetworkProxy
name|getNetworkProxy
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Returns a map that contains a list of repository handlers for each repository type.      * @return The map with the repository type as key and a list of handler objects as value.      */
name|Map
argument_list|<
name|RepositoryType
argument_list|,
name|List
argument_list|<
name|RepositoryProxyHandler
argument_list|>
argument_list|>
name|getAllHandler
parameter_list|()
function_decl|;
comment|/**      * Returns the repository handler that are defined for the given repository type.      *      * @param type The repository type      * @return Returns the list of the handler objects, or a empty list, if none defined.      */
name|List
argument_list|<
name|RepositoryProxyHandler
argument_list|>
name|getHandler
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
function_decl|;
comment|/**      * Returns true, if there are proxy handler registered for the given type.      *      * @param type The repository type      * @return True, if a handler is registered, otherwise false.      */
name|boolean
name|hasHandler
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
function_decl|;
comment|/**      * Returns the list of all proxy connectors.      * @return      */
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|model
operator|.
name|ProxyConnector
argument_list|>
name|getProxyConnectors
parameter_list|( )
function_decl|;
comment|/**      * Returns a map of connector lists with the source repository id as key      * @return A map with source repository ids as key and list of corresponding proxy connector objects as value.      */
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|>
name|getProxyConnectorAsMap
parameter_list|( )
function_decl|;
comment|/**      * Reloads the proxies from the configuration.      */
name|void
name|reload
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

