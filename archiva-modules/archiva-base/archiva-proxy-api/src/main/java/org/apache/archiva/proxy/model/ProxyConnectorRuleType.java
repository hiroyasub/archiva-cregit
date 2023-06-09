begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_enum
specifier|public
enum|enum
name|ProxyConnectorRuleType
block|{
name|WHITE_LIST
argument_list|(
literal|"whiteList"
argument_list|)
block|,
name|BLACK_LIST
argument_list|(
literal|"blackList"
argument_list|)
block|;
specifier|private
name|String
name|ruleType
decl_stmt|;
specifier|private
name|ProxyConnectorRuleType
parameter_list|(
name|String
name|ruleType
parameter_list|)
block|{
name|this
operator|.
name|ruleType
operator|=
name|ruleType
expr_stmt|;
block|}
specifier|public
name|void
name|setRuleType
parameter_list|(
name|String
name|ruleType
parameter_list|)
block|{
name|this
operator|.
name|ruleType
operator|=
name|ruleType
expr_stmt|;
block|}
specifier|public
name|String
name|getRuleType
parameter_list|()
block|{
return|return
name|ruleType
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
literal|"ProxyConnectorRuleType"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{ruleType='"
argument_list|)
operator|.
name|append
argument_list|(
name|ruleType
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
end_enum

end_unit

