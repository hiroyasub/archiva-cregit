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
name|services
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
name|managed
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ArchivaRestServiceException
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|RepositoriesService
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
name|ArchivaIndexingTaskExecutor
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
name|RepositoryArchivaTaskScheduler
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
name|RepositoryTask
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
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

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositoriesService#rest"
argument_list|)
specifier|public
class|class
name|DefaultRepositoriesService
extends|extends
name|AbstractRestService
implements|implements
name|RepositoriesService
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
literal|"archivaTaskScheduler#repository"
argument_list|)
specifier|private
name|RepositoryArchivaTaskScheduler
name|repositoryTaskScheduler
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"taskExecutor#indexing"
argument_list|)
specifier|private
name|ArchivaIndexingTaskExecutor
name|archivaIndexingTaskExecutor
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|PlexusSisuBridge
name|plexusSisuBridge
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|MavenIndexerUtils
name|mavenIndexerUtils
decl_stmt|;
comment|// FIXME olamy move this to repository admin component !
specifier|public
name|Boolean
name|scanRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|boolean
name|fullScan
parameter_list|)
block|{
if|if
condition|(
name|repositoryTaskScheduler
operator|.
name|isProcessingRepositoryTask
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"scanning of repository with id {} already scheduled"
argument_list|)
expr_stmt|;
block|}
name|RepositoryTask
name|task
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setRepositoryId
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
name|task
operator|.
name|setScanAll
argument_list|(
name|fullScan
argument_list|)
expr_stmt|;
try|try
block|{
name|repositoryTaskScheduler
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
literal|"failed to schedule scanning of repo with id {}"
argument_list|,
name|repositoryId
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|Boolean
name|alreadyScanning
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
return|return
name|repositoryTaskScheduler
operator|.
name|isProcessingRepositoryTask
argument_list|(
name|repositoryId
argument_list|)
return|;
block|}
specifier|public
name|Boolean
name|removeScanningTaskFromQueue
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"repositoryId"
argument_list|)
name|String
name|repositoryId
parameter_list|)
block|{
name|RepositoryTask
name|task
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setRepositoryId
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|repositoryTaskScheduler
operator|.
name|unQueueTask
argument_list|(
name|task
argument_list|)
return|;
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
literal|"failed to unschedule scanning of repo with id {}"
argument_list|,
name|repositoryId
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|Boolean
name|scanRepositoryNow
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|boolean
name|fullScan
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
name|ManagedRepository
name|repository
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
name|repositoryId
argument_list|)
decl_stmt|;
name|IndexingContext
name|context
init|=
name|ArtifactIndexingTask
operator|.
name|createContext
argument_list|(
name|repository
argument_list|,
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|NexusIndexer
operator|.
name|class
argument_list|)
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|IndexCreator
argument_list|>
argument_list|(
name|mavenIndexerUtils
operator|.
name|getAllIndexCreators
argument_list|()
argument_list|)
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
name|task
operator|.
name|setExecuteOnEntireRepo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|task
operator|.
name|setOnlyUpdate
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|archivaIndexingTaskExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|TRUE
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

