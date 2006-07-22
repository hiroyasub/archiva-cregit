begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
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
name|repository
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
name|repository
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
name|repository
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
name|repository
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
name|repository
operator|.
name|discovery
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
name|repository
operator|.
name|discovery
operator|.
name|MetadataDiscoverer
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
name|repository
operator|.
name|indexing
operator|.
name|ArtifactRepositoryIndex
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
name|repository
operator|.
name|indexing
operator|.
name|MetadataRepositoryIndex
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
name|repository
operator|.
name|indexing
operator|.
name|PomRepositoryIndex
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
name|repository
operator|.
name|indexing
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
name|repository
operator|.
name|indexing
operator|.
name|RepositoryIndexingFactory
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
comment|/**  * Task for discovering changes in the repository.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @plexus.component role="org.apache.maven.repository.scheduler.RepositoryTask" role-hint="indexer"  */
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
name|RepositoryIndexingFactory
name|indexFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfiguredRepositoryFactory
name|repoFactory
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.repository.discovery.ArtifactDiscoverer"      */
specifier|private
name|Map
name|artifactDiscoverers
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.repository.discovery.MetadataDiscoverer"      */
specifier|private
name|Map
name|metadataDiscoverers
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
literal|"Starting repository discovery process"
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|blacklistedPatterns
init|=
name|configuration
operator|.
name|getDiscoveryBlackListPatterns
argument_list|()
decl_stmt|;
name|boolean
name|includeSnapshots
init|=
name|configuration
operator|.
name|isDiscoverSnapshots
argument_list|()
decl_stmt|;
name|ArtifactRepository
name|defaultRepository
init|=
name|repoFactory
operator|.
name|createRepository
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|String
name|layoutProperty
init|=
name|configuration
operator|.
name|getRepositoryLayout
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
name|List
name|artifacts
init|=
name|discoverer
operator|.
name|discoverArtifacts
argument_list|(
name|defaultRepository
argument_list|,
name|blacklistedPatterns
argument_list|,
name|includeSnapshots
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
name|indexArtifact
argument_list|(
name|artifacts
argument_list|,
name|indexPath
argument_list|,
name|defaultRepository
argument_list|)
expr_stmt|;
block|}
name|List
name|models
init|=
name|discoverer
operator|.
name|discoverStandalonePoms
argument_list|(
name|defaultRepository
argument_list|,
name|blacklistedPatterns
argument_list|,
name|includeSnapshots
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|models
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Indexing "
operator|+
name|models
operator|.
name|size
argument_list|()
operator|+
literal|" new POMs"
argument_list|)
expr_stmt|;
name|indexPom
argument_list|(
name|models
argument_list|,
name|indexPath
argument_list|,
name|defaultRepository
argument_list|)
expr_stmt|;
block|}
name|MetadataDiscoverer
name|metadataDiscoverer
init|=
operator|(
name|MetadataDiscoverer
operator|)
name|metadataDiscoverers
operator|.
name|get
argument_list|(
name|layoutProperty
argument_list|)
decl_stmt|;
name|List
name|metadataList
init|=
name|metadataDiscoverer
operator|.
name|discoverMetadata
argument_list|(
operator|new
name|File
argument_list|(
name|defaultRepository
operator|.
name|getBasedir
argument_list|()
argument_list|)
argument_list|,
name|blacklistedPatterns
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|metadataList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"Indexing "
operator|+
name|metadataList
operator|.
name|size
argument_list|()
operator|+
literal|" new metadata files"
argument_list|)
expr_stmt|;
name|indexMetadata
argument_list|(
name|metadataList
argument_list|,
name|indexPath
argument_list|,
name|defaultRepository
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
name|ArtifactRepository
name|repository
init|=
name|repoFactory
operator|.
name|createRepository
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|ArtifactRepositoryIndex
name|artifactIndex
init|=
name|indexFactory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifactIndex
operator|.
name|indexExists
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
comment|/**      * Index the artifacts in the list      *      * @param artifacts  the artifacts to be indexed      * @param indexPath  the path to the index file      * @param repository the repository where the artifacts are located      */
specifier|protected
name|void
name|indexArtifact
parameter_list|(
name|List
name|artifacts
parameter_list|,
name|File
name|indexPath
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|ArtifactRepositoryIndex
name|artifactIndex
init|=
name|indexFactory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|artifactIndex
operator|.
name|indexArtifacts
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|artifactIndex
operator|.
name|optimize
argument_list|()
expr_stmt|;
block|}
comment|/**      * Index the metadata in the list      *      * @param metadataList the metadata to be indexed      * @param indexPath    the path to the index file      */
specifier|protected
name|void
name|indexMetadata
parameter_list|(
name|List
name|metadataList
parameter_list|,
name|File
name|indexPath
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|MetadataRepositoryIndex
name|metadataIndex
init|=
name|indexFactory
operator|.
name|createMetadataRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|metadataIndex
operator|.
name|indexMetadata
argument_list|(
name|metadataList
argument_list|)
expr_stmt|;
name|metadataIndex
operator|.
name|optimize
argument_list|()
expr_stmt|;
block|}
comment|/**      * Index the poms in the list      *      * @param models     list of poms that will be indexed      * @param indexPath  the path to the index      * @param repository the artifact repository where the poms were discovered      */
specifier|protected
name|void
name|indexPom
parameter_list|(
name|List
name|models
parameter_list|,
name|File
name|indexPath
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryIndexException
block|{
name|PomRepositoryIndex
name|pomIndex
init|=
name|indexFactory
operator|.
name|createPomRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|pomIndex
operator|.
name|indexPoms
argument_list|(
name|models
argument_list|)
expr_stmt|;
name|pomIndex
operator|.
name|optimize
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

