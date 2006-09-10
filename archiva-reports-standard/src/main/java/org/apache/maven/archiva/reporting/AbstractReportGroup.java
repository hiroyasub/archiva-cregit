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
name|Iterator
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
comment|/**  * Basic functionality for all report groups.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractReportGroup
implements|implements
name|ReportGroup
block|{
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.reporting.ArtifactReportProcessor"      */
specifier|private
name|Map
name|artifactReports
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.reporting.MetadataReportProcessor"      */
specifier|private
name|Map
name|metadataReports
decl_stmt|;
specifier|public
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
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|artifactReports
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|includeReport
argument_list|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|ArtifactReportProcessor
name|report
init|=
operator|(
name|ArtifactReportProcessor
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|report
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
argument_list|,
name|reportingDatabase
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
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
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|metadataReports
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|includeReport
argument_list|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|MetadataReportProcessor
name|report
init|=
operator|(
name|MetadataReportProcessor
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|report
operator|.
name|processMetadata
argument_list|(
name|repositoryMetadata
argument_list|,
name|repository
argument_list|,
name|reportingDatabase
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

