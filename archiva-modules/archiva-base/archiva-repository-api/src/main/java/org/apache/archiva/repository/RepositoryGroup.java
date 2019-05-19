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
name|StorageAsset
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
comment|/**  * Interface for repository groups.  *  * Repository groups are a combined view over a list of repositories.  * All repositories of this group must be of the same type.  *  * Repository groups are read only. You cannot store artifacts into a repository group.  *  * This interface extends<code>{@link RepositoryStorage}</code> to provide access to the merged  * index data files and other metadata.  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryGroup
extends|extends
name|Repository
extends|,
name|RepositoryStorage
block|{
comment|/**      * Returns the list of repositories. The order of the elements represents      * the order of getting artifacts (first one wins).      *      *      * @return      */
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|getRepositories
parameter_list|()
function_decl|;
comment|/**      * Returns true, if the given repository is part of this group.      *      * @param repository The repository to check.      * @return True, if it is part, otherwise false.      */
name|boolean
name|contains
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Returns true, if the repository with the given id is part of this group.      *      * @param id The repository id to check      * @return True, if it is part, otherwise false      */
name|boolean
name|contains
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Returns the path to the merged index      * @return      */
name|StorageAsset
name|getMergedIndexPath
parameter_list|()
function_decl|;
comment|/**      * Returns the time to live in seconds for the merged index.      *      * @return      */
name|int
name|getMergedIndexTTL
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

