begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Consumer - the base set of methods for a consumer.  *  *  */
end_comment

begin_interface
specifier|public
specifier|abstract
interface|interface
name|Consumer
block|{
comment|/**      * This is the id for the consumer.      *       * @return the consumer id.      */
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * The human readable description for this consumer.      *       * @return the human readable description for this consumer.      */
name|String
name|getDescription
parameter_list|()
function_decl|;
comment|/**      * Add a consumer monitor to the consumer.      *       * @param monitor the monitor to add.      */
name|void
name|addConsumerMonitor
parameter_list|(
name|ConsumerMonitor
name|monitor
parameter_list|)
function_decl|;
comment|/**      * Remove a consumer monitor.      *       * @param monitor the monitor to remove.      */
name|void
name|removeConsumerMonitor
parameter_list|(
name|ConsumerMonitor
name|monitor
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

