begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * The editable part of a managed repository.  */
end_comment

begin_interface
specifier|public
interface|interface
name|EditableManagedRepository
extends|extends
name|EditableRepository
extends|,
name|ManagedRepository
block|{
comment|/**      * If true, the repository blocks redeployments of artifacts with the same version.      * @param blocksRedeployment The flag for blocking redeployments.      */
name|void
name|setBlocksRedeployment
parameter_list|(
name|boolean
name|blocksRedeployment
parameter_list|)
function_decl|;
comment|/**      * Sets the content      * @param content      */
name|void
name|setContent
parameter_list|(
name|ManagedRepositoryContent
name|content
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

