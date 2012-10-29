begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|repository
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepository
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepositoryException
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySession
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySessionFactory
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
name|metadata
operator|.
name|repository
operator|.
name|stats
operator|.
name|RepositoryStatistics
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
name|metadata
operator|.
name|repository
operator|.
name|stats
operator|.
name|RepositoryStatisticsManager
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
name|scanner
operator|.
name|RepositoryContentConsumers
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
name|scanner
operator|.
name|RepositoryScanStatistics
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
name|scanner
operator|.
name|RepositoryScanner
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
name|scanner
operator|.
name|RepositoryScannerException
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
name|codehaus
operator|.
name|plexus
operator|.
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
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
name|redback
operator|.
name|components
operator|.
name|taskqueue
operator|.
name|Task
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
name|redback
operator|.
name|components
operator|.
name|taskqueue
operator|.
name|execution
operator|.
name|TaskExecutionException
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
name|redback
operator|.
name|components
operator|.
name|taskqueue
operator|.
name|execution
operator|.
name|TaskExecutor
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
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/**  * ArchivaRepositoryScanningTaskExecutor  *  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"taskExecutor#repository-scanning"
argument_list|)
specifier|public
class|class
name|ArchivaRepositoryScanningTaskExecutor
implements|implements
name|TaskExecutor
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ArchivaRepositoryScanningTaskExecutor
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
specifier|private
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
comment|/**      * The repository scanner component.      */
annotation|@
name|Inject
specifier|private
name|RepositoryScanner
name|repoScanner
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
specifier|private
name|RepositoryContentConsumers
name|consumers
decl_stmt|;
specifier|private
name|Task
name|task
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
specifier|private
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
decl_stmt|;
comment|/**      * TODO: may be different implementations      */
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Initialized {}"
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|executeTask
parameter_list|(
name|Task
name|task
parameter_list|)
throws|throws
name|TaskExecutionException
block|{
try|try
block|{
comment|// TODO: replace this whole class with the prescribed content scanning service/action
comment|// - scan repository for artifacts that do not have corresponding metadata or have been updated and
comment|// send events for each
comment|// - scan metadata for artifacts that have been removed and send events for each
comment|// - scan metadata for missing plugin data
comment|// - store information so that it can restart upon failure (publish event on the server recovery
comment|// queue, remove it on successful completion)
name|this
operator|.
name|task
operator|=
name|task
expr_stmt|;
name|RepositoryTask
name|repoTask
init|=
operator|(
name|RepositoryTask
operator|)
name|task
decl_stmt|;
name|String
name|repoId
init|=
name|repoTask
operator|.
name|getRepositoryId
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Unable to execute RepositoryTask with blank repository Id."
argument_list|)
throw|;
block|}
name|ManagedRepository
name|arepo
init|=
name|managedRepositoryAdmin
operator|.
name|getManagedRepository
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
comment|// execute consumers on resource file if set
if|if
condition|(
name|repoTask
operator|.
name|getResourceFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Executing task from queue with job name: {}"
argument_list|,
name|repoTask
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|executeConsumers
argument_list|(
name|arepo
argument_list|,
name|repoTask
operator|.
name|getResourceFile
argument_list|()
argument_list|,
name|repoTask
operator|.
name|isUpdateRelatedArtifacts
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
literal|"Executing task from queue with job name: {}"
argument_list|,
name|repoTask
argument_list|)
expr_stmt|;
comment|// otherwise, execute consumers on whole repository
if|if
condition|(
name|arepo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Unable to execute RepositoryTask with invalid repository id: "
operator|+
name|repoId
argument_list|)
throw|;
block|}
name|long
name|sinceWhen
init|=
name|RepositoryScanner
operator|.
name|FRESH_SCAN
decl_stmt|;
name|long
name|previousFileCount
init|=
literal|0
decl_stmt|;
name|RepositorySession
name|repositorySession
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
decl_stmt|;
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|repoTask
operator|.
name|isScanAll
argument_list|()
condition|)
block|{
name|RepositoryStatistics
name|previousStats
init|=
name|repositoryStatisticsManager
operator|.
name|getLastStatistics
argument_list|(
name|metadataRepository
argument_list|,
name|repoId
argument_list|)
decl_stmt|;
if|if
condition|(
name|previousStats
operator|!=
literal|null
condition|)
block|{
name|sinceWhen
operator|=
name|previousStats
operator|.
name|getScanStartTime
argument_list|()
operator|.
name|getTime
argument_list|()
expr_stmt|;
name|previousFileCount
operator|=
name|previousStats
operator|.
name|getTotalFileCount
argument_list|()
expr_stmt|;
block|}
block|}
name|RepositoryScanStatistics
name|stats
decl_stmt|;
try|try
block|{
name|stats
operator|=
name|repoScanner
operator|.
name|scan
argument_list|(
name|arepo
argument_list|,
name|sinceWhen
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryScannerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Repository error when executing repository job."
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|log
operator|.
name|info
argument_list|(
literal|"Finished first scan: {}"
argument_list|,
name|stats
operator|.
name|toDump
argument_list|(
name|arepo
argument_list|)
argument_list|)
expr_stmt|;
comment|// further statistics will be populated by the following method
name|Date
name|endTime
init|=
operator|new
name|Date
argument_list|(
name|stats
operator|.
name|getWhenGathered
argument_list|()
operator|.
name|getTime
argument_list|()
operator|+
name|stats
operator|.
name|getDuration
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Gathering repository statistics"
argument_list|)
expr_stmt|;
name|repositoryStatisticsManager
operator|.
name|addStatisticsAfterScan
argument_list|(
name|metadataRepository
argument_list|,
name|repoId
argument_list|,
name|stats
operator|.
name|getWhenGathered
argument_list|()
argument_list|,
name|endTime
argument_list|,
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|,
name|stats
operator|.
name|getTotalFileCount
argument_list|()
operator|-
name|previousFileCount
argument_list|)
expr_stmt|;
name|repositorySession
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Unable to store updated statistics: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|//                log.info( "Scanning for removed repository content" );
comment|//                metadataRepository.findAllProjects();
comment|// FIXME: do something
name|log
operator|.
name|info
argument_list|(
literal|"Finished repository task: {}"
argument_list|,
name|repoTask
argument_list|)
expr_stmt|;
name|this
operator|.
name|task
operator|=
literal|null
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
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
name|TaskExecutionException
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
name|Task
name|getCurrentTaskInExecution
parameter_list|()
block|{
return|return
name|task
return|;
block|}
specifier|public
name|RepositoryScanner
name|getRepoScanner
parameter_list|()
block|{
return|return
name|repoScanner
return|;
block|}
specifier|public
name|void
name|setRepoScanner
parameter_list|(
name|RepositoryScanner
name|repoScanner
parameter_list|)
block|{
name|this
operator|.
name|repoScanner
operator|=
name|repoScanner
expr_stmt|;
block|}
specifier|public
name|RepositoryContentConsumers
name|getConsumers
parameter_list|()
block|{
return|return
name|consumers
return|;
block|}
specifier|public
name|void
name|setConsumers
parameter_list|(
name|RepositoryContentConsumers
name|consumers
parameter_list|)
block|{
name|this
operator|.
name|consumers
operator|=
name|consumers
expr_stmt|;
block|}
specifier|public
name|RepositorySessionFactory
name|getRepositorySessionFactory
parameter_list|()
block|{
return|return
name|repositorySessionFactory
return|;
block|}
specifier|public
name|void
name|setRepositorySessionFactory
parameter_list|(
name|RepositorySessionFactory
name|repositorySessionFactory
parameter_list|)
block|{
name|this
operator|.
name|repositorySessionFactory
operator|=
name|repositorySessionFactory
expr_stmt|;
block|}
specifier|public
name|RepositoryStatisticsManager
name|getRepositoryStatisticsManager
parameter_list|()
block|{
return|return
name|repositoryStatisticsManager
return|;
block|}
specifier|public
name|void
name|setRepositoryStatisticsManager
parameter_list|(
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
parameter_list|)
block|{
name|this
operator|.
name|repositoryStatisticsManager
operator|=
name|repositoryStatisticsManager
expr_stmt|;
block|}
specifier|public
name|ManagedRepositoryAdmin
name|getManagedRepositoryAdmin
parameter_list|()
block|{
return|return
name|managedRepositoryAdmin
return|;
block|}
specifier|public
name|void
name|setManagedRepositoryAdmin
parameter_list|(
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
parameter_list|)
block|{
name|this
operator|.
name|managedRepositoryAdmin
operator|=
name|managedRepositoryAdmin
expr_stmt|;
block|}
block|}
end_class

end_unit

