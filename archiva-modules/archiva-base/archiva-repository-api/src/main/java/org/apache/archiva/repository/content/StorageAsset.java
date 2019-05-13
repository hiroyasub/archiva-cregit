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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
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
name|function
operator|.
name|Consumer
import|;
end_import

begin_comment
comment|/**  * A instance of this interface represents information about an specific asset in a repository.  * The asset may be an real artifact, a directory, or a virtual asset.  *  * Each asset has a unique path relative to the repository.  *  * The implementation may read the data directly from the filesystem or underlying storage implementation.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|StorageAsset
block|{
comment|/**      * Returns the complete path relative to the repository to the given asset.      *      * @return A path starting with '/' that uniquely identifies the asset in the repository.      */
name|String
name|getPath
parameter_list|()
function_decl|;
comment|/**      * Returns the name of the asset. It may be just the filename.      * @return      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Returns the time of the last modification.      *      * @return      */
name|Instant
name|getModificationTime
parameter_list|()
function_decl|;
comment|/**      * Returns true, if this asset is a container type and contains further child assets.      * @return      */
name|boolean
name|isContainer
parameter_list|()
function_decl|;
comment|/**      * List the child assets.      *      * @return The list of children. If there are no children, a empty list will be returned.      */
name|List
argument_list|<
name|StorageAsset
argument_list|>
name|list
parameter_list|()
function_decl|;
comment|/**      * The size in bytes of the asset. If the asset does not have a size, -1 should be returned.      *      * @return The size if the asset has a size, otherwise -1      */
name|long
name|getSize
parameter_list|()
function_decl|;
comment|/**      * Returns the input stream of the artifact content.      * It will throw a IOException, if the stream could not be created.      * Implementations should create a new stream instance for each invocation.      *      * @return The InputStream representing the content of the artifact.      * @throws IOException      */
name|InputStream
name|getData
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      *      * Returns an output stream where you can write data to the asset.      *      * @param replace If true, the original data will be replaced, otherwise the data will be appended.      * @return The OutputStream where the data can be written.      * @throws IOException      */
name|OutputStream
name|writeData
parameter_list|(
name|boolean
name|replace
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Replaces the content. The implementation may do an atomic move operation, or keep a backup. If      * the operation fails, the implementation should try to restore the old data, if possible.      *      * The original file may be deleted, if the storage was successful.      *      * @param newData Replaces the data by the content of the given file.      */
name|boolean
name|storeDataFile
parameter_list|(
name|Path
name|newData
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns true, if the asset exists.      *      * @return True, if the asset exists, otherwise false.      */
name|boolean
name|exists
parameter_list|()
function_decl|;
comment|/**      * Creates the asset in the underlying storage, if it does not exist.      */
name|void
name|create
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the real path to the asset, if it exist. Not all implementations may implement this method.      *      * @return The filesystem path to the asset.      * @throws UnsupportedOperationException      */
name|Path
name|getFilePath
parameter_list|()
throws|throws
name|UnsupportedOperationException
function_decl|;
block|}
end_interface

end_unit

