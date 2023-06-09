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
operator|.
name|functors
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
name|archiva
operator|.
name|consumers
operator|.
name|RepositoryContentConsumer
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
name|apache
operator|.
name|commons
operator|.
name|collections4
operator|.
name|Closure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * TriggerScanCompletedClosure  */
end_comment

begin_class
specifier|public
class|class
name|TriggerScanCompletedClosure
implements|implements
name|Closure
argument_list|<
name|RepositoryContentConsumer
argument_list|>
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|TriggerScanCompletedClosure
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|ManagedRepository
name|repository
decl_stmt|;
specifier|private
name|boolean
name|executeOnEntireRepo
init|=
literal|true
decl_stmt|;
specifier|public
name|TriggerScanCompletedClosure
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
block|}
specifier|public
name|TriggerScanCompletedClosure
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|this
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|this
operator|.
name|executeOnEntireRepo
operator|=
name|executeOnEntireRepo
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|RepositoryContentConsumer
name|input
parameter_list|)
block|{
name|RepositoryContentConsumer
name|consumer
init|=
operator|(
name|RepositoryContentConsumer
operator|)
name|input
decl_stmt|;
name|consumer
operator|.
name|completeScan
argument_list|(
name|executeOnEntireRepo
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Consumer [{}] completed for repository [{}]"
argument_list|,
name|consumer
operator|.
name|getId
argument_list|( )
argument_list|,
name|repository
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

