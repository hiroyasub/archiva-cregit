begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|policies
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Standard Options for policies.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_enum
specifier|public
enum|enum
name|StandardOption
implements|implements
name|PolicyOption
block|{
name|NOOP
argument_list|(
literal|"noop"
argument_list|)
block|,
name|YES
argument_list|(
literal|"yes"
argument_list|)
block|,
name|NO
argument_list|(
literal|"no"
argument_list|)
block|;
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
name|StandardOption
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
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
specifier|public
name|PolicyOption
name|ofId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
for|for
control|(
name|StandardOption
name|option
range|:
name|StandardOption
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|option
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|option
return|;
block|}
block|}
return|return
name|NOOP
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|id
return|;
block|}
block|}
end_enum

end_unit

