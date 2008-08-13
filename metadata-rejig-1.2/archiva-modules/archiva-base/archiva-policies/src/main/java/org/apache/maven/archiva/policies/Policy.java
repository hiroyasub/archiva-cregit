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

begin_interface
specifier|public
interface|interface
name|Policy
block|{
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
comment|/**      * Get the display name for this policy.      *      * @todo i18n      *      * @return the name for this policy      */
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

