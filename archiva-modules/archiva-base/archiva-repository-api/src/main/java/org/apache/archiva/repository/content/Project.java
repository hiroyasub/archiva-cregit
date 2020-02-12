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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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

begin_comment
comment|/**  *  * The project is the container for several versions each with different artifacts.  *  *<pre>  * project +--> version 1 + ->  artifact 1  *         |              |  *         |              + ->  artifact 2  *         |  *         +--> version 2 ----> artifact 3  *</pre>  *  * Implementations must provide proper hash and equals methods.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Project
extends|extends
name|ContentItem
block|{
comment|/**      * The namespace of the project      * @return the namespace      */
name|String
name|getNamespace
parameter_list|()
function_decl|;
comment|/**      * The id of the project      * @return the project id      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * The repository this project is part of.      * @return the repository content      */
name|ManagedRepositoryContent
name|getRepository
parameter_list|()
function_decl|;
comment|/**      * Returns the asset that corresponds to this project.      * It depends of the implementation, if the asset provides additional information about the      * project or not.      *      * @return the repository asset that represents this project      */
name|StorageAsset
name|getAsset
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

