begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|layout
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
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArchivaArtifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
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
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ProjectReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|VersionedReference
import|;
end_import

begin_comment
comment|/**  * BidirectionalRepositoryLayout - Similar in scope to ArtifactRepositoryLayout, but does  * the both the Path to Artifact, and Artifact to Path conversions.    *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|BidirectionalRepositoryLayout
block|{
comment|/**      * Get the identifier for this layout.      *       * @return the identifier for this layout.      */
specifier|public
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Given an ArchivaArtifact, return the relative path to the artifact.      *       * @param artifact the artifact to use.      * @return the relative path to the artifact.       */
specifier|public
name|String
name|toPath
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
function_decl|;
comment|/**      * Given an ArtifactReference, return the relative path to the artifact.      *       * @param reference the artifact reference to use.      * @return the relative path to the artifact.       */
specifier|public
name|String
name|toPath
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
function_decl|;
comment|/**      * Given an {@link VersionedReference}, return the relative path to that reference.      *       * @param reference the versioned project reference to use.      * @return the relative path to the project reference.       */
specifier|public
name|String
name|toPath
parameter_list|(
name|VersionedReference
name|reference
parameter_list|)
function_decl|;
comment|/**      * Given an ProjectReference, return the relative path to that reference.      *       * @param reference the project reference to use.      * @return the relative path to the project reference.       */
specifier|public
name|String
name|toPath
parameter_list|(
name|ProjectReference
name|reference
parameter_list|)
function_decl|;
comment|/**      * Given a repository relative path to a filename, return the {@link ArchivaArtifact} object suitable for the path.      *       * @param path the path relative to the repository base dir for the artifact.      * @return the {@link ArchivaArtifact} representing the path. (or null if path cannot be converted to       *         an {@link ArchivaArtifact})      * @throws LayoutException if there was a problem converting the path to an artifact.      */
specifier|public
name|ArchivaArtifact
name|toArtifact
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      * Given a repository relative path to a filename, return the {@link ProjectReference} object suitable for the path.      *       * @param path the path relative to the repository base dir for the artifact.      * @return the {@link ProjectReference} representing the path.  (or null if path cannot be converted to       *         a {@link ProjectReference})      * @throws LayoutException if there was a problem converting the path to an artifact.      */
specifier|public
name|ProjectReference
name|toProjectReference
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      * Given a repository relative path to a filename, return the {@link VersionedReference} object suitable for the path.      *       * @param path the path relative to the repository base dir for the artifact.      * @return the {@link VersionedReference} representing the path.  (or null if path cannot be converted to       *         a {@link VersionedReference})      * @throws LayoutException if there was a problem converting the path to an artifact.      */
specifier|public
name|VersionedReference
name|toVersionedReference
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
comment|/**      * Given a repository relative path to a filename, return the {@link VersionedReference} object suitable for the path.      *       * @param path the path relative to the repository base dir for the artifact.      * @return the {@link ArtifactReference} representing the path.  (or null if path cannot be converted to       *         a {@link ArtifactReference})      * @throws LayoutException if there was a problem converting the path to an artifact.      */
specifier|public
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
block|}
end_interface

end_unit

