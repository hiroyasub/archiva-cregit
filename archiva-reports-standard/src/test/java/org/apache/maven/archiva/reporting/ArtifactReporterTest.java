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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactReporterTest
extends|extends
name|AbstractRepositoryReportsTestCase
block|{
specifier|private
name|ReportingDatabase
name|reporter
decl_stmt|;
specifier|private
name|Artifact
name|artifact
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
name|ArtifactFactory
name|artifactFactory
init|=
operator|(
name|ArtifactFactory
operator|)
name|lookup
argument_list|(
name|ArtifactFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|artifact
operator|=
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
expr_stmt|;
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
block|}
specifier|public
name|void
name|testArtifactReporterSingleFailure
parameter_list|()
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"failed once"
argument_list|)
expr_stmt|;
name|Iterator
name|artifactIterator
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
name|artifactIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|artifactIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|results
operator|.
name|getFailures
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
name|count
operator|++
control|)
block|{
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
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
block|}
specifier|public
name|void
name|testArtifactReporterMultipleFailure
parameter_list|()
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"failed once"
argument_list|)
expr_stmt|;
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"failed twice"
argument_list|)
expr_stmt|;
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"failed thrice"
argument_list|)
expr_stmt|;
name|Iterator
name|artifactIterator
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
name|artifactIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|artifactIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|results
operator|.
name|getFailures
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
name|count
operator|++
control|)
block|{
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
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
name|testFailureMessages
parameter_list|()
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"failed once"
argument_list|)
expr_stmt|;
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"failed twice"
argument_list|)
expr_stmt|;
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"failed thrice"
argument_list|)
expr_stmt|;
name|Iterator
name|artifactIterator
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
name|artifactIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|artifactIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|failure
init|=
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"failed once"
argument_list|,
operator|(
operator|(
name|Result
operator|)
name|failure
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"failed twice"
argument_list|,
operator|(
operator|(
name|Result
operator|)
name|failure
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"failed thrice"
argument_list|,
operator|(
operator|(
name|Result
operator|)
name|failure
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactReporterSingleWarning
parameter_list|()
block|{
name|reporter
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"you've been warned"
argument_list|)
expr_stmt|;
name|Iterator
name|artifactIterator
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
name|artifactIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|artifactIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|results
operator|.
name|getWarnings
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
name|count
operator|++
control|)
block|{
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|count
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
literal|1
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
name|testArtifactReporterMultipleWarning
parameter_list|()
block|{
name|reporter
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"i'm warning you"
argument_list|)
expr_stmt|;
name|reporter
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"you have to stop now"
argument_list|)
expr_stmt|;
name|reporter
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"all right... that does it!"
argument_list|)
expr_stmt|;
name|Iterator
name|artifactIterator
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
name|artifactIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|artifactIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|results
operator|.
name|getWarnings
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
name|count
operator|++
control|)
block|{
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|count
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
literal|3
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
name|testWarningMessages
parameter_list|()
block|{
name|reporter
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"i'm warning you"
argument_list|)
expr_stmt|;
name|reporter
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"you have to stop now"
argument_list|)
expr_stmt|;
name|reporter
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
literal|"all right... that does it!"
argument_list|)
expr_stmt|;
name|Iterator
name|artifactIterator
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
name|artifactIterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|artifactIterator
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
name|warning
init|=
name|results
operator|.
name|getWarnings
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"i'm warning you"
argument_list|,
operator|(
operator|(
name|Result
operator|)
name|warning
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"you have to stop now"
argument_list|,
operator|(
operator|(
name|Result
operator|)
name|warning
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"all right... that does it!"
argument_list|,
operator|(
operator|(
name|Result
operator|)
name|warning
operator|.
name|next
argument_list|()
operator|)
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

