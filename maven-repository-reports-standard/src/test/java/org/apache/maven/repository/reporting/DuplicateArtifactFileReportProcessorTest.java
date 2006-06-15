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
name|model
operator|.
name|Model
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
name|repository
operator|.
name|digest
operator|.
name|Digester
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
name|repository
operator|.
name|indexing
operator|.
name|ArtifactRepositoryIndex
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

begin_comment
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|DuplicateArtifactFileReportProcessorTest
extends|extends
name|AbstractRepositoryReportsTestCase
block|{
specifier|private
name|MockArtifactReporter
name|reporter
decl_stmt|;
specifier|private
name|Artifact
name|artifact
decl_stmt|;
specifier|private
name|Model
name|model
decl_stmt|;
specifier|private
name|ArtifactReportProcessor
name|processor
decl_stmt|;
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
name|File
name|indexDirectory
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
name|Digester
name|digester
init|=
operator|(
name|Digester
operator|)
name|lookup
argument_list|(
name|Digester
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|indexDirectory
operator|=
name|getTestFile
argument_list|(
literal|"target/indexDirectory"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|indexDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
name|indexDirectory
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
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
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|reporter
operator|=
operator|new
name|MockArtifactReporter
argument_list|()
expr_stmt|;
name|model
operator|=
operator|new
name|Model
argument_list|()
expr_stmt|;
name|ArtifactRepositoryIndex
name|index
init|=
operator|new
name|ArtifactRepositoryIndex
argument_list|(
name|indexDirectory
argument_list|,
name|repository
argument_list|,
name|digester
argument_list|)
decl_stmt|;
name|index
operator|.
name|indexArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|index
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|index
operator|.
name|close
argument_list|()
expr_stmt|;
name|processor
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
literal|"duplicate"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|//FileUtils.deleteDirectory( indexDirectory );
name|processor
operator|=
literal|null
expr_stmt|;
name|model
operator|=
literal|null
expr_stmt|;
name|artifact
operator|=
literal|null
expr_stmt|;
name|reporter
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testNullArtifactFile
parameter_list|()
throws|throws
name|Exception
block|{
name|artifact
operator|.
name|setFile
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|model
argument_list|,
name|artifact
argument_list|,
name|reporter
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no successes"
argument_list|,
literal|0
argument_list|,
name|reporter
operator|.
name|getSuccesses
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check warnings"
argument_list|,
literal|1
argument_list|,
name|reporter
operator|.
name|getWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no failures"
argument_list|,
literal|0
argument_list|,
name|reporter
operator|.
name|getFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessOnAlreadyIndexedArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|processor
operator|.
name|processArtifact
argument_list|(
name|model
argument_list|,
name|artifact
argument_list|,
name|reporter
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no successes"
argument_list|,
literal|1
argument_list|,
name|reporter
operator|.
name|getSuccesses
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check warnings"
argument_list|,
literal|0
argument_list|,
name|reporter
operator|.
name|getWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no failures"
argument_list|,
literal|0
argument_list|,
name|reporter
operator|.
name|getFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessOnDifferentGroupId
parameter_list|()
throws|throws
name|Exception
block|{
name|artifact
operator|.
name|setGroupId
argument_list|(
literal|"different.groupId"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|model
argument_list|,
name|artifact
argument_list|,
name|reporter
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no successes"
argument_list|,
literal|1
argument_list|,
name|reporter
operator|.
name|getSuccesses
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check warnings"
argument_list|,
literal|0
argument_list|,
name|reporter
operator|.
name|getWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no failures"
argument_list|,
literal|0
argument_list|,
name|reporter
operator|.
name|getFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSuccessOnNewArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|Artifact
name|newArtifact
init|=
name|createArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"1.0-alpha-1"
argument_list|,
literal|"pom"
argument_list|)
decl_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|model
argument_list|,
name|newArtifact
argument_list|,
name|reporter
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no successes"
argument_list|,
literal|1
argument_list|,
name|reporter
operator|.
name|getSuccesses
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check warnings"
argument_list|,
literal|0
argument_list|,
name|reporter
operator|.
name|getWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no failures"
argument_list|,
literal|0
argument_list|,
name|reporter
operator|.
name|getFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|Artifact
name|duplicate
init|=
name|createArtifact
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
literal|"snapshot-artifact"
argument_list|,
literal|"1.0-alpha-1-SNAPSHOT"
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|duplicate
operator|.
name|setFile
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|model
argument_list|,
name|duplicate
argument_list|,
name|reporter
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no successes"
argument_list|,
literal|0
argument_list|,
name|reporter
operator|.
name|getSuccesses
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check warnings"
argument_list|,
literal|0
argument_list|,
name|reporter
operator|.
name|getWarnings
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check no failures"
argument_list|,
literal|1
argument_list|,
name|reporter
operator|.
name|getFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Artifact
name|createArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|baseVersion
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|setBaseVersion
argument_list|(
name|baseVersion
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
block|}
end_class

end_unit

