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

begin_comment
comment|/**  * GraphCopier   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|GraphCopier
extends|extends
name|BaseVisitor
implements|implements
name|DependencyGraphVisitor
block|{
specifier|protected
name|DependencyGraph
name|copiedGraph
decl_stmt|;
specifier|public
name|DependencyGraph
name|getGraph
parameter_list|()
block|{
return|return
name|copiedGraph
return|;
block|}
specifier|public
name|void
name|setGraph
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|)
block|{
name|this
operator|.
name|copiedGraph
operator|=
name|graph
expr_stmt|;
block|}
specifier|public
name|void
name|discoverNode
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
block|{
if|if
condition|(
name|copiedGraph
operator|==
literal|null
condition|)
block|{
name|copiedGraph
operator|=
operator|new
name|DependencyGraph
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Be sure to override and NOT call this method in your sub class,      * if you want to copy edges based on some kind of criteria.      */
specifier|public
name|void
name|discoverEdge
parameter_list|(
name|DependencyGraphEdge
name|edge
parameter_list|)
block|{
name|copyEdge
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|copyEdge
parameter_list|(
name|DependencyGraphEdge
name|edge
parameter_list|)
block|{
name|DependencyGraphNode
name|nodeFrom
init|=
name|graph
operator|.
name|getNode
argument_list|(
name|edge
operator|.
name|getNodeFrom
argument_list|()
argument_list|)
decl_stmt|;
name|DependencyGraphNode
name|nodeTo
init|=
name|graph
operator|.
name|getNode
argument_list|(
name|edge
operator|.
name|getNodeTo
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|copiedGraph
operator|.
name|addNode
argument_list|(
name|nodeFrom
argument_list|)
expr_stmt|;
name|this
operator|.
name|copiedGraph
operator|.
name|addNode
argument_list|(
name|nodeTo
argument_list|)
expr_stmt|;
name|this
operator|.
name|copiedGraph
operator|.
name|addEdge
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|this
operator|.
name|copiedGraph
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

