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
name|DependencyScope
import|;
end_import

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * DependencyGraphEdge   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DependencyGraphEdge
block|{
specifier|private
name|ArtifactReference
name|nodeFrom
decl_stmt|;
specifier|private
name|ArtifactReference
name|nodeTo
decl_stmt|;
specifier|private
name|String
name|scope
decl_stmt|;
specifier|private
name|boolean
name|disabled
init|=
literal|false
decl_stmt|;
specifier|private
name|int
name|disabledType
decl_stmt|;
specifier|private
name|String
name|disabledReason
decl_stmt|;
specifier|public
name|DependencyGraphEdge
parameter_list|(
name|ArtifactReference
name|fromNode
parameter_list|,
name|ArtifactReference
name|toNode
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|nodeFrom
operator|=
name|fromNode
expr_stmt|;
name|this
operator|.
name|nodeTo
operator|=
name|toNode
expr_stmt|;
name|this
operator|.
name|scope
operator|=
name|DependencyScope
operator|.
name|COMPILE
expr_stmt|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|DependencyGraphEdge
name|other
init|=
operator|(
name|DependencyGraphEdge
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|nodeFrom
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|nodeFrom
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|nodeFrom
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodeFrom
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|nodeTo
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|other
operator|.
name|nodeTo
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|else if
condition|(
operator|!
name|nodeTo
operator|.
name|equals
argument_list|(
name|other
operator|.
name|nodeTo
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getDisabledReason
parameter_list|()
block|{
return|return
name|disabledReason
return|;
block|}
specifier|public
name|int
name|getDisabledType
parameter_list|()
block|{
return|return
name|disabledType
return|;
block|}
specifier|public
name|ArtifactReference
name|getNodeFrom
parameter_list|()
block|{
return|return
name|nodeFrom
return|;
block|}
specifier|public
name|ArtifactReference
name|getNodeTo
parameter_list|()
block|{
return|return
name|nodeTo
return|;
block|}
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|PRIME
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|nodeFrom
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|nodeFrom
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
name|result
operator|=
name|PRIME
operator|*
name|result
operator|+
operator|(
operator|(
name|nodeTo
operator|==
literal|null
operator|)
condition|?
literal|0
else|:
name|nodeTo
operator|.
name|hashCode
argument_list|()
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|isDisabled
parameter_list|()
block|{
return|return
name|disabled
return|;
block|}
specifier|public
name|void
name|setDisabled
parameter_list|(
name|boolean
name|disabled
parameter_list|)
block|{
name|this
operator|.
name|disabled
operator|=
name|disabled
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|disabled
operator|==
literal|false
condition|)
block|{
name|this
operator|.
name|disabledReason
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|disabledType
operator|=
operator|-
literal|1
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setDisabledReason
parameter_list|(
name|String
name|disabledReason
parameter_list|)
block|{
name|this
operator|.
name|disabledReason
operator|=
name|disabledReason
expr_stmt|;
block|}
specifier|public
name|void
name|setDisabledType
parameter_list|(
name|int
name|disabledType
parameter_list|)
block|{
name|this
operator|.
name|disabledType
operator|=
name|disabledType
expr_stmt|;
block|}
specifier|public
name|void
name|setNodeFrom
parameter_list|(
name|ArtifactReference
name|ref
parameter_list|)
block|{
name|this
operator|.
name|nodeFrom
operator|=
name|ref
expr_stmt|;
block|}
specifier|public
name|void
name|setNodeFrom
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
block|{
name|this
operator|.
name|nodeFrom
operator|=
name|node
operator|.
name|getArtifact
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setNodeTo
parameter_list|(
name|ArtifactReference
name|ref
parameter_list|)
block|{
name|this
operator|.
name|nodeTo
operator|=
name|ref
expr_stmt|;
block|}
specifier|public
name|void
name|setNodeTo
parameter_list|(
name|DependencyGraphNode
name|node
parameter_list|)
block|{
name|this
operator|.
name|nodeTo
operator|=
name|node
operator|.
name|getArtifact
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuffer
name|sb
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"GraphEdge["
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"from="
argument_list|)
operator|.
name|append
argument_list|(
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
name|nodeFrom
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|",to="
argument_list|)
operator|.
name|append
argument_list|(
name|DependencyGraphKeys
operator|.
name|toKey
argument_list|(
name|nodeTo
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

