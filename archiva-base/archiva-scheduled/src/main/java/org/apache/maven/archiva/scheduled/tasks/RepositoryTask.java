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
operator|.
name|tasks
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|Task
import|;
end_import

begin_comment
comment|/**  * A repository task.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryTask
extends|extends
name|Task
block|{
name|String
name|QUEUE_POLICY_WAIT
init|=
literal|"wait"
decl_stmt|;
name|String
name|QUEUE_POLICY_SKIP
init|=
literal|"skip"
decl_stmt|;
comment|/**      * Gets the queue policy for this task.      *      * @return Queue policy for this task      */
name|String
name|getQueuePolicy
parameter_list|()
function_decl|;
comment|/**      * Sets the queue policy for this task.      *      * @param policy      */
name|void
name|setQueuePolicy
parameter_list|(
name|String
name|policy
parameter_list|)
function_decl|;
comment|/**      * Sets the job name to represent a group of similar / identical job tasks.  Can be used to check the      * task queue for similar / identical job tasks.      */
name|void
name|setJobName
parameter_list|(
name|String
name|jobName
parameter_list|)
function_decl|;
name|String
name|getJobName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

