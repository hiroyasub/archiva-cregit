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

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ResourceBundle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * Abstract policy class that handles the name and description loading with message bundles.  *  * The prefix for the keys is normally:  *<ul>  *<li>Policies: POLICY-ID.policy.</li>  *<li>Options: POLICY-ID.option.</li>  *</ul>  *  * This prefix can be changed by subclasses.  *  * For each policy and each option there must exist a name and description entry in the message bundle.  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractPolicy
implements|implements
name|Policy
block|{
specifier|private
name|String
name|policyPrefix
decl_stmt|;
specifier|private
name|String
name|optionPrefix
decl_stmt|;
specifier|public
name|AbstractPolicy
parameter_list|()
block|{
name|policyPrefix
operator|=
name|getId
argument_list|()
operator|+
literal|".policy."
expr_stmt|;
name|optionPrefix
operator|=
name|getId
argument_list|()
operator|+
literal|".option."
expr_stmt|;
block|}
specifier|protected
name|String
name|getPolicyPrefix
parameter_list|()
block|{
return|return
name|policyPrefix
return|;
block|}
specifier|protected
name|String
name|getOptionPrefix
parameter_list|()
block|{
return|return
name|optionPrefix
return|;
block|}
specifier|protected
name|void
name|setPolicyPrefix
parameter_list|(
name|String
name|policyPrefix
parameter_list|)
block|{
name|this
operator|.
name|policyPrefix
operator|=
name|policyPrefix
expr_stmt|;
block|}
specifier|public
name|void
name|setOptionPrefix
parameter_list|(
name|String
name|optionPrefix
parameter_list|)
block|{
name|this
operator|.
name|optionPrefix
operator|=
name|optionPrefix
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
name|ResourceBundle
name|getBundle
parameter_list|(
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|ResourceBundle
operator|.
name|getBundle
argument_list|(
name|RESOURCE_BUNDLE
argument_list|,
name|locale
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getName
argument_list|(
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|(
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
name|getPolicyPrefix
argument_list|()
operator|+
literal|"name"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|(
name|Locale
name|locale
parameter_list|)
block|{
return|return
name|MessageFormat
operator|.
name|format
argument_list|(
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
name|getPolicyPrefix
argument_list|()
operator|+
literal|"description"
argument_list|)
argument_list|,
name|getOptions
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|o
lambda|->
name|o
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getOptionDescription
parameter_list|(
name|Locale
name|locale
parameter_list|,
name|PolicyOption
name|option
parameter_list|)
block|{
return|return
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
name|getOptionPrefix
argument_list|()
operator|+
name|option
operator|.
name|getId
argument_list|()
operator|+
literal|".description"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getOptionName
parameter_list|(
name|Locale
name|locale
parameter_list|,
name|PolicyOption
name|option
parameter_list|)
block|{
return|return
name|getBundle
argument_list|(
name|locale
argument_list|)
operator|.
name|getString
argument_list|(
name|getOptionPrefix
argument_list|()
operator|+
name|option
operator|.
name|getId
argument_list|()
operator|+
literal|".name"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

