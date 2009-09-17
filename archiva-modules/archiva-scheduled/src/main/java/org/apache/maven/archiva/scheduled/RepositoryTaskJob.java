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
name|scheduled
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
name|maven
operator|.
name|archiva
operator|.
name|scheduled
operator|.
name|tasks
operator|.
name|ArchivaTask
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|scheduled
operator|.
name|tasks
operator|.
name|RepositoryTask
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|scheduled
operator|.
name|tasks
operator|.
name|TaskCreator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|scheduler
operator|.
name|AbstractJob
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|taskqueue
operator|.
name|TaskQueue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
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
name|quartz
operator|.
name|JobDataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobExecutionContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobExecutionException
import|;
end_import

begin_comment
comment|/**  * This class is the repository job that is executed by the scheduler.  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryTaskJob
extends|extends
name|AbstractJob
block|{
specifier|static
specifier|final
name|String
name|TASK_KEY
init|=
literal|"EXECUTION"
decl_stmt|;
specifier|static
specifier|final
name|String
name|TASK_QUEUE
init|=
literal|"TASK_QUEUE"
decl_stmt|;
specifier|static
specifier|final
name|String
name|TASK_QUEUE_POLICY
init|=
literal|"TASK_QUEUE_POLICY"
decl_stmt|;
specifier|static
specifier|final
name|String
name|TASK_REPOSITORY
init|=
literal|"TASK_REPOSITORY"
decl_stmt|;
comment|/**      * Execute the discoverer and the indexer.      *      * @param context      * @throws org.quartz.JobExecutionException      *      */
specifier|public
name|void
name|execute
parameter_list|(
name|JobExecutionContext
name|context
parameter_list|)
throws|throws
name|JobExecutionException
block|{
name|JobDataMap
name|dataMap
init|=
name|context
operator|.
name|getJobDetail
argument_list|()
operator|.
name|getJobDataMap
argument_list|()
decl_stmt|;
name|setJobDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|TaskQueue
name|taskQueue
init|=
operator|(
name|TaskQueue
operator|)
name|dataMap
operator|.
name|get
argument_list|(
name|TASK_QUEUE
argument_list|)
decl_stmt|;
name|String
name|queuePolicy
init|=
name|dataMap
operator|.
name|get
argument_list|(
name|TASK_QUEUE_POLICY
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|RepositoryTask
name|task
init|=
name|TaskCreator
operator|.
name|createRepositoryTask
argument_list|(
operator|(
name|String
operator|)
name|dataMap
operator|.
name|get
argument_list|(
name|TASK_REPOSITORY
argument_list|)
argument_list|,
literal|""
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|task
operator|.
name|setName
argument_list|(
name|context
operator|.
name|getJobDetail
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|taskQueue
operator|.
name|getQueueSnapshot
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|taskQueue
operator|.
name|put
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|ArchivaTask
operator|.
name|QUEUE_POLICY_WAIT
operator|.
name|equals
argument_list|(
name|queuePolicy
argument_list|)
condition|)
block|{
name|taskQueue
operator|.
name|put
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|ArchivaTask
operator|.
name|QUEUE_POLICY_SKIP
operator|.
name|equals
argument_list|(
name|queuePolicy
argument_list|)
condition|)
block|{
comment|// do not queue anymore, policy is to skip
block|}
block|}
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|JobExecutionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

