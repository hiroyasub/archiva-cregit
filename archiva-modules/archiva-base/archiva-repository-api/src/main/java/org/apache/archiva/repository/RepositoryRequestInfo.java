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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArtifactReference
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
name|features
operator|.
name|RepositoryFeature
import|;
end_import

begin_comment
comment|/**  * This interface is for mapping web request paths to artifacts.  * The file system storage may differ from the paths used for web access.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryRequestInfo
block|{
comment|/**      * Returns the artifact reference for a given path.      * Takes an incoming requested path (in "/" format) and gleans the layout      * and ArtifactReference appropriate for that content.      *      * @param requestPath The path of the web request      * @return The artifact reference      * @throws LayoutException      */
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|requestPath
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      *<p>      * Tests the path to see if it conforms to the expectations of a metadata request.      *</p>      *<p>      * NOTE: The implementation may do only a cursory check on the path's extension.  A result of true      * from this method is not a guarantee that the support resource is in a valid format, or      * that it even contains data.      *</p>      *      * @param requestPath the path to test.      * @return true if the requestedPath is likely a metadata request.      */
name|boolean
name|isMetadata
parameter_list|(
name|String
name|requestPath
parameter_list|)
function_decl|;
comment|/**      * Returns true, if the given request points to a archetype catalog.      *      * @param requestPath      * @return true if the requestedPath is likely an archetype catalog request.      */
name|boolean
name|isArchetypeCatalog
parameter_list|(
name|String
name|requestPath
parameter_list|)
function_decl|;
comment|/**      *<p>      * Tests the path to see if it conforms to the expectations of a support file request. Support files are used      * for signing and validating the artifact files.      *</p>      *<p>      * May test for certain extensions like<code>.sha1</code>,<code>.md5</code>,<code>.asc</code>, and<code>.php</code>.      *</p>      *<p>      * NOTE: The implementation may do only a cursory check on the path's extension.  A result of true      * from this method is not a guarantee that the support resource is in a valid format, or      * that it even contains data.      *</p>      *      * @param requestPath the path to test.      * @return true if the requestedPath is likely that of a support file request.      */
name|boolean
name|isSupportFile
parameter_list|(
name|String
name|requestPath
parameter_list|)
function_decl|;
comment|/**      *<p>      * Tests the path to see if it conforms to the expectations of a support file request of the metadata file.      *</p>      *<p>      * May test for certain extensions like<code>.sha1</code>,<code>.md5</code>,<code>.asc</code>, and<code>.php</code>.      *</p>      *<p>      * NOTE: The implementation may do only a cursory check on the path's extension.  A result of true      * from this method is not a guarantee that the support resource is in a valid format, or      * that it even contains data.      *</p>      *      * @param requestPath the path to test.      * @return true if the requestedPath is likely that of a support file request.      */
name|boolean
name|isMetadataSupportFile
parameter_list|(
name|String
name|requestPath
parameter_list|)
function_decl|;
comment|/**      * Returns the likely layout type for the given request.      * Implementations may only check the path elements for this.  To make sure, the path is valid,      * you should call {@link #toArtifactReference(String)}      *      * @return      */
name|String
name|getLayout
parameter_list|(
name|String
name|requestPath
parameter_list|)
function_decl|;
comment|/**      * Adjust the requestedPath to conform to the native layout of the provided {@link BaseRepositoryContentLayout}.      *      * @param requestPath the incoming requested path.      * @return the adjusted (to native) path.      * @throws LayoutException if the path cannot be parsed.      */
name|String
name|toNativePath
parameter_list|(
name|String
name|requestPath
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      * Extension method that allows to provide different features that are not supported by all      * repository types.      *      * @param clazz The feature class that is requested      * @param<T>   This is the class of the feature      * @return The feature implementation for this repository instance, if it is supported      * @throws UnsupportedFeatureException if the feature is not supported by this repository type      */
parameter_list|<
name|T
extends|extends
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
parameter_list|>
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
name|getFeature
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|UnsupportedFeatureException
function_decl|;
comment|/**      * Returns true, if the requested feature is supported by this repository.      *      * @param clazz The requested feature class      * @param<T>   The requested feature class      * @return True, if the feature is supported, otherwise false.      */
parameter_list|<
name|T
extends|extends
name|RepositoryFeature
argument_list|<
name|T
argument_list|>
parameter_list|>
name|boolean
name|supportsFeature
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

