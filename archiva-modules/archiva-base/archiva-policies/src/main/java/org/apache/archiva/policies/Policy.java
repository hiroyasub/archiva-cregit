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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|MissingResourceException
import|;
end_import

begin_comment
comment|/**  * This is a generic interface for policies. Policies define different actions to apply to artifacts during the  * repository lifecycle, e.g. download, upload, errors.  */
end_comment

begin_interface
specifier|public
interface|interface
name|Policy
block|{
name|String
name|RESOURCE_BUNDLE
init|=
literal|"archiva_policies"
decl_stmt|;
comment|/**      * Get the list of options for this policy.      *      * @return the list of options for this policy.      */
name|List
argument_list|<
name|String
argument_list|>
name|getOptions
parameter_list|()
function_decl|;
comment|/**      * Get the default option for this policy.      *      * @return the default policy for this policy.      */
name|String
name|getDefaultOption
parameter_list|()
function_decl|;
comment|/**      * Get the id for this policy.      *      * @return the id for this policy.      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Get the display name for this policy.      *      * @return the name for this policy      */
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Get the policy name in the language of the given locale.      * @param locale The locale      * @return The policy name      */
name|String
name|getName
parameter_list|(
name|Locale
name|locale
parameter_list|)
function_decl|;
comment|/**      * Return a description of the policy.      * @param locale The language      * @return The description      */
name|String
name|getDescription
parameter_list|(
name|Locale
name|locale
parameter_list|)
function_decl|;
comment|/**      * Returns a description for the given option.      * @param locale The locale for the description.      * @param option The option to ask the description for.      * @return A description of the option in the requested language.      * @throws MissingResourceException if the option is not known by this policy.      */
name|String
name|getOptionDescription
parameter_list|(
name|Locale
name|locale
parameter_list|,
name|String
name|option
parameter_list|)
throws|throws
name|MissingResourceException
function_decl|;
comment|/**      * Returns a name for the given option.      * @param locale The locale for the name      * @param option  The option identifier      * @return  A name in the requested language.      * @throws MissingResourceException if the option is not known by this policy.      */
name|String
name|getOptionName
parameter_list|(
name|Locale
name|locale
parameter_list|,
name|String
name|option
parameter_list|)
throws|throws
name|MissingResourceException
function_decl|;
block|}
end_interface

end_unit

