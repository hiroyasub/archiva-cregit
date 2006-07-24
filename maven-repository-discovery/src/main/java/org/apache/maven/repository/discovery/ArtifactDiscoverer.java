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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
comment|/**  * Interface for implementation that can discover artifacts within a repository.  *  * @author John Casey  * @author Brett Porter  * @todo do we want blacklisted patterns in another form? Part of the object construction?  * @todo should includeSnapshots be configuration on the component? If not, should the methods be changed to include alternates for both possibilities (discoverReleaseArtifacts, discoverReleaseAndSnapshotArtifacts)?  * @todo instead of a returned list, should a listener be passed in?  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArtifactDiscoverer
extends|extends
name|Discoverer
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
comment|/**      * Discover artifacts in the repository. Only artifacts added since the last attempt at discovery will be found.      * This process guarantees never to miss an artifact, however it is possible that an artifact will be received twice      * consecutively even if unchanged, so any users of this list must handle such a situation gracefully.      *      * @param repository          the location of the repository      * @param operation           the operation being used to discover for timestamp checking      * @param blacklistedPatterns pattern that lists any files to prevent from being included when scanning      * @param includeSnapshots    whether to discover snapshots      * @return the list of artifacts discovered      * @throws DiscovererException if there was an unrecoverable problem discovering artifacts or recording progress      */
name|List
name|discoverArtifacts
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|operation
parameter_list|,
name|String
name|blacklistedPatterns
parameter_list|,
name|boolean
name|includeSnapshots
parameter_list|)
throws|throws
name|DiscovererException
function_decl|;
comment|/**      * Build an artifact from a path in the repository      *      * @param path the path      * @return the artifact      * @throws DiscovererException if the file is not a valid artifact      * @todo this should be in maven-artifact      */
name|Artifact
name|buildArtifact
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|DiscovererException
function_decl|;
comment|/**      * Reset the time in the repository that indicates the last time a check was performed.      *      * @param repository the location of the repository      * @param operation  the operation to record the timestamp for      * @throws java.io.IOException if there is a non-recoverable problem reading or writing the metadata      */
name|void
name|resetLastCheckedTime
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|operation
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Set the time in the repository that indicates the last time a check was performed.      *      * @param repository the location of the repository      * @param operation  the operation to record the timestamp for      * @param date       the date to set the last check to      * @throws java.io.IOException if there is a non-recoverable problem reading or writing the metadata      */
name|void
name|setLastCheckedTime
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|operation
parameter_list|,
name|Date
name|date
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

