begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|redback
operator|.
name|components
operator|.
name|taskqueue
operator|.
name|TaskQueueException
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
name|scheduler
operator|.
name|repository
operator|.
name|model
operator|.
name|RepositoryArchivaTaskScheduler
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
name|scheduler
operator|.
name|repository
operator|.
name|model
operator|.
name|RepositoryTask
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaTaskScheduler#repositoryMock"
argument_list|)
specifier|public
class|class
name|MockRepositoryArchivaTaskScheduler
implements|implements
name|RepositoryArchivaTaskScheduler
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|isProcessingRepositoryTask
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isProcessingRepositoryTask
parameter_list|(
name|RepositoryTask
name|task
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|queueTask
parameter_list|(
name|RepositoryTask
name|task
parameter_list|)
throws|throws
name|TaskQueueException
block|{
comment|// no op
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|unQueueTask
parameter_list|(
name|RepositoryTask
name|task
parameter_list|)
throws|throws
name|TaskQueueException
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

