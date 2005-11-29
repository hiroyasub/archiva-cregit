begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|discovery
package|;
end_package

begin_comment
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_comment
comment|/**  * Interface for implementation that can discover artifacts within a repository.  *  * @author John Casey  * @author Brett Porter  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArtifactDiscoverer
block|{
name|String
name|ROLE
init|=
name|ArtifactDiscoverer
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Discover artifacts in the repository.      *      * @param repositoryBase      the base directory of the repository on the local filesystem      * @param blacklistedPatterns pattern that lists any files to prevent from being included when scanning      * @param includeSnapshots    whether to discover snapshots      * @return the list of artifacts discovered      * @todo replace repositoryBase with wagon repository      * @todo do we want blacklisted patterns in another form? Part of the object construction?      * @todo should includeSnapshots be configuration on the component?      * @todo instead of a returned list, should a listener be passed in?      */
name|List
name|discoverArtifacts
parameter_list|(
name|File
name|repositoryBase
parameter_list|,
name|String
name|blacklistedPatterns
parameter_list|,
name|boolean
name|includeSnapshots
parameter_list|)
function_decl|;
comment|/**      * Get the list of paths kicked out during the discovery process.      *      * @return the paths as Strings.      */
name|Iterator
name|getKickedOutPathsIterator
parameter_list|()
function_decl|;
comment|/**      * Get the list of paths excluded during the discovery process.      *      * @return the paths as Strings.      */
name|Iterator
name|getExcludedPathsIterator
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

