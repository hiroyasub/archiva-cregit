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
operator|.
name|connector
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
name|repository
operator|.
name|ManagedRepository
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
name|RemoteRepository
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
comment|/**  *  * A RepositoryConnector maps a managed repository to a remote repository.  *  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryConnector
block|{
comment|/**      * Returns the local repository that is connected to the remote.      * @return The local managed repository.      */
name|ManagedRepository
name|getSourceRepository
parameter_list|()
function_decl|;
comment|/**      * Returns the remote repository that is connected to the local.      * @return The remote repository.      */
name|RemoteRepository
name|getTargetRepository
parameter_list|()
function_decl|;
comment|/**      * Returns a list of paths that are not fetched from the remote repository.      * @return A list of paths.      */
name|List
argument_list|<
name|String
argument_list|>
name|getBlacklist
parameter_list|()
function_decl|;
comment|/**      * Returns a list of paths that are fetched from the remote repository, even if a      * parent path is in the blacklist.      *      * @return The list of paths.      */
name|List
argument_list|<
name|String
argument_list|>
name|getWhitelist
parameter_list|()
function_decl|;
comment|/**      * Returns true, if this connector is enabled, otherwise false.      * @return True, if enabled.      */
name|boolean
name|isEnabled
parameter_list|()
function_decl|;
comment|/**      * Enables this connector, if it was disabled before, otherwise does nothing.      */
name|void
name|enable
parameter_list|()
function_decl|;
comment|/**      * Disables this connector, if it was enabled before, otherwise does nothing.      */
name|void
name|disable
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

