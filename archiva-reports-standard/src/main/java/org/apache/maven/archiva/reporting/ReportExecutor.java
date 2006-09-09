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
name|reporting
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
name|archiva
operator|.
name|discoverer
operator|.
name|DiscovererException
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|resolver
operator|.
name|filter
operator|.
name|ArtifactFilter
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
comment|/**  * Executes a report or report group.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ReportExecutor
block|{
comment|/**      * Plexus component role name.      */
name|String
name|ROLE
init|=
name|ReportExecutor
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Run reports on a set of metadata.      *      * @param metadata   the RepositoryMetadata objects to report on      * @param repository the repository that they come from      * @throws ReportingStoreException if there is a problem reading/writing the report database      */
specifier|public
name|void
name|runMetadataReports
parameter_list|(
name|List
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|ReportingStoreException
function_decl|;
comment|/**      * Run reports on a set of artifacts.      *      * @param artifacts  the Artifact objects to report on      * @param repository the repository that they come from      * @throws ReportingStoreException if there is a problem reading/writing the report database      */
specifier|public
name|void
name|runArtifactReports
parameter_list|(
name|List
name|artifacts
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|ReportingStoreException
function_decl|;
comment|/**      * Get the report database in use for a given repository.      *      * @param repository the repository      * @return the report database      * @throws ReportingStoreException if there is a problem reading the report database      */
name|ReportingDatabase
name|getReportDatabase
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|ReportingStoreException
function_decl|;
comment|/**      * Run the artifact and metadata reports for the repository. The artifacts and metadata will be discovered.      *      * @param repository          the repository to run from      * @param blacklistedPatterns the patterns to exclude during discovery      * @param filter              the filter to use during discovery to get a consistent list of artifacts      * @throws ReportingStoreException if there is a problem reading/writing the report database      * @throws org.apache.maven.archiva.discoverer.DiscovererException      *                                 if there is a problem finding the artifacts and metadata to report on      */
specifier|public
name|void
name|runReports
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|List
name|blacklistedPatterns
parameter_list|,
name|ArtifactFilter
name|filter
parameter_list|)
throws|throws
name|DiscovererException
throws|,
name|ReportingStoreException
function_decl|;
block|}
end_interface

end_unit

