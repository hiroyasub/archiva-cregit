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
name|maven
operator|.
name|archiva
operator|.
name|common
operator|.
name|ArchivaException
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|Startable
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
name|StartingException
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
name|StoppingException
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
name|scheduler
operator|.
name|CronExpressionValidator
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
name|scheduler
operator|.
name|Scheduler
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
name|TaskQueue
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
name|quartz
operator|.
name|CronTrigger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobDataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobDetail
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|SchedulerException
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
name|text
operator|.
name|ParseException
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
name|HashSet
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Default implementation of a scheduling component for archiva.  *  * @plexus.component role="org.apache.archiva.scheduler.ArchivaTaskScheduler" role-hint="repository"  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryArchivaTaskScheduler
implements|implements
name|ArchivaTaskScheduler
argument_list|<
name|RepositoryTask
argument_list|>
implements|,
name|Startable
implements|,
name|ConfigurationListener
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RepositoryArchivaTaskScheduler
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|Scheduler
name|scheduler
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="repository-scanning"      */
specifier|private
name|TaskQueue
name|repositoryScanningQueue
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
decl_stmt|;
comment|/**      * TODO: could have multiple implementations      *      * @plexus.requirement      */
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPOSITORY_SCAN_GROUP
init|=
literal|"rg"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPOSITORY_JOB
init|=
literal|"rj"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPOSITORY_JOB_TRIGGER
init|=
literal|"rjt"
decl_stmt|;
specifier|static
specifier|final
name|String
name|TASK_QUEUE
init|=
literal|"TASK_QUEUE"
decl_stmt|;
specifier|static
specifier|final
name|String
name|TASK_REPOSITORY
init|=
literal|"TASK_REPOSITORY"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CRON_HOURLY
init|=
literal|"0 0 * * * ?"
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|jobs
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|queuedRepos
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|startup
parameter_list|()
throws|throws
name|ArchivaException
block|{
name|archivaConfiguration
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
try|try
block|{
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|StartingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaException
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
name|start
parameter_list|()
throws|throws
name|StartingException
block|{
name|List
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
name|repositories
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
name|RepositorySession
name|repositorySession
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
decl_stmt|;
try|try
block|{
name|MetadataRepository
name|metadataRepository
init|=
name|repositorySession
operator|.
name|getRepository
argument_list|()
decl_stmt|;
for|for
control|(
name|ManagedRepositoryConfiguration
name|repoConfig
range|:
name|repositories
control|)
block|{
if|if
condition|(
name|repoConfig
operator|.
name|isScanned
argument_list|()
condition|)
block|{
try|try
block|{
name|scheduleRepositoryJobs
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SchedulerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StartingException
argument_list|(
literal|"Unable to start scheduler: "
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
try|try
block|{
if|if
condition|(
operator|!
name|isPreviouslyScanned
argument_list|(
name|repoConfig
argument_list|,
name|metadataRepository
argument_list|)
condition|)
block|{
name|queueInitialRepoScan
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to determine if a repository is already scanned, skipping initial scan: "
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
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|StoppingException
block|{
try|try
block|{
for|for
control|(
name|String
name|job
range|:
name|jobs
control|)
block|{
name|scheduler
operator|.
name|unscheduleJob
argument_list|(
name|job
argument_list|,
name|REPOSITORY_SCAN_GROUP
argument_list|)
expr_stmt|;
block|}
name|jobs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|queuedRepos
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SchedulerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StoppingException
argument_list|(
literal|"Unable to unschedule tasks"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|boolean
name|isProcessingRepositoryTask
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
synchronized|synchronized
init|(
name|repositoryScanningQueue
init|)
block|{
name|List
argument_list|<
name|RepositoryTask
argument_list|>
name|queue
init|=
literal|null
decl_stmt|;
try|try
block|{
name|queue
operator|=
name|repositoryScanningQueue
operator|.
name|getQueueSnapshot
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
comment|// not possible with plexus-taskqueue implementation, ignore
block|}
for|for
control|(
name|RepositoryTask
name|queuedTask
range|:
name|queue
control|)
block|{
if|if
condition|(
name|queuedTask
operator|.
name|getRepositoryId
argument_list|()
operator|.
name|equals
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|boolean
name|isProcessingRepositoryTask
parameter_list|(
name|RepositoryTask
name|task
parameter_list|)
block|{
synchronized|synchronized
init|(
name|repositoryScanningQueue
init|)
block|{
name|List
argument_list|<
name|RepositoryTask
argument_list|>
name|queue
init|=
literal|null
decl_stmt|;
try|try
block|{
name|queue
operator|=
name|repositoryScanningQueue
operator|.
name|getQueueSnapshot
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
comment|// not possible with plexus-taskqueue implementation, ignore
block|}
for|for
control|(
name|RepositoryTask
name|queuedTask
range|:
name|queue
control|)
block|{
if|if
condition|(
name|task
operator|.
name|equals
argument_list|(
name|queuedTask
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
specifier|public
name|void
name|queueTask
parameter_list|(
name|RepositoryTask
name|task
parameter_list|)
throws|throws
name|TaskQueueException
block|{
synchronized|synchronized
init|(
name|repositoryScanningQueue
init|)
block|{
if|if
condition|(
name|isProcessingRepositoryTask
argument_list|(
name|task
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Repository task '"
operator|+
name|task
operator|+
literal|"' is already queued. Skipping task."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// add check if the task is already queued if it is a file scan
name|repositoryScanningQueue
operator|.
name|put
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|configurationEvent
parameter_list|(
name|ConfigurationEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getType
argument_list|()
operator|==
name|ConfigurationEvent
operator|.
name|SAVED
condition|)
block|{
for|for
control|(
name|String
name|job
range|:
name|jobs
control|)
block|{
try|try
block|{
name|scheduler
operator|.
name|unscheduleJob
argument_list|(
name|job
argument_list|,
name|REPOSITORY_SCAN_GROUP
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SchedulerException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error restarting the repository scanning job after property change."
argument_list|)
expr_stmt|;
block|}
block|}
name|jobs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ManagedRepositoryConfiguration
argument_list|>
name|repositories
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|ManagedRepositoryConfiguration
name|repoConfig
range|:
name|repositories
control|)
block|{
if|if
condition|(
name|repoConfig
operator|.
name|getRefreshCronExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|scheduleRepositoryJobs
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SchedulerException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"error restarting job: "
operator|+
name|REPOSITORY_JOB
operator|+
literal|":"
operator|+
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|boolean
name|isPreviouslyScanned
parameter_list|(
name|ManagedRepositoryConfiguration
name|repoConfig
parameter_list|,
name|MetadataRepository
name|metadataRepository
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
return|return
name|repositoryStatisticsManager
operator|.
name|getLastStatistics
argument_list|(
name|metadataRepository
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|// MRM-848: Pre-configured repository initially appear to be empty
specifier|private
specifier|synchronized
name|void
name|queueInitialRepoScan
parameter_list|(
name|ManagedRepositoryConfiguration
name|repoConfig
parameter_list|)
block|{
name|String
name|repoId
init|=
name|repoConfig
operator|.
name|getId
argument_list|()
decl_stmt|;
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
name|repoId
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|queuedRepos
operator|.
name|contains
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Repository ["
operator|+
name|repoId
operator|+
literal|"] is queued to be scanned as it hasn't been previously."
argument_list|)
expr_stmt|;
try|try
block|{
name|queuedRepos
operator|.
name|add
argument_list|(
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|this
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
literal|"Error occurred while queueing repository ["
operator|+
name|repoId
operator|+
literal|"] task : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|scheduleRepositoryJobs
parameter_list|(
name|ManagedRepositoryConfiguration
name|repoConfig
parameter_list|)
throws|throws
name|SchedulerException
block|{
if|if
condition|(
name|repoConfig
operator|.
name|getRefreshCronExpression
argument_list|()
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Skipping job, no cron expression for "
operator|+
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|repoConfig
operator|.
name|isScanned
argument_list|()
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Skipping job, repository scannable has been disabled for "
operator|+
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// get the cron string for these database scanning jobs
name|String
name|cronString
init|=
name|repoConfig
operator|.
name|getRefreshCronExpression
argument_list|()
decl_stmt|;
name|CronExpressionValidator
name|cronValidator
init|=
operator|new
name|CronExpressionValidator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|cronValidator
operator|.
name|validate
argument_list|(
name|cronString
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cron expression ["
operator|+
name|cronString
operator|+
literal|"] for repository ["
operator|+
name|repoConfig
operator|.
name|getId
argument_list|()
operator|+
literal|"] is invalid.  Defaulting to hourly."
argument_list|)
expr_stmt|;
name|cronString
operator|=
name|CRON_HOURLY
expr_stmt|;
block|}
comment|// setup the unprocessed artifact job
name|JobDetail
name|repositoryJob
init|=
operator|new
name|JobDetail
argument_list|(
name|REPOSITORY_JOB
operator|+
literal|":"
operator|+
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|REPOSITORY_SCAN_GROUP
argument_list|,
name|RepositoryTaskJob
operator|.
name|class
argument_list|)
decl_stmt|;
name|JobDataMap
name|dataMap
init|=
operator|new
name|JobDataMap
argument_list|()
decl_stmt|;
name|dataMap
operator|.
name|put
argument_list|(
name|TASK_QUEUE
argument_list|,
name|repositoryScanningQueue
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|put
argument_list|(
name|TASK_REPOSITORY
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryJob
operator|.
name|setJobDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
try|try
block|{
name|CronTrigger
name|trigger
init|=
operator|new
name|CronTrigger
argument_list|(
name|REPOSITORY_JOB_TRIGGER
operator|+
literal|":"
operator|+
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|REPOSITORY_SCAN_GROUP
argument_list|,
name|cronString
argument_list|)
decl_stmt|;
name|jobs
operator|.
name|add
argument_list|(
name|REPOSITORY_JOB
operator|+
literal|":"
operator|+
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|scheduleJob
argument_list|(
name|repositoryJob
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"ParseException in repository scanning cron expression, disabling repository scanning for '"
operator|+
name|repoConfig
operator|.
name|getId
argument_list|()
operator|+
literal|"': "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

