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
name|manager
operator|.
name|web
operator|.
name|execution
package|;
end_package

begin_comment
comment|/*  * Copyright 2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepositoryFactory
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
name|DefaultArtifactRepositoryFactory
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
name|layout
operator|.
name|ArtifactRepositoryLayout
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
name|metadata
operator|.
name|RepositoryMetadata
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
name|Artifact
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
name|RepositoryIndexingFactory
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
name|manager
operator|.
name|web
operator|.
name|job
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
name|model
operator|.
name|Model
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexReader
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_comment
comment|/**  * This is the class that executes the discoverer and indexer.  *  * @plexus.component role="org.apache.maven.repository.manager.web.execution.DiscovererExecution"  */
end_comment

begin_class
specifier|public
class|class
name|DiscovererExecution
extends|extends
name|AbstractLogEnabled
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|Configuration
name|config
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.repository.discovery.ArtifactDiscoverer" role-hint="org.apache.maven.repository.discovery.DefaultArtifactDiscoverer"      */
specifier|private
name|ArtifactDiscoverer
name|defaultArtifactDiscoverer
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.repository.discovery.ArtifactDiscoverer" role-hint="org.apache.maven.repository.discovery.LegacyArtifactDiscoverer"      */
specifier|private
name|ArtifactDiscoverer
name|legacyArtifactDiscoverer
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.repository.discovery.MetadataDiscoverer" role-hint="org.apache.maven.repository.discovery.DefaultMetadataDiscoverer"      */
specifier|private
name|MetadataDiscoverer
name|defaultMetadataDiscoverer
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryIndexingFactory
name|indexFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactRepositoryFactory
name|repoFactory
decl_stmt|;
specifier|private
name|ArtifactRepositoryLayout
name|layout
decl_stmt|;
specifier|private
name|Properties
name|props
decl_stmt|;
specifier|private
name|String
name|indexPath
decl_stmt|;
specifier|private
name|String
name|blacklistedPatterns
decl_stmt|;
specifier|private
name|boolean
name|includeSnapshots
decl_stmt|;
specifier|private
name|boolean
name|convertSnapshots
decl_stmt|;
specifier|private
name|ArtifactRepository
name|defaultRepository
decl_stmt|;
comment|/**      * Executes discoverer and indexer if an index does not exist yet      *      * @throws MalformedURLException      * @throws RepositoryIndexException      */
specifier|public
name|void
name|executeDiscovererIfIndexDoesNotExist
parameter_list|()
throws|throws
name|MalformedURLException
throws|,
name|RepositoryIndexException
block|{
name|props
operator|=
name|config
operator|.
name|getProperties
argument_list|()
expr_stmt|;
name|indexPath
operator|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"index.path"
argument_list|)
expr_stmt|;
name|File
name|indexDir
init|=
operator|new
name|File
argument_list|(
name|indexPath
argument_list|)
decl_stmt|;
name|boolean
name|isExisting
decl_stmt|;
if|if
condition|(
name|IndexReader
operator|.
name|indexExists
argument_list|(
name|indexDir
argument_list|)
condition|)
block|{
name|isExisting
operator|=
literal|true
expr_stmt|;
block|}
if|else if
condition|(
operator|!
name|indexDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|isExisting
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|isExisting
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isExisting
condition|)
block|{
name|executeDiscoverer
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Method that executes the discoverer and indexer      */
specifier|public
name|void
name|executeDiscoverer
parameter_list|()
throws|throws
name|MalformedURLException
throws|,
name|RepositoryIndexException
block|{
name|props
operator|=
name|config
operator|.
name|getProperties
argument_list|()
expr_stmt|;
name|indexPath
operator|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"index.path"
argument_list|)
expr_stmt|;
name|layout
operator|=
name|config
operator|.
name|getLayout
argument_list|()
expr_stmt|;
name|blacklistedPatterns
operator|=
name|props
operator|.
name|getProperty
argument_list|(
literal|"blacklist.patterns"
argument_list|)
expr_stmt|;
name|includeSnapshots
operator|=
operator|new
name|Boolean
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
literal|"include.snapshots"
argument_list|)
argument_list|)
operator|.
name|booleanValue
argument_list|()
expr_stmt|;
name|convertSnapshots
operator|=
operator|new
name|Boolean
argument_list|(
name|props
operator|.
name|getProperty
argument_list|(
literal|"convert.snapshots"
argument_list|)
argument_list|)
operator|.
name|booleanValue
argument_list|()
expr_stmt|;
try|try
block|{
name|defaultRepository
operator|=
name|getDefaultRepository
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|me
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
name|me
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"[DiscovererExecution] Started discovery and indexing.."
argument_list|)
expr_stmt|;
if|if
condition|(
name|props
operator|.
name|getProperty
argument_list|(
literal|"layout"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"default"
argument_list|)
condition|)
block|{
name|executeDiscovererInDefaultRepo
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|props
operator|.
name|getProperty
argument_list|(
literal|"layout"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"legacy"
argument_list|)
condition|)
block|{
name|executeDiscovererInLegacyRepo
argument_list|()
expr_stmt|;
block|}
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"[DiscovererExecution] Finished discovery and indexing."
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method that discovers and indexes artifacts, poms and metadata in a default      * m2 repository structure      *      * @throws MalformedURLException      * @throws RepositoryIndexException      */
specifier|protected
name|void
name|executeDiscovererInDefaultRepo
parameter_list|()
throws|throws
name|MalformedURLException
throws|,
name|RepositoryIndexException
block|{
name|List
name|artifacts
init|=
name|defaultArtifactDiscoverer
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
name|indexArtifact
argument_list|(
name|artifacts
argument_list|,
name|indexPath
argument_list|,
name|defaultRepository
argument_list|)
expr_stmt|;
name|List
name|models
init|=
name|defaultArtifactDiscoverer
operator|.
name|discoverStandalonePoms
argument_list|(
name|defaultRepository
argument_list|,
name|blacklistedPatterns
argument_list|,
name|convertSnapshots
argument_list|)
decl_stmt|;
name|indexPom
argument_list|(
name|models
argument_list|,
name|indexPath
argument_list|,
name|defaultRepository
argument_list|)
expr_stmt|;
name|List
name|metadataList
init|=
name|defaultMetadataDiscoverer
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
name|indexMetadata
argument_list|(
name|metadataList
argument_list|,
name|indexPath
argument_list|,
operator|new
name|File
argument_list|(
name|defaultRepository
operator|.
name|getBasedir
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method that discovers and indexes artifacts in a legacy type repository      *      * @throws RepositoryIndexException      */
specifier|protected
name|void
name|executeDiscovererInLegacyRepo
parameter_list|()
throws|throws
name|RepositoryIndexException
block|{
name|List
name|artifacts
init|=
name|legacyArtifactDiscoverer
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
comment|/**      * Index the artifacts in the list      *      * @param artifacts  the artifacts to be indexed      * @param indexPath  the path to the index file      * @param repository the repository where the artifacts are located      */
specifier|protected
name|void
name|indexArtifact
parameter_list|(
name|List
name|artifacts
parameter_list|,
name|String
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
for|for
control|(
name|Iterator
name|iter
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Artifact
name|artifact
init|=
operator|(
name|Artifact
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|artifactIndex
operator|.
name|indexArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|RepositoryIndexException
condition|)
block|{
throw|throw
operator|(
name|RepositoryIndexException
operator|)
name|e
throw|;
block|}
block|}
if|if
condition|(
name|artifactIndex
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|artifactIndex
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|artifactIndex
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Index the metadata in the list      *      * @param metadataList   the metadata to be indexed      * @param indexPath      the path to the index file      * @param repositoryBase the repository where the metadata are located      */
specifier|protected
name|void
name|indexMetadata
parameter_list|(
name|List
name|metadataList
parameter_list|,
name|String
name|indexPath
parameter_list|,
name|File
name|repositoryBase
parameter_list|)
throws|throws
name|RepositoryIndexException
throws|,
name|MalformedURLException
block|{
name|String
name|repoDir
init|=
name|repositoryBase
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ArtifactRepository
name|repository
init|=
name|repoFactory
operator|.
name|createArtifactRepository
argument_list|(
literal|"repository"
argument_list|,
name|repoDir
argument_list|,
name|layout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
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
for|for
control|(
name|Iterator
name|iter
init|=
name|metadataList
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|RepositoryMetadata
name|repoMetadata
init|=
operator|(
name|RepositoryMetadata
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|metadataIndex
operator|.
name|index
argument_list|(
name|repoMetadata
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|RepositoryIndexException
condition|)
block|{
throw|throw
operator|(
name|RepositoryIndexException
operator|)
name|e
throw|;
block|}
block|}
if|if
condition|(
name|metadataIndex
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|metadataIndex
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|metadataIndex
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Index the poms in the list      *      * @param models     list of poms that will be indexed      * @param indexPath  the path to the index      * @param repository the artifact repository where the poms were discovered      */
specifier|protected
name|void
name|indexPom
parameter_list|(
name|List
name|models
parameter_list|,
name|String
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
for|for
control|(
name|Iterator
name|iter
init|=
name|models
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Model
name|model
init|=
operator|(
name|Model
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|pomIndex
operator|.
name|indexPom
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|RepositoryIndexException
condition|)
block|{
throw|throw
operator|(
name|RepositoryIndexException
operator|)
name|e
throw|;
block|}
block|}
if|if
condition|(
name|pomIndex
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|pomIndex
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|pomIndex
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Method that creates the artifact repository      *      * @return an ArtifactRepository instance      * @throws java.net.MalformedURLException      */
specifier|protected
name|ArtifactRepository
name|getDefaultRepository
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|File
name|repositoryDirectory
init|=
operator|new
name|File
argument_list|(
name|config
operator|.
name|getRepositoryDirectory
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|repoDir
init|=
name|repositoryDirectory
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ArtifactRepositoryFactory
name|repoFactory
init|=
operator|new
name|DefaultArtifactRepositoryFactory
argument_list|()
decl_stmt|;
return|return
name|repoFactory
operator|.
name|createArtifactRepository
argument_list|(
literal|"test"
argument_list|,
name|repoDir
argument_list|,
name|config
operator|.
name|getLayout
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Method that sets the configuration object      *      * @param config      */
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Configuration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
comment|/**      * Returns the cofiguration      *      * @return a Configuration object that contains the configuration values      */
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|config
return|;
block|}
block|}
end_class

end_unit

