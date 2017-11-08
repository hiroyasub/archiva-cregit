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
name|RepositoryType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|time
operator|.
name|LocalDateTime
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
comment|/**  * This represents a indexing context that is used to manage the index of a certain repository.  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArchivaIndexingContext
block|{
comment|/**      * The identifier of the context      * @return      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Returns the repository this index context is associated to.      * @return      */
name|Repository
name|getRepository
parameter_list|()
function_decl|;
comment|/**      * The path where the index is stored.      * @return      */
name|URI
name|getPath
parameter_list|()
function_decl|;
comment|/**      * Returns true, if the index has no entries or is not initialized.      * @return      */
name|boolean
name|isEmpty
parameter_list|()
function_decl|;
comment|/**      * Writes the last changes to the index.      * @throws IOException      */
name|void
name|commit
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Throws away the last changes.      * @throws IOException      */
name|void
name|rollback
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Optimizes the index      * @throws IOException      */
name|void
name|optimize
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Closes any resources, this context has open.      * @param deleteFiles True, if the index files should be deleted.      * @throws IOException      */
name|void
name|close
parameter_list|(
name|boolean
name|deleteFiles
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Removes all entries from the index. After this method finished,      * isEmpty() should return true.      * @throws IOException      */
name|void
name|purge
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns true, if this index implementation has support for the given repository specific      * implementation class.      * @param clazz      * @return      */
name|boolean
name|supports
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
function_decl|;
comment|/**      * Returns the repository specific implementation of the index. E.g. the maven index class.      * @param clazz the specific class      * @return the instance of the given class representing this index      * @throws UnsupportedOperationException if the implementation is not supported      */
parameter_list|<
name|T
parameter_list|>
name|T
name|getBaseContext
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|UnsupportedOperationException
function_decl|;
comment|/**      * Returns the list of groups that are assigned to this index      * @return      */
name|Set
argument_list|<
name|String
argument_list|>
name|getGroups
parameter_list|()
function_decl|;
comment|/**      * Updates the timestamp of the index.      * @param save      * @throws IOException      */
name|void
name|updateTimestamp
parameter_list|(
name|boolean
name|save
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Updates the timestamp with the given time.      * @param save      * @param time      * @throws IOException      */
name|void
name|updateTimestamp
parameter_list|(
name|boolean
name|save
parameter_list|,
name|LocalDateTime
name|time
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

