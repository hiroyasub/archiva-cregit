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
name|functors
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
comment|/**  * NodePredicate   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|NodePredicate
implements|implements
name|Predicate
block|{
specifier|private
name|ArtifactReference
name|ref
decl_stmt|;
specifier|public
name|NodePredicate
parameter_list|(
name|ArtifactReference
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
specifier|public
name|NodePredicate
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
block|{
name|this
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|evaluate
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|boolean
name|satisfies
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|DependencyGraphNode
condition|)
block|{
name|DependencyGraphNode
name|node
init|=
operator|(
name|DependencyGraphNode
operator|)
name|object
decl_stmt|;
name|satisfies
operator|=
name|ref
operator|.
name|equals
argument_list|(
name|node
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|satisfies
return|;
block|}
block|}
end_class

end_unit

