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
name|List
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
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
name|ObjectNotFoundException
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
name|constraints
operator|.
name|ArtifactsByRepositoryConstraint
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
name|constraints
operator|.
name|MostRecentRepositoryScanStatistics
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
name|constraints
operator|.
name|UniqueArtifactIdConstraint
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
name|constraints
operator|.
name|UniqueGroupIdConstraint
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
name|ArchivaArtifact
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
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
comment|/**  * ArchivaRepositoryScanningTaskExecutor   *  * @version $Id$  *   * @plexus.component  *   role="org.codehaus.plexus.taskqueue.execution.TaskExecutor"  *   role-hint="repository-scanning"  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaRepositoryScanningTaskExecutor
implements|implements
name|TaskExecutor
implements|,
name|Initializable
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
comment|/**      * TODO: just for stats, remove this and use the main stats module      *       * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * The repository scanner component.      *       * @plexus.requirement      */
specifier|private
name|RepositoryScanner
name|repoScanner
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryContentConsumers
name|consumers
decl_stmt|;
specifier|private
name|Task
name|task
decl_stmt|;
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
literal|"Initialized "
operator|+
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
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|repoTask
operator|.
name|getRepositoryId
argument_list|()
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
name|ManagedRepositoryConfiguration
name|arepo
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findManagedRepositoryById
argument_list|(
name|repoTask
operator|.
name|getRepositoryId
argument_list|()
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
literal|"Executing task from queue with job name: "
operator|+
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
literal|"Executing task from queue with job name: "
operator|+
name|repoTask
argument_list|)
expr_stmt|;
comment|// otherwise, execute consumers on whole repository
try|try
block|{
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
name|repoTask
operator|.
name|getRepositoryId
argument_list|()
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
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
name|results
init|=
operator|(
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
operator|new
name|MostRecentRepositoryScanStatistics
argument_list|(
name|arepo
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|results
argument_list|)
condition|)
block|{
name|RepositoryContentStatistics
name|lastStats
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|repoTask
operator|.
name|isScanAll
argument_list|()
condition|)
block|{
name|sinceWhen
operator|=
name|lastStats
operator|.
name|getWhenGathered
argument_list|()
operator|.
name|getTime
argument_list|()
operator|+
name|lastStats
operator|.
name|getDuration
argument_list|()
expr_stmt|;
block|}
block|}
name|RepositoryScanStatistics
name|stats
init|=
name|repoScanner
operator|.
name|scan
argument_list|(
name|arepo
argument_list|,
name|sinceWhen
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Finished repository task: "
operator|+
name|stats
operator|.
name|toDump
argument_list|(
name|arepo
argument_list|)
argument_list|)
expr_stmt|;
name|RepositoryContentStatistics
name|dbstats
init|=
name|constructRepositoryStatistics
argument_list|(
name|arepo
argument_list|,
name|sinceWhen
argument_list|,
name|results
argument_list|,
name|stats
argument_list|)
decl_stmt|;
name|dao
operator|.
name|getRepositoryContentStatisticsDAO
argument_list|()
operator|.
name|saveRepositoryContentStatistics
argument_list|(
name|dbstats
argument_list|)
expr_stmt|;
name|this
operator|.
name|task
operator|=
literal|null
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
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|RepositoryContentStatistics
name|constructRepositoryStatistics
parameter_list|(
name|ManagedRepositoryConfiguration
name|arepo
parameter_list|,
name|long
name|sinceWhen
parameter_list|,
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
name|results
parameter_list|,
name|RepositoryScanStatistics
name|stats
parameter_list|)
block|{
comment|// I hate jpox and modello<-- and so do I
name|RepositoryContentStatistics
name|dbstats
init|=
operator|new
name|RepositoryContentStatistics
argument_list|()
decl_stmt|;
name|dbstats
operator|.
name|setDuration
argument_list|(
name|stats
operator|.
name|getDuration
argument_list|()
argument_list|)
expr_stmt|;
name|dbstats
operator|.
name|setNewFileCount
argument_list|(
name|stats
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|dbstats
operator|.
name|setRepositoryId
argument_list|(
name|stats
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|dbstats
operator|.
name|setTotalFileCount
argument_list|(
name|stats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|dbstats
operator|.
name|setWhenGathered
argument_list|(
name|stats
operator|.
name|getWhenGathered
argument_list|()
argument_list|)
expr_stmt|;
comment|// total artifact count
try|try
block|{
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|queryArtifacts
argument_list|(
operator|new
name|ArtifactsByRepositoryConstraint
argument_list|(
name|arepo
operator|.
name|getId
argument_list|()
argument_list|,
name|stats
operator|.
name|getWhenGathered
argument_list|()
argument_list|,
literal|"groupId"
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|dbstats
operator|.
name|setTotalArtifactCount
argument_list|(
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|oe
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Object not found in the database : "
operator|+
name|oe
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|ae
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error occurred while querying artifacts for artifact count : "
operator|+
name|ae
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// total repo size
name|long
name|size
init|=
name|FileUtils
operator|.
name|sizeOfDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|arepo
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|dbstats
operator|.
name|setTotalSize
argument_list|(
name|size
argument_list|)
expr_stmt|;
comment|// total unique groups
name|List
argument_list|<
name|String
argument_list|>
name|repos
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|repos
operator|.
name|add
argument_list|(
name|arepo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|groupIds
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueGroupIdConstraint
argument_list|(
name|repos
argument_list|)
argument_list|)
decl_stmt|;
name|dbstats
operator|.
name|setTotalGroupCount
argument_list|(
name|groupIds
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|artifactIds
init|=
operator|(
name|List
argument_list|<
name|Object
index|[]
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueArtifactIdConstraint
argument_list|(
name|arepo
operator|.
name|getId
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
decl_stmt|;
name|dbstats
operator|.
name|setTotalProjectCount
argument_list|(
name|artifactIds
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|dbstats
return|;
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
block|}
end_class

end_unit

