begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
operator|.
name|utils
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
name|consumers
operator|.
name|RepositoryContentConsumer
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|AdminRepositoryConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections4
operator|.
name|Closure
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * AddAdminRepoConsumerClosure   *  *  */
end_comment

begin_class
specifier|public
class|class
name|AddAdminRepoConsumerClosure
implements|implements
name|Closure
block|{
specifier|private
name|List
argument_list|<
name|AdminRepositoryConsumer
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|selectedIds
decl_stmt|;
specifier|public
name|AddAdminRepoConsumerClosure
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|selectedIds
parameter_list|)
block|{
name|this
operator|.
name|selectedIds
operator|=
name|selectedIds
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|instanceof
name|RepositoryContentConsumer
condition|)
block|{
name|RepositoryContentConsumer
name|consumer
init|=
operator|(
name|RepositoryContentConsumer
operator|)
name|input
decl_stmt|;
name|boolean
name|enabled
init|=
name|this
operator|.
name|selectedIds
operator|.
name|contains
argument_list|(
name|consumer
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|AdminRepositoryConsumer
name|adminconsumer
init|=
operator|new
name|AdminRepositoryConsumer
argument_list|()
decl_stmt|;
name|adminconsumer
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
name|adminconsumer
operator|.
name|setId
argument_list|(
name|consumer
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|adminconsumer
operator|.
name|setDescription
argument_list|(
name|consumer
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|adminconsumer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|AdminRepositoryConsumer
argument_list|>
name|getList
parameter_list|()
block|{
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

