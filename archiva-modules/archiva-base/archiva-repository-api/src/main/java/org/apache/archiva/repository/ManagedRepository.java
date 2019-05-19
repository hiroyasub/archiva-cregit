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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|RepositoryStorage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Represents a managed repository, that is readable and writable.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ManagedRepository
extends|extends
name|Repository
block|{
comment|/**      * Returns the interface to access the contents of this repository.      *      * @return The repository content.      */
name|ManagedRepositoryContent
name|getContent
parameter_list|()
function_decl|;
comment|/**      * Returns true, if repeated deployments of the same artifact with the same version throws exceptions.      * @return      */
name|boolean
name|blocksRedeployments
parameter_list|()
function_decl|;
comment|/**      * Returns the release schemes that are active by this repository. E.g. for maven repositories      * this may either be a release repository, a snapshot repository or a combined repository.      * @return      */
name|Set
argument_list|<
name|ReleaseScheme
argument_list|>
name|getActiveReleaseSchemes
parameter_list|()
function_decl|;
comment|/**      * Returns the request info object, which you can use for gathering information from the web request path.      * @return Instance of a request info object that corresponds to this repository      */
name|RepositoryRequestInfo
name|getRequestInfo
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

