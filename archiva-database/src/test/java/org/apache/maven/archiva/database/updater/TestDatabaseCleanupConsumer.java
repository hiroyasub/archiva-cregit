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
name|database
operator|.
name|updater
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|DatabaseCleanupConsumer
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
name|model
operator|.
name|ArchivaArtifact
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
comment|/**  * TestDatabaseCleanupConsumer   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|TestDatabaseCleanupConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|DatabaseCleanupConsumer
block|{
specifier|private
name|int
name|countBegin
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|countComplete
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|countProcessed
init|=
literal|0
decl_stmt|;
specifier|public
name|void
name|resetCount
parameter_list|()
block|{
name|countBegin
operator|=
literal|0
expr_stmt|;
name|countProcessed
operator|=
literal|0
expr_stmt|;
name|countComplete
operator|=
literal|0
expr_stmt|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|()
block|{
name|countBegin
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
name|countComplete
operator|++
expr_stmt|;
block|}
specifier|public
name|List
name|getIncludedTypes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|processArchivaArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|countProcessed
operator|++
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Test Consumer for Database Unprocessed"
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"test-db-unprocessed"
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
name|int
name|getCountBegin
parameter_list|()
block|{
return|return
name|countBegin
return|;
block|}
specifier|public
name|int
name|getCountComplete
parameter_list|()
block|{
return|return
name|countComplete
return|;
block|}
specifier|public
name|int
name|getCountProcessed
parameter_list|()
block|{
return|return
name|countProcessed
return|;
block|}
block|}
end_class

end_unit

