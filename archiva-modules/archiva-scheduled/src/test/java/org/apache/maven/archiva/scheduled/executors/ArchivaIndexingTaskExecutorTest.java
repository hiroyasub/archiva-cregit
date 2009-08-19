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
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipInputStream
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
name|lucene
operator|.
name|search
operator|.
name|BooleanQuery
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
name|search
operator|.
name|IndexSearcher
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
name|search
operator|.
name|TopDocs
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
name|search
operator|.
name|BooleanClause
operator|.
name|Occur
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
name|ArtifactIndexingTask
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
name|TaskCreator
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
name|PlexusInSpringTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|MockControl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|ArtifactInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|FlatSearchRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|FlatSearchResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|IndexerEngine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|NexusIndexer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|context
operator|.
name|IndexingContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|nexus
operator|.
name|index
operator|.
name|packer
operator|.
name|IndexPacker
import|;
end_import

begin_comment
comment|/**  * ArchivaIndexingTaskExecutorTest  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaIndexingTaskExecutorTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
name|ArchivaIndexingTaskExecutor
name|indexingExecutor
decl_stmt|;
specifier|private
name|IndexerEngine
name|indexerEngine
decl_stmt|;
specifier|private
name|IndexPacker
name|indexPacker
decl_stmt|;
specifier|private
name|MockControl
name|archivaConfigControl
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|ManagedRepositoryConfiguration
name|repositoryConfig
decl_stmt|;
specifier|private
name|Configuration
name|configuration
decl_stmt|;
specifier|private
name|NexusIndexer
name|indexer
decl_stmt|;
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
name|indexingExecutor
operator|=
operator|new
name|ArchivaIndexingTaskExecutor
argument_list|()
expr_stmt|;
name|indexingExecutor
operator|.
name|initialize
argument_list|()
expr_stmt|;
name|repositoryConfig
operator|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
expr_stmt|;
name|repositoryConfig
operator|.
name|setId
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setLocation
argument_list|(
name|getBasedir
argument_list|()
operator|+
literal|"/target/test-classes/test-repo"
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setName
argument_list|(
literal|"Test Repository"
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setScanned
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setSnapshots
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|configuration
operator|=
operator|new
name|Configuration
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|repositoryConfig
argument_list|)
expr_stmt|;
name|archivaConfigControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|archivaConfigControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|indexer
operator|=
operator|(
name|NexusIndexer
operator|)
name|lookup
argument_list|(
name|NexusIndexer
operator|.
name|class
argument_list|)
expr_stmt|;
name|indexerEngine
operator|=
operator|(
name|IndexerEngine
operator|)
name|lookup
argument_list|(
name|IndexerEngine
operator|.
name|class
argument_list|)
expr_stmt|;
name|indexPacker
operator|=
operator|(
name|IndexPacker
operator|)
name|lookup
argument_list|(
name|IndexPacker
operator|.
name|class
argument_list|)
expr_stmt|;
name|indexingExecutor
operator|.
name|setIndexerEngine
argument_list|(
name|indexerEngine
argument_list|)
expr_stmt|;
name|indexingExecutor
operator|.
name|setIndexPacker
argument_list|(
name|indexPacker
argument_list|)
expr_stmt|;
name|indexingExecutor
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// delete created index in the repository
name|File
name|indexDir
init|=
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".indexer"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexDir
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|indexDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|indexDir
operator|=
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".index"
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexDir
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|indexDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testAddArtifactToIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
name|ArtifactIndexingTask
name|task
init|=
name|TaskCreator
operator|.
name|createIndexingTask
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|ADD
argument_list|)
decl_stmt|;
name|archivaConfigControl
operator|.
name|expectAndReturn
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfigControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|archivaConfigControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|BooleanQuery
name|q
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|GROUP_ID
argument_list|,
literal|"org.apache.archiva"
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|ARTIFACT_ID
argument_list|,
literal|"archiva-index-methods-jar-test"
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|IndexingContext
name|context
init|=
name|indexer
operator|.
name|addIndexingContext
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".indexer"
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|NexusIndexer
operator|.
name|FULL_INDEX
argument_list|)
decl_stmt|;
name|context
operator|.
name|setSearchable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|FlatSearchRequest
name|request
init|=
operator|new
name|FlatSearchRequest
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|FlatSearchResponse
name|response
init|=
name|indexer
operator|.
name|searchFlat
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".indexer"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".index"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
operator|.
name|getTotalHits
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ArtifactInfo
argument_list|>
name|results
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
name|ArtifactInfo
name|artifactInfo
init|=
operator|(
name|ArtifactInfo
operator|)
name|results
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.archiva"
argument_list|,
name|artifactInfo
operator|.
name|groupId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-index-methods-jar-test"
argument_list|,
name|artifactInfo
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test-repo"
argument_list|,
name|artifactInfo
operator|.
name|repository
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateArtifactInIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
name|ArtifactIndexingTask
name|task
init|=
name|TaskCreator
operator|.
name|createIndexingTask
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|ADD
argument_list|)
decl_stmt|;
name|archivaConfigControl
operator|.
name|expectAndReturn
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|configuration
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|archivaConfigControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|archivaConfigControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|BooleanQuery
name|q
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|GROUP_ID
argument_list|,
literal|"org.apache.archiva"
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|ARTIFACT_ID
argument_list|,
literal|"archiva-index-methods-jar-test"
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|IndexSearcher
name|searcher
init|=
operator|new
name|IndexSearcher
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
operator|+
literal|"/.indexer"
argument_list|)
decl_stmt|;
name|TopDocs
name|topDocs
init|=
name|searcher
operator|.
name|search
argument_list|(
name|q
argument_list|,
literal|null
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".indexer"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".index"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// should only return 1 hit!
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|topDocs
operator|.
name|totalHits
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRemoveArtifactFromIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
name|ArtifactIndexingTask
name|task
init|=
name|TaskCreator
operator|.
name|createIndexingTask
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|ADD
argument_list|)
decl_stmt|;
name|archivaConfigControl
operator|.
name|expectAndReturn
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|configuration
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|archivaConfigControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// add artifact to index
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|BooleanQuery
name|q
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|GROUP_ID
argument_list|,
literal|"org.apache.archiva"
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|ARTIFACT_ID
argument_list|,
literal|"archiva-index-methods-jar-test"
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|IndexSearcher
name|searcher
init|=
operator|new
name|IndexSearcher
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
operator|+
literal|"/.indexer"
argument_list|)
decl_stmt|;
name|TopDocs
name|topDocs
init|=
name|searcher
operator|.
name|search
argument_list|(
name|q
argument_list|,
literal|null
argument_list|,
literal|10
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".indexer"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".index"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// should return 1 hit
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|topDocs
operator|.
name|totalHits
argument_list|)
expr_stmt|;
comment|// remove added artifact from index
name|task
operator|=
name|TaskCreator
operator|.
name|createIndexingTask
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|DELETE
argument_list|)
expr_stmt|;
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|archivaConfigControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|q
operator|=
operator|new
name|BooleanQuery
argument_list|()
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|GROUP_ID
argument_list|,
literal|"org.apache.archiva"
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|ARTIFACT_ID
argument_list|,
literal|"archiva-index-methods-jar-test"
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|searcher
operator|=
operator|new
name|IndexSearcher
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
operator|+
literal|"/.indexer"
argument_list|)
expr_stmt|;
name|topDocs
operator|=
name|searcher
operator|.
name|search
argument_list|(
name|q
argument_list|,
literal|null
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".indexer"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".index"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// artifact should have been removed from the index!
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|topDocs
operator|.
name|totalHits
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPackagedIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
name|ArtifactIndexingTask
name|task
init|=
name|TaskCreator
operator|.
name|createIndexingTask
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|ADD
argument_list|)
decl_stmt|;
name|archivaConfigControl
operator|.
name|expectAndReturn
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfigControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|archivaConfigControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".indexer"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".index"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// unpack .zip index
name|File
name|destDir
init|=
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".index/tmp"
argument_list|)
decl_stmt|;
name|unzipIndex
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
literal|".index"
argument_list|)
operator|.
name|getPath
argument_list|()
argument_list|,
name|destDir
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|BooleanQuery
name|q
init|=
operator|new
name|BooleanQuery
argument_list|()
decl_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|GROUP_ID
argument_list|,
literal|"org.apache.archiva"
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|constructQuery
argument_list|(
name|ArtifactInfo
operator|.
name|ARTIFACT_ID
argument_list|,
literal|"archiva-index-methods-jar-test"
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|IndexingContext
name|context
init|=
name|indexer
operator|.
name|addIndexingContext
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|File
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|,
name|destDir
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|NexusIndexer
operator|.
name|FULL_INDEX
argument_list|)
decl_stmt|;
name|context
operator|.
name|setSearchable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|FlatSearchRequest
name|request
init|=
operator|new
name|FlatSearchRequest
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|FlatSearchResponse
name|response
init|=
name|indexer
operator|.
name|searchFlat
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|response
operator|.
name|getTotalHits
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ArtifactInfo
argument_list|>
name|results
init|=
name|response
operator|.
name|getResults
argument_list|()
decl_stmt|;
name|ArtifactInfo
name|artifactInfo
init|=
operator|(
name|ArtifactInfo
operator|)
name|results
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.archiva"
argument_list|,
name|artifactInfo
operator|.
name|groupId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-index-methods-jar-test"
argument_list|,
name|artifactInfo
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test-repo"
argument_list|,
name|artifactInfo
operator|.
name|repository
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|unzipIndex
parameter_list|(
name|String
name|indexDir
parameter_list|,
name|String
name|destDir
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|IOException
block|{
specifier|final
name|int
name|buff
init|=
literal|2048
decl_stmt|;
operator|new
name|File
argument_list|(
name|destDir
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|BufferedOutputStream
name|out
init|=
literal|null
decl_stmt|;
name|FileInputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|indexDir
argument_list|,
literal|"nexus-maven-repository-index.zip"
argument_list|)
argument_list|)
decl_stmt|;
name|ZipInputStream
name|in
init|=
operator|new
name|ZipInputStream
argument_list|(
operator|new
name|BufferedInputStream
argument_list|(
name|fin
argument_list|)
argument_list|)
decl_stmt|;
name|ZipEntry
name|entry
decl_stmt|;
while|while
condition|(
operator|(
name|entry
operator|=
name|in
operator|.
name|getNextEntry
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|int
name|count
decl_stmt|;
name|byte
name|data
index|[]
init|=
operator|new
name|byte
index|[
name|buff
index|]
decl_stmt|;
name|FileOutputStream
name|fout
init|=
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|out
operator|=
operator|new
name|BufferedOutputStream
argument_list|(
name|fout
argument_list|,
name|buff
argument_list|)
expr_stmt|;
while|while
condition|(
operator|(
name|count
operator|=
name|in
operator|.
name|read
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|buff
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

