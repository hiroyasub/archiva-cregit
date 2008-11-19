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
name|CollectionUtils
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
name|AndPredicate
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|NodePredicate
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
name|OrphanedNodePredicate
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
name|ArchivaProjectModel
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
name|archiva
operator|.
name|model
operator|.
name|DependencyScope
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
name|Exclusion
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
name|VersionedReference
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
name|Iterator
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Utilities for manipulating the DependencyGraph.   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DependencyGraphUtils
block|{
comment|/**      * Standard way to add a model to the graph.      *       * NOTE: Used by archiva-repository-layer runtime and archiva-dependency-graph tests.      *       * @param model the model to add      * @param graph the graph to add it to      * @param fromNode the node to add it from.      */
specifier|public
specifier|static
name|void
name|addNodeFromModel
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|,
name|DependencyGraph
name|graph
parameter_list|,
name|DependencyGraphNode
name|fromNode
parameter_list|)
block|{
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unable to add null model for "
operator|+
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
name|fromNode
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|model
operator|.
name|getRelocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// We need to CHANGE this node.
name|ArtifactReference
name|refTO
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|refTO
operator|.
name|setGroupId
argument_list|(
name|fromNode
operator|.
name|getArtifact
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|refTO
operator|.
name|setArtifactId
argument_list|(
name|fromNode
operator|.
name|getArtifact
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|refTO
operator|.
name|setVersion
argument_list|(
name|fromNode
operator|.
name|getArtifact
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|refTO
operator|.
name|setClassifier
argument_list|(
name|fromNode
operator|.
name|getArtifact
argument_list|()
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|refTO
operator|.
name|setType
argument_list|(
name|fromNode
operator|.
name|getArtifact
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|VersionedReference
name|relocation
init|=
name|model
operator|.
name|getRelocation
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|relocation
operator|.
name|getGroupId
argument_list|()
argument_list|)
condition|)
block|{
name|refTO
operator|.
name|setGroupId
argument_list|(
name|relocation
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|relocation
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
name|refTO
operator|.
name|setArtifactId
argument_list|(
name|relocation
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|relocation
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|refTO
operator|.
name|setVersion
argument_list|(
name|relocation
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DependencyGraphNode
name|nodeTO
init|=
operator|new
name|DependencyGraphNode
argument_list|(
name|refTO
argument_list|)
decl_stmt|;
name|graph
operator|.
name|addNode
argument_list|(
name|nodeTO
argument_list|)
expr_stmt|;
name|collapseNodes
argument_list|(
name|graph
argument_list|,
name|fromNode
argument_list|,
name|nodeTO
argument_list|)
expr_stmt|;
return|return;
block|}
name|boolean
name|isRootNode
init|=
name|graph
operator|.
name|getRootNode
argument_list|()
operator|.
name|equals
argument_list|(
name|fromNode
argument_list|)
decl_stmt|;
name|Iterator
name|it
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|model
operator|.
name|getDependencyManagement
argument_list|()
argument_list|)
condition|)
block|{
name|it
operator|=
name|model
operator|.
name|getDependencyManagement
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Dependency
name|dependency
init|=
operator|(
name|Dependency
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|fromNode
operator|.
name|addDependencyManagement
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|model
operator|.
name|getDependencies
argument_list|()
argument_list|)
condition|)
block|{
name|it
operator|=
name|model
operator|.
name|getDependencies
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Dependency
name|dependency
init|=
operator|(
name|Dependency
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|scope
init|=
name|dependency
operator|.
name|getScope
argument_list|()
decl_stmt|;
comment|// Test scopes *NOT* from root node can be skipped.
if|if
condition|(
name|DependencyScope
operator|.
name|TEST
operator|.
name|equals
argument_list|(
name|scope
argument_list|)
operator|&&
operator|!
name|isRootNode
condition|)
block|{
comment|// skip add of test scope
continue|continue;
block|}
name|ArtifactReference
name|artifactRef
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|artifactRef
operator|.
name|setGroupId
argument_list|(
name|dependency
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|artifactRef
operator|.
name|setArtifactId
argument_list|(
name|dependency
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|artifactRef
operator|.
name|setVersion
argument_list|(
name|dependency
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|artifactRef
operator|.
name|setClassifier
argument_list|(
name|dependency
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|artifactRef
operator|.
name|setType
argument_list|(
name|dependency
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyGraphNode
name|toNode
init|=
operator|new
name|DependencyGraphNode
argument_list|(
name|artifactRef
argument_list|)
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|dependency
operator|.
name|getExclusions
argument_list|()
argument_list|)
condition|)
block|{
name|Iterator
name|itexclusion
init|=
name|dependency
operator|.
name|getExclusions
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itexclusion
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Exclusion
name|exclusion
init|=
operator|(
name|Exclusion
operator|)
name|itexclusion
operator|.
name|next
argument_list|()
decl_stmt|;
name|toNode
operator|.
name|addExclude
argument_list|(
name|exclusion
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|dependency
operator|.
name|isFromParent
argument_list|()
condition|)
block|{
name|toNode
operator|.
name|setFromParent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// Add node (to)
name|graph
operator|.
name|addNode
argument_list|(
name|toNode
argument_list|)
expr_stmt|;
name|DependencyGraphEdge
name|edge
init|=
operator|new
name|DependencyGraphEdge
argument_list|(
name|fromNode
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|toNode
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|edge
operator|.
name|setScope
argument_list|(
name|StringUtils
operator|.
name|defaultIfEmpty
argument_list|(
name|dependency
operator|.
name|getScope
argument_list|()
argument_list|,
name|DependencyScope
operator|.
name|COMPILE
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|dependency
operator|.
name|isOptional
argument_list|()
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
name|DISABLED_OPTIONAL
argument_list|)
expr_stmt|;
name|edge
operator|.
name|setDisabledReason
argument_list|(
literal|"Optional Dependency"
argument_list|)
expr_stmt|;
block|}
name|graph
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
block|}
name|fromNode
operator|.
name|setResolved
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|graph
operator|.
name|addNode
argument_list|(
name|fromNode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Clean out any nodes that may have become orphaned in the graph.      *       * @param graph the graph to check.      */
specifier|public
specifier|static
name|void
name|cleanupOrphanedNodes
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|)
block|{
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|Predicate
name|orphanedNodePredicate
init|=
operator|new
name|OrphanedNodePredicate
argument_list|(
name|graph
argument_list|)
decl_stmt|;
name|Predicate
name|notRootNode
init|=
name|NotPredicate
operator|.
name|getInstance
argument_list|(
operator|new
name|NodePredicate
argument_list|(
name|graph
operator|.
name|getRootNode
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Predicate
name|orphanedChildNodePredicate
init|=
name|AndPredicate
operator|.
name|getInstance
argument_list|(
name|notRootNode
argument_list|,
name|orphanedNodePredicate
argument_list|)
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
comment|// Find orphaned node.
name|DependencyGraphNode
name|orphanedNode
init|=
operator|(
name|DependencyGraphNode
operator|)
name|CollectionUtils
operator|.
name|find
argument_list|(
name|graph
operator|.
name|getNodes
argument_list|()
argument_list|,
name|orphanedChildNodePredicate
argument_list|)
decl_stmt|;
if|if
condition|(
name|orphanedNode
operator|==
literal|null
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
break|break;
block|}
comment|// Remove edges FROM orphaned node.
name|List
name|edgesFrom
init|=
name|graph
operator|.
name|getEdgesFrom
argument_list|(
name|orphanedNode
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|edgesFrom
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DependencyGraphEdge
name|edge
init|=
operator|(
name|DependencyGraphEdge
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|graph
operator|.
name|removeEdge
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
comment|// Remove orphaned node.
name|graph
operator|.
name|removeNode
argument_list|(
name|orphanedNode
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Functionaly similar to {@link #collapseVersions(DependencyGraph, ArtifactReference, String, String)}, but       * in a new, easier to use, format.      *       * 1) Removes the FROM edges connected to the FROM node      * 2) Moves the TO edges connected to the FROM node to the TO node.      * 3) Removes the FROM node (which is now orphaned)        *        * @param graph the graph to perform operation on      * @param nodeFrom the node to collapse from      * @param nodeTo the node to collapse to      */
specifier|public
specifier|static
name|void
name|collapseNodes
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|,
name|DependencyGraphNode
name|nodeFROM
parameter_list|,
name|DependencyGraphNode
name|nodeTO
parameter_list|)
block|{
name|Iterator
name|it
decl_stmt|;
name|Set
name|edgesToRemove
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
comment|// 1) Remove all of the edge.from references from nodeFROM
name|List
name|fromEdges
init|=
name|graph
operator|.
name|getEdgesFrom
argument_list|(
name|nodeFROM
argument_list|)
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|fromEdges
argument_list|)
condition|)
block|{
name|edgesToRemove
operator|.
name|addAll
argument_list|(
name|fromEdges
argument_list|)
expr_stmt|;
block|}
comment|// 2) Swing all of the edge.to references from nodeFROM to nodeTO.
comment|//        System.out.println( "Swinging incoming edges from " + nodeFROM );
comment|//        System.out.println( "                          to " + nodeTO );
name|List
name|toEdges
init|=
name|graph
operator|.
name|getEdgesTo
argument_list|(
name|nodeFROM
argument_list|)
decl_stmt|;
name|it
operator|=
name|toEdges
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DependencyGraphEdge
name|edge
init|=
operator|(
name|DependencyGraphEdge
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// Identify old edge to remove.
name|edgesToRemove
operator|.
name|add
argument_list|(
name|edge
argument_list|)
expr_stmt|;
comment|// Clone edge, set edge.to and add to graph.
name|DependencyGraphEdge
name|newedge
init|=
name|clone
argument_list|(
name|edge
argument_list|)
decl_stmt|;
name|newedge
operator|.
name|setNodeTo
argument_list|(
name|nodeTO
argument_list|)
expr_stmt|;
comment|//            System.out.println( "   edge from: " + edge );
comment|//            System.out.println( "          to: " + newedge );
name|graph
operator|.
name|addEdge
argument_list|(
name|newedge
argument_list|)
expr_stmt|;
block|}
comment|// Actually remove the old edges.
name|it
operator|=
name|edgesToRemove
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DependencyGraphEdge
name|edge
init|=
operator|(
name|DependencyGraphEdge
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|graph
operator|.
name|removeEdge
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
comment|// 3) Remove the nodeFROM
name|graph
operator|.
name|removeNode
argument_list|(
name|nodeFROM
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a clone of an edge.      *       * @param edge the edge to clone.      * @return the cloned edge.      */
specifier|public
specifier|static
name|DependencyGraphEdge
name|clone
parameter_list|(
name|DependencyGraphEdge
name|edge
parameter_list|)
block|{
name|DependencyGraphEdge
name|cloned
init|=
operator|new
name|DependencyGraphEdge
argument_list|(
name|edge
operator|.
name|getNodeFrom
argument_list|()
argument_list|,
name|edge
operator|.
name|getNodeTo
argument_list|()
argument_list|)
decl_stmt|;
name|cloned
operator|.
name|setDisabled
argument_list|(
name|edge
operator|.
name|isDisabled
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setDisabledReason
argument_list|(
name|edge
operator|.
name|getDisabledReason
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setDisabledType
argument_list|(
name|edge
operator|.
name|getDisabledType
argument_list|()
argument_list|)
expr_stmt|;
name|cloned
operator|.
name|setScope
argument_list|(
name|edge
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cloned
return|;
block|}
block|}
end_class

end_unit

