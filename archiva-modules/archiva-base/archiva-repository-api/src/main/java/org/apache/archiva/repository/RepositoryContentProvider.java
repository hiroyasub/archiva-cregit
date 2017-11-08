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
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * A repository content provider creates repository content instances for specific repository types.  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryContentProvider
block|{
comment|/**      * Returns true, if this content object supports the given layout otherwise, false.      * @param layout the layout string      * @return true, if layout is supported, otherwise false.      */
name|boolean
name|supportsLayout
parameter_list|(
name|String
name|layout
parameter_list|)
function_decl|;
comment|/**      * Returns the repository types, this content object can be used for.      *      * @return all supported repository types.      */
name|Set
argument_list|<
name|RepositoryType
argument_list|>
name|getSupportedRepositoryTypes
parameter_list|()
function_decl|;
comment|/**      * Returns true, if this content object supports the given repository type.      *      * @param type the type to check.      * @return true, if the type is supported, otherwise false.      */
name|boolean
name|supports
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
function_decl|;
comment|/**      * Creates a new instance of RemoteRepositoryContent. The returned instance should be initialized      * from the given repository data.      *      * @param repository the repository      * @return a repository content instance      * @throws RepositoryException if the layout is not supported, or a error occured during initialization      */
name|RemoteRepositoryContent
name|createRemoteContent
parameter_list|(
name|RemoteRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Creates a new instance of ManagedRepositoryContent.      *      * @param repository the repository      * @return a new instance      * @throws RepositoryException if the layout is not supported, or a error occured during initialization      */
name|ManagedRepositoryContent
name|createManagedContent
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
comment|/**      * Creates a generic content object.      *      * @param repository the repository      * @param clazz  the content class      * @param<T> the generic type of the content      * @param<V> the generic type of the repository (must correspond to the content class)      * @return a new instance      * @throws RepositoryException if the clazz, or layout is not supported, or something went wrong during initialization      */
parameter_list|<
name|T
extends|extends
name|RepositoryContent
parameter_list|,
name|V
extends|extends
name|Repository
parameter_list|>
name|T
name|createContent
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|V
name|repository
parameter_list|)
throws|throws
name|RepositoryException
function_decl|;
block|}
end_interface

end_unit

