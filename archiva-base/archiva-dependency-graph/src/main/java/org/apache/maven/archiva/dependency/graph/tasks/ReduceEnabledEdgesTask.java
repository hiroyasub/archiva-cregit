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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|functors
operator|.
name|TruePredicate
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
name|GraphTask
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
name|dependency
operator|.
name|graph
operator|.
name|walk
operator|.
name|WalkDepthFirstSearch
import|;
end_import

begin_comment
comment|/**  * ReduceEnabledEdgesTask   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component   *      role="org.apache.maven.archiva.dependency.graph.GraphTask"  *      role-hint="reduce-enabled-edges"  *      instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ReduceEnabledEdgesTask
implements|implements
name|GraphTask
block|{
specifier|public
name|void
name|executeTask
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|)
block|{
name|DependencyGraphWalker
name|walker
init|=
operator|new
name|WalkDepthFirstSearch
argument_list|()
decl_stmt|;
name|walker
operator|.
name|setEdgePredicate
argument_list|(
name|TruePredicate
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|ReduceEnabledEdgesVisitor
name|reduceEnabledEdgesResolver
init|=
operator|new
name|ReduceEnabledEdgesVisitor
argument_list|()
decl_stmt|;
name|walker
operator|.
name|visit
argument_list|(
name|graph
argument_list|,
name|reduceEnabledEdgesResolver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getTaskId
parameter_list|()
block|{
return|return
literal|"reduce-enabled-edges"
return|;
block|}
block|}
end_class

end_unit

