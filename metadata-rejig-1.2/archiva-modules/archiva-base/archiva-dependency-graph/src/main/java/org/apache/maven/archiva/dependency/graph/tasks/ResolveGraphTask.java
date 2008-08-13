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
name|CollectionUtils
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
name|DependencyGraphBuilder
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
name|UnresolvedGraphNodePredicate
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

begin_comment
comment|/**  * Loop through the unresolved nodes and resolve them, until there  * are no more unresolved nodes.  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ResolveGraphTask
implements|implements
name|GraphTask
implements|,
name|PotentialCyclicEdgeProducer
block|{
specifier|private
name|DependencyGraphBuilder
name|builder
decl_stmt|;
specifier|private
name|int
name|resolvedCount
init|=
literal|0
decl_stmt|;
specifier|private
name|VersionedReference
name|toVersionedReference
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
block|{
name|VersionedReference
name|ref
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|ref
operator|.
name|setGroupId
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setArtifactId
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|ref
operator|.
name|setVersion
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ref
return|;
block|}
specifier|public
name|void
name|executeTask
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|)
block|{
name|resolvedCount
operator|=
literal|0
expr_stmt|;
name|VersionedReference
name|rootRef
init|=
name|toVersionedReference
argument_list|(
name|graph
operator|.
name|getRootNode
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|graph
operator|.
name|getRootNode
argument_list|()
operator|.
name|isResolved
argument_list|()
condition|)
block|{
name|builder
operator|.
name|resolveNode
argument_list|(
name|graph
argument_list|,
name|graph
operator|.
name|getRootNode
argument_list|()
argument_list|,
name|rootRef
argument_list|)
expr_stmt|;
name|resolvedCount
operator|++
expr_stmt|;
block|}
name|boolean
name|done
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
name|DependencyGraphNode
name|node
init|=
name|findUnresolvedNode
argument_list|(
name|graph
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
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
name|VersionedReference
name|otherRef
init|=
name|toVersionedReference
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|builder
operator|.
name|resolveNode
argument_list|(
name|graph
argument_list|,
name|node
argument_list|,
name|otherRef
argument_list|)
expr_stmt|;
name|resolvedCount
operator|++
expr_stmt|;
block|}
block|}
specifier|private
name|DependencyGraphNode
name|findUnresolvedNode
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|)
block|{
return|return
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
name|UnresolvedGraphNodePredicate
operator|.
name|getInstance
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|DependencyGraphBuilder
name|getBuilder
parameter_list|()
block|{
return|return
name|builder
return|;
block|}
specifier|public
name|void
name|setBuilder
parameter_list|(
name|DependencyGraphBuilder
name|graphBuilder
parameter_list|)
block|{
name|this
operator|.
name|builder
operator|=
name|graphBuilder
expr_stmt|;
block|}
specifier|public
name|String
name|getTaskId
parameter_list|()
block|{
return|return
literal|"resolve-graph"
return|;
block|}
specifier|public
name|int
name|getResolvedCount
parameter_list|()
block|{
return|return
name|resolvedCount
return|;
block|}
block|}
end_class

end_unit

