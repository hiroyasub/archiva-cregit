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
name|model
operator|.
name|ArtifactReference
import|;
end_import

begin_comment
comment|/**  * Walk nodes of the {@link DependencyGraph}.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|DependencyGraphWalker
block|{
comment|/**      * A {@link #getNodeVisitState(ArtifactReference)} for a node not yet seen in the walker.      */
specifier|public
specifier|static
specifier|final
name|Integer
name|UNSEEN
init|=
operator|new
name|Integer
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|/**      * A {@link #getNodeVisitState(ArtifactReference)} for a node that is actively being processed,       * but not yet finished processing.      */
specifier|public
specifier|static
specifier|final
name|Integer
name|PROCESSING
init|=
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|/**      * A {@link #getNodeVisitState(ArtifactReference)} for a node that has been seen, and fully processed.      */
specifier|public
specifier|static
specifier|final
name|Integer
name|SEEN
init|=
operator|new
name|Integer
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|/**      * For a provided node, get the current node visit state.      *        * @param node the node that you are interested in.      * @return the state of that node. (Can be {@link #UNSEEN}, {@link #PROCESSING}, or {@link #SEEN} )      */
specifier|public
name|Integer
name|getNodeVisitState
parameter_list|(
name|ArtifactReference
name|artifact
parameter_list|)
function_decl|;
comment|/**      * Get the predicate used to determine if the walker should traverse an edge (or not).      *       * @return the Predicate that returns true for edges that should be traversed.      */
specifier|public
name|Predicate
name|getEdgePredicate
parameter_list|()
function_decl|;
comment|/**      * Set the predicate used for edge traversal      *       * @param edgePredicate the Predicate that returns true for edges that should be traversed.      */
specifier|public
name|void
name|setEdgePredicate
parameter_list|(
name|Predicate
name|edgePredicate
parameter_list|)
function_decl|;
comment|/**      * Visit every node and edge in the graph from the startNode.      *       * @param graph the graph to visit.      * @param startNode the node to start the visit on.      * @param visitor the visitor object to use during this visit.       */
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
function_decl|;
comment|/**      * Visit every node and edge in the entire graph.      *       * @param graph the graph to visit.      * @param visitor the visitor object to use during this visit.       */
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
function_decl|;
block|}
end_interface

end_unit

