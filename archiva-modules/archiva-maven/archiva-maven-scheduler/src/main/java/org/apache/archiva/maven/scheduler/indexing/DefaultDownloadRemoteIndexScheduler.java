begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|scheduler
operator|.
name|indexing
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|ProxyRegistry
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
name|proxy
operator|.
name|model
operator|.
name|NetworkProxy
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
name|DownloadRemoteIndexException
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
name|DownloadRemoteIndexScheduler
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
name|configuration
operator|.
name|provider
operator|.
name|ArchivaConfiguration
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
name|configuration
operator|.
name|provider
operator|.
name|ConfigurationEvent
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
name|configuration
operator|.
name|provider
operator|.
name|ConfigurationListener
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
name|indexer
operator|.
name|UnsupportedBaseContextException
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
name|maven
operator|.
name|common
operator|.
name|proxy
operator|.
name|WagonFactory
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
name|RepositoryRegistry
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
name|features
operator|.
name|RemoteIndexFeature
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
name|lang3
operator|.
name|StringUtils
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
name|index
operator|.
name|context
operator|.
name|IndexingContext
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
name|index
operator|.
name|packer
operator|.
name|IndexPacker
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
name|index
operator|.
name|updater
operator|.
name|IndexUpdater
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
name|annotation
operator|.
name|PostConstruct
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"downloadRemoteIndexScheduler#default"
argument_list|)
specifier|public
class|class
name|DefaultDownloadRemoteIndexScheduler
implements|implements
name|ConfigurationListener
implements|,
name|DownloadRemoteIndexScheduler
block|{
specifier|private
name|Logger
name|log
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
literal|"taskScheduler#indexDownloadRemote"
argument_list|)
specifier|private
name|TaskScheduler
name|taskScheduler
decl_stmt|;
annotation|@
name|Inject
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|WagonFactory
name|wagonFactory
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|IndexUpdater
name|indexUpdater
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|IndexPacker
name|indexPacker
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ProxyRegistry
name|proxyRegistry
decl_stmt|;
comment|// store ids about currently running remote download : updated in DownloadRemoteIndexTask
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|runningRemoteDownloadIds
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|startup
parameter_list|()
throws|throws
name|DownloadRemoteIndexException
throws|,
name|UnsupportedBaseContextException
block|{
name|archivaConfiguration
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// TODO add indexContexts even if null
for|for
control|(
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|RemoteRepository
name|remoteRepository
range|:
name|repositoryRegistry
operator|.
name|getRemoteRepositories
argument_list|()
control|)
block|{
name|String
name|contextKey
init|=
literal|"remote-"
operator|+
name|remoteRepository
operator|.
name|getId
argument_list|()
decl_stmt|;
name|IndexingContext
name|context
init|=
name|remoteRepository
operator|.
name|getIndexingContext
argument_list|()
operator|.
name|getBaseContext
argument_list|(
name|IndexingContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|RemoteIndexFeature
name|rif
init|=
name|remoteRepository
operator|.
name|getFeature
argument_list|(
name|RemoteIndexFeature
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// TODO record jobs from configuration
if|if
condition|(
name|rif
operator|.
name|isDownloadRemoteIndex
argument_list|()
operator|&&
name|StringUtils
operator|.
name|isNotEmpty
argument_list|(
name|remoteRepository
operator|.
name|getSchedulingDefinition
argument_list|()
argument_list|)
condition|)
block|{
name|boolean
name|fullDownload
init|=
name|context
operator|.
name|getIndexDirectoryFile
argument_list|()
operator|.
name|list
argument_list|()
operator|.
name|length
operator|==
literal|0
decl_stmt|;
name|scheduleDownloadRemote
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|,
literal|false
argument_list|,
name|fullDownload
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|configurationEvent
parameter_list|(
name|ConfigurationEvent
name|event
parameter_list|)
block|{
comment|// TODO remove jobs and add again
block|}
annotation|@
name|Override
specifier|public
name|void
name|scheduleDownloadRemote
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|boolean
name|now
parameter_list|,
name|boolean
name|fullDownload
parameter_list|)
throws|throws
name|DownloadRemoteIndexException
block|{
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|RemoteRepository
name|remoteRepo
init|=
name|repositoryRegistry
operator|.
name|getRemoteRepository
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
if|if
condition|(
name|remoteRepo
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"ignore scheduleDownloadRemote for repo with id {} as not exists"
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|remoteRepo
operator|.
name|supportsFeature
argument_list|(
name|RemoteIndexFeature
operator|.
name|class
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"ignore scheduleDownloadRemote for repo with id {}. Does not support remote index."
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
return|return;
block|}
name|RemoteIndexFeature
name|rif
init|=
name|remoteRepo
operator|.
name|getFeature
argument_list|(
name|RemoteIndexFeature
operator|.
name|class
argument_list|)
decl_stmt|;
name|NetworkProxy
name|networkProxy
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|rif
operator|.
name|getProxyId
argument_list|()
argument_list|)
condition|)
block|{
name|networkProxy
operator|=
name|proxyRegistry
operator|.
name|getNetworkProxy
argument_list|(
name|rif
operator|.
name|getProxyId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|networkProxy
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"your remote repository is configured to download remote index trought a proxy we cannot find id:{}"
argument_list|,
name|rif
operator|.
name|getProxyId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|DownloadRemoteIndexTaskRequest
name|downloadRemoteIndexTaskRequest
init|=
operator|new
name|DownloadRemoteIndexTaskRequest
argument_list|()
comment|//
operator|.
name|setRemoteRepository
argument_list|(
name|remoteRepo
argument_list|)
comment|//
operator|.
name|setNetworkProxy
argument_list|(
name|networkProxy
argument_list|)
comment|//
operator|.
name|setFullDownload
argument_list|(
name|fullDownload
argument_list|)
comment|//
operator|.
name|setWagonFactory
argument_list|(
name|wagonFactory
argument_list|)
comment|//
operator|.
name|setIndexUpdater
argument_list|(
name|indexUpdater
argument_list|)
comment|//
operator|.
name|setIndexPacker
argument_list|(
name|this
operator|.
name|indexPacker
argument_list|)
decl_stmt|;
if|if
condition|(
name|now
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"schedule download remote index for repository {}"
argument_list|,
name|remoteRepo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// do it now
name|taskScheduler
operator|.
name|schedule
argument_list|(
operator|new
name|DownloadRemoteIndexTask
argument_list|(
name|downloadRemoteIndexTaskRequest
argument_list|,
name|this
operator|.
name|runningRemoteDownloadIds
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"schedule download remote index for repository {} with cron expression {}"
argument_list|,
name|remoteRepo
operator|.
name|getId
argument_list|()
argument_list|,
name|remoteRepo
operator|.
name|getSchedulingDefinition
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|CronTrigger
name|cronTrigger
init|=
operator|new
name|CronTrigger
argument_list|(
name|remoteRepo
operator|.
name|getSchedulingDefinition
argument_list|()
argument_list|)
decl_stmt|;
name|taskScheduler
operator|.
name|schedule
argument_list|(
operator|new
name|DownloadRemoteIndexTask
argument_list|(
name|downloadRemoteIndexTaskRequest
argument_list|,
name|this
operator|.
name|runningRemoteDownloadIds
argument_list|)
argument_list|,
name|cronTrigger
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to schedule remote index download: {}"
argument_list|,
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rif
operator|.
name|isDownloadRemoteIndexOnStartup
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"remote repository {} configured with downloadRemoteIndexOnStartup schedule now a download"
argument_list|,
name|remoteRepo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|taskScheduler
operator|.
name|schedule
argument_list|(
operator|new
name|DownloadRemoteIndexTask
argument_list|(
name|downloadRemoteIndexTaskRequest
argument_list|,
name|this
operator|.
name|runningRemoteDownloadIds
argument_list|)
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|TaskScheduler
name|getTaskScheduler
parameter_list|()
block|{
return|return
name|taskScheduler
return|;
block|}
specifier|public
name|void
name|setTaskScheduler
parameter_list|(
name|TaskScheduler
name|taskScheduler
parameter_list|)
block|{
name|this
operator|.
name|taskScheduler
operator|=
name|taskScheduler
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRunningRemoteDownloadIds
parameter_list|()
block|{
return|return
name|runningRemoteDownloadIds
return|;
block|}
block|}
end_class

end_unit

