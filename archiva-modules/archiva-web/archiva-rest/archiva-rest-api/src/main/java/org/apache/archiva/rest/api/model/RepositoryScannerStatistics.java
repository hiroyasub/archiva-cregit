begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|model
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"repositoryScannerStatistics"
argument_list|)
specifier|public
class|class
name|RepositoryScannerStatistics
implements|implements
name|Serializable
block|{
specifier|private
name|ManagedRepository
name|managedRepository
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ConsumerScanningStatistics
argument_list|>
name|consumerScanningStatistics
decl_stmt|;
specifier|private
name|long
name|totalFileCount
init|=
literal|0
decl_stmt|;
specifier|private
name|long
name|newFileCount
init|=
literal|0
decl_stmt|;
specifier|public
name|RepositoryScannerStatistics
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|ManagedRepository
name|getManagedRepository
parameter_list|()
block|{
return|return
name|managedRepository
return|;
block|}
specifier|public
name|void
name|setManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
block|{
name|this
operator|.
name|managedRepository
operator|=
name|managedRepository
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|ConsumerScanningStatistics
argument_list|>
name|getConsumerScanningStatistics
parameter_list|()
block|{
return|return
name|consumerScanningStatistics
return|;
block|}
specifier|public
name|void
name|setConsumerScanningStatistics
parameter_list|(
name|List
argument_list|<
name|ConsumerScanningStatistics
argument_list|>
name|consumerScanningStatistics
parameter_list|)
block|{
name|this
operator|.
name|consumerScanningStatistics
operator|=
name|consumerScanningStatistics
expr_stmt|;
block|}
specifier|public
name|long
name|getTotalFileCount
parameter_list|()
block|{
return|return
name|totalFileCount
return|;
block|}
specifier|public
name|void
name|setTotalFileCount
parameter_list|(
name|long
name|totalFileCount
parameter_list|)
block|{
name|this
operator|.
name|totalFileCount
operator|=
name|totalFileCount
expr_stmt|;
block|}
specifier|public
name|long
name|getNewFileCount
parameter_list|()
block|{
return|return
name|newFileCount
return|;
block|}
specifier|public
name|void
name|setNewFileCount
parameter_list|(
name|long
name|newFileCount
parameter_list|)
block|{
name|this
operator|.
name|newFileCount
operator|=
name|newFileCount
expr_stmt|;
block|}
block|}
end_class

end_unit

