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
name|codehaus
operator|.
name|plexus
operator|.
name|taskqueue
operator|.
name|execution
operator|.
name|TaskExecutionException
import|;
end_import

begin_comment
comment|/**  * The component that takes care of scheduling in the application.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArchivaTaskScheduler
block|{
comment|/**      * The Plexus component role.      */
specifier|public
specifier|final
specifier|static
name|String
name|ROLE
init|=
name|ArchivaTaskScheduler
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|public
name|ArchivaTaskQueue
name|getTaskQueue
parameter_list|()
function_decl|;
specifier|public
name|void
name|scheduleAllRepositoryTasks
parameter_list|()
throws|throws
name|TaskExecutionException
function_decl|;
specifier|public
name|void
name|scheduleDatabaseTasks
parameter_list|()
throws|throws
name|TaskExecutionException
function_decl|;
specifier|public
name|void
name|scheduleRepositoryTask
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|TaskExecutionException
function_decl|;
block|}
end_interface

end_unit

