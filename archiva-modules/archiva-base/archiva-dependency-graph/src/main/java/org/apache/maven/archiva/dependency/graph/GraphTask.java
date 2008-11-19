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
name|dependency
operator|.
name|graph
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * A Graph Task.  *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|GraphTask
block|{
comment|/**      * Get the id for this task.      *       * @return the id for this task. (used in Exception messages and {@link GraphPhaseEvent})      */
specifier|public
name|String
name|getTaskId
parameter_list|()
function_decl|;
comment|/**      * Execute the Graph Task.      *       * @param graph the graph to execute the task on.      */
specifier|public
name|void
name|executeTask
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|)
throws|throws
name|GraphTaskException
function_decl|;
block|}
end_interface

end_unit

