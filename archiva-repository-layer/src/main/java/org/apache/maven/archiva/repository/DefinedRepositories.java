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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * DefinedRepositories - maintains the list of defined repositories.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|DefinedRepositories
block|{
comment|/**      * Get the entire list of repositories.      *       * @return the list of repositories.      */
specifier|public
name|List
name|getAllRepositories
parameter_list|()
function_decl|;
comment|/**      * Get the list of managed (local) repositories.      *       * @return the list of managed (local) repositories.      */
specifier|public
name|List
name|getManagedRepositories
parameter_list|()
function_decl|;
comment|/**      * Get the list of remote repositories.      *       * @return the list of remote repositories.      */
specifier|public
name|List
name|getRemoteRepositories
parameter_list|()
function_decl|;
comment|/**      * Add a repository.      *       * @param repository the repository to add.      */
specifier|public
name|void
name|addRepository
parameter_list|(
name|Repository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Remove a repository.      *       * @param repository the repository to add.      */
specifier|public
name|void
name|removeRepository
parameter_list|(
name|Repository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Get a repository using the provided repository key.      *        * @param repositoryKey the repository key to find the repository via.      * @return the repository associated with the provided Repository Key, or null if not found.      */
specifier|public
name|Repository
name|getRepository
parameter_list|(
name|String
name|repositoryKey
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

