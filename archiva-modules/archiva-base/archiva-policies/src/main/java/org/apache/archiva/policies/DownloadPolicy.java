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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|StorageAsset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * DownloadPolicy   *  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|DownloadPolicy
extends|extends
name|Policy
block|{
comment|/**      * Apply the download policy.      *       * @param policySetting the policy setting.      * @param request the list of request properties that the policy might use.      * @param localFile      *      * @throws PolicyViolationException if the policy has been violated.      */
name|void
name|applyPolicy
parameter_list|(
name|String
name|policySetting
parameter_list|,
name|Properties
name|request
parameter_list|,
name|StorageAsset
name|localFile
parameter_list|)
throws|throws
name|PolicyViolationException
throws|,
name|PolicyConfigurationException
function_decl|;
block|}
end_interface

end_unit

