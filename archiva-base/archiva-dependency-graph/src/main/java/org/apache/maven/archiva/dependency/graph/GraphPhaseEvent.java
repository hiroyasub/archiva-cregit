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
comment|/**  * GraphPhaseEvent   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|GraphPhaseEvent
block|{
comment|/**      * Graph Phase Event Type - New Graph has been created.  No tasks have been run yet.      * NOTE: {{@link #getTask()} will be null for this type.      */
specifier|public
specifier|static
specifier|final
name|int
name|GRAPH_NEW
init|=
literal|0
decl_stmt|;
comment|/**      * Graph Phase Event Type - Graph Task is about to run.      */
specifier|public
specifier|static
specifier|final
name|int
name|GRAPH_TASK_PRE
init|=
literal|1
decl_stmt|;
comment|/**      * Graph Phase Event Type - Graph Task has finished.      */
specifier|public
specifier|static
specifier|final
name|int
name|GRAPH_TASK_POST
init|=
literal|2
decl_stmt|;
comment|/**      * Graph Phase Event Type - All Graph Tasks are done.      * NOTE: {{@link #getTask()} will be null for this type.      */
specifier|public
specifier|static
specifier|final
name|int
name|GRAPH_DONE
init|=
literal|10
decl_stmt|;
specifier|private
name|int
name|type
decl_stmt|;
specifier|private
name|GraphTask
name|task
decl_stmt|;
specifier|private
name|DependencyGraph
name|graph
decl_stmt|;
specifier|public
name|GraphPhaseEvent
parameter_list|(
name|int
name|type
parameter_list|,
name|GraphTask
name|task
parameter_list|,
name|DependencyGraph
name|graph
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|task
operator|=
name|task
expr_stmt|;
name|this
operator|.
name|graph
operator|=
name|graph
expr_stmt|;
block|}
specifier|public
name|DependencyGraph
name|getGraph
parameter_list|()
block|{
return|return
name|graph
return|;
block|}
specifier|public
name|GraphTask
name|getTask
parameter_list|()
block|{
return|return
name|task
return|;
block|}
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

