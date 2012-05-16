begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"artifactContentEntry"
argument_list|)
specifier|public
class|class
name|ArtifactContentEntry
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|text
decl_stmt|;
specifier|private
name|boolean
name|file
decl_stmt|;
specifier|private
name|int
name|depth
decl_stmt|;
specifier|private
name|boolean
name|hasChildren
decl_stmt|;
specifier|public
name|ArtifactContentEntry
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|ArtifactContentEntry
parameter_list|(
name|String
name|text
parameter_list|,
name|boolean
name|file
parameter_list|,
name|int
name|depth
parameter_list|,
name|boolean
name|hasChildren
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
name|this
operator|.
name|depth
operator|=
name|depth
expr_stmt|;
name|this
operator|.
name|hasChildren
operator|=
name|hasChildren
expr_stmt|;
block|}
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|text
return|;
block|}
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
specifier|public
name|boolean
name|isFile
parameter_list|()
block|{
return|return
name|file
return|;
block|}
specifier|public
name|void
name|setFile
parameter_list|(
name|boolean
name|file
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
block|}
specifier|public
name|int
name|getDepth
parameter_list|()
block|{
return|return
name|depth
return|;
block|}
specifier|public
name|void
name|setDepth
parameter_list|(
name|int
name|depth
parameter_list|)
block|{
name|this
operator|.
name|depth
operator|=
name|depth
expr_stmt|;
block|}
specifier|public
name|boolean
name|isHasChildren
parameter_list|()
block|{
return|return
name|hasChildren
return|;
block|}
specifier|public
name|void
name|setHasChildren
parameter_list|(
name|boolean
name|hasChildren
parameter_list|)
block|{
name|this
operator|.
name|hasChildren
operator|=
name|hasChildren
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|ArtifactContentEntry
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ArtifactContentEntry
name|that
init|=
operator|(
name|ArtifactContentEntry
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|hasChildren
operator|!=
name|that
operator|.
name|hasChildren
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|depth
operator|!=
name|that
operator|.
name|depth
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|file
operator|!=
name|that
operator|.
name|file
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|text
operator|!=
literal|null
condition|?
operator|!
name|text
operator|.
name|equals
argument_list|(
name|that
operator|.
name|text
argument_list|)
else|:
name|that
operator|.
name|text
operator|!=
literal|null
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
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|text
operator|!=
literal|null
condition|?
name|text
operator|.
name|hashCode
argument_list|()
else|:
literal|0
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|file
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|depth
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|hasChildren
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"ArtifactContentEntry"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{text='"
argument_list|)
operator|.
name|append
argument_list|(
name|text
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", file="
argument_list|)
operator|.
name|append
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", depth="
argument_list|)
operator|.
name|append
argument_list|(
name|depth
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", children="
argument_list|)
operator|.
name|append
argument_list|(
name|hasChildren
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
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

