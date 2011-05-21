begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|scanner
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
name|ManagedRepositoryConfiguration
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
name|consumers
operator|.
name|AbstractMonitoredConsumer
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
name|maven
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|KnownRepositoryContentConsumer
import|;
end_import

begin_comment
comment|/**  * SampleKnownConsumer   *  * @version $Id$  *   * plexus.component role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"  *                   role-hint="sample-known"  */
end_comment

begin_class
specifier|public
class|class
name|SampleKnownConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
block|{
comment|/**      * plexus.configuration default-value="unset-id"      */
specifier|private
name|String
name|id
init|=
literal|"unset-id"
decl_stmt|;
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
block|{
comment|/* nothing to do */
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepositoryConfiguration
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
name|beginScan
argument_list|(
name|repository
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* nothing to do */
block|}
specifier|public
name|void
name|completeScan
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|completeScan
argument_list|()
expr_stmt|;
block|}
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
block|}
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
block|}
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
comment|/* nothing to do */
block|}
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
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Sample Known Consumer"
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
block|}
end_class

end_unit

