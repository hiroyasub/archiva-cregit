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
name|ManagedRepositoryContent
import|;
end_import

begin_comment
comment|/**  *  * Basic interface for content layouts.  * A content layout provides specific content item instances for the content structure like Namespace,  * Project, Version and their relationships.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ManagedRepositoryContentLayout
block|{
comment|/**      * Returns the repository content, that this layout is attached to.      * @return the content instance      */
name|ManagedRepositoryContent
name|getGenericContent
parameter_list|()
function_decl|;
comment|/**      * Adapts a generic content item to a specific implementation class.      *      * @param clazz the target implementation      * @param item the content item      * @param<T> the target class      * @return the adapted instance      * @throws LayoutException if the conversion is not possible      */
parameter_list|<
name|T
extends|extends
name|ContentItem
parameter_list|>
name|T
name|adaptItem
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
block|}
end_interface

end_unit

