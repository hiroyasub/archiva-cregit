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

begin_comment
comment|/**  * This class tests the InvalidPomArtifactReportProcessor class.  */
end_comment

begin_class
specifier|public
class|class
name|InvalidPomArtifactReportProcessorTest
extends|extends
name|AbstractRepositoryReportsTestCase
block|{
specifier|private
name|ArtifactReportProcessor
name|artifactReportProcessor
decl_stmt|;
specifier|private
name|ReportingDatabase
name|reporter
init|=
operator|new
name|ReportingDatabase
argument_list|()
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
literal|"invalid-pom"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the InvalidPomArtifactReportProcessor when the artifact is an invalid pom.      */
specifier|public
name|void
name|testInvalidPomArtifactReportProcessorFailure
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-3"
argument_list|,
literal|"pom"
argument_list|)
decl_stmt|;
name|artifactReportProcessor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
literal|null
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|reporter
operator|.
name|getNumFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the InvalidPomArtifactReportProcessor when the artifact is a valid pom.      */
specifier|public
name|void
name|testInvalidPomArtifactReportProcessorSuccess
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-2"
argument_list|,
literal|"pom"
argument_list|)
decl_stmt|;
name|artifactReportProcessor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
literal|null
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|reporter
operator|.
name|getNumFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|reporter
operator|.
name|getNumWarnings
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the InvalidPomArtifactReportProcessor when the artifact is not a pom.      */
specifier|public
name|void
name|testNotAPomArtifactReportProcessorSuccess
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|artifactReportProcessor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
literal|null
argument_list|,
name|reporter
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|reporter
operator|.
name|getNumFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|reporter
operator|.
name|getNumWarnings
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

