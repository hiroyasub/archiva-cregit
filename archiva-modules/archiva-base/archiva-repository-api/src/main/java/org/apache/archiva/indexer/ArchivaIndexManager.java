begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
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
name|Repository
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
name|RepositoryEventListener
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
name|net
operator|.
name|URI
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

begin_interface
specifier|public
interface|interface
name|ArchivaIndexManager
block|{
comment|/**      * Compresses the index to a more dense packed format.      * @param context      */
name|void
name|pack
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|)
throws|throws
name|IndexUpdateFailedException
function_decl|;
comment|/**      * Rescans the whole repository, this index is associated to.      * @param context      *      */
name|void
name|scan
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|)
throws|throws
name|IndexUpdateFailedException
function_decl|;
comment|/**      * Updates the index from the remote url.      * @param context      * @param fullUpdate      */
name|void
name|update
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|,
name|boolean
name|fullUpdate
parameter_list|)
throws|throws
name|IndexUpdateFailedException
function_decl|;
comment|/**      * Adds a list of artifacts to the index.      * @param context      * @param artifactReference      */
name|void
name|addArtifactsToIndex
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|,
name|Collection
argument_list|<
name|URI
argument_list|>
name|artifactReference
parameter_list|)
throws|throws
name|IndexUpdateFailedException
function_decl|;
comment|/**      * Removes a list of artifacts from the index.      * @param context      * @param artifactReference      */
name|void
name|removeArtifactsFromIndex
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|,
name|Collection
argument_list|<
name|URI
argument_list|>
name|artifactReference
parameter_list|)
throws|throws
name|IndexUpdateFailedException
function_decl|;
comment|/**      * Returns true, if this manager is able to apply the index actions for the given repository type.      * @param type      * @return      */
name|boolean
name|supportsRepository
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
function_decl|;
comment|/**      * Creates the indexing context for the given repository.      * @param repository the repository for which the index context should be created      * @return the index context      */
name|ArchivaIndexingContext
name|createContext
parameter_list|(
name|Repository
name|repository
parameter_list|)
throws|throws
name|IndexCreationFailedException
function_decl|;
comment|/**      * Reinitializes the index. E.g. remove the files and create a new empty index.      *      * @param context      * @return the new created index      */
name|ArchivaIndexingContext
name|reset
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|)
throws|throws
name|IndexUpdateFailedException
function_decl|;
comment|/**      * Moves the context to a new directory. It's up to the implementation, if a new context is created      * or the context is moved only.      *      * @param context The current context      * @param repo The repository      * @return The new context      * @throws IndexCreationFailedException      */
name|ArchivaIndexingContext
name|move
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|,
name|Repository
name|repo
parameter_list|)
throws|throws
name|IndexCreationFailedException
function_decl|;
block|}
end_interface

end_unit

