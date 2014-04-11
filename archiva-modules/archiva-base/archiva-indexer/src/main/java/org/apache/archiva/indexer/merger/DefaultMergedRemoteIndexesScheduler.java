begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|merger
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
name|RepositoryGroup
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
name|MergedRemoteIndexesScheduler
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
name|lang
operator|.
name|StringUtils
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
name|org
operator|.
name|springframework
operator|.
name|scheduling
operator|.
name|TaskScheduler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|scheduling
operator|.
name|support
operator|.
name|CronTrigger
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
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledFuture
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 2.0.0  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"mergedRemoteIndexesScheduler#default"
argument_list|)
specifier|public
class|class
name|DefaultMergedRemoteIndexesScheduler
implements|implements
name|MergedRemoteIndexesScheduler
block|{
specifier|private
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"taskScheduler#mergeRemoteIndexes"
argument_list|)
specifier|private
name|TaskScheduler
name|taskScheduler
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|IndexMerger
name|indexMerger
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ScheduledFuture
argument_list|>
name|scheduledFutureMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|schedule
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|,
name|File
name|directory
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryGroup
operator|.
name|getCronExpression
argument_list|()
argument_list|)
condition|)
block|{
return|return;
block|}
name|CronTrigger
name|cronTrigger
init|=
operator|new
name|CronTrigger
argument_list|(
name|repositoryGroup
operator|.
name|getCronExpression
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|repositories
init|=
name|repositoryGroup
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|IndexMergerRequest
name|indexMergerRequest
init|=
operator|new
name|IndexMergerRequest
argument_list|(
name|repositories
argument_list|,
literal|true
argument_list|,
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryGroup
operator|.
name|getMergedIndexPath
argument_list|()
argument_list|,
name|repositoryGroup
operator|.
name|getMergedIndexTtl
argument_list|()
argument_list|)
operator|.
name|mergedIndexDirectory
argument_list|(
name|directory
argument_list|)
decl_stmt|;
name|MergedRemoteIndexesTaskRequest
name|taskRequest
init|=
operator|new
name|MergedRemoteIndexesTaskRequest
argument_list|(
name|indexMergerRequest
argument_list|,
name|indexMerger
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"schedule merge remote index for group {} with cron {}"
argument_list|,
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryGroup
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|ScheduledFuture
name|scheduledFuture
init|=
name|taskScheduler
operator|.
name|schedule
argument_list|(
operator|new
name|MergedRemoteIndexesTask
argument_list|(
name|taskRequest
argument_list|)
argument_list|,
name|cronTrigger
argument_list|)
decl_stmt|;
name|scheduledFutureMap
operator|.
name|put
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|scheduledFuture
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|unschedule
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
block|{
name|ScheduledFuture
name|scheduledFuture
init|=
name|scheduledFutureMap
operator|.
name|remove
argument_list|(
name|repositoryGroup
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|scheduledFuture
operator|!=
literal|null
condition|)
block|{
name|scheduledFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

