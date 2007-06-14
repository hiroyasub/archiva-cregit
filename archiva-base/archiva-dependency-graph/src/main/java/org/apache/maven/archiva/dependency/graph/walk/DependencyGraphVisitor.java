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

begin_comment
comment|/**  * Interface for progress during search.  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|DependencyGraphVisitor
block|{
comment|/**      * Called once, for when the graph itself is discovered.      *       * @param graph the graph that was discovered.      */
specifier|public
name|void
name|discoverGraph
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|)
function_decl|;
comment|/**      * Called for each node, when that node is visited.      *       * @param node the node that is being visited.      */
specifier|public
name|void
name|discoverNode
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
function_decl|;
comment|/**      * Called for each edge, when that edge is visited.      *       * @param edge the edge that is being visited.      */
specifier|public
name|void
name|discoverEdge
parameter_list|(
name|DependencyGraphEdge
name|edge
parameter_list|)
function_decl|;
comment|/**      * Called for each edge, when that edge has been fully visited.      *       * @param edge the edge that was finished being visited.      */
specifier|public
name|void
name|finishEdge
parameter_list|(
name|DependencyGraphEdge
name|edge
parameter_list|)
function_decl|;
comment|/**      * Called for each node, when the node has been fully visited.      *       * @param node the node that was finished being visited.      */
specifier|public
name|void
name|finishNode
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
function_decl|;
comment|/**      * Called once, for when the graph is finished being visited.      *       * @param graph the graph that finished being visited.      */
specifier|public
name|void
name|finishGraph
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

