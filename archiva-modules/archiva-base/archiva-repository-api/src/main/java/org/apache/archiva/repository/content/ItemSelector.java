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
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
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
comment|/**  * The item selector is used to specify coordinates for retrieving ContentItem elements.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ItemSelector
block|{
comment|/**      * Selects the namespace to search for. You can use the {@link #searchSubNamespaces()} flag      * to decide, if only the given namespace or the namespace and all sub namespaces (if they exist) should be      * queried. If empty, the root namespace is searched.      * @return the namespace to search      */
name|String
name|getNamespace
parameter_list|( )
function_decl|;
comment|/**      * Selects the project id to search for. If empty all projects are searched.      * @return the project id      */
name|String
name|getProjectId
parameter_list|( )
function_decl|;
comment|/**      * Selects the version to search for. If empty all versions are searched.      * @return the version      */
name|String
name|getVersion
parameter_list|( )
function_decl|;
comment|/**      * Selects a specific artifact version. This may be different from the version, e.g.      * for SNAPSHOT versions. If empty, the artifact version will be ignored.      * @return the artifact version or empty string      */
name|String
name|getArtifactVersion
parameter_list|( )
function_decl|;
comment|/**      * Returns the artifact id to search for. If empty, all artifacts are returned.      * @return the artifact id or a empty string      */
name|String
name|getArtifactId
parameter_list|( )
function_decl|;
comment|/**      * Returns the type to search for. If empty, the type is ignored.      * @return the type or a empty string.      */
name|String
name|getType
parameter_list|( )
function_decl|;
comment|/**      * Returns the classifier string used for querying, or empty string if no classifier.      * If it returns a '*' than all classifiers should be selected.      * @return the classifier string      */
name|String
name|getClassifier
parameter_list|( )
function_decl|;
comment|/**      * Returns the attribute to search for or<code>null</code>, if the      * attribute key should not be used for search.      * @param key the attribute key      * @return      */
name|String
name|getAttribute
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * The extension of the file/asset.      * @return      */
name|String
name|getExtension
parameter_list|( )
function_decl|;
comment|/**      * The map of attributes to search for      * @return      */
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAttributes
parameter_list|( )
function_decl|;
comment|/**      * Returns<code>true</code>, if not only the given namespace but all sub namespaces      * of the given namespace should be queried too.      */
name|boolean
name|searchSubNamespaces
parameter_list|()
function_decl|;
comment|/**      *<code>true</code>, if all files/assets should be returned that match the given selector,      * or<code>false</code>, if only the main assets should be returned.      * Related assets are e.g. hash files or signature files.      * @return<code>true</code>, if all assets should be found otherwise<code>false</code>      */
name|boolean
name|findRelatedArtifacts
parameter_list|()
function_decl|;
specifier|default
name|boolean
name|hasNamespace
parameter_list|( )
block|{
return|return
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getNamespace
argument_list|( )
argument_list|)
return|;
block|}
specifier|default
name|boolean
name|hasProjectId
parameter_list|( )
block|{
return|return
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getProjectId
argument_list|( )
argument_list|)
return|;
block|}
specifier|default
name|boolean
name|hasVersion
parameter_list|( )
block|{
return|return
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getVersion
argument_list|( )
argument_list|)
return|;
block|}
specifier|default
name|boolean
name|hasArtifactId
parameter_list|( )
block|{
return|return
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getArtifactId
argument_list|( )
argument_list|)
return|;
block|}
specifier|default
name|boolean
name|hasArtifactVersion
parameter_list|( )
block|{
return|return
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getArtifactVersion
argument_list|( )
argument_list|)
return|;
block|}
specifier|default
name|boolean
name|hasType
parameter_list|( )
block|{
return|return
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getType
argument_list|( )
argument_list|)
return|;
block|}
specifier|default
name|boolean
name|hasClassifier
parameter_list|( )
block|{
return|return
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|getClassifier
argument_list|( )
argument_list|)
return|;
block|}
name|boolean
name|hasAttributes
parameter_list|( )
function_decl|;
name|boolean
name|hasExtension
parameter_list|( )
function_decl|;
block|}
end_interface

end_unit

