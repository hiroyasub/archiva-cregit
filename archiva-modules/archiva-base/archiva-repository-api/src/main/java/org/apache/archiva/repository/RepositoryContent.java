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
name|model
operator|.
name|VersionedReference
import|;
end_import

begin_comment
comment|/**  * Common aspects of content provider interfaces  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryContent
block|{
comment|/**      * Given a repository relative path to a filename, return the {@link VersionedReference} object suitable for the path.      *      * @param path the path relative to the repository base dir for the artifact.      * @return the {@link ArtifactReference} representing the path.  (or null if path cannot be converted to      *         a {@link ArtifactReference})      * @throws LayoutException if there was a problem converting the path to an artifact.      */
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      * Given an {@link ArtifactReference}, return the relative path to the artifact.      *      * @param reference the artifact reference to use.      * @return the relative path to the artifact.      */
name|String
name|toPath
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
function_decl|;
block|}
end_interface

end_unit
