begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|provider
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * ConfigurationEvent  *  *  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationEvent
block|{
specifier|public
specifier|static
specifier|final
name|int
name|SAVED
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|CHANGED
init|=
literal|2
decl_stmt|;
specifier|private
name|int
name|type
decl_stmt|;
specifier|private
name|String
name|tag
decl_stmt|;
specifier|public
name|ConfigurationEvent
parameter_list|(
name|int
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|tag
operator|=
literal|""
expr_stmt|;
block|}
specifier|public
name|ConfigurationEvent
parameter_list|(
name|int
name|type
parameter_list|,
name|String
name|tag
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|tag
operator|=
name|tag
expr_stmt|;
block|}
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|String
name|getTag
parameter_list|( )
block|{
return|return
name|tag
return|;
block|}
specifier|public
name|void
name|setTag
parameter_list|(
name|String
name|tag
parameter_list|)
block|{
name|this
operator|.
name|tag
operator|=
name|tag
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
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|( )
operator|!=
name|o
operator|.
name|getClass
argument_list|( )
condition|)
return|return
literal|false
return|;
name|ConfigurationEvent
name|that
init|=
operator|(
name|ConfigurationEvent
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|type
operator|!=
name|that
operator|.
name|type
condition|)
return|return
literal|false
return|;
return|return
name|tag
operator|.
name|equals
argument_list|(
name|that
operator|.
name|tag
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|( )
block|{
name|int
name|result
init|=
name|type
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|tag
operator|.
name|hashCode
argument_list|( )
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit
