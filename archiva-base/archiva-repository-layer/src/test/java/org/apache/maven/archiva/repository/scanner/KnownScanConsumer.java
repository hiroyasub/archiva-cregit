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
name|ArchivaRepository
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
comment|/**  * ScanConsumer   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|KnownScanConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
block|{
specifier|private
name|int
name|processCount
init|=
literal|0
decl_stmt|;
specifier|private
name|List
name|includes
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|public
name|List
name|getExcludes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setIncludes
parameter_list|(
name|String
name|includesArray
index|[]
parameter_list|)
block|{
name|this
operator|.
name|includes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|includes
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|includesArray
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"test-scan-consumer"
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Scan Consumer (for testing)"
return|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|)
throws|throws
name|ConsumerException
block|{
comment|/* do nothing */
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
name|this
operator|.
name|processCount
operator|++
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|int
name|getProcessCount
parameter_list|()
block|{
return|return
name|processCount
return|;
block|}
specifier|public
name|void
name|setProcessCount
parameter_list|(
name|int
name|processCount
parameter_list|)
block|{
name|this
operator|.
name|processCount
operator|=
name|processCount
expr_stmt|;
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
block|}
end_class

end_unit

