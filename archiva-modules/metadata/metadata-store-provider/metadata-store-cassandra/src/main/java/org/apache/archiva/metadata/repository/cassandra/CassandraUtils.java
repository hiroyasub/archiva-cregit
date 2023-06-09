begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|cassandra
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * @author Olivier Lamy  * @since 2.0.0  */
end_comment

begin_class
specifier|public
class|class
name|CassandraUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_VALUE
init|=
literal|""
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SEPARATOR
init|=
literal|"->"
decl_stmt|;
specifier|public
specifier|static
name|String
name|generateKey
parameter_list|(
specifier|final
name|String
modifier|...
name|bases
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|bases
operator|==
literal|null
operator|||
name|bases
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
for|for
control|(
specifier|final
name|String
name|s
range|:
name|bases
control|)
block|{
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|EMPTY_VALUE
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|builder
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|setLength
argument_list|(
name|builder
operator|.
name|length
argument_list|()
operator|-
name|SEPARATOR
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|CassandraUtils
parameter_list|()
block|{
comment|// no-op
block|}
block|}
end_class

end_unit

