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
name|commons
operator|.
name|collections
operator|.
name|Closure
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
name|common
operator|.
name|utils
operator|.
name|BaseFile
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
name|RepositoryContentConsumer
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * ConsumerProcessFileClosure   *  */
end_comment

begin_class
specifier|public
class|class
name|ConsumerProcessFileClosure
implements|implements
name|Closure
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ConsumerProcessFileClosure
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|BaseFile
name|basefile
decl_stmt|;
specifier|private
name|boolean
name|executeOnEntireRepo
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|consumerTimings
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|consumerCounts
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
if|if
condition|(
name|input
operator|instanceof
name|RepositoryContentConsumer
condition|)
block|{
name|RepositoryContentConsumer
name|consumer
init|=
operator|(
name|RepositoryContentConsumer
operator|)
name|input
decl_stmt|;
name|String
name|id
init|=
name|consumer
operator|.
name|getId
argument_list|()
decl_stmt|;
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending to consumer: {}"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|long
name|startTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|processFile
argument_list|(
name|basefile
operator|.
name|getRelativePath
argument_list|()
argument_list|,
name|executeOnEntireRepo
argument_list|)
expr_stmt|;
name|long
name|endTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumerTimings
operator|!=
literal|null
condition|)
block|{
name|Long
name|value
init|=
name|consumerTimings
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|consumerTimings
operator|.
name|put
argument_list|(
name|id
argument_list|,
operator|(
name|value
operator|!=
literal|null
condition|?
name|value
else|:
literal|0
operator|)
operator|+
name|endTime
operator|-
name|startTime
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|consumerCounts
operator|!=
literal|null
condition|)
block|{
name|Long
name|value
init|=
name|consumerCounts
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|consumerCounts
operator|.
name|put
argument_list|(
name|id
argument_list|,
operator|(
name|value
operator|!=
literal|null
condition|?
name|value
else|:
literal|0
operator|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|/* Intentionally Catch all exceptions.                  * So that the discoverer processing can continue.                  */
name|log
operator|.
name|error
argument_list|(
literal|"Consumer ["
operator|+
name|id
operator|+
literal|"] had an error when processing file ["
operator|+
name|basefile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"]: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|BaseFile
name|getBasefile
parameter_list|()
block|{
return|return
name|basefile
return|;
block|}
specifier|public
name|void
name|setBasefile
parameter_list|(
name|BaseFile
name|basefile
parameter_list|)
block|{
name|this
operator|.
name|basefile
operator|=
name|basefile
expr_stmt|;
block|}
specifier|public
name|boolean
name|isExecuteOnEntireRepo
parameter_list|()
block|{
return|return
name|executeOnEntireRepo
return|;
block|}
specifier|public
name|void
name|setExecuteOnEntireRepo
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|this
operator|.
name|executeOnEntireRepo
operator|=
name|executeOnEntireRepo
expr_stmt|;
block|}
specifier|public
name|void
name|setConsumerTimings
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|consumerTimings
parameter_list|)
block|{
name|this
operator|.
name|consumerTimings
operator|=
name|consumerTimings
expr_stmt|;
block|}
specifier|public
name|void
name|setConsumerCounts
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|consumerCounts
parameter_list|)
block|{
name|this
operator|.
name|consumerCounts
operator|=
name|consumerCounts
expr_stmt|;
block|}
specifier|public
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|log
return|;
block|}
specifier|public
name|void
name|setLogger
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|log
operator|=
name|logger
expr_stmt|;
block|}
block|}
end_class

end_unit

