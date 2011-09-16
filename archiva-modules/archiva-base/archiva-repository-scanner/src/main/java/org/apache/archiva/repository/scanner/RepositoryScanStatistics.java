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
name|text
operator|.
name|SimpleDateFormat
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
name|Map
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
name|collections
operator|.
name|CollectionUtils
import|;
end_import

begin_comment
comment|/**  * RepositoryScanStatistics - extension to the RepositoryContentStatistics model.  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryScanStatistics
block|{
specifier|private
specifier|transient
name|List
argument_list|<
name|String
argument_list|>
name|knownConsumers
decl_stmt|;
specifier|private
specifier|transient
name|List
argument_list|<
name|String
argument_list|>
name|invalidConsumers
decl_stmt|;
specifier|private
specifier|transient
name|long
name|startTimestamp
decl_stmt|;
specifier|private
name|SimpleDateFormat
name|df
init|=
operator|new
name|SimpleDateFormat
argument_list|()
decl_stmt|;
comment|/**      * Field repositoryId      */
specifier|private
name|String
name|repositoryId
decl_stmt|;
comment|/**      * Field whenGathered      */
specifier|private
name|java
operator|.
name|util
operator|.
name|Date
name|whenGathered
decl_stmt|;
comment|/**      * Field duration      */
specifier|private
name|long
name|duration
init|=
literal|0
decl_stmt|;
comment|/**      * Field totalFileCount      */
specifier|private
name|long
name|totalFileCount
init|=
literal|0
decl_stmt|;
comment|/**      * Field newFileCount      */
specifier|private
name|long
name|newFileCount
init|=
literal|0
decl_stmt|;
comment|/**      * Field totalSize      */
specifier|private
name|long
name|totalSize
init|=
literal|0
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
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|consumerTimings
decl_stmt|;
specifier|public
name|void
name|triggerStart
parameter_list|()
block|{
name|startTimestamp
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
specifier|public
name|java
operator|.
name|util
operator|.
name|Date
name|getWhenGathered
parameter_list|()
block|{
return|return
name|whenGathered
return|;
block|}
specifier|public
name|void
name|triggerFinished
parameter_list|()
block|{
name|long
name|finished
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|this
operator|.
name|duration
operator|=
name|finished
operator|-
name|startTimestamp
expr_stmt|;
name|this
operator|.
name|whenGathered
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|Date
argument_list|(
name|finished
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|increaseFileCount
parameter_list|()
block|{
name|long
name|count
init|=
name|getTotalFileCount
argument_list|()
decl_stmt|;
name|this
operator|.
name|totalFileCount
operator|=
operator|++
name|count
expr_stmt|;
block|}
specifier|public
name|void
name|increaseNewFileCount
parameter_list|()
block|{
name|long
name|count
init|=
name|getNewFileCount
argument_list|()
decl_stmt|;
name|this
operator|.
name|newFileCount
operator|=
operator|++
name|count
expr_stmt|;
block|}
specifier|public
name|void
name|setKnownConsumers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|consumers
parameter_list|)
block|{
name|knownConsumers
operator|=
name|consumers
expr_stmt|;
block|}
specifier|public
name|void
name|setInvalidConsumers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|consumers
parameter_list|)
block|{
name|invalidConsumers
operator|=
name|consumers
expr_stmt|;
block|}
specifier|public
name|String
name|toDump
parameter_list|(
name|ManagedRepository
name|repo
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n.\\ Scan of "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" \\.__________________________________________"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n  Repository Dir    : "
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n  Repository Name   : "
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n  Repository Layout : "
argument_list|)
operator|.
name|append
argument_list|(
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n  Known Consumers   : "
argument_list|)
expr_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|knownConsumers
argument_list|)
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|knownConsumers
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" configured)"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|id
range|:
name|knownConsumers
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\n                      "
argument_list|)
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumerTimings
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|long
name|time
init|=
name|consumerTimings
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" (Total: "
argument_list|)
operator|.
name|append
argument_list|(
name|time
argument_list|)
operator|.
name|append
argument_list|(
literal|"ms"
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumerCounts
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|long
name|total
init|=
name|consumerCounts
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"; Avg.: "
operator|+
operator|(
name|time
operator|/
name|total
operator|)
operator|+
literal|"; Count: "
operator|+
name|total
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<none>"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"\n  Invalid Consumers : "
argument_list|)
expr_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|invalidConsumers
argument_list|)
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|invalidConsumers
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" configured)"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|id
range|:
name|invalidConsumers
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\n                      "
argument_list|)
operator|.
name|append
argument_list|(
name|id
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumerTimings
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|long
name|time
init|=
name|consumerTimings
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" (Total: "
argument_list|)
operator|.
name|append
argument_list|(
name|time
argument_list|)
operator|.
name|append
argument_list|(
literal|"ms"
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumerCounts
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|long
name|total
init|=
name|consumerCounts
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"; Avg.: "
operator|+
operator|(
name|time
operator|/
name|total
operator|)
operator|+
literal|"ms; Count: "
operator|+
name|total
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<none>"
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"\n  Duration          : "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
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
name|DateUtil
operator|.
name|getDuration
argument_list|(
name|this
operator|.
name|getDuration
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n  When Gathered     : "
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|getWhenGathered
argument_list|()
operator|==
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"<null>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
name|df
operator|.
name|format
argument_list|(
name|this
operator|.
name|getWhenGathered
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"\n  Total File Count  : "
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|averageMsPerFile
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|getTotalFileCount
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|averageMsPerFile
operator|=
operator|(
name|this
operator|.
name|getDuration
argument_list|()
operator|/
name|this
operator|.
name|getTotalFileCount
argument_list|()
operator|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"\n  Avg Time Per File : "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
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
name|DateUtil
operator|.
name|getDuration
argument_list|(
name|averageMsPerFile
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"\n______________________________________________________________"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|void
name|setRepositoryId
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
name|this
operator|.
name|repositoryId
operator|=
name|repositoryId
expr_stmt|;
block|}
specifier|public
name|String
name|getRepositoryId
parameter_list|()
block|{
return|return
name|repositoryId
return|;
block|}
specifier|public
name|long
name|getDuration
parameter_list|()
block|{
return|return
name|duration
return|;
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
name|long
name|getNewFileCount
parameter_list|()
block|{
return|return
name|newFileCount
return|;
block|}
specifier|public
name|long
name|getTotalSize
parameter_list|()
block|{
return|return
name|totalSize
return|;
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
block|}
end_class

end_unit

