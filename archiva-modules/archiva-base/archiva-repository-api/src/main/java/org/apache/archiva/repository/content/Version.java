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
name|UnsupportedRepositoryTypeException
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
name|Map
import|;
end_import

begin_comment
comment|/**  *  * Each artifact is attached to exactly one version.  *  * Implementations must provide proper hash and equals methods.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|Version
extends|extends
name|Comparable
argument_list|<
name|Version
argument_list|>
block|{
comment|/**      * Returns the version string.      *      * @return the version string      */
name|String
name|getVersion
parameter_list|()
function_decl|;
comment|/**      * Returns the local representation of the version. For maven this is a directory.      * It is implementation dependent, what exactly this asset points to.      *      * @return the local storage representation of the version      */
name|StorageAsset
name|getAsset
parameter_list|()
function_decl|;
comment|/**      * Each version is attached to a project.      * @return the attached project      */
name|Project
name|getProject
parameter_list|()
function_decl|;
comment|/**      * Returns additional attributes.      * @return the map of attribute key, value pairs      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAttributes
parameter_list|()
function_decl|;
comment|/**      * Returns the repository type specific implementation      * @param clazz the specific implementation class      * @param<T> the class or interface      * @return the specific project implementation      */
parameter_list|<
name|T
extends|extends
name|Project
parameter_list|>
name|T
name|adapt
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|UnsupportedRepositoryTypeException
function_decl|;
comment|/**      * Returns<code>true</code>, if this project supports the given adaptor class.      * @param clazz the class to convert this project to      * @param<T> the type      * @return<code>true/code>, if the implementation is supported, otherwise false      */
parameter_list|<
name|T
extends|extends
name|Project
parameter_list|>
name|boolean
name|supports
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

