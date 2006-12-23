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
name|database
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|MetadataResults
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
name|DefaultArtifact
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
name|versioning
operator|.
name|VersionRange
import|;
end_import

begin_comment
comment|/**  * Test for {@link ReportingDatabase}.  *  * @author<a href="mailto:carlos@apache.org">Carlos Sanchez</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ReportingDatabaseTest
extends|extends
name|TestCase
block|{
specifier|private
name|Artifact
name|artifact
decl_stmt|;
specifier|private
name|String
name|processor
decl_stmt|,
name|problem
decl_stmt|,
name|reason
decl_stmt|;
specifier|private
name|ReportingDatabase
name|reportingDatabase
decl_stmt|;
specifier|private
name|RepositoryMetadata
name|metadata
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
name|artifact
operator|=
operator|new
name|DefaultArtifact
argument_list|(
literal|"group"
argument_list|,
literal|"artifact"
argument_list|,
name|VersionRange
operator|.
name|createFromVersion
argument_list|(
literal|"1.0"
argument_list|)
argument_list|,
literal|"scope"
argument_list|,
literal|"type"
argument_list|,
literal|"classifier"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|processor
operator|=
literal|"processor"
expr_stmt|;
name|problem
operator|=
literal|"problem"
expr_stmt|;
name|reason
operator|=
literal|"reason"
expr_stmt|;
name|reportingDatabase
operator|=
operator|new
name|ReportingDatabase
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|metadata
operator|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddNoticeArtifactStringStringString
parameter_list|()
block|{
name|reportingDatabase
operator|.
name|addNotice
argument_list|(
name|artifact
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|ArtifactResults
name|artifactResults
init|=
name|reportingDatabase
operator|.
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumNotices
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifactResults
operator|.
name|getNotices
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|reportingDatabase
operator|.
name|addNotice
argument_list|(
name|artifact
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|artifactResults
operator|=
name|reportingDatabase
operator|.
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumNotices
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifactResults
operator|.
name|getNotices
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddWarningArtifactStringStringString
parameter_list|()
block|{
name|reportingDatabase
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|ArtifactResults
name|artifactResults
init|=
name|reportingDatabase
operator|.
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifactResults
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|reportingDatabase
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|artifactResults
operator|=
name|reportingDatabase
operator|.
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifactResults
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddFailureArtifactStringStringString
parameter_list|()
block|{
name|reportingDatabase
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|ArtifactResults
name|artifactResults
init|=
name|reportingDatabase
operator|.
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifactResults
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|reportingDatabase
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|artifactResults
operator|=
name|reportingDatabase
operator|.
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifactResults
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddNoticeRepositoryMetadataStringStringString
parameter_list|()
block|{
name|reportingDatabase
operator|.
name|addNotice
argument_list|(
name|metadata
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|MetadataResults
name|metadataResults
init|=
name|reportingDatabase
operator|.
name|getMetadataResults
argument_list|(
name|metadata
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumNotices
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|metadataResults
operator|.
name|getNotices
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|reportingDatabase
operator|.
name|addNotice
argument_list|(
name|metadata
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|metadataResults
operator|=
name|reportingDatabase
operator|.
name|getMetadataResults
argument_list|(
name|metadata
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumNotices
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|metadataResults
operator|.
name|getNotices
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddWarningRepositoryMetadataStringStringString
parameter_list|()
block|{
name|reportingDatabase
operator|.
name|addWarning
argument_list|(
name|metadata
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|MetadataResults
name|metadataResults
init|=
name|reportingDatabase
operator|.
name|getMetadataResults
argument_list|(
name|metadata
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|metadataResults
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|reportingDatabase
operator|.
name|addWarning
argument_list|(
name|metadata
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|metadataResults
operator|=
name|reportingDatabase
operator|.
name|getMetadataResults
argument_list|(
name|metadata
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|metadataResults
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testAddFailureRepositoryMetadataStringStringString
parameter_list|()
block|{
name|reportingDatabase
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|MetadataResults
name|metadataResults
init|=
name|reportingDatabase
operator|.
name|getMetadataResults
argument_list|(
name|metadata
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|metadataResults
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|reportingDatabase
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
name|processor
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
name|metadataResults
operator|=
name|reportingDatabase
operator|.
name|getMetadataResults
argument_list|(
name|metadata
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reportingDatabase
operator|.
name|getNumFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|metadataResults
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

