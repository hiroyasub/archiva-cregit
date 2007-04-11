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
name|proxy
operator|.
name|policy
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * Policy to apply after the fetch of content, but before returning to  * the client.   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|PostfetchPolicy
block|{
comment|/**      * Apply the policy using the provided policy code and local file.      *       * @param policyCode the policy code to use.      * @param localFile the local file that might affect the policy.      * @return true if the policy passes, false if the policy prevents      *      the returning of success to the client.      */
specifier|public
name|boolean
name|applyPolicy
parameter_list|(
name|String
name|policyCode
parameter_list|,
name|File
name|localFile
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

