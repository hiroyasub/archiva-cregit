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
name|processor
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
name|AbstractRepositoryReportsTestCase
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
name|reporting
operator|.
name|database
operator|.
name|ArtifactResultsDatabase
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
name|reporting
operator|.
name|model
operator|.
name|ArtifactResults
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
name|reporting
operator|.
name|model
operator|.
name|ResultReason
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
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|FileUtils
import|;
end_import

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

begin_comment
comment|/**  * This class tests the OldArtifactReportProcessor.  */
end_comment

begin_class
specifier|public
class|class
name|OldArtifactReportProcessorTest
extends|extends
name|AbstractRepositoryReportsTestCase
block|{
specifier|private
name|ArtifactReportProcessor
name|artifactReportProcessor
decl_stmt|;
specifier|private
name|ArtifactResultsDatabase
name|database
decl_stmt|;
specifier|public
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
name|database
operator|=
operator|(
name|ArtifactResultsDatabase
operator|)
name|lookup
argument_list|(
name|ArtifactResultsDatabase
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|artifactReportProcessor
operator|=
operator|(
name|ArtifactReportProcessor
operator|)
name|lookup
argument_list|(
name|ArtifactReportProcessor
operator|.
name|ROLE
argument_list|,
literal|"old-artifact"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testOldArtifact
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-model"
argument_list|,
literal|"2.0"
argument_list|)
decl_stmt|;
name|artifactReportProcessor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|database
operator|.
name|getNumFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|database
operator|.
name|getNumWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check notices"
argument_list|,
literal|1
argument_list|,
name|database
operator|.
name|getNumNotices
argument_list|()
argument_list|)
expr_stmt|;
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|database
operator|.
name|getIterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|results
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|results
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|results
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|results
operator|.
name|getNotices
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|i
init|=
name|results
operator|.
name|getNotices
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|ResultReason
name|result
init|=
operator|(
name|ResultReason
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"old-artifact"
argument_list|,
name|result
operator|.
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNewArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|repository
init|=
name|getTestFile
argument_list|(
literal|"target/test-repository"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectoryStructure
argument_list|(
name|getTestFile
argument_list|(
literal|"src/test/repository/groupId"
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|repository
argument_list|,
literal|"groupId"
argument_list|)
argument_list|)
expr_stmt|;
name|Artifact
name|artifact
init|=
name|createArtifactFromRepository
argument_list|(
name|repository
argument_list|,
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-1"
argument_list|)
decl_stmt|;
name|artifactReportProcessor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|database
operator|.
name|getNumFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|database
operator|.
name|getNumWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no notices"
argument_list|,
literal|0
argument_list|,
name|database
operator|.
name|getNumNotices
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testMissingArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|,
literal|"XP"
argument_list|)
decl_stmt|;
try|try
block|{
name|artifactReportProcessor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should not have passed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

