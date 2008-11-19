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
name|functors
operator|.
name|EdgeWithinScopePredicate
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
comment|/**  * ReduceScopeVisitor   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ReduceScopeVisitor
extends|extends
name|AbstractReduceEdgeVisitor
implements|implements
name|DependencyGraphVisitor
block|{
specifier|private
name|Predicate
name|scopedPredicate
decl_stmt|;
specifier|public
name|ReduceScopeVisitor
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|scopedPredicate
operator|=
operator|new
name|EdgeWithinScopePredicate
argument_list|(
name|scope
argument_list|)
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
if|if
condition|(
operator|!
name|scopedPredicate
operator|.
name|evaluate
argument_list|(
name|edge
argument_list|)
condition|)
block|{
name|super
operator|.
name|graph
operator|.
name|removeEdge
argument_list|(
name|edge
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

