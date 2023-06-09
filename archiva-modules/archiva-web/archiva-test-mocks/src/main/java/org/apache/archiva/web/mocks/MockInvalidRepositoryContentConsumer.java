begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|mocks
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
name|consumers
operator|.
name|ConsumerException
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
name|consumers
operator|.
name|ConsumerMonitor
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
name|consumers
operator|.
name|InvalidRepositoryContentConsumer
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
name|repository
operator|.
name|ManagedRepository
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"InvalidRepositoryContentConsumer#mock"
argument_list|)
specifier|public
class|class
name|MockInvalidRepositoryContentConsumer
implements|implements
name|InvalidRepositoryContentConsumer
block|{
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"foo"
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"the foo"
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addConsumerMonitor
parameter_list|(
name|ConsumerMonitor
name|monitor
parameter_list|)
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeConsumerMonitor
parameter_list|(
name|ConsumerMonitor
name|monitor
parameter_list|)
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|ConsumerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ConsumerException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|Exception
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|void
name|completeScan
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isProcessUnmodified
parameter_list|()
block|{
return|return
literal|false
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
block|}
end_class

end_unit

