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
name|indexing
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
name|artifact
operator|.
name|factory
operator|.
name|ArtifactFactory
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
name|ArtifactRepositoryMetadata
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
name|GroupRepositoryMetadata
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
name|repository
operator|.
name|metadata
operator|.
name|SnapshotArtifactRepositoryMetadata
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
name|io
operator|.
name|xpp3
operator|.
name|MetadataXpp3Reader
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
name|maven
operator|.
name|model
operator|.
name|io
operator|.
name|xpp3
operator|.
name|MavenXpp3Reader
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
name|digest
operator|.
name|DefaultDigester
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
name|digest
operator|.
name|Digester
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
name|PlexusTestCase
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
name|util
operator|.
name|FileUtils
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
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
comment|/**  *<p/>  * This class tests the RepositoryIndexSearchLayer.  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryIndexSearchLayerTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|private
name|Digester
name|digester
decl_stmt|;
specifier|private
name|String
name|indexPath
decl_stmt|;
comment|/**      * Setup method      *      * @throws Exception      */
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|File
name|repositoryDirectory
init|=
name|getTestFile
argument_list|(
literal|"src/test/repository"
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
name|ArtifactRepositoryLayout
name|layout
init|=
operator|(
name|ArtifactRepositoryLayout
operator|)
name|lookup
argument_list|(
name|ArtifactRepositoryLayout
operator|.
name|ROLE
argument_list|,
literal|"default"
argument_list|)
decl_stmt|;
name|ArtifactRepositoryFactory
name|repoFactory
init|=
operator|(
name|ArtifactRepositoryFactory
operator|)
name|lookup
argument_list|(
name|ArtifactRepositoryFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|repository
operator|=
name|repoFactory
operator|.
name|createArtifactRepository
argument_list|(
literal|"test"
argument_list|,
name|repoDir
argument_list|,
name|layout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|digester
operator|=
operator|new
name|DefaultDigester
argument_list|()
expr_stmt|;
name|indexPath
operator|=
literal|"target/index"
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexPath
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tear down method      *      * @throws Exception      */
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
comment|/**      * Method for creating the index used for testing      *      * @throws Exception      */
specifier|private
name|void
name|createTestIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryIndexingFactory
name|factory
init|=
operator|(
name|RepositoryIndexingFactory
operator|)
name|lookup
argument_list|(
name|RepositoryIndexingFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|ArtifactRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-artifact"
argument_list|,
literal|"2.0.1"
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|artifact
operator|=
name|getArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-model"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|artifact
operator|=
name|getArtifact
argument_list|(
literal|"test"
argument_list|,
literal|"test-artifactId"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|artifact
operator|=
name|getArtifact
argument_list|(
literal|"test"
argument_list|,
literal|"test-artifactId"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setFile
argument_list|(
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|indexArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|indexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|MetadataRepositoryIndex
name|metaIndexer
init|=
name|factory
operator|.
name|createMetadataRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|RepositoryMetadata
name|repoMetadata
init|=
name|getMetadata
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"maven-metadata.xml"
argument_list|,
name|metaIndexer
operator|.
name|GROUP_METADATA
argument_list|)
decl_stmt|;
name|metaIndexer
operator|.
name|index
argument_list|(
name|repoMetadata
argument_list|)
expr_stmt|;
name|metaIndexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|metaIndexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
name|getMetadata
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-artifact"
argument_list|,
literal|"2.0.1"
argument_list|,
literal|"maven-metadata.xml"
argument_list|,
name|metaIndexer
operator|.
name|ARTIFACT_METADATA
argument_list|)
expr_stmt|;
name|metaIndexer
operator|.
name|index
argument_list|(
name|repoMetadata
argument_list|)
expr_stmt|;
name|metaIndexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|metaIndexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
name|getMetadata
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-artifact"
argument_list|,
literal|"2.0.1"
argument_list|,
literal|"maven-metadata.xml"
argument_list|,
name|metaIndexer
operator|.
name|SNAPSHOT_METADATA
argument_list|)
expr_stmt|;
name|metaIndexer
operator|.
name|index
argument_list|(
name|repoMetadata
argument_list|)
expr_stmt|;
name|metaIndexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|metaIndexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
name|getMetadata
argument_list|(
literal|"test"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"maven-metadata.xml"
argument_list|,
name|metaIndexer
operator|.
name|GROUP_METADATA
argument_list|)
expr_stmt|;
name|metaIndexer
operator|.
name|index
argument_list|(
name|repoMetadata
argument_list|)
expr_stmt|;
name|metaIndexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|metaIndexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|PomRepositoryIndex
name|pomIndexer
init|=
name|factory
operator|.
name|createPomRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|Model
name|pom
init|=
name|getPom
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-artifact"
argument_list|,
literal|"2.0.1"
argument_list|)
decl_stmt|;
name|pomIndexer
operator|.
name|indexPom
argument_list|(
name|pom
argument_list|)
expr_stmt|;
name|pomIndexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|pomIndexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|pom
operator|=
name|getPom
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-model"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
name|pomIndexer
operator|.
name|indexPom
argument_list|(
name|pom
argument_list|)
expr_stmt|;
name|pomIndexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|pomIndexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|pom
operator|=
name|getPom
argument_list|(
literal|"test"
argument_list|,
literal|"test-artifactId"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|pomIndexer
operator|.
name|indexPom
argument_list|(
name|pom
argument_list|)
expr_stmt|;
name|pomIndexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|pomIndexer
operator|.
name|close
argument_list|()
expr_stmt|;
name|pom
operator|=
name|getPom
argument_list|(
literal|"test"
argument_list|,
literal|"test-artifactId"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|pomIndexer
operator|.
name|indexPom
argument_list|(
name|pom
argument_list|)
expr_stmt|;
name|pomIndexer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|pomIndexer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Method for testing the "query everything" searcher      *      * @throws Exception      */
specifier|public
name|void
name|testGeneralSearcher
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestIndex
argument_list|()
expr_stmt|;
name|RepositoryIndexingFactory
name|factory
init|=
operator|(
name|RepositoryIndexingFactory
operator|)
name|lookup
argument_list|(
name|RepositoryIndexingFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|ArtifactRepositoryIndex
name|indexer
init|=
name|factory
operator|.
name|createArtifactRepositoryIndex
argument_list|(
name|indexPath
argument_list|,
name|repository
argument_list|)
decl_stmt|;
name|RepositoryIndexSearchLayer
name|searchLayer
init|=
name|factory
operator|.
name|createRepositoryIndexSearchLayer
argument_list|(
name|indexer
argument_list|)
decl_stmt|;
name|List
name|returnList
init|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
literal|"org.apache.maven"
argument_list|)
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|returnList
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
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
name|map
init|=
name|result
operator|.
name|getFieldMatches
argument_list|()
decl_stmt|;
name|Set
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|entries
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_PACKAGES
argument_list|)
condition|)
block|{
name|List
name|packages
init|=
operator|(
name|List
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|packages
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|assertTrue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
operator|)
operator|.
name|indexOf
argument_list|(
literal|"org.apache.maven"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|//POM license urls
name|returnList
operator|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
literal|"http://www.apache.org/licenses/LICENSE-2.0.txt"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|returnList
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
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
name|map
init|=
name|result
operator|.
name|getFieldMatches
argument_list|()
decl_stmt|;
name|Set
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|entries
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_LICENSE_URLS
argument_list|)
condition|)
block|{
name|List
name|packages
init|=
operator|(
name|List
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|packages
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|assertEquals
argument_list|(
literal|"http://www.apache.org/licenses/LICENSE-2.0.txt"
argument_list|,
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|//POM dependency
name|returnList
operator|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
literal|"org.codehaus.plexus:plexus-utils:1.0.5"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|returnList
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
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
name|map
init|=
name|result
operator|.
name|getFieldMatches
argument_list|()
decl_stmt|;
name|Set
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|entries
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_DEPENDENCIES
argument_list|)
condition|)
block|{
name|List
name|packages
init|=
operator|(
name|List
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|packages
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|assertEquals
argument_list|(
literal|"org.codehaus.plexus:plexus-utils:1.0.5"
argument_list|,
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// POM reporting plugin
name|returnList
operator|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
literal|"org.apache.maven.plugins:maven-checkstyle-plugin:2.0"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|returnList
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
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
name|map
init|=
name|result
operator|.
name|getFieldMatches
argument_list|()
decl_stmt|;
name|Set
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|entries
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_PLUGINS_REPORT
argument_list|)
condition|)
block|{
name|List
name|packages
init|=
operator|(
name|List
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|packages
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|assertEquals
argument_list|(
literal|"org.apache.maven.plugins:maven-checkstyle-plugin:2.0"
argument_list|,
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// POM build plugin
name|returnList
operator|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
literal|"org.codehaus.modello:modello-maven-plugin:2.0"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|returnList
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
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
name|map
init|=
name|result
operator|.
name|getFieldMatches
argument_list|()
decl_stmt|;
name|Set
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|entries
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_PLUGINS_BUILD
argument_list|)
condition|)
block|{
name|List
name|packages
init|=
operator|(
name|List
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|packages
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|assertEquals
argument_list|(
literal|"org.codehaus.modello:modello-maven-plugin:2.0"
argument_list|,
operator|(
name|String
operator|)
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|//maven-artifact-2.0.1.jar MD5 checksum
name|returnList
operator|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
literal|"F5A934ABBBC70A33136D89A996B9D5C09F652766"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|returnList
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
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
name|map
init|=
name|result
operator|.
name|getFieldMatches
argument_list|()
decl_stmt|;
name|Set
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|entries
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_MD5
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
operator|)
operator|.
name|indexOf
argument_list|(
literal|"F5A934ABBBC70A33136D89A996B9D5C09F652766"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|//maven-artifact-2.0.1.jar SHA1 checksum
name|returnList
operator|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
literal|"AE55D9B5720E11B6CF19FE1E31A42E51"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|returnList
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
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
name|map
init|=
name|result
operator|.
name|getFieldMatches
argument_list|()
decl_stmt|;
name|Set
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|entries
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_SHA1
argument_list|)
condition|)
block|{
name|assertTrue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
operator|)
operator|.
name|indexOf
argument_list|(
literal|"AE55D9B5720E11B6CF19FE1E31A42E516"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|//packaging jar
name|returnList
operator|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|returnList
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
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Map
name|map
init|=
name|result
operator|.
name|getFieldMatches
argument_list|()
decl_stmt|;
name|Set
name|entries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|entries
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|RepositoryIndex
operator|.
name|FLD_PACKAGING
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"jar"
argument_list|,
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|returnList
operator|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|returnList
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
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|result
operator|.
name|getArtifact
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
name|returnList
operator|=
name|searchLayer
operator|.
name|searchGeneral
argument_list|(
literal|"test-artifactId"
argument_list|)
expr_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|returnList
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
name|SearchResult
name|result
init|=
operator|(
name|SearchResult
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|result
operator|.
name|getArtifact
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|,
literal|"test-artifactId"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Method for creating RepositoryMetadata object      *      * @param groupId      the groupId to be set      * @param artifactId   the artifactId to be set      * @param version      the version to be set      * @param filename     the name of the metadata file      * @param metadataType the type of RepositoryMetadata object to be created (GROUP, ARTIFACT or SNAPSHOT)      * @return RepositoryMetadata      * @throws Exception      */
specifier|private
name|RepositoryMetadata
name|getMetadata
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|filename
parameter_list|,
name|String
name|metadataType
parameter_list|)
throws|throws
name|Exception
block|{
name|RepositoryMetadata
name|repoMetadata
init|=
literal|null
decl_stmt|;
name|URL
name|url
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|MetadataXpp3Reader
name|metadataReader
init|=
operator|new
name|MetadataXpp3Reader
argument_list|()
decl_stmt|;
comment|//group metadata
if|if
condition|(
name|metadataType
operator|.
name|equals
argument_list|(
name|MetadataRepositoryIndex
operator|.
name|GROUP_METADATA
argument_list|)
condition|)
block|{
name|url
operator|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
operator|+
name|groupId
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|filename
argument_list|)
operator|.
name|toURL
argument_list|()
expr_stmt|;
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
operator|new
name|GroupRepositoryMetadata
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|metadataReader
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//artifact metadata
if|else if
condition|(
name|metadataType
operator|.
name|equals
argument_list|(
name|MetadataRepositoryIndex
operator|.
name|ARTIFACT_METADATA
argument_list|)
condition|)
block|{
name|url
operator|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
operator|+
name|groupId
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"/"
operator|+
name|filename
argument_list|)
operator|.
name|toURL
argument_list|()
expr_stmt|;
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
argument_list|)
expr_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|metadataReader
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|//snapshot/version metadata
if|else if
condition|(
name|metadataType
operator|.
name|equals
argument_list|(
name|MetadataRepositoryIndex
operator|.
name|SNAPSHOT_METADATA
argument_list|)
condition|)
block|{
name|url
operator|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
operator|+
name|groupId
operator|.
name|replace
argument_list|(
literal|'.'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|artifactId
operator|+
literal|"/"
operator|+
name|version
operator|+
literal|"/"
operator|+
name|filename
argument_list|)
operator|.
name|toURL
argument_list|()
expr_stmt|;
name|is
operator|=
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|repoMetadata
operator|=
operator|new
name|SnapshotArtifactRepositoryMetadata
argument_list|(
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
argument_list|)
expr_stmt|;
name|repoMetadata
operator|.
name|setMetadata
argument_list|(
name|metadataReader
operator|.
name|read
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|repoMetadata
return|;
block|}
comment|/**      * Method for creating Artifact object      *      * @param groupId    the groupId of the artifact to be created      * @param artifactId the artifactId of the artifact to be created      * @param version    the version of the artifact to be created      * @return Artifact object      * @throws Exception      */
specifier|private
name|Artifact
name|getArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|artifactFactory
operator|==
literal|null
condition|)
block|{
name|artifactFactory
operator|=
operator|(
name|ArtifactFactory
operator|)
name|lookup
argument_list|(
name|ArtifactFactory
operator|.
name|ROLE
argument_list|)
expr_stmt|;
block|}
return|return
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|"jar"
argument_list|)
return|;
block|}
comment|/**      * Method for creating a Model object given the groupId, artifactId and version      *      * @param groupId    the groupId of the model to be created      * @param artifactId the artifactId of the model to be created      * @param version    the version of the model to be created      * @return Model object      * @throws Exception      */
specifier|private
name|Model
name|getPom
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
return|return
name|getPom
argument_list|(
name|artifact
argument_list|)
return|;
block|}
comment|/**      * Method for creating a Model object given an artifact      *      * @param artifact the artifact to be created a Model object for      * @return Model object      * @throws Exception      */
specifier|private
name|Model
name|getPom
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|pomFile
init|=
name|getPomFile
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|MavenXpp3Reader
name|pomReader
init|=
operator|new
name|MavenXpp3Reader
argument_list|()
decl_stmt|;
return|return
name|pomReader
operator|.
name|read
argument_list|(
operator|new
name|FileReader
argument_list|(
name|pomFile
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Method for creating a pom file      *      * @param artifact      * @return File      */
specifier|private
name|File
name|getPomFile
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|String
name|path
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
operator|+
literal|".pom"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

