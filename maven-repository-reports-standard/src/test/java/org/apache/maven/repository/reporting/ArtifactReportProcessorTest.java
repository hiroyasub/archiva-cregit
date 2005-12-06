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
name|model
operator|.
name|Dependency
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

begin_comment
comment|/**  * @author<a href="mailto:jtolentino@mergere.com">John Tolentino</a>  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactReportProcessorTest
extends|extends
name|AbstractRepositoryReportsTestCase
block|{
specifier|protected
name|MockArtifactReporter
name|reporter
decl_stmt|;
specifier|protected
name|Artifact
name|artifact
decl_stmt|;
specifier|protected
name|Model
name|model
decl_stmt|;
specifier|protected
name|DefaultArtifactReportProcessor
name|processor
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
name|reporter
operator|=
operator|new
name|MockArtifactReporter
argument_list|()
expr_stmt|;
name|artifact
operator|=
operator|new
name|MockArtifact
argument_list|()
expr_stmt|;
name|model
operator|=
operator|new
name|Model
argument_list|()
expr_stmt|;
name|processor
operator|=
operator|new
name|DefaultArtifactReportProcessor
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testNullArtifact
parameter_list|()
block|{
name|processor
operator|.
name|processArtifact
argument_list|(
name|model
argument_list|,
literal|null
argument_list|,
name|reporter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getSuccesses
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getFailures
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getWarnings
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNoProjectDescriptor
parameter_list|()
block|{
name|processor
operator|.
name|processArtifact
argument_list|(
literal|null
argument_list|,
name|artifact
argument_list|,
name|reporter
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getSuccesses
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getFailures
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getWarnings
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactFoundButNoDirectDependencies
parameter_list|()
block|{
name|MockRepositoryQueryLayer
name|queryLayer
init|=
operator|new
name|MockRepositoryQueryLayer
argument_list|()
decl_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setRepositoryQueryLayer
argument_list|(
name|queryLayer
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
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getSuccesses
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getFailures
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getWarnings
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactNotFound
parameter_list|()
block|{
name|MockRepositoryQueryLayer
name|queryLayer
init|=
operator|new
name|MockRepositoryQueryLayer
argument_list|()
decl_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_NOT_FOUND
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setRepositoryQueryLayer
argument_list|(
name|queryLayer
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
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getSuccesses
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getFailures
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getWarnings
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidArtifactWithValidSingleDependency
parameter_list|()
block|{
name|MockArtifactFactory
name|artifactFactory
init|=
operator|new
name|MockArtifactFactory
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setArtifactFactory
argument_list|(
name|artifactFactory
argument_list|)
expr_stmt|;
name|MockRepositoryQueryLayer
name|queryLayer
init|=
operator|new
name|MockRepositoryQueryLayer
argument_list|()
decl_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|Dependency
name|dependency
init|=
operator|new
name|Dependency
argument_list|()
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setRepositoryQueryLayer
argument_list|(
name|queryLayer
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
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getSuccesses
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getFailures
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getWarnings
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidArtifactWithValidMultipleDependencies
parameter_list|()
block|{
name|MockArtifactFactory
name|artifactFactory
init|=
operator|new
name|MockArtifactFactory
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setArtifactFactory
argument_list|(
name|artifactFactory
argument_list|)
expr_stmt|;
name|MockRepositoryQueryLayer
name|queryLayer
init|=
operator|new
name|MockRepositoryQueryLayer
argument_list|()
decl_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|Dependency
name|dependency
init|=
operator|new
name|Dependency
argument_list|()
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setRepositoryQueryLayer
argument_list|(
name|queryLayer
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
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getSuccesses
argument_list|()
operator|==
literal|6
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getFailures
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getWarnings
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidArtifactWithAnInvalidDependency
parameter_list|()
block|{
name|MockArtifactFactory
name|artifactFactory
init|=
operator|new
name|MockArtifactFactory
argument_list|()
decl_stmt|;
name|processor
operator|.
name|setArtifactFactory
argument_list|(
name|artifactFactory
argument_list|)
expr_stmt|;
name|MockRepositoryQueryLayer
name|queryLayer
init|=
operator|new
name|MockRepositoryQueryLayer
argument_list|()
decl_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|Dependency
name|dependency
init|=
operator|new
name|Dependency
argument_list|()
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_NOT_FOUND
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|queryLayer
operator|.
name|addReturnValue
argument_list|(
name|RepositoryQueryLayer
operator|.
name|ARTIFACT_FOUND
argument_list|)
expr_stmt|;
name|processor
operator|.
name|setRepositoryQueryLayer
argument_list|(
name|queryLayer
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
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getSuccesses
argument_list|()
operator|==
literal|5
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getFailures
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getWarnings
argument_list|()
operator|==
literal|0
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
block|}
end_class

end_unit

