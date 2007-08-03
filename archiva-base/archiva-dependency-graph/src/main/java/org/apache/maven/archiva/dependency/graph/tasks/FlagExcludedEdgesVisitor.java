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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
import|;
end_import

begin_comment
comment|/**  * FlagExcludedEdgesVisitor   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|FlagExcludedEdgesVisitor
extends|extends
name|BaseVisitor
implements|implements
name|DependencyGraphVisitor
block|{
specifier|private
name|Stack
name|nodePath
init|=
operator|new
name|Stack
argument_list|()
decl_stmt|;
specifier|public
name|void
name|discoverEdge
parameter_list|(
name|DependencyGraphEdge
name|edge
parameter_list|)
block|{
name|ArtifactReference
name|artifact
init|=
name|edge
operator|.
name|getNodeTo
argument_list|()
decl_stmt|;
comment|// Process for excluded edges.
name|String
name|toKey
init|=
name|DependencyGraphKeys
operator|.
name|toManagementKey
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|this
operator|.
name|nodePath
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
name|DependencyGraphNode
name|pathNode
init|=
operator|(
name|DependencyGraphNode
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// Process dependency declared exclusions.
if|if
condition|(
name|pathNode
operator|.
name|getExcludes
argument_list|()
operator|.
name|contains
argument_list|(
name|toKey
argument_list|)
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
name|DISABLED_EXCLUDED
argument_list|)
expr_stmt|;
name|String
name|whoExcluded
init|=
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
name|pathNode
argument_list|)
decl_stmt|;
name|edge
operator|.
name|setDisabledReason
argument_list|(
literal|"Specifically Excluded by "
operator|+
name|whoExcluded
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
specifier|public
name|void
name|discoverNode
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
block|{
name|super
operator|.
name|discoverNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|nodePath
operator|.
name|push
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|finishNode
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
block|{
name|super
operator|.
name|finishNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|DependencyGraphNode
name|pathNode
init|=
operator|(
name|DependencyGraphNode
operator|)
name|nodePath
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|node
operator|.
name|equals
argument_list|(
name|pathNode
argument_list|)
condition|)
block|{
name|String
name|pathNodeKey
init|=
name|ArtifactReference
operator|.
name|toKey
argument_list|(
name|pathNode
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|finishNodeKey
init|=
name|ArtifactReference
operator|.
name|toKey
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Encountered bad visitor state.  Expected finish on node "
operator|+
name|pathNodeKey
operator|+
literal|", but instead got notified of node "
operator|+
name|finishNodeKey
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

