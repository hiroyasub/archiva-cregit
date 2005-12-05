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
name|reporting
package|;
end_package

begin_comment
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|factory
operator|.
name|ArtifactFactory
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
name|ArtifactRepositoryMetadata
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
name|GroupRepositoryMetadata
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
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|Snapshot
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
name|Versioning
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
name|Plugin
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
name|SnapshotArtifactRepositoryMetadata
import|;
end_import

begin_comment
comment|/**  * @todo???  should use MetadataXpp3Reader instead ?  */
end_comment

begin_class
specifier|public
class|class
name|BadMetadataReportProcessorTest
extends|extends
name|AbstractRepositoryReportsTestCase
block|{
specifier|protected
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|private
name|MetadataReportProcessor
name|badMetadataReportProcessor
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|artifactFactory
operator|=
operator|(
name|ArtifactFactory
operator|)
name|lookup
argument_list|(
name|ArtifactFactory
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|badMetadataReportProcessor
operator|=
operator|(
name|MetadataReportProcessor
operator|)
name|lookup
argument_list|(
name|MetadataReportProcessor
operator|.
name|ROLE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMetadataMissingLastUpdated
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
name|Versioning
name|versioning
init|=
operator|new
name|Versioning
argument_list|()
decl_stmt|;
name|versioning
operator|.
name|addVersion
argument_list|(
literal|"1.0-alpha-1"
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|addVersion
argument_list|(
literal|"1.0-alpha-2"
argument_list|)
expr_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|,
name|versioning
argument_list|)
decl_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryMetadataResult
name|result
init|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check metadata"
argument_list|,
name|metadata
argument_list|,
name|result
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Missing lastUpdated element inside the metadata."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check no more failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMetadataValidVersions
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
name|Versioning
name|versioning
init|=
operator|new
name|Versioning
argument_list|()
decl_stmt|;
name|versioning
operator|.
name|addVersion
argument_list|(
literal|"1.0-alpha-1"
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|addVersion
argument_list|(
literal|"1.0-alpha-2"
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|setLastUpdated
argument_list|(
literal|"20050611.202020"
argument_list|)
expr_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|,
name|versioning
argument_list|)
decl_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"check there are no failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMetadataMissingADirectory
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
name|Versioning
name|versioning
init|=
operator|new
name|Versioning
argument_list|()
decl_stmt|;
name|versioning
operator|.
name|addVersion
argument_list|(
literal|"1.0-alpha-1"
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|setLastUpdated
argument_list|(
literal|"20050611.202020"
argument_list|)
expr_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|,
name|versioning
argument_list|)
decl_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryMetadataResult
name|result
init|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check metadata"
argument_list|,
name|metadata
argument_list|,
name|result
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Artifact version 1.0-alpha-2 found in the repository but missing in the metadata."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check no more failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMetadataInvalidArtifactVersion
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
name|Versioning
name|versioning
init|=
operator|new
name|Versioning
argument_list|()
decl_stmt|;
name|versioning
operator|.
name|addVersion
argument_list|(
literal|"1.0-alpha-1"
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|addVersion
argument_list|(
literal|"1.0-alpha-2"
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|addVersion
argument_list|(
literal|"1.0-alpha-3"
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|setLastUpdated
argument_list|(
literal|"20050611.202020"
argument_list|)
expr_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|,
name|versioning
argument_list|)
decl_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryMetadataResult
name|result
init|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check metadata"
argument_list|,
name|metadata
argument_list|,
name|result
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Artifact version 1.0-alpha-3 is present in metadata but missing in the repository."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check no more failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMoreThanOneMetadataVersionErrors
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
name|Versioning
name|versioning
init|=
operator|new
name|Versioning
argument_list|()
decl_stmt|;
name|versioning
operator|.
name|addVersion
argument_list|(
literal|"1.0-alpha-1"
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|addVersion
argument_list|(
literal|"1.0-alpha-3"
argument_list|)
expr_stmt|;
name|versioning
operator|.
name|setLastUpdated
argument_list|(
literal|"20050611.202020"
argument_list|)
expr_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|,
name|versioning
argument_list|)
decl_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryMetadataResult
name|result
init|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check metadata"
argument_list|,
name|metadata
argument_list|,
name|result
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Artifact version 1.0-alpha-3 is present in metadata but missing in the repository."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a 2nd failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
expr_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Artifact version 1.0-alpha-2 found in the repository but missing in the metadata."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check no more failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidPluginMetadata
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
literal|"groupId"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|addPlugin
argument_list|(
name|createMetadataPlugin
argument_list|(
literal|"artifactId"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"check there are no failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMissingMetadataPlugin
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
literal|"groupId"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|addPlugin
argument_list|(
name|createMetadataPlugin
argument_list|(
literal|"missing-plugin"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryMetadataResult
name|result
init|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Metadata plugin missing-plugin is not present in the repository"
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check no more failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidPluginArtifactId
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
literal|"groupId"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|addPlugin
argument_list|(
name|createMetadataPlugin
argument_list|(
literal|null
argument_list|,
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|addPlugin
argument_list|(
name|createMetadataPlugin
argument_list|(
literal|""
argument_list|,
literal|"default2"
argument_list|)
argument_list|)
expr_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryMetadataResult
name|result
init|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Missing or empty artifactId in group metadata."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a 2nd failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
expr_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Missing or empty artifactId in group metadata."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check no more failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidPluginPrefix
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
literal|"groupId"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|addPlugin
argument_list|(
name|createMetadataPlugin
argument_list|(
literal|"artifactId"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|addPlugin
argument_list|(
name|createMetadataPlugin
argument_list|(
literal|"snapshot-artifact"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryMetadataResult
name|result
init|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Missing or empty plugin prefix for artifactId artifactId."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a 2nd failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
expr_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Missing or empty plugin prefix for artifactId snapshot-artifact."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check no more failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDuplicatePluginPrefixes
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
literal|"groupId"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|addPlugin
argument_list|(
name|createMetadataPlugin
argument_list|(
literal|"artifactId"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|getMetadata
argument_list|()
operator|.
name|addPlugin
argument_list|(
name|createMetadataPlugin
argument_list|(
literal|"snapshot-artifact"
argument_list|,
literal|"default"
argument_list|)
argument_list|)
expr_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryMetadataResult
name|result
init|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Duplicate plugin prefix found: default."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check no more failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidSnapshotMetadata
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"snapshot-artifact"
argument_list|,
literal|"1.0-alpha-1-SNAPSHOT"
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
name|Snapshot
name|snapshot
init|=
operator|new
name|Snapshot
argument_list|()
decl_stmt|;
name|snapshot
operator|.
name|setBuildNumber
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|snapshot
operator|.
name|setTimestamp
argument_list|(
literal|"20050611.202024"
argument_list|)
expr_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|SnapshotArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|,
name|snapshot
argument_list|)
decl_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"check there are no failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidSnapshotMetadata
parameter_list|()
throws|throws
name|ReportProcessorException
block|{
name|ArtifactReporter
name|reporter
init|=
operator|new
name|MockArtifactReporter
argument_list|()
decl_stmt|;
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"snapshot-artifact"
argument_list|,
literal|"1.0-alpha-1-SNAPSHOT"
argument_list|,
literal|"type"
argument_list|)
decl_stmt|;
name|Snapshot
name|snapshot
init|=
operator|new
name|Snapshot
argument_list|()
decl_stmt|;
name|snapshot
operator|.
name|setBuildNumber
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|snapshot
operator|.
name|setTimestamp
argument_list|(
literal|"20050611.202024"
argument_list|)
expr_stmt|;
name|RepositoryMetadata
name|metadata
init|=
operator|new
name|SnapshotArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|,
name|snapshot
argument_list|)
decl_stmt|;
name|badMetadataReportProcessor
operator|.
name|processMetadata
argument_list|(
name|metadata
argument_list|,
name|repository
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getRepositoryMetadataFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check there is a failure"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryMetadataResult
name|result
init|=
operator|(
name|RepositoryMetadataResult
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check metadata"
argument_list|,
name|metadata
argument_list|,
name|result
operator|.
name|getMetadata
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: should be more robust
name|assertEquals
argument_list|(
literal|"check reason"
argument_list|,
literal|"Snapshot artifact 20050611.202024-2 does not exist."
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check no more failures"
argument_list|,
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Plugin
name|createMetadataPlugin
parameter_list|(
name|String
name|artifactId
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|Plugin
name|plugin
init|=
operator|new
name|Plugin
argument_list|()
decl_stmt|;
name|plugin
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|plugin
operator|.
name|setName
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|plugin
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
return|return
name|plugin
return|;
block|}
block|}
end_class

end_unit

