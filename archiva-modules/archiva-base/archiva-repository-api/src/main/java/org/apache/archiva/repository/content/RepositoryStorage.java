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
name|content
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_comment
comment|/**  * Repository storage gives access to the files and directories on the storage.  * The storage may be on a filesystem but can be any other storage system.  *  * This API is low level repository access. If you use this API you must  * either have knowledge about the specific repository layout or use the structure  * as it is, e.g. for browsing.  *  * It is the decision of the implementation, if this API provides access to all elements, or  * just a selected view.  *  * Checking access is not part of this API.  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryStorage
block|{
comment|/**      * Returns information about a specific storage asset.      * @param path      * @return      */
name|StorageAsset
name|getAsset
parameter_list|(
name|String
name|path
parameter_list|)
function_decl|;
comment|/**      * Consumes the data and sets a lock for the file during the operation.      *      * @param asset      * @param consumerFunction      * @param readLock      * @throws IOException      */
name|void
name|consumeData
parameter_list|(
name|StorageAsset
name|asset
parameter_list|,
name|Consumer
argument_list|<
name|InputStream
argument_list|>
name|consumerFunction
parameter_list|,
name|boolean
name|readLock
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Adds a new asset to the underlying storage.      * @param path The path to the asset.      * @param container True, if the asset should be a container, false, if it is a file.      * @return      */
name|StorageAsset
name|addAsset
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|container
parameter_list|)
function_decl|;
comment|/**      * Removes the given asset from the storage.      *      * @param asset      * @throws IOException      */
name|void
name|removeAsset
parameter_list|(
name|StorageAsset
name|asset
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Moves the asset to the given location and returns the asset object for the destination.      *      * @param origin      * @param destination      * @return      */
name|StorageAsset
name|moveAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|String
name|destination
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Copies the given asset to the new destination.      *      * @param origin      * @param destination      * @return      * @throws IOException      */
name|StorageAsset
name|copyAsset
parameter_list|(
name|StorageAsset
name|origin
parameter_list|,
name|String
name|destination
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

