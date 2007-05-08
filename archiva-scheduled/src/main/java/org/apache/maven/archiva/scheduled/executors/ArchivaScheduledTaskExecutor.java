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
operator|.
name|executors
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
name|RepositoryScanningConfiguration
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
name|database
operator|.
name|ArchivaDAO
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
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|RepositoryDAO
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
name|database
operator|.
name|updater
operator|.
name|DatabaseUpdater
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
name|model
operator|.
name|ArchivaRepository
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
name|model
operator|.
name|RepositoryContentStatistics
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
name|repository
operator|.
name|RepositoryException
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
name|repository
operator|.
name|scanner
operator|.
name|RepositoryContentConsumerUtil
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
name|TaskExecutionException
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
name|TaskExecutor
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

begin_comment
comment|/**  *  * @author<a href="mailto:jmcconnell@apache.org">Jesse McConnell</a>  * @version $Id:$  *   * @plexus.component role="org.codehaus.plexus.taskqueue.execution.TaskExecutor"   *      role-hint="archiva-task-executor"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaScheduledTaskExecutor
extends|extends
name|AbstractLogEnabled
implements|implements
name|TaskExecutor
block|{
comment|/**      * Configuration store.      *      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|DatabaseUpdater
name|databaseUpdater
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|RepositoryDAO
name|repositoryDAO
decl_stmt|;
comment|/**      * The collection of available database consumers.      * @plexus.requirement role="org.apache.maven.archiva.consumers.ArchivaArtifactConsumer"      */
specifier|private
name|Map
name|availableDBConsumers
decl_stmt|;
comment|/**      * The collection of available repository consumers.      *       * @plexus.requirement      */
specifier|private
name|RepositoryContentConsumerUtil
name|repositoryContentConsumerUtil
decl_stmt|;
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
if|if
condition|(
name|task
operator|instanceof
name|DatabaseTask
condition|)
block|{
name|executeDatabaseTask
argument_list|(
operator|(
name|DatabaseTask
operator|)
name|task
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|task
operator|instanceof
name|RepositoryTask
condition|)
block|{
name|executeRepositoryTask
argument_list|(
operator|(
name|RepositoryTask
operator|)
name|task
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Unknown Task: "
operator|+
name|task
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|executeDatabaseTask
parameter_list|(
name|DatabaseTask
name|task
parameter_list|)
throws|throws
name|TaskExecutionException
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Executing task from queue with job name: "
operator|+
name|task
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
try|try
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Task: Updating unprocessed artifacts"
argument_list|)
expr_stmt|;
name|databaseUpdater
operator|.
name|updateAllUnprocessed
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Error running unprocessed updater"
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Task: Updating processed artifacts"
argument_list|)
expr_stmt|;
name|databaseUpdater
operator|.
name|updateAllProcessed
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Error running processed updater"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|time
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|time
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Finished database task in "
operator|+
name|time
operator|+
literal|"ms."
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|executeRepositoryTask
parameter_list|(
name|RepositoryTask
name|task
parameter_list|)
throws|throws
name|TaskExecutionException
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Executing task from queue with job name: "
operator|+
name|task
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|ArchivaRepository
name|arepo
init|=
name|repositoryDAO
operator|.
name|getRepository
argument_list|(
name|task
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
decl_stmt|;
name|RepositoryScanner
name|scanner
init|=
operator|new
name|RepositoryScanner
argument_list|()
decl_stmt|;
name|RepositoryContentStatistics
name|stats
init|=
name|scanner
operator|.
name|scan
argument_list|(
name|arepo
argument_list|,
name|getActiveConsumerList
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|dao
operator|.
name|save
argument_list|(
name|stats
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Finished repository task: "
operator|+
name|stats
operator|.
name|getDuration
argument_list|()
operator|+
literal|" ms."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TaskExecutionException
argument_list|(
literal|"Database error when executing repository job."
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
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
block|}
specifier|private
name|List
name|getActiveConsumerList
parameter_list|()
block|{
name|List
name|activeConsumers
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|RepositoryScanningConfiguration
name|repoScanningConfig
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryScanning
argument_list|()
decl_stmt|;
name|List
name|configuredGoodConsumers
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|List
name|configuredBadConsumers
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|configuredGoodConsumers
operator|.
name|addAll
argument_list|(
name|CollectionUtils
operator|.
name|select
argument_list|(
name|repoScanningConfig
operator|.
name|getGoodConsumers
argument_list|()
argument_list|,
name|repositoryContentConsumerUtil
operator|.
name|getKnownSelectionPredicate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|configuredBadConsumers
operator|.
name|addAll
argument_list|(
name|CollectionUtils
operator|.
name|select
argument_list|(
name|repoScanningConfig
operator|.
name|getBadConsumers
argument_list|()
argument_list|,
name|repositoryContentConsumerUtil
operator|.
name|getInvalidSelectionPredicate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|activeConsumers
return|;
block|}
block|}
end_class

end_unit

