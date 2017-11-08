begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
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
comment|/**  * This represents a repository that is not fully managed by archiva. Its some kind of proxy that  * forwards requests to the remote repository and is able to cache artifacts locally.  */
end_comment

begin_interface
specifier|public
interface|interface
name|RemoteRepository
extends|extends
name|Repository
block|{
comment|/**      * Returns the interface to access the content of the repository.      * @return      */
name|RemoteRepositoryContent
name|getContent
parameter_list|()
function_decl|;
comment|/**      * Returns the credentials used to login to the remote repository.      * @return the credentials, null if not set.      */
name|RepositoryCredentials
name|getLoginCredentials
parameter_list|()
function_decl|;
comment|/**      * Returns the path relative to the root url of the repository that should be used      * to check the availability of the repository.      * @return The check path, null if not set.      */
name|String
name|getCheckPath
parameter_list|()
function_decl|;
comment|/**      * Returns additional parameters, that are used for accessing the remote repository.      * @return A map of key, value pairs.      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraParameters
parameter_list|()
function_decl|;
comment|/**      * Returns extra headers that are added to the request to the remote repository.      * @return      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraHeaders
parameter_list|()
function_decl|;
comment|/**      * Returns the time duration, after that the request is aborted and a error is returned, if the remote repository      * does not respond.      * @return The timeout.      */
name|Duration
name|getTimeout
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

