begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|repository
operator|.
name|admin
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

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
name|List
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_class
specifier|public
class|class
name|FileType
implements|implements
name|Serializable
block|{
comment|/**      * Field id.      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * Field patterns.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|patterns
decl_stmt|;
specifier|public
name|FileType
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|FileType
parameter_list|(
name|String
name|id
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|patterns
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|patterns
operator|=
name|patterns
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPatterns
parameter_list|()
block|{
if|if
condition|(
name|patterns
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|patterns
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|patterns
return|;
block|}
specifier|public
name|void
name|setPatterns
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|patterns
parameter_list|)
block|{
name|this
operator|.
name|patterns
operator|=
name|patterns
expr_stmt|;
block|}
specifier|public
name|void
name|addPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|getPatterns
argument_list|()
operator|.
name|add
argument_list|(
name|pattern
argument_list|)
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
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|FileType
name|fileType
init|=
operator|(
name|FileType
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|?
operator|!
name|id
operator|.
name|equals
argument_list|(
name|fileType
operator|.
name|id
argument_list|)
else|:
name|fileType
operator|.
name|id
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
return|return
name|id
operator|!=
literal|null
condition|?
literal|37
operator|+
name|id
operator|.
name|hashCode
argument_list|()
else|:
literal|0
return|;
block|}
block|}
end_class

end_unit

