begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|ArrayList
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
comment|/**  * Information about running and queued repository scans.  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
class|class
name|ScanStatus
implements|implements
name|Serializable
block|{
specifier|private
name|List
argument_list|<
name|IndexingTask
argument_list|>
name|indexingQueue
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MetadataScanTask
argument_list|>
name|scanQueue
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|public
name|ScanStatus
parameter_list|( )
block|{
block|}
specifier|public
name|boolean
name|isMetadataScanRunning
parameter_list|( )
block|{
return|return
name|scanQueue
operator|.
name|size
argument_list|( )
operator|>
literal|0
operator|&&
name|scanQueue
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isRunning
argument_list|()
return|;
block|}
specifier|public
name|int
name|getMetadataScanQueueSize
parameter_list|()
block|{
name|int
name|size
init|=
name|scanQueue
operator|.
name|size
argument_list|( )
decl_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
return|return
name|size
operator|-
literal|1
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
specifier|public
name|boolean
name|isIndexScanRunning
parameter_list|( )
block|{
return|return
name|indexingQueue
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|indexingQueue
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|isRunning
argument_list|()
return|;
block|}
specifier|public
name|int
name|getIndexScanQueueSize
parameter_list|()
block|{
name|int
name|size
init|=
name|indexingQueue
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
return|return
name|size
operator|-
literal|1
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|IndexingTask
argument_list|>
name|getIndexingQueue
parameter_list|( )
block|{
return|return
name|indexingQueue
return|;
block|}
specifier|public
name|void
name|setIndexingQueue
parameter_list|(
name|List
argument_list|<
name|IndexingTask
argument_list|>
name|indexingQueue
parameter_list|)
block|{
name|this
operator|.
name|indexingQueue
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|indexingQueue
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|MetadataScanTask
argument_list|>
name|getScanQueue
parameter_list|( )
block|{
return|return
name|scanQueue
return|;
block|}
specifier|public
name|void
name|setScanQueue
parameter_list|(
name|List
argument_list|<
name|MetadataScanTask
argument_list|>
name|scanQueue
parameter_list|)
block|{
name|this
operator|.
name|scanQueue
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|scanQueue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

