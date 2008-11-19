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
name|consumers
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|configuration
operator|.
name|FileTypes
import|;
end_import

begin_comment
comment|/**  * AbstractMonitoredConsumer   *  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractMonitoredConsumer
implements|implements
name|Consumer
block|{
specifier|private
name|Set
argument_list|<
name|ConsumerMonitor
argument_list|>
name|monitors
init|=
operator|new
name|HashSet
argument_list|<
name|ConsumerMonitor
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|addConsumerMonitor
parameter_list|(
name|ConsumerMonitor
name|monitor
parameter_list|)
block|{
name|monitors
operator|.
name|add
argument_list|(
name|monitor
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeConsumerMonitor
parameter_list|(
name|ConsumerMonitor
name|monitor
parameter_list|)
block|{
name|monitors
operator|.
name|remove
argument_list|(
name|monitor
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|triggerConsumerError
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|message
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|ConsumerMonitor
argument_list|>
name|itmonitors
init|=
name|monitors
operator|.
name|iterator
argument_list|()
init|;
name|itmonitors
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ConsumerMonitor
name|monitor
init|=
name|itmonitors
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|monitor
operator|.
name|consumerError
argument_list|(
name|this
argument_list|,
name|type
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|/* discard error */
block|}
block|}
block|}
specifier|protected
name|void
name|triggerConsumerWarning
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|message
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|ConsumerMonitor
argument_list|>
name|itmonitors
init|=
name|monitors
operator|.
name|iterator
argument_list|()
init|;
name|itmonitors
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ConsumerMonitor
name|monitor
init|=
name|itmonitors
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|monitor
operator|.
name|consumerWarning
argument_list|(
name|this
argument_list|,
name|type
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|/* discard error */
block|}
block|}
block|}
specifier|protected
name|void
name|triggerConsumerInfo
parameter_list|(
name|String
name|message
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|ConsumerMonitor
argument_list|>
name|itmonitors
init|=
name|monitors
operator|.
name|iterator
argument_list|()
init|;
name|itmonitors
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ConsumerMonitor
name|monitor
init|=
name|itmonitors
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|monitor
operator|.
name|consumerInfo
argument_list|(
name|this
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|/* discard error */
block|}
block|}
block|}
specifier|public
name|boolean
name|isProcessUnmodified
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getDefaultArtifactExclusions
parameter_list|()
block|{
return|return
name|FileTypes
operator|.
name|DEFAULT_EXCLUSIONS
return|;
block|}
block|}
end_class

end_unit

