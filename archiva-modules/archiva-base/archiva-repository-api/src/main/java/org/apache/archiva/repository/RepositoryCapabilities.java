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
comment|/**  * Describe the capabilities a repository implementation supports.  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryCapabilities
block|{
comment|/**      * Returns true, if this repository has a mechanism for indexes      * @return true, if this repository is indexable, otherwise false.      */
specifier|default
name|boolean
name|isIndexable
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Returns true, if this repository type is storing its artifacts on the filesystem.      * @return true, if this is a file based repository, otherwise false.      */
specifier|default
name|boolean
name|isFileBased
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Returns true, if this repository allows to block redeployments to prevent overriding      * released artifacts      * @return true, if this repo can block redeployments, otherwise false.      */
specifier|default
name|boolean
name|canBlockRedeployments
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Returns true, if the artifacts can be scanned for metadata retrieval or maintenance tasks      * @return true, if this repository can be scanned regularily, otherwise false.      */
specifier|default
name|boolean
name|isScannable
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Returns true, if this repository can use failover repository urls      * @return true, if there is a failover mechanism for repository access, otherwise false.      */
specifier|default
name|boolean
name|allowsFailover
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Returns the release schemes this repository type can handle      */
name|Set
argument_list|<
name|ReleaseScheme
argument_list|>
name|supportedReleaseSchemes
parameter_list|()
function_decl|;
comment|/**      * Returns the layouts this repository type can provide      * @return The list of layouts supported by this repository.      */
name|Set
argument_list|<
name|String
argument_list|>
name|supportedLayouts
parameter_list|()
function_decl|;
comment|/**      * Returns additional capabilities, that this repository type implements.      * @return A list of custom capabilities.      */
name|Set
argument_list|<
name|String
argument_list|>
name|customCapabilities
parameter_list|()
function_decl|;
comment|/**      * Returns the supported features this repository type supports. This method returns      * string that corresponds to fully qualified class names.      * We use string representation to allow implementations provide their own feature      * implementations if necessary and to avoid class errors.      *      * @return The list of supported features as string values.      */
name|Set
argument_list|<
name|String
argument_list|>
name|supportedFeatures
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

