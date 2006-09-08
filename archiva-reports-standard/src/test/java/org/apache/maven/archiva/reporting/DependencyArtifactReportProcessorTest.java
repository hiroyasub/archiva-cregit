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
name|Result
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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|DependencyArtifactReportProcessorTest
extends|extends
name|AbstractRepositoryReportsTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|VALID_GROUP_ID
init|=
literal|"groupId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VALID_ARTIFACT_ID
init|=
literal|"artifactId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VALID_VERSION
init|=
literal|"1.0-alpha-1"
decl_stmt|;
specifier|private
name|ReportingDatabase
name|reporter
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
specifier|private
specifier|static
specifier|final
name|String
name|INVALID
init|=
literal|"invalid"
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
name|ReportingDatabase
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
operator|(
name|ArtifactReportProcessor
operator|)
name|lookup
argument_list|(
name|ArtifactReportProcessor
operator|.
name|ROLE
argument_list|,
literal|"dependency"
argument_list|)
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
block|}
specifier|public
name|void
name|testArtifactFoundButNoDirectDependencies
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createValidArtifact
argument_list|()
decl_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
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
specifier|private
name|Artifact
name|createValidArtifact
parameter_list|()
block|{
name|Artifact
name|projectArtifact
init|=
name|artifactFactory
operator|.
name|createProjectArtifact
argument_list|(
name|VALID_GROUP_ID
argument_list|,
name|VALID_ARTIFACT_ID
argument_list|,
name|VALID_VERSION
argument_list|)
decl_stmt|;
name|projectArtifact
operator|.
name|setRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
return|return
name|projectArtifact
return|;
block|}
specifier|public
name|void
name|testArtifactNotFound
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createProjectArtifact
argument_list|(
name|INVALID
argument_list|,
name|INVALID
argument_list|,
name|INVALID
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|setRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
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
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getArtifactIterator
argument_list|()
decl_stmt|;
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|failures
operator|=
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|Result
name|result
init|=
operator|(
name|Result
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Artifact does not exist in the repository"
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidArtifactWithNullDependency
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createValidArtifact
argument_list|()
decl_stmt|;
name|Dependency
name|dependency
init|=
name|createValidDependency
argument_list|()
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
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
specifier|private
name|Dependency
name|createValidDependency
parameter_list|()
block|{
return|return
name|createDependency
argument_list|(
name|VALID_GROUP_ID
argument_list|,
name|VALID_ARTIFACT_ID
argument_list|,
name|VALID_VERSION
argument_list|)
return|;
block|}
specifier|public
name|void
name|testValidArtifactWithValidSingleDependency
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createValidArtifact
argument_list|()
decl_stmt|;
name|Dependency
name|dependency
init|=
name|createValidDependency
argument_list|()
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
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
specifier|public
name|void
name|testValidArtifactWithValidMultipleDependencies
parameter_list|()
block|{
name|Dependency
name|dependency
init|=
name|createValidDependency
argument_list|()
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|Artifact
name|artifact
init|=
name|createValidArtifact
argument_list|()
decl_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
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
specifier|public
name|void
name|testValidArtifactWithAnInvalidDependency
parameter_list|()
block|{
name|Dependency
name|dependency
init|=
name|createValidDependency
argument_list|()
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|createDependency
argument_list|(
name|INVALID
argument_list|,
name|INVALID
argument_list|,
name|INVALID
argument_list|)
argument_list|)
expr_stmt|;
name|Artifact
name|artifact
init|=
name|createValidArtifact
argument_list|()
decl_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
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
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getArtifactIterator
argument_list|()
decl_stmt|;
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|failures
operator|=
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|Result
name|result
init|=
operator|(
name|Result
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|getDependencyNotFoundMessage
argument_list|(
name|createDependency
argument_list|(
name|INVALID
argument_list|,
name|INVALID
argument_list|,
name|INVALID
argument_list|)
argument_list|)
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidArtifactWithInvalidDependencyGroupId
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createValidArtifact
argument_list|()
decl_stmt|;
name|Dependency
name|dependency
init|=
name|createDependency
argument_list|(
name|INVALID
argument_list|,
name|VALID_ARTIFACT_ID
argument_list|,
name|VALID_VERSION
argument_list|)
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
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
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getArtifactIterator
argument_list|()
decl_stmt|;
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|failures
operator|=
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|Result
name|result
init|=
operator|(
name|Result
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|getDependencyNotFoundMessage
argument_list|(
name|dependency
argument_list|)
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Dependency
name|createDependency
parameter_list|(
name|String
name|o
parameter_list|,
name|String
name|valid
parameter_list|,
name|String
name|s
parameter_list|)
block|{
name|Dependency
name|dependency
init|=
operator|new
name|Dependency
argument_list|()
decl_stmt|;
name|dependency
operator|.
name|setGroupId
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setArtifactId
argument_list|(
name|valid
argument_list|)
expr_stmt|;
name|dependency
operator|.
name|setVersion
argument_list|(
name|s
argument_list|)
expr_stmt|;
return|return
name|dependency
return|;
block|}
specifier|public
name|void
name|testValidArtifactWithInvalidDependencyArtifactId
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createValidArtifact
argument_list|()
decl_stmt|;
name|Dependency
name|dependency
init|=
name|createDependency
argument_list|(
name|VALID_GROUP_ID
argument_list|,
name|INVALID
argument_list|,
name|VALID_VERSION
argument_list|)
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
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
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getArtifactIterator
argument_list|()
decl_stmt|;
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|failures
operator|=
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|Result
name|result
init|=
operator|(
name|Result
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|getDependencyNotFoundMessage
argument_list|(
name|dependency
argument_list|)
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidArtifactWithIncorrectDependencyVersion
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createValidArtifact
argument_list|()
decl_stmt|;
name|Dependency
name|dependency
init|=
name|createDependency
argument_list|(
name|VALID_GROUP_ID
argument_list|,
name|VALID_ARTIFACT_ID
argument_list|,
name|INVALID
argument_list|)
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
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
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getArtifactIterator
argument_list|()
decl_stmt|;
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|failures
operator|=
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|Result
name|result
init|=
operator|(
name|Result
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|getDependencyNotFoundMessage
argument_list|(
name|dependency
argument_list|)
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidArtifactWithInvalidDependencyVersion
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|createValidArtifact
argument_list|()
decl_stmt|;
name|Dependency
name|dependency
init|=
name|createDependency
argument_list|(
name|VALID_GROUP_ID
argument_list|,
name|VALID_ARTIFACT_ID
argument_list|,
literal|"["
argument_list|)
decl_stmt|;
name|model
operator|.
name|addDependency
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processArtifact
argument_list|(
name|artifact
argument_list|,
name|model
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
name|Iterator
name|failures
init|=
name|reporter
operator|.
name|getArtifactIterator
argument_list|()
decl_stmt|;
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|failures
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|failures
operator|=
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|Result
name|result
init|=
operator|(
name|Result
operator|)
name|failures
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|getDependencyVersionInvalidMessage
argument_list|(
name|dependency
argument_list|,
literal|"["
argument_list|)
argument_list|,
name|result
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getDependencyVersionInvalidMessage
parameter_list|(
name|Dependency
name|dependency
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
literal|"Artifact's dependency "
operator|+
name|getDependencyString
argument_list|(
name|dependency
argument_list|)
operator|+
literal|" contains an invalid version "
operator|+
name|version
return|;
block|}
specifier|private
specifier|static
name|String
name|getDependencyString
parameter_list|(
name|Dependency
name|dependency
parameter_list|)
block|{
return|return
name|DependencyArtifactReportProcessor
operator|.
name|getDependencyString
argument_list|(
name|dependency
argument_list|)
return|;
block|}
specifier|private
name|String
name|getDependencyNotFoundMessage
parameter_list|(
name|Dependency
name|dependency
parameter_list|)
block|{
return|return
literal|"Artifact's dependency "
operator|+
name|getDependencyString
argument_list|(
name|dependency
argument_list|)
operator|+
literal|" does not exist in the repository"
return|;
block|}
block|}
end_class

end_unit

