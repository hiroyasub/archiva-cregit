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
operator|.
name|task
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
name|ConfiguredRepositoryFactory
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
name|RepositoryConfiguration
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
name|discoverer
operator|.
name|ArtifactDiscoverer
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
name|discoverer
operator|.
name|DiscovererException
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
name|discoverer
operator|.
name|filter
operator|.
name|SnapshotArtifactFilter
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
name|indexer
operator|.
name|record
operator|.
name|IndexRecordExistsArtifactFilter
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
name|record
operator|.
name|RepositoryIndexRecordFactory
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
name|TaskExecutionException
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|artifact
operator|.
name|resolver
operator|.
name|filter
operator|.
name|AndArtifactFilter
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
name|Collection
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
name|Iterator
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
comment|/**  * Task for discovering changes in the repository and updating the index accordingly.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @plexus.component role="org.apache.maven.archiva.scheduler.task.RepositoryTask" role-hint="indexer"  */
end_comment

begin_class
specifier|public
class|class
name|IndexerTask
extends|extends
name|AbstractLogEnabled
implements|implements
name|RepositoryTask
block|{
comment|/**      * Configuration store.      *      * @plexus.requirement      */
specifier|private
name|ConfigurationStore
name|configurationStore
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryArtifactIndexFactory
name|indexFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfiguredRepositoryFactory
name|repoFactory
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.discoverer.ArtifactDiscoverer"      */
specifier|private
name|Map
name|artifactDiscoverers
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="standard"      */
specifier|private
name|RepositoryIndexRecordFactory
name|recordFactory
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
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
name|execute
argument_list|(
name|configuration
argument_list|,
name|indexPath
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|execute
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|File
name|indexPath
parameter_list|)
throws|throws
name|TaskExecutionException
block|{
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Starting repository indexing process"
argument_list|)
expr_stmt|;
name|RepositoryArtifactIndex
name|index
init|=
name|indexFactory
operator|.
name|createStandardIndex
argument_list|(
name|indexPath
argument_list|)
decl_stmt|;
try|try
block|{
name|Collection
name|keys
decl_stmt|;
if|if
condition|(
name|index
operator|.
name|exists
argument_list|()
condition|)
block|{
name|keys
operator|=
name|index
operator|.
name|getAllRecordKeys
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|keys
operator|=
name|Collections
operator|.
name|EMPTY_LIST
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|i
init|=
name|configuration
operator|.
name|getRepositories
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|RepositoryConfiguration
name|repositoryConfiguration
init|=
operator|(
name|RepositoryConfiguration
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|repositoryConfiguration
operator|.
name|isIndexed
argument_list|()
condition|)
block|{
name|List
name|blacklistedPatterns
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
if|if
condition|(
name|repositoryConfiguration
operator|.
name|getBlackListPatterns
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|blacklistedPatterns
operator|.
name|addAll
argument_list|(
name|repositoryConfiguration
operator|.
name|getBlackListPatterns
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getGlobalBlackListPatterns
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|blacklistedPatterns
operator|.
name|addAll
argument_list|(
name|configuration
operator|.
name|getGlobalBlackListPatterns
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|boolean
name|includeSnapshots
init|=
name|repositoryConfiguration
operator|.
name|isIncludeSnapshots
argument_list|()
decl_stmt|;
name|ArtifactRepository
name|repository
init|=
name|repoFactory
operator|.
name|createRepository
argument_list|(
name|repositoryConfiguration
argument_list|)
decl_stmt|;
name|String
name|layoutProperty
init|=
name|repositoryConfiguration
operator|.
name|getLayout
argument_list|()
decl_stmt|;
name|ArtifactDiscoverer
name|discoverer
init|=
operator|(
name|ArtifactDiscoverer
operator|)
name|artifactDiscoverers
operator|.
name|get
argument_list|(
name|layoutProperty
argument_list|)
decl_stmt|;
name|AndArtifactFilter
name|filter
init|=
operator|new
name|AndArtifactFilter
argument_list|()
decl_stmt|;
name|filter
operator|.
name|add
argument_list|(
operator|new
name|IndexRecordExistsArtifactFilter
argument_list|(
name|keys
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|includeSnapshots
condition|)
block|{
name|filter
operator|.
name|add
argument_list|(
operator|new
name|SnapshotArtifactFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Save some memory by not tracking paths we won't use
comment|// TODO: Plexus CDC should be able to inject this configuration
name|discoverer
operator|.
name|setTrackOmittedPaths
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Searching repository "
operator|+
name|repositoryConfiguration
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|repository
argument_list|,
name|blacklistedPatterns
argument_list|,
name|filter
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifacts
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// TODO! reporting
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Indexing "
operator|+
name|artifacts
operator|.
name|size
argument_list|()
operator|+
literal|" new artifacts"
argument_list|)
expr_stmt|;
name|index
operator|.
name|indexArtifacts
argument_list|(
name|artifacts
argument_list|,
name|recordFactory
argument_list|)
expr_stmt|;
block|}
block|}
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
name|DiscovererException
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
literal|"Finished repository indexing process in "
operator|+
name|time
operator|+
literal|"ms"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|executeNowIfNeeded
parameter_list|()
throws|throws
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
name|execute
argument_list|(
name|configuration
argument_list|,
name|indexPath
argument_list|)
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
block|}
block|}
end_class

end_unit

