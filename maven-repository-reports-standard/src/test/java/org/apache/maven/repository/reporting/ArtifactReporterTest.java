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
name|metadata
operator|.
name|Versioning
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
comment|/**  * @author<a href="mailto:jtolentino@mergere.com">John Tolentino</a>  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactReporterTest
extends|extends
name|AbstractRepositoryReportsTestCase
block|{
specifier|protected
name|ArtifactReporter
name|reporter
decl_stmt|;
specifier|protected
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|protected
name|Artifact
name|artifact
decl_stmt|;
specifier|protected
name|MockArtifactReportProcessor
name|processor
decl_stmt|;
specifier|protected
name|Versioning
name|versioning
decl_stmt|;
specifier|protected
name|Model
name|model
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
name|DefaultArtifactReporter
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
name|processor
operator|=
operator|new
name|MockArtifactReportProcessor
argument_list|()
expr_stmt|;
name|versioning
operator|=
operator|new
name|Versioning
argument_list|()
expr_stmt|;
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
name|model
operator|=
operator|new
name|Model
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactReporterSingleSuccess
parameter_list|()
block|{
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|SUCCESS
argument_list|,
name|artifact
argument_list|,
literal|"all is good"
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
name|Iterator
name|success
init|=
name|reporter
operator|.
name|getArtifactSuccessIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|success
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|success
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|success
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactReporterMultipleSuccess
parameter_list|()
block|{
name|processor
operator|.
name|clearList
argument_list|()
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|SUCCESS
argument_list|,
name|artifact
argument_list|,
literal|"one"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|SUCCESS
argument_list|,
name|artifact
argument_list|,
literal|"two"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|SUCCESS
argument_list|,
name|artifact
argument_list|,
literal|"three"
argument_list|)
expr_stmt|;
name|reporter
operator|=
operator|new
name|DefaultArtifactReporter
argument_list|()
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
name|Iterator
name|success
init|=
name|reporter
operator|.
name|getArtifactSuccessIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|success
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|i
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
init|;
name|success
operator|.
name|hasNext
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|success
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|i
operator|==
literal|3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reporter
operator|.
name|getSuccesses
argument_list|()
operator|==
literal|3
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
name|testArtifactReporterSingleFailure
parameter_list|()
block|{
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|FAILURE
argument_list|,
name|artifact
argument_list|,
literal|"failed once"
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
name|Iterator
name|failure
init|=
name|reporter
operator|.
name|getArtifactFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|failure
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|failure
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|failure
operator|.
name|hasNext
argument_list|()
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
name|testArtifactReporterMultipleFailure
parameter_list|()
block|{
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|FAILURE
argument_list|,
name|artifact
argument_list|,
literal|"failed once"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|FAILURE
argument_list|,
name|artifact
argument_list|,
literal|"failed twice"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|FAILURE
argument_list|,
name|artifact
argument_list|,
literal|"failed thrice"
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
name|Iterator
name|failure
init|=
name|reporter
operator|.
name|getArtifactFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|failure
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|i
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
init|;
name|failure
operator|.
name|hasNext
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|failure
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|i
operator|==
literal|3
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
literal|3
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
name|testFailureMessages
parameter_list|()
block|{
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|FAILURE
argument_list|,
name|artifact
argument_list|,
literal|"failed once"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|FAILURE
argument_list|,
name|artifact
argument_list|,
literal|"failed twice"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|FAILURE
argument_list|,
name|artifact
argument_list|,
literal|"failed thrice"
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
name|Iterator
name|failure
init|=
name|reporter
operator|.
name|getArtifactFailureIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"failed once"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ArtifactResult
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
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"failed twice"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ArtifactResult
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
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"failed thrice"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ArtifactResult
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
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactReporterSingleWarning
parameter_list|()
block|{
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|WARNING
argument_list|,
name|artifact
argument_list|,
literal|"you've been warned"
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
name|Iterator
name|warning
init|=
name|reporter
operator|.
name|getArtifactWarningIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|warning
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|warning
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|warning
operator|.
name|hasNext
argument_list|()
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
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactReporterMultipleWarning
parameter_list|()
block|{
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|WARNING
argument_list|,
name|artifact
argument_list|,
literal|"i'm warning you"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|WARNING
argument_list|,
name|artifact
argument_list|,
literal|"you have to stop now"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|WARNING
argument_list|,
name|artifact
argument_list|,
literal|"all right... that does it!"
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
name|Iterator
name|warning
init|=
name|reporter
operator|.
name|getArtifactWarningIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|warning
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|i
decl_stmt|;
for|for
control|(
name|i
operator|=
literal|0
init|;
name|warning
operator|.
name|hasNext
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|warning
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|i
operator|==
literal|3
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
literal|3
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testWarningMessages
parameter_list|()
block|{
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|WARNING
argument_list|,
name|artifact
argument_list|,
literal|"i'm warning you"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|WARNING
argument_list|,
name|artifact
argument_list|,
literal|"you have to stop now"
argument_list|)
expr_stmt|;
name|processor
operator|.
name|addReturnValue
argument_list|(
name|ReportCondition
operator|.
name|WARNING
argument_list|,
name|artifact
argument_list|,
literal|"all right... that does it!"
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
name|Iterator
name|warning
init|=
name|reporter
operator|.
name|getArtifactWarningIterator
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"i'm warning you"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ArtifactResult
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
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"you have to stop now"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ArtifactResult
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
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"all right... that does it!"
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ArtifactResult
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
name|versioning
operator|=
literal|null
expr_stmt|;
name|processor
operator|.
name|clearList
argument_list|()
expr_stmt|;
name|processor
operator|=
literal|null
expr_stmt|;
name|artifactFactory
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

