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
operator|.
name|group
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
name|maven
operator|.
name|archiva
operator|.
name|reporting
operator|.
name|database
operator|.
name|ReportingDatabase
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
name|metadata
operator|.
name|RepositoryMetadata
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
name|model
operator|.
name|Model
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
comment|/**  * A grouping or report processors for execution as a visible report from the web interface - eg, "health",  * "old artifacts", etc.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ReportGroup
block|{
comment|/**      * Plexus component role.      */
name|String
name|ROLE
init|=
name|ReportGroup
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|/**      * Run any artifact related reports in the report set.      *      * @param artifact          the artifact to process      * @param model             the POM associated with the artifact to process      * @param reportingDatabase the report database to store results in      */
name|void
name|processArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Model
name|model
parameter_list|,
name|ReportingDatabase
name|reportingDatabase
parameter_list|)
function_decl|;
comment|/**      * Run any metadata related reports in the report set.      *      * @param repositoryMetadata the metadata to process      * @param repository         the repository the metadata is located in      * @param reportingDatabase  the report database to store results in      */
name|void
name|processMetadata
parameter_list|(
name|RepositoryMetadata
name|repositoryMetadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ReportingDatabase
name|reportingDatabase
parameter_list|)
function_decl|;
comment|/**      * Whether a report with the given role hint is included in this report set.      *      * @param key the report role hint.      * @return whether the report is included      */
name|boolean
name|includeReport
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Get the report processors in this set. The map is keyed by the report's role hint, and the value is it's      * display name.      *      * @return the reports      */
name|Map
name|getReports
parameter_list|()
function_decl|;
comment|/**      * Get the user-friendly name of this report.      *      * @return the report name      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Get the filename of the reports within the repository's reports directory.      *      * @return the filename      */
name|String
name|getFilename
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

