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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Map
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
name|map
operator|.
name|MultiValueMap
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
name|DependencyGraphKeys
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
name|PotentialCyclicEdgeProducer
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
name|ToArtifactReferenceTransformer
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
comment|/**  * RefineConflictsTask   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RefineConflictsTask
implements|implements
name|GraphTask
implements|,
name|PotentialCyclicEdgeProducer
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|RefineConflictsVisitor
name|refineConflictsVisitor
init|=
operator|new
name|RefineConflictsVisitor
argument_list|()
decl_stmt|;
name|MultiValueMap
name|depMap
init|=
operator|new
name|MultiValueMap
argument_list|()
decl_stmt|;
comment|// Identify deps that need to be resolved.
for|for
control|(
name|DependencyGraphNode
name|node
range|:
name|graph
operator|.
name|getNodes
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|DependencyGraphKeys
operator|.
name|toManagementKey
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
comment|// This will add this node to the specified key, not replace a previous one.
name|depMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
comment|// Process those depMap entries with more than 1 value.
name|ToArtifactReferenceTransformer
name|nodeToArtifact
init|=
operator|new
name|ToArtifactReferenceTransformer
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|DependencyGraphNode
argument_list|>
argument_list|>
argument_list|>
name|it
init|=
name|depMap
operator|.
name|entrySet
argument_list|()
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
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|DependencyGraphNode
argument_list|>
argument_list|>
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|DependencyGraphNode
argument_list|>
name|nodes
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|nodes
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|List
argument_list|<
name|DependencyGraphNode
argument_list|>
name|conflictingArtifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|DependencyGraphNode
argument_list|>
argument_list|()
decl_stmt|;
name|conflictingArtifacts
operator|.
name|addAll
argument_list|(
name|nodes
argument_list|)
expr_stmt|;
name|CollectionUtils
operator|.
name|transform
argument_list|(
name|conflictingArtifacts
argument_list|,
name|nodeToArtifact
argument_list|)
expr_stmt|;
name|refineConflictsVisitor
operator|.
name|resetConflictingArtifacts
argument_list|()
expr_stmt|;
name|refineConflictsVisitor
operator|.
name|addAllConflictingArtifacts
argument_list|(
name|conflictingArtifacts
argument_list|)
expr_stmt|;
name|walker
operator|.
name|visit
argument_list|(
name|graph
argument_list|,
name|refineConflictsVisitor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|String
name|getTaskId
parameter_list|()
block|{
return|return
literal|"refine-conflicts"
return|;
block|}
block|}
end_class

end_unit

