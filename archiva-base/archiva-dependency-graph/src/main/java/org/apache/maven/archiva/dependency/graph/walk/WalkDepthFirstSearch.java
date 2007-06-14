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
name|walk
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
name|commons
operator|.
name|collections
operator|.
name|Predicate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|functors
operator|.
name|NotPredicate
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
name|DependencyGraphNode
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
name|functors
operator|.
name|EdgeDisabledPredicate
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
name|HashMap
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Perform a walk of the graph using the DepthFirstSearch algorithm.  *   * NOTE: Default edgePredicate is to NOT traverse disabled edges.  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|WalkDepthFirstSearch
implements|implements
name|DependencyGraphWalker
block|{
specifier|private
name|Map
name|nodeVisitStates
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|Predicate
name|edgePredicate
decl_stmt|;
specifier|public
name|WalkDepthFirstSearch
parameter_list|()
block|{
name|this
operator|.
name|edgePredicate
operator|=
name|NotPredicate
operator|.
name|getInstance
argument_list|(
operator|new
name|EdgeDisabledPredicate
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Predicate
name|getEdgePredicate
parameter_list|()
block|{
return|return
name|this
operator|.
name|edgePredicate
return|;
block|}
specifier|public
name|void
name|setEdgePredicate
parameter_list|(
name|Predicate
name|edgePredicate
parameter_list|)
block|{
name|this
operator|.
name|edgePredicate
operator|=
name|edgePredicate
expr_stmt|;
block|}
specifier|public
name|Integer
name|getNodeVisitState
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return
name|SEEN
return|;
block|}
return|return
operator|(
name|Integer
operator|)
name|nodeVisitStates
operator|.
name|get
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Integer
name|getNodeVisitState
parameter_list|(
name|ArtifactReference
name|artifact
parameter_list|)
block|{
return|return
operator|(
name|Integer
operator|)
name|nodeVisitStates
operator|.
name|get
argument_list|(
name|artifact
argument_list|)
return|;
block|}
specifier|public
name|void
name|setNodeVisitState
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|,
name|Integer
name|state
parameter_list|)
block|{
name|this
operator|.
name|nodeVisitStates
operator|.
name|put
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setNodeVisitState
parameter_list|(
name|ArtifactReference
name|artifact
parameter_list|,
name|Integer
name|state
parameter_list|)
block|{
name|this
operator|.
name|nodeVisitStates
operator|.
name|put
argument_list|(
name|artifact
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|visitEdge
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|,
name|DependencyGraphEdge
name|e
parameter_list|,
name|DependencyGraphVisitor
name|visitor
parameter_list|)
block|{
name|visitor
operator|.
name|discoverEdge
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|DependencyGraphNode
name|node
init|=
name|graph
operator|.
name|getNode
argument_list|(
name|e
operator|.
name|getNodeTo
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|getNodeVisitState
argument_list|(
name|node
argument_list|)
operator|==
name|UNSEEN
condition|)
block|{
name|visitNode
argument_list|(
name|graph
argument_list|,
name|node
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
block|}
name|visitor
operator|.
name|finishEdge
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|visitNode
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|,
name|DependencyGraphNode
name|node
parameter_list|,
name|DependencyGraphVisitor
name|visitor
parameter_list|)
block|{
name|setNodeVisitState
argument_list|(
name|node
argument_list|,
name|PROCESSING
argument_list|)
expr_stmt|;
name|visitor
operator|.
name|discoverNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|Iterator
name|edges
init|=
name|graph
operator|.
name|getEdgesFrom
argument_list|(
name|node
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|edges
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DependencyGraphEdge
name|e
init|=
operator|(
name|DependencyGraphEdge
operator|)
name|edges
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|edgePredicate
operator|.
name|evaluate
argument_list|(
name|e
argument_list|)
condition|)
block|{
name|visitEdge
argument_list|(
name|graph
argument_list|,
name|e
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
block|}
block|}
name|visitor
operator|.
name|finishNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|setNodeVisitState
argument_list|(
name|node
argument_list|,
name|SEEN
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|visit
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|,
name|DependencyGraphVisitor
name|visitor
parameter_list|)
block|{
name|visit
argument_list|(
name|graph
argument_list|,
name|graph
operator|.
name|getRootNode
argument_list|()
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|visit
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|,
name|DependencyGraphNode
name|startNode
parameter_list|,
name|DependencyGraphVisitor
name|visitor
parameter_list|)
block|{
name|nodeVisitStates
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Iterator
name|nodes
init|=
name|graph
operator|.
name|getNodes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|nodes
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|setNodeVisitState
argument_list|(
operator|(
name|DependencyGraphNode
operator|)
name|nodes
operator|.
name|next
argument_list|()
argument_list|,
name|UNSEEN
argument_list|)
expr_stmt|;
block|}
name|visitor
operator|.
name|discoverGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
name|visitNode
argument_list|(
name|graph
argument_list|,
name|startNode
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
name|visitor
operator|.
name|finishGraph
argument_list|(
name|graph
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

