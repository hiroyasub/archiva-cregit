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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|ContentAccessException
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
name|ContentItem
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
name|ItemNotFoundException
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
name|ItemSelector
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
name|LayoutException
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
name|ManagedRepositoryContentLayout
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
name|storage
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ManagedRepositoryContent
extends|extends
name|RepositoryContent
block|{
comment|/**      * Returns the path of the given item.      *      * @param item      * @return      */
name|String
name|toPath
parameter_list|(
name|ContentItem
name|item
parameter_list|)
function_decl|;
comment|/**      *<p>      * Convenience method to get the repository id.      *</p>      *<p>      * Equivalent to calling<code>.getRepository().getId()</code>      *</p>      *      * @return the repository id.      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Delete all items that match the given selector. The type and number of deleted items      * depend on the specific selector:      *<ul>      *<li>namespace: the complete namespace is deleted (recursively if the recurse flag is set)</li>      *<li>project: the complete project and all contained versions are deleted</li>      *<li>version: the version inside the project is deleted (project is required)</li>      *<li>artifactId: all artifacts that match the id (project and version are required)</li>      *<li>artifactVersion: all artifacts that match the version (project and version are required)</li>      *<li></li>      *</ul>      *      * @param selector the item selector that selects the artifacts to delete      * @param consumer a consumer of the items that will be called after deletion      * @returns the list of items that are deleted      * @throws ContentAccessException if the deletion was not possible or only partly successful, because the access      * to the artifacts failed      * @throws IllegalArgumentException if the selector does not specify valid artifacts to delete      */
name|void
name|deleteAllItems
parameter_list|(
name|ItemSelector
name|selector
parameter_list|,
name|Consumer
argument_list|<
name|ItemDeleteStatus
argument_list|>
name|consumer
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
function_decl|;
comment|/**      * Removes the specified content item and if the item is a container or directory,      * all content stored under the given item.      *      * @param item the item.      * @throws ItemNotFoundException if the item cannot be found      * @throws ContentAccessException if the deletion was not possible or only partly successful, because the access      *  to the artifacts failed      */
name|void
name|deleteItem
parameter_list|(
name|ContentItem
name|item
parameter_list|)
throws|throws
name|ItemNotFoundException
throws|,
name|ContentAccessException
function_decl|;
comment|/**      * Returns a item for the given selector. The type of the returned item depends on the      * selector.      *      * @param selector the item selector      * @return the content item that matches the given selector      * @throws ContentAccessException if an error occured while accessing the backend      * @throws IllegalArgumentException if the selector does not select a valid content item      */
name|ContentItem
name|getItem
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
function_decl|;
comment|/**      * Returns a stream of items that match the given selector. It may return a stream of mixed types,      * like namespaces, projects, versions and artifacts. It will not select a specific type.      * The selector can specify the '*' pattern for all fields.      * The returned elements will be provided by depth first.      *      * @param selector the item selector that specifies the items      * @return the stream of content items      * @throws ContentAccessException if the access to the underlying storage failed      * @throws IllegalArgumentException if a illegal coordinate combination was provided      */
name|Stream
argument_list|<
name|?
extends|extends
name|ContentItem
argument_list|>
name|newItemStream
parameter_list|(
name|ItemSelector
name|selector
parameter_list|,
name|boolean
name|parallel
parameter_list|)
throws|throws
name|ContentAccessException
throws|,
name|IllegalArgumentException
function_decl|;
comment|/**      * Returns the item that matches the given path. The item at the path must not exist.      *      * @param path the path string that points to the item      * @return the content item if the path is a valid item path      * @throws LayoutException if the path is not valid for the repository layout      */
name|ContentItem
name|toItem
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      * Returns the item that matches the given asset path. The asset must not exist.      *      * @param assetPath the path to the artifact or directory      * @return the item, if it is a valid path for the repository layout      * @throws LayoutException if the path is not valid for the repository      */
name|ContentItem
name|toItem
parameter_list|(
name|StorageAsset
name|assetPath
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      * Returns true, if the selector coordinates point to a existing item in the repository.      *      * @param selector the item selector      * @return<code>true</code>, if there exists such a item, otherwise<code>false</code>      */
name|boolean
name|hasContent
parameter_list|(
name|ItemSelector
name|selector
parameter_list|)
function_decl|;
comment|/**      * Get the repository configuration associated with this      * repository content.      *      * @return the repository that is associated with this repository content.      */
name|ManagedRepository
name|getRepository
parameter_list|()
function_decl|;
comment|/**      * Set the repository configuration to associate with this      * repository content.      *      * @param repo the repository to associate with this repository content.      */
name|void
name|setRepository
parameter_list|(
name|ManagedRepository
name|repo
parameter_list|)
function_decl|;
comment|/**      * Returns the parent of the item.      * @param item the current item      * @return the parent item, or<code>null</code> if no such item exists      */
name|ContentItem
name|getParent
parameter_list|(
name|ContentItem
name|item
parameter_list|)
function_decl|;
comment|/**      * Returns the list of children items.      * @param item the current item      * @return the list of children, or a empty list, if no children exist      */
name|List
argument_list|<
name|?
extends|extends
name|ContentItem
argument_list|>
name|getChildren
parameter_list|(
name|ContentItem
name|item
parameter_list|)
function_decl|;
comment|/**      * Tries to apply the given characteristic to the content item. If the layout does not allow this,      * it will throw a<code>LayoutException</code>.      *      * @param clazz the characteristic class to apply      * @param item the content item      * @param<T> The characteristic      * @return the applied characteristic      */
parameter_list|<
name|T
extends|extends
name|ContentItem
parameter_list|>
name|T
name|applyCharacteristic
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|ContentItem
name|item
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      * Returns the given layout from the content.      * @param clazz The layout class      * @param<T> the layout class      * @return the specific layout      * @throws LayoutException if the repository does not support this layout type      */
parameter_list|<
name|T
extends|extends
name|ManagedRepositoryContentLayout
parameter_list|>
name|T
name|getLayout
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      * Returns<code>true</code>, if the specific layout is supported by this content.      * @param clazz the layout class      * @return<code>true</code>, if the layout is supported, otherwise<code>false</code>      */
parameter_list|<
name|T
extends|extends
name|ManagedRepositoryContentLayout
parameter_list|>
name|boolean
name|supportsLayout
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
function_decl|;
comment|/**      * Returns a list of supported layout classes      * @return      */
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|ManagedRepositoryContentLayout
argument_list|>
argument_list|>
name|getSupportedLayouts
parameter_list|( )
function_decl|;
block|}
end_interface

end_unit

