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
name|List
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
name|ArrayList
import|;
end_import

begin_comment
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * @author<a href="mailto:jtolentino@mergere.com">John Tolentino</a>  */
end_comment

begin_class
specifier|public
class|class
name|MockArtifactReportProcessor
implements|implements
name|ArtifactReportProcessor
block|{
specifier|private
name|List
name|reportConditions
decl_stmt|;
specifier|private
name|Iterator
name|iterator
decl_stmt|;
specifier|public
name|MockArtifactReportProcessor
parameter_list|()
block|{
name|reportConditions
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|processArtifact
parameter_list|(
name|Model
name|model
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ArtifactReporter
name|reporter
parameter_list|,
name|ArtifactRepository
name|artifactRepository
parameter_list|)
block|{
if|if
condition|(
name|iterator
operator|==
literal|null
operator|||
operator|!
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
comment|// not initialized or reached end of the list. start again
block|{
name|iterator
operator|=
name|reportConditions
operator|.
name|iterator
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|reportConditions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ReportCondition
name|reportCondition
init|=
operator|(
name|ReportCondition
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|reportCondition
operator|.
name|getResult
argument_list|()
condition|)
block|{
case|case
name|ReportCondition
operator|.
name|SUCCESS
case|:
block|{
name|reporter
operator|.
name|addSuccess
argument_list|(
name|reportCondition
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|ReportCondition
operator|.
name|WARNING
case|:
block|{
name|reporter
operator|.
name|addWarning
argument_list|(
name|reportCondition
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|reportCondition
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
name|ReportCondition
operator|.
name|FAILURE
case|:
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|reportCondition
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|reportCondition
operator|.
name|getReason
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
specifier|public
name|void
name|addReturnValue
parameter_list|(
name|int
name|result
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|reportConditions
operator|.
name|add
argument_list|(
operator|new
name|ReportCondition
argument_list|(
name|result
argument_list|,
name|artifact
argument_list|,
name|reason
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|clearList
parameter_list|()
block|{
name|reportConditions
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

