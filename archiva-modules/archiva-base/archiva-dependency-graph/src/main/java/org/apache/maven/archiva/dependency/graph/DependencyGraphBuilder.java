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
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|VersionedReference
import|;
end_import

begin_comment
comment|/**  * DependencyGraphBuilder   *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|DependencyGraphBuilder
block|{
comment|/**      * Given a node and a versioned project rexpandeference, resolve the details of the node, creating      * any dependencies and edges as needed.      *       * @param graph the graph to add nodes and edges to.      * @param node the node where the resolution should occur.      * @param versionedProjectReference the versioned project reference for the node      *                                  that needs to be resolved.      */
specifier|public
name|void
name|resolveNode
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|,
name|DependencyGraphNode
name|node
parameter_list|,
name|VersionedReference
name|versionedProjectReference
parameter_list|)
function_decl|;
comment|/**      * Create a new graph, with the root of the graph for the node specified.       *       * @param versionedProjectReference the root node for the graph.      * @return the new DependencyGraph, complete with root node and direct dependencies.       */
specifier|public
name|DependencyGraph
name|createGraph
parameter_list|(
name|VersionedReference
name|versionedProjectReference
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

