begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|dependency
operator|.
name|tree
operator|.
name|maven2
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
name|archiva
operator|.
name|maven2
operator|.
name|model
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven2
operator|.
name|model
operator|.
name|TreeEntry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|modelmapper
operator|.
name|ModelMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|graph
operator|.
name|DependencyNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|graph
operator|.
name|DependencyVisitor
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

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
specifier|public
class|class
name|TreeDependencyNodeVisitor
implements|implements
name|DependencyVisitor
block|{
specifier|final
name|List
argument_list|<
name|TreeEntry
argument_list|>
name|treeEntries
decl_stmt|;
specifier|private
name|TreeEntry
name|currentEntry
decl_stmt|;
specifier|private
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|graph
operator|.
name|DependencyNode
name|firstDependencyNode
decl_stmt|;
specifier|public
name|TreeDependencyNodeVisitor
parameter_list|(
name|List
argument_list|<
name|TreeEntry
argument_list|>
name|treeEntries
parameter_list|)
block|{
name|this
operator|.
name|treeEntries
operator|=
name|treeEntries
expr_stmt|;
block|}
specifier|public
name|boolean
name|visitEnter
parameter_list|(
name|DependencyNode
name|dependencyNode
parameter_list|)
block|{
name|TreeEntry
name|entry
init|=
operator|new
name|TreeEntry
argument_list|(
name|getModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|dependencyNode
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|,
name|Artifact
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|entry
operator|.
name|getArtifact
argument_list|()
operator|.
name|setScope
argument_list|(
name|dependencyNode
operator|.
name|getDependency
argument_list|()
operator|.
name|getScope
argument_list|()
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setParent
argument_list|(
name|currentEntry
argument_list|)
expr_stmt|;
name|currentEntry
operator|=
name|entry
expr_stmt|;
if|if
condition|(
name|firstDependencyNode
operator|==
literal|null
condition|)
block|{
name|firstDependencyNode
operator|=
name|dependencyNode
expr_stmt|;
name|treeEntries
operator|.
name|add
argument_list|(
name|currentEntry
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|currentEntry
operator|.
name|getParent
argument_list|()
operator|.
name|getChilds
argument_list|()
operator|.
name|add
argument_list|(
name|currentEntry
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitLeave
parameter_list|(
name|DependencyNode
name|dependencyNode
parameter_list|)
block|{
name|currentEntry
operator|=
name|currentEntry
operator|.
name|getParent
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|private
specifier|static
class|class
name|ModelMapperHolder
block|{
specifier|private
specifier|static
name|ModelMapper
name|MODEL_MAPPER
init|=
operator|new
name|ModelMapper
argument_list|()
decl_stmt|;
block|}
specifier|protected
name|ModelMapper
name|getModelMapper
parameter_list|()
block|{
return|return
name|ModelMapperHolder
operator|.
name|MODEL_MAPPER
return|;
block|}
block|}
end_class

end_unit

