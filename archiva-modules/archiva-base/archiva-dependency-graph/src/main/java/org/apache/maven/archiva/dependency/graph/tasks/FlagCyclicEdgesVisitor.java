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
name|dependency
operator|.
name|graph
operator|.
name|tasks
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|dependency
operator|.
name|graph
operator|.
name|DependencyGraph
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
name|dependency
operator|.
name|graph
operator|.
name|DependencyGraphEdge
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
name|dependency
operator|.
name|graph
operator|.
name|walk
operator|.
name|BaseVisitor
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
name|dependency
operator|.
name|graph
operator|.
name|walk
operator|.
name|DependencyGraphVisitor
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
name|dependency
operator|.
name|graph
operator|.
name|walk
operator|.
name|DependencyGraphWalker
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
name|model
operator|.
name|ArtifactReference
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * FlagCyclicEdgesVisitor   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|FlagCyclicEdgesVisitor
extends|extends
name|BaseVisitor
implements|implements
name|DependencyGraphVisitor
block|{
specifier|private
name|DependencyGraphWalker
name|walker
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|DependencyGraphEdge
argument_list|>
name|cyclicEdges
init|=
operator|new
name|HashSet
argument_list|<
name|DependencyGraphEdge
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|FlagCyclicEdgesVisitor
parameter_list|(
name|DependencyGraphWalker
name|walker
parameter_list|)
block|{
name|this
operator|.
name|walker
operator|=
name|walker
expr_stmt|;
block|}
specifier|public
name|void
name|discoverEdge
parameter_list|(
name|DependencyGraphEdge
name|edge
parameter_list|)
block|{
name|ArtifactReference
name|artifact
init|=
name|edge
operator|.
name|getNodeTo
argument_list|()
decl_stmt|;
comment|// Process for cyclic edges.
if|if
condition|(
name|walker
operator|.
name|getNodeVisitState
argument_list|(
name|artifact
argument_list|)
operator|==
name|DependencyGraphWalker
operator|.
name|PROCESSING
condition|)
block|{
name|edge
operator|.
name|setDisabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|edge
operator|.
name|setDisabledType
argument_list|(
name|DependencyGraph
operator|.
name|DISABLED_CYCLIC
argument_list|)
expr_stmt|;
name|edge
operator|.
name|setDisabledReason
argument_list|(
literal|"Cycle detected"
argument_list|)
expr_stmt|;
comment|// TODO: insert into reason the path for the cycle that was detected.
name|cyclicEdges
operator|.
name|add
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Set
argument_list|<
name|DependencyGraphEdge
argument_list|>
name|getCyclicEdges
parameter_list|()
block|{
return|return
name|cyclicEdges
return|;
block|}
block|}
end_class

end_unit

