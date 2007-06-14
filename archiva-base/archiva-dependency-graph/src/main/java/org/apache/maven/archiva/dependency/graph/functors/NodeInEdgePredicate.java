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
name|model
operator|.
name|ArtifactReference
import|;
end_import

begin_comment
comment|/**  * NodeInEdgePredicate   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|NodeInEdgePredicate
implements|implements
name|Predicate
block|{
specifier|private
name|ArtifactReference
name|nodeRef
decl_stmt|;
specifier|public
name|NodeInEdgePredicate
parameter_list|(
name|ArtifactReference
name|ref
parameter_list|)
block|{
name|this
operator|.
name|nodeRef
operator|=
name|ref
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
name|DependencyGraphEdge
condition|)
block|{
name|DependencyGraphEdge
name|edge
init|=
operator|(
name|DependencyGraphEdge
operator|)
name|object
decl_stmt|;
name|satisfies
operator|=
operator|(
name|edge
operator|.
name|getNodeFrom
argument_list|()
operator|.
name|equals
argument_list|(
name|nodeRef
argument_list|)
operator|||
name|edge
operator|.
name|getNodeTo
argument_list|()
operator|.
name|equals
argument_list|(
name|nodeRef
argument_list|)
operator|)
expr_stmt|;
block|}
return|return
name|satisfies
return|;
block|}
block|}
end_class

end_unit

