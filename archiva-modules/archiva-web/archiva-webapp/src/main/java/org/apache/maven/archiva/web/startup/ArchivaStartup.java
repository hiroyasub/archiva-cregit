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
name|web
operator|.
name|startup
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
name|DefaultScheduler
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
name|spring
operator|.
name|PlexusToSpringUtils
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
name|spring
operator|.
name|PlexusWebApplicationContext
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
name|execution
operator|.
name|TaskQueueExecutor
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
name|ThreadedTaskQueueExecutor
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
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|context
operator|.
name|WebApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|context
operator|.
name|support
operator|.
name|WebApplicationContextUtils
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContextEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContextListener
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
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
name|ExecutorService
import|;
end_import

begin_comment
comment|/**  * ArchivaStartup - the startup of all archiva features in a deterministic order.  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaStartup
implements|implements
name|ServletContextListener
block|{
specifier|private
name|ThreadedTaskQueueExecutor
name|tqeDbScanning
decl_stmt|;
specifier|private
name|ThreadedTaskQueueExecutor
name|tqeRepoScanning
decl_stmt|;
specifier|private
name|ThreadedTaskQueueExecutor
name|tqeIndexing
decl_stmt|;
specifier|private
name|RepositoryArchivaTaskScheduler
name|repositoryTaskScheduler
decl_stmt|;
specifier|public
name|void
name|contextInitialized
parameter_list|(
name|ServletContextEvent
name|contextEvent
parameter_list|)
block|{
name|WebApplicationContext
name|wac
init|=
name|WebApplicationContextUtils
operator|.
name|getRequiredWebApplicationContext
argument_list|(
name|contextEvent
operator|.
name|getServletContext
argument_list|()
argument_list|)
decl_stmt|;
name|SecuritySynchronization
name|securitySync
init|=
operator|(
name|SecuritySynchronization
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|SecuritySynchronization
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|repositoryTaskScheduler
operator|=
operator|(
name|RepositoryArchivaTaskScheduler
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|ArchivaTaskScheduler
operator|.
name|class
argument_list|,
literal|"repository"
argument_list|)
argument_list|)
expr_stmt|;
name|tqeRepoScanning
operator|=
operator|(
name|ThreadedTaskQueueExecutor
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|TaskQueueExecutor
operator|.
name|class
argument_list|,
literal|"repository-scanning"
argument_list|)
argument_list|)
expr_stmt|;
name|tqeIndexing
operator|=
operator|(
name|ThreadedTaskQueueExecutor
operator|)
name|wac
operator|.
name|getBean
argument_list|(
name|PlexusToSpringUtils
operator|.
name|buildSpringId
argument_list|(
name|TaskQueueExecutor
operator|.
name|class
argument_list|,
literal|"indexing"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|securitySync
operator|.
name|startup
argument_list|()
expr_stmt|;
name|repositoryTaskScheduler
operator|.
name|startup
argument_list|()
expr_stmt|;
name|Banner
operator|.
name|display
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to properly startup archiva: "
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
name|void
name|contextDestroyed
parameter_list|(
name|ServletContextEvent
name|contextEvent
parameter_list|)
block|{
name|WebApplicationContext
name|applicationContext
init|=
name|WebApplicationContextUtils
operator|.
name|getRequiredWebApplicationContext
argument_list|(
name|contextEvent
operator|.
name|getServletContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|applicationContext
operator|!=
literal|null
operator|&&
name|applicationContext
operator|instanceof
name|ClassPathXmlApplicationContext
condition|)
block|{
operator|(
operator|(
name|ClassPathXmlApplicationContext
operator|)
name|applicationContext
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|applicationContext
operator|!=
literal|null
operator|&&
name|applicationContext
operator|instanceof
name|PlexusWebApplicationContext
condition|)
block|{
comment|// stop task queue executors
name|stopTaskQueueExecutor
argument_list|(
name|tqeDbScanning
argument_list|)
expr_stmt|;
name|stopTaskQueueExecutor
argument_list|(
name|tqeRepoScanning
argument_list|)
expr_stmt|;
name|stopTaskQueueExecutor
argument_list|(
name|tqeIndexing
argument_list|)
expr_stmt|;
comment|// stop the DefaultArchivaTaskScheduler and its scheduler
if|if
condition|(
name|repositoryTaskScheduler
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|repositoryTaskScheduler
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|StoppingException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
try|try
block|{
comment|// shutdown the scheduler, otherwise Quartz scheduler and Threads still exists
name|Field
name|schedulerField
init|=
name|repositoryTaskScheduler
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"scheduler"
argument_list|)
decl_stmt|;
name|schedulerField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DefaultScheduler
name|scheduler
init|=
operator|(
name|DefaultScheduler
operator|)
name|schedulerField
operator|.
name|get
argument_list|(
name|repositoryTaskScheduler
argument_list|)
decl_stmt|;
name|scheduler
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|// close the application context
operator|(
operator|(
name|PlexusWebApplicationContext
operator|)
name|applicationContext
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|stopTaskQueueExecutor
parameter_list|(
name|ThreadedTaskQueueExecutor
name|taskQueueExecutor
parameter_list|)
block|{
if|if
condition|(
name|taskQueueExecutor
operator|!=
literal|null
condition|)
block|{
name|Task
name|currentTask
init|=
name|taskQueueExecutor
operator|.
name|getCurrentTask
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentTask
operator|!=
literal|null
condition|)
block|{
name|taskQueueExecutor
operator|.
name|cancelTask
argument_list|(
name|currentTask
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|taskQueueExecutor
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ExecutorService
name|service
init|=
name|getExecutorServiceForTTQE
argument_list|(
name|taskQueueExecutor
argument_list|)
decl_stmt|;
if|if
condition|(
name|service
operator|!=
literal|null
condition|)
block|{
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|ExecutorService
name|getExecutorServiceForTTQE
parameter_list|(
name|ThreadedTaskQueueExecutor
name|ttqe
parameter_list|)
block|{
name|ExecutorService
name|service
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Field
name|executorServiceField
init|=
name|ttqe
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredField
argument_list|(
literal|"executorService"
argument_list|)
decl_stmt|;
name|executorServiceField
operator|.
name|setAccessible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|service
operator|=
operator|(
name|ExecutorService
operator|)
name|executorServiceField
operator|.
name|get
argument_list|(
name|ttqe
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|service
return|;
block|}
block|}
end_class

end_unit

