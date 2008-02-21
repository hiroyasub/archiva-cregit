begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|scheduled
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|List
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
name|ManagedRepositoryConfiguration
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
name|scheduled
operator|.
name|tasks
operator|.
name|ArchivaTask
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
name|scheduled
operator|.
name|tasks
operator|.
name|DatabaseTask
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
name|scheduled
operator|.
name|tasks
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
name|archiva
operator|.
name|scheduled
operator|.
name|tasks
operator|.
name|RepositoryTaskSelectionPredicate
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
name|Task
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
name|codehaus
operator|.
name|plexus
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

begin_comment
comment|/**  * Default implementation of a scheduling component for archiva.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @author<a href="mailto:jmcconnell@apache.org">Jesse McConnell</a>  * @plexus.component role="org.apache.maven.archiva.scheduled.ArchivaTaskScheduler" role-hint="default"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultArchivaTaskScheduler
implements|implements
name|ArchivaTaskScheduler
implements|,
name|Startable
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
name|DefaultArchivaTaskScheduler
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|Scheduler
name|scheduler
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="database-update"      */
specifier|private
name|TaskQueue
name|databaseUpdateQueue
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
specifier|public
specifier|static
specifier|final
name|String
name|DATABASE_SCAN_GROUP
init|=
literal|"database-group"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATABASE_JOB
init|=
literal|"database-job"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATABASE_JOB_TRIGGER
init|=
literal|"database-job-trigger"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_SCAN_GROUP
init|=
literal|"repository-group"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_JOB
init|=
literal|"repository-job"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REPOSITORY_JOB_TRIGGER
init|=
literal|"repository-job-trigger"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CRON_HOURLY
init|=
literal|"0 0 * * * ?"
decl_stmt|;
specifier|public
name|void
name|startup
parameter_list|()
throws|throws
name|ArchivaException
block|{
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
try|try
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
name|scheduleRepositoryJobs
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
block|}
block|}
name|scheduleDatabaseJobs
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
block|}
specifier|private
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
name|RepositoryTaskJob
operator|.
name|TASK_QUEUE
argument_list|,
name|repositoryScanningQueue
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|put
argument_list|(
name|RepositoryTaskJob
operator|.
name|TASK_QUEUE_POLICY
argument_list|,
name|ArchivaTask
operator|.
name|QUEUE_POLICY_WAIT
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|put
argument_list|(
name|RepositoryTaskJob
operator|.
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
specifier|private
name|void
name|scheduleDatabaseJobs
parameter_list|()
throws|throws
name|SchedulerException
block|{
name|String
name|cronString
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDatabaseScanning
argument_list|()
operator|.
name|getCronExpression
argument_list|()
decl_stmt|;
comment|// setup the unprocessed artifact job
name|JobDetail
name|databaseJob
init|=
operator|new
name|JobDetail
argument_list|(
name|DATABASE_JOB
argument_list|,
name|DATABASE_SCAN_GROUP
argument_list|,
name|DatabaseTaskJob
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
name|DatabaseTaskJob
operator|.
name|TASK_QUEUE
argument_list|,
name|databaseUpdateQueue
argument_list|)
expr_stmt|;
name|databaseJob
operator|.
name|setJobDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
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
literal|"] for database update is invalid.  Defaulting to hourly."
argument_list|)
expr_stmt|;
name|cronString
operator|=
name|CRON_HOURLY
expr_stmt|;
block|}
try|try
block|{
name|CronTrigger
name|trigger
init|=
operator|new
name|CronTrigger
argument_list|(
name|DATABASE_JOB_TRIGGER
argument_list|,
name|DATABASE_SCAN_GROUP
argument_list|,
name|cronString
argument_list|)
decl_stmt|;
name|scheduler
operator|.
name|scheduleJob
argument_list|(
name|databaseJob
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
literal|"ParseException in database scanning cron expression, disabling database scanning: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
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
name|scheduler
operator|.
name|unscheduleJob
argument_list|(
name|DATABASE_JOB
argument_list|,
name|DATABASE_SCAN_GROUP
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
name|StoppingException
argument_list|(
literal|"Unable to unschedule tasks"
argument_list|,
name|e
argument_list|)
throw|;
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
comment|// nothing to do
block|}
comment|/**      *      */
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
comment|// cronExpression comes from the database scanning section
if|if
condition|(
literal|"cronExpression"
operator|.
name|equals
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Restarting the database scheduled task after property change: "
operator|+
name|propertyName
argument_list|)
expr_stmt|;
try|try
block|{
name|scheduler
operator|.
name|unscheduleJob
argument_list|(
name|DATABASE_JOB
argument_list|,
name|DATABASE_SCAN_GROUP
argument_list|)
expr_stmt|;
name|scheduleDatabaseJobs
argument_list|()
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
literal|"Error restarting the database scanning job after property change."
argument_list|)
expr_stmt|;
block|}
block|}
comment|// refreshCronExpression comes from the repositories section
comment|//
comment|// currently we have to reschedule all repo jobs because we don't know where the changed one came from
if|if
condition|(
literal|"refreshCronExpression"
operator|.
name|equals
argument_list|(
name|propertyName
argument_list|)
condition|)
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
comment|// unschedule handles jobs that might not exist
name|scheduler
operator|.
name|unscheduleJob
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
argument_list|)
expr_stmt|;
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
specifier|public
name|void
name|scheduleDatabaseTasks
parameter_list|()
throws|throws
name|TaskExecutionException
block|{
try|try
block|{
name|scheduleDatabaseJobs
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
name|TaskExecutionException
argument_list|(
literal|"Unable to schedule repository jobs: "
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
block|}
specifier|public
name|boolean
name|isProcessingAnyRepositoryTask
parameter_list|()
throws|throws
name|ArchivaException
block|{
name|List
argument_list|<
name|?
extends|extends
name|Task
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
throw|throw
operator|new
name|ArchivaException
argument_list|(
literal|"Unable to get repository scanning queue:"
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
return|return
operator|!
name|queue
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isProcessingRepositoryTask
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaException
block|{
name|List
argument_list|<
name|?
extends|extends
name|Task
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
throw|throw
operator|new
name|ArchivaException
argument_list|(
literal|"Unable to get repository scanning queue:"
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
return|return
name|CollectionUtils
operator|.
name|exists
argument_list|(
name|queue
argument_list|,
operator|new
name|RepositoryTaskSelectionPredicate
argument_list|(
name|repositoryId
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isProcessingDatabaseTask
parameter_list|()
throws|throws
name|ArchivaException
block|{
name|List
argument_list|<
name|?
extends|extends
name|Task
argument_list|>
name|queue
init|=
literal|null
decl_stmt|;
try|try
block|{
name|queue
operator|=
name|databaseUpdateQueue
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
throw|throw
operator|new
name|ArchivaException
argument_list|(
literal|"Unable to get database update queue:"
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
return|return
operator|!
name|queue
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|void
name|queueRepositoryTask
parameter_list|(
name|RepositoryTask
name|task
parameter_list|)
throws|throws
name|TaskQueueException
block|{
name|repositoryScanningQueue
operator|.
name|put
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|queueDatabaseTask
parameter_list|(
name|DatabaseTask
name|task
parameter_list|)
throws|throws
name|TaskQueueException
block|{
name|databaseUpdateQueue
operator|.
name|put
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

