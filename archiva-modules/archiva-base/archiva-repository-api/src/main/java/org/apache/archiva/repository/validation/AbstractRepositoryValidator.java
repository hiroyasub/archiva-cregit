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
operator|.
name|validation
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|CheckedResult
import|;
end_import

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
name|Repository
import|;
end_import

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
name|RepositoryRegistry
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryValidator
parameter_list|<
name|R
extends|extends
name|Repository
parameter_list|>
implements|implements
name|RepositoryValidator
argument_list|<
name|R
argument_list|>
block|{
specifier|protected
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|setRepositoryRegistry
parameter_list|(
name|RepositoryRegistry
name|repositoryRegistry
parameter_list|)
block|{
name|this
operator|.
name|repositoryRegistry
operator|=
name|repositoryRegistry
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|ValidationResponse
argument_list|<
name|R
argument_list|>
name|apply
parameter_list|(
name|R
name|repo
parameter_list|,
name|boolean
name|update
parameter_list|)
function_decl|;
annotation|@
name|Override
specifier|public
name|ValidationResponse
argument_list|<
name|R
argument_list|>
name|apply
parameter_list|(
name|R
name|r
parameter_list|)
block|{
return|return
name|apply
argument_list|(
name|r
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ValidationResponse
argument_list|<
name|R
argument_list|>
name|applyForUpdate
parameter_list|(
name|R
name|repo
parameter_list|)
block|{
return|return
name|apply
argument_list|(
name|repo
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

