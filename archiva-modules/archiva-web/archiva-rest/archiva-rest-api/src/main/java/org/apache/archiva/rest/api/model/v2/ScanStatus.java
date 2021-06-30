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
operator|.
name|v2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|media
operator|.
name|Schema
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|MetadataScanTask
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
name|scheduler
operator|.
name|indexing
operator|.
name|ArtifactIndexingTask
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
name|scheduler
operator|.
name|repository
operator|.
name|model
operator|.
name|RepositoryTask
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"ScanStatus"
argument_list|,
name|description
operator|=
literal|"Status of repository scan tasks"
argument_list|)
specifier|public
class|class
name|ScanStatus
implements|implements
name|Serializable
block|{
specifier|private
name|boolean
name|scanRunning
init|=
literal|false
decl_stmt|;
specifier|private
name|int
name|scanQueued
init|=
literal|0
decl_stmt|;
specifier|private
name|boolean
name|indexRunning
init|=
literal|false
decl_stmt|;
specifier|private
name|int
name|indexQueued
init|=
literal|0
decl_stmt|;
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
name|ScanTask
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
specifier|static
name|ScanStatus
name|of
parameter_list|(
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
name|ScanStatus
name|modelStatus
parameter_list|)
block|{
name|ScanStatus
name|status
init|=
operator|new
name|ScanStatus
argument_list|( )
decl_stmt|;
name|status
operator|.
name|setIndexRunning
argument_list|(
name|modelStatus
operator|.
name|isIndexScanRunning
argument_list|()
argument_list|)
expr_stmt|;
name|status
operator|.
name|setScanRunning
argument_list|(
name|modelStatus
operator|.
name|isMetadataScanRunning
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
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
name|IndexingTask
argument_list|>
name|indexQueue
init|=
name|modelStatus
operator|.
name|getIndexingQueue
argument_list|( )
decl_stmt|;
name|status
operator|.
name|setIndexingQueue
argument_list|(
name|indexQueue
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|IndexingTask
operator|::
name|of
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|status
operator|.
name|setIndexQueued
argument_list|(
name|indexQueue
operator|.
name|size
argument_list|( )
operator|>
literal|0
condition|?
name|indexQueue
operator|.
name|size
argument_list|( )
operator|-
literal|1
else|:
literal|0
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MetadataScanTask
argument_list|>
name|scanQueue
init|=
name|modelStatus
operator|.
name|getScanQueue
argument_list|( )
decl_stmt|;
name|status
operator|.
name|setScanQueue
argument_list|(
name|scanQueue
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|ScanTask
operator|::
name|of
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|status
operator|.
name|setScanQueued
argument_list|(
name|scanQueue
operator|.
name|size
argument_list|( )
operator|>
literal|0
condition|?
name|scanQueue
operator|.
name|size
argument_list|( )
operator|-
literal|1
else|:
literal|0
argument_list|)
expr_stmt|;
return|return
name|status
return|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"scan_running"
argument_list|,
name|description
operator|=
literal|"True, if a scan is currently running"
argument_list|)
specifier|public
name|boolean
name|isScanRunning
parameter_list|( )
block|{
return|return
name|scanRunning
return|;
block|}
specifier|public
name|void
name|setScanRunning
parameter_list|(
name|boolean
name|scanRunning
parameter_list|)
block|{
name|this
operator|.
name|scanRunning
operator|=
name|scanRunning
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"scan_queued"
argument_list|,
name|description
operator|=
literal|"Number of scans in the task queue"
argument_list|)
specifier|public
name|int
name|getScanQueued
parameter_list|( )
block|{
return|return
name|scanQueued
return|;
block|}
specifier|public
name|void
name|setScanQueued
parameter_list|(
name|int
name|scanQueued
parameter_list|)
block|{
name|this
operator|.
name|scanQueued
operator|=
name|scanQueued
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"index_running"
argument_list|,
name|description
operator|=
literal|"True, if there is a index task currently running"
argument_list|)
specifier|public
name|boolean
name|isIndexRunning
parameter_list|( )
block|{
return|return
name|indexRunning
return|;
block|}
specifier|public
name|void
name|setIndexRunning
parameter_list|(
name|boolean
name|indexRunning
parameter_list|)
block|{
name|this
operator|.
name|indexRunning
operator|=
name|indexRunning
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"index_queued"
argument_list|,
name|description
operator|=
literal|"Number of queued index tasks"
argument_list|)
specifier|public
name|int
name|getIndexQueued
parameter_list|( )
block|{
return|return
name|indexQueued
return|;
block|}
specifier|public
name|void
name|setIndexQueued
parameter_list|(
name|int
name|indexQueued
parameter_list|)
block|{
name|this
operator|.
name|indexQueued
operator|=
name|indexQueued
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"indexing_queue"
argument_list|,
name|description
operator|=
literal|"List of indexing tasks waiting for execution"
argument_list|)
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
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"scan_queue"
argument_list|,
name|description
operator|=
literal|"List of scan tasks waiting for execution"
argument_list|)
specifier|public
name|List
argument_list|<
name|ScanTask
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
name|ScanTask
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
