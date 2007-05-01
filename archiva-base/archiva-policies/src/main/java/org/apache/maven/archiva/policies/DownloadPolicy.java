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
name|Properties
import|;
end_import

begin_comment
comment|/**  * DownloadPolicy   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|DownloadPolicy
block|{
comment|/**      * The IGNORED policy means that the policy is ignored.      */
specifier|public
specifier|static
specifier|final
name|String
name|IGNORED
init|=
literal|"ignored"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|boolean
name|PASS
init|=
literal|true
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|boolean
name|FAIL
init|=
literal|false
decl_stmt|;
comment|/**      * Get the default policy setting.      *       * @return the default policy setting.      */
specifier|public
name|String
name|getDefaultPolicySetting
parameter_list|()
function_decl|;
comment|/**      * Apply the download policy.      *       * @param policySetting the policy setting.      * @param request the list of request properties that the policy might use.      * @param localFile      *       * @return true if the policy passes.      */
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
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

