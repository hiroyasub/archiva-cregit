begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|scheduler
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

begin_comment
comment|/**  * The component that takes care of scheduling in the application.  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArchivaTaskScheduler
parameter_list|<
name|T
extends|extends
name|Task
parameter_list|>
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
comment|/**      * Adds the task to the scanning queue.      *       * @param task      * @throws TaskQueueException      */
specifier|public
name|void
name|queueTask
parameter_list|(
name|T
name|task
parameter_list|)
throws|throws
name|TaskQueueException
function_decl|;
block|}
end_interface

end_unit

