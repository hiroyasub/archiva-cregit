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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
comment|/**  * Policy to apply after the download has completed, but before the  * resource is made available to the calling client.  *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|DownloadErrorPolicy
extends|extends
name|Policy
block|{
comment|/**      * Apply the download error policy.      *      * @param policySetting      the policy setting.      * @param request            the list of request properties that the policy might use.      * @param localFile      * @param exception          the exception that triggered the error      * @param previousExceptions any previously triggered exceptions      * @return whether to process the exception or not      * @throws PolicyConfigurationException if the policy is improperly configured      */
specifier|public
name|boolean
name|applyPolicy
parameter_list|(
name|String
name|policySetting
parameter_list|,
name|Properties
name|request
parameter_list|,
name|File
name|localFile
parameter_list|,
name|Exception
name|exception
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Exception
argument_list|>
name|previousExceptions
parameter_list|)
throws|throws
name|PolicyConfigurationException
function_decl|;
block|}
end_interface

end_unit

