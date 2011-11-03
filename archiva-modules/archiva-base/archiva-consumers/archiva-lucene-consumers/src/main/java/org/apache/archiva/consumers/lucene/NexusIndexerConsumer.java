begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|lucene
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
name|admin
operator|.
name|model
operator|.
name|RepositoryAdminException
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
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|managed
operator|.
name|ManagedRepositoryAdmin
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
name|plexusbridge
operator|.
name|MavenIndexerUtils
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
name|plexusbridge
operator|.
name|PlexusSisuBridge
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
name|plexusbridge
operator|.
name|PlexusSisuBridgeException
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
name|ConfigurationNames
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
name|FileTypes
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
name|AbstractMonitoredConsumer
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
name|ConsumerException
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
name|KnownRepositoryContentConsumer
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
name|ArchivaTaskScheduler
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
name|maven
operator|.
name|index
operator|.
name|NexusIndexer
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
name|IndexCreator
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
name|codehaus
operator|.
name|plexus
operator|.
name|registry
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|registry
operator|.
name|RegistryListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|taskqueue
operator|.
name|TaskQueueException
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
name|context
operator|.
name|annotation
operator|.
name|Scope
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_comment
comment|/**  * Consumer for indexing the repository to provide search and IDE integration features.  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"knownRepositoryContentConsumer#index-content"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|NexusIndexerConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
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
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
specifier|private
name|File
name|managedRepository
decl_stmt|;
specifier|private
name|ArchivaTaskScheduler
argument_list|<
name|ArtifactIndexingTask
argument_list|>
name|scheduler
decl_stmt|;
specifier|private
name|IndexingContext
name|context
decl_stmt|;
specifier|private
name|NexusIndexer
name|nexusIndexer
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ManagedRepository
name|repository
decl_stmt|;
specifier|private
name|List
argument_list|<
name|?
extends|extends
name|IndexCreator
argument_list|>
name|allIndexCreators
decl_stmt|;
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|NexusIndexerConsumer
parameter_list|(
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaTaskScheduler#indexing"
argument_list|)
name|ArchivaTaskScheduler
argument_list|<
name|ArtifactIndexingTask
argument_list|>
name|scheduler
parameter_list|,
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaConfiguration"
argument_list|)
name|ArchivaConfiguration
name|configuration
parameter_list|,
name|FileTypes
name|filetypes
parameter_list|,
name|PlexusSisuBridge
name|plexusSisuBridge
parameter_list|,
name|MavenIndexerUtils
name|mavenIndexerUtils
parameter_list|,
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
parameter_list|)
throws|throws
name|PlexusSisuBridgeException
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|filetypes
operator|=
name|filetypes
expr_stmt|;
name|this
operator|.
name|scheduler
operator|=
name|scheduler
expr_stmt|;
name|this
operator|.
name|nexusIndexer
operator|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|NexusIndexer
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|allIndexCreators
operator|=
name|mavenIndexerUtils
operator|.
name|getAllIndexCreators
argument_list|()
expr_stmt|;
name|this
operator|.
name|managedRepositoryAdmin
operator|=
name|managedRepositoryAdmin
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Indexes the repository to provide search and IDE integration features"
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"index-content"
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
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|managedRepository
operator|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Creating indexing context for repo : {}"
argument_list|,
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|=
name|managedRepositoryAdmin
operator|.
name|createIndexContext
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
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
if|if
condition|(
name|executeOnEntireRepo
condition|)
block|{
name|beginScan
argument_list|(
name|repository
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|managedRepository
operator|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|managedRepository
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|ArtifactIndexingTask
name|task
init|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repository
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|,
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Queueing indexing task '{}' to add or update the artifact in the index."
argument_list|,
name|task
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|queueTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
if|if
condition|(
name|executeOnEntireRepo
condition|)
block|{
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|managedRepository
argument_list|,
name|path
argument_list|)
decl_stmt|;
comment|// specify in indexing task that this is not a repo scan request!
name|ArtifactIndexingTask
name|task
init|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repository
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|,
name|context
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// only update index we don't need to scan the full repo here
name|task
operator|.
name|setOnlyUpdate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Queueing indexing task '{}' to add or update the artifact in the index."
argument_list|,
name|task
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|queueTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
name|ArtifactIndexingTask
name|task
init|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repository
argument_list|,
literal|null
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|FINISH
argument_list|,
name|context
argument_list|)
decl_stmt|;
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Queueing indexing task '{}' to finish indexing."
argument_list|,
name|task
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|queueTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error queueing task: "
operator|+
name|task
operator|+
literal|": "
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
name|context
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
if|if
condition|(
name|executeOnEntireRepo
condition|)
block|{
name|completeScan
argument_list|()
expr_stmt|;
block|}
comment|// else, do nothing as the context will be closed when indexing task is executed if not a repo scan request!
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
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|ConfigurationNames
operator|.
name|isRepositoryScanning
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|private
name|void
name|initIncludes
parameter_list|()
block|{
name|includes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|includes
operator|.
name|addAll
argument_list|(
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
name|FileTypes
operator|.
name|INDEXABLE_CONTENT
argument_list|)
argument_list|)
expr_stmt|;
name|includes
operator|.
name|addAll
argument_list|(
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|initIncludes
argument_list|()
expr_stmt|;
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
name|includes
return|;
block|}
block|}
end_class

end_unit

