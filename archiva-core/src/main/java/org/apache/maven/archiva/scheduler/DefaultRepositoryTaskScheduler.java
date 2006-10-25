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
name|scheduler
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|Configuration
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
name|ConfigurationChangeException
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
name|ConfigurationChangeListener
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
name|ConfigurationStore
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
name|ConfigurationStoreException
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
name|InvalidConfigurationException
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
name|indexer
operator|.
name|RepositoryArtifactIndex
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
name|indexer
operator|.
name|RepositoryArtifactIndexFactory
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
name|indexer
operator|.
name|RepositoryIndexException
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
name|scheduler
operator|.
name|executors
operator|.
name|IndexerTaskExecutor
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
name|scheduler
operator|.
name|task
operator|.
name|IndexerTask
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
name|logging
operator|.
name|AbstractLogEnabled
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
name|text
operator|.
name|ParseException
import|;
end_import

begin_comment
comment|/**  * Default implementation of a scheduling component for the application.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @plexus.component role="org.apache.maven.archiva.scheduler.RepositoryTaskScheduler"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultRepositoryTaskScheduler
extends|extends
name|AbstractLogEnabled
implements|implements
name|RepositoryTaskScheduler
implements|,
name|Startable
implements|,
name|ConfigurationChangeListener
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|Scheduler
name|scheduler
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="indexer"      */
specifier|private
name|TaskQueue
name|indexerQueue
decl_stmt|;
comment|/**      * @plexus.requirement role="org.codehaus.plexus.taskqueue.execution.TaskExecutor" role-hint="indexer"      */
specifier|private
name|IndexerTaskExecutor
name|indexerTaskExecutor
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfigurationStore
name|configurationStore
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryArtifactIndexFactory
name|indexFactory
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DISCOVERER_GROUP
init|=
literal|"DISCOVERER"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|INDEXER_JOB
init|=
literal|"indexerTask"
decl_stmt|;
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|StartingException
block|{
name|Configuration
name|configuration
decl_stmt|;
try|try
block|{
name|configuration
operator|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
expr_stmt|;
name|configurationStore
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationStoreException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StartingException
argument_list|(
literal|"Unable to read configuration from the store"
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|scheduleJobs
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|StartingException
argument_list|(
literal|"Invalid configuration: "
operator|+
name|configuration
operator|.
name|getIndexerCronExpression
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
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
name|scheduleJobs
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|ParseException
throws|,
name|SchedulerException
block|{
if|if
condition|(
name|configuration
operator|.
name|getIndexPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|JobDetail
name|jobDetail
init|=
name|createJobDetail
argument_list|(
name|INDEXER_JOB
argument_list|)
decl_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Scheduling indexer: "
operator|+
name|configuration
operator|.
name|getIndexerCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|CronTrigger
name|trigger
init|=
operator|new
name|CronTrigger
argument_list|(
name|INDEXER_JOB
operator|+
literal|"Trigger"
argument_list|,
name|DISCOVERER_GROUP
argument_list|,
name|configuration
operator|.
name|getIndexerCronExpression
argument_list|()
argument_list|)
decl_stmt|;
name|scheduler
operator|.
name|scheduleJob
argument_list|(
name|jobDetail
argument_list|,
name|trigger
argument_list|)
expr_stmt|;
try|try
block|{
name|queueNowIfNeeded
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
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
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Error executing task first time, continuing anyway: "
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
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Not scheduling indexer - index path is not configured"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|JobDetail
name|createJobDetail
parameter_list|(
name|String
name|jobName
parameter_list|)
block|{
name|JobDetail
name|jobDetail
init|=
operator|new
name|JobDetail
argument_list|(
name|jobName
argument_list|,
name|DISCOVERER_GROUP
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
name|indexerQueue
argument_list|)
expr_stmt|;
name|jobDetail
operator|.
name|setJobDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
return|return
name|jobDetail
return|;
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
name|INDEXER_JOB
argument_list|,
name|DISCOVERER_GROUP
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
name|notifyOfConfigurationChange
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|InvalidConfigurationException
throws|,
name|ConfigurationChangeException
block|{
try|try
block|{
name|stop
argument_list|()
expr_stmt|;
name|scheduleJobs
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|StoppingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigurationChangeException
argument_list|(
literal|"Unable to unschedule previous tasks"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidConfigurationException
argument_list|(
literal|"indexerCronExpression"
argument_list|,
literal|"Invalid cron expression"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SchedulerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConfigurationChangeException
argument_list|(
literal|"Unable to schedule new tasks"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|runIndexer
parameter_list|()
throws|throws
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|TaskExecutionException
block|{
name|IndexerTask
name|task
init|=
operator|new
name|IndexerTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setJobName
argument_list|(
literal|"INDEX_INIT"
argument_list|)
expr_stmt|;
try|try
block|{
name|indexerQueue
operator|.
name|put
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|scheduler
operator|.
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
name|void
name|queueNowIfNeeded
parameter_list|()
throws|throws
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
block|{
name|Configuration
name|configuration
decl_stmt|;
try|try
block|{
name|configuration
operator|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationStoreException
name|e
parameter_list|)
block|{
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
name|File
name|indexPath
init|=
operator|new
name|File
argument_list|(
name|configuration
operator|.
name|getIndexPath
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|RepositoryArtifactIndex
name|artifactIndex
init|=
name|indexFactory
operator|.
name|createStandardIndex
argument_list|(
name|indexPath
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifactIndex
operator|.
name|exists
argument_list|()
condition|)
block|{
name|runIndexer
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryIndexException
name|e
parameter_list|)
block|{
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
catch|catch
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|TaskExecutionException
name|e
parameter_list|)
block|{
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
block|}
end_class

end_unit

