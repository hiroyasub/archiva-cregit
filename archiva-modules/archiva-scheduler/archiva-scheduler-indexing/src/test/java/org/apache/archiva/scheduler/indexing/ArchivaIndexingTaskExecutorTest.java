begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|indexing
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|admin
operator|.
name|model
operator|.
name|managed
operator|.
name|ManagedRepositoryAdmin
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
name|common
operator|.
name|plexusbridge
operator|.
name|MavenIndexerUtils
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridge
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
name|maven
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
name|apache
operator|.
name|maven
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
name|apache
operator|.
name|maven
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
name|apache
operator|.
name|maven
operator|.
name|index
operator|.
name|MAVEN
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
name|index
operator|.
name|NexusIndexer
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
name|apache
operator|.
name|maven
operator|.
name|index
operator|.
name|expr
operator|.
name|SourcedSearchExpression
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
name|index
operator|.
name|expr
operator|.
name|StringSearchExpression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

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
name|archiva
operator|.
name|test
operator|.
name|utils
operator|.
name|ArchivaSpringJUnit4ClassRunner
import|;
end_import

begin_comment
comment|/**  * ArchivaIndexingTaskExecutorTest  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath*:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|ArchivaIndexingTaskExecutorTest
extends|extends
name|TestCase
block|{
annotation|@
name|Inject
specifier|private
name|ArchivaIndexingTaskExecutor
name|indexingExecutor
decl_stmt|;
specifier|private
name|ManagedRepository
name|repositoryConfig
decl_stmt|;
specifier|private
name|NexusIndexer
name|indexer
decl_stmt|;
annotation|@
name|Inject
name|PlexusSisuBridge
name|plexusSisuBridge
decl_stmt|;
annotation|@
name|Inject
name|MavenIndexerUtils
name|mavenIndexerUtils
decl_stmt|;
annotation|@
name|Inject
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
decl_stmt|;
annotation|@
name|Before
specifier|public
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
name|repositoryConfig
operator|=
operator|new
name|ManagedRepository
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
operator|new
name|File
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
argument_list|,
literal|"target/test-classes/test-repo"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
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
name|indexer
operator|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|NexusIndexer
operator|.
name|class
argument_list|)
expr_stmt|;
name|managedRepositoryAdmin
operator|.
name|createIndexContext
argument_list|(
name|repositoryConfig
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|IndexingContext
name|indexingContext
range|:
name|indexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|indexer
operator|.
name|removeIndexingContext
argument_list|(
name|indexingContext
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
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
specifier|protected
name|IndexingContext
name|getIndexingContext
parameter_list|()
block|{
return|return
name|indexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|get
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Test
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
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repositoryConfig
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|,
name|getIndexingContext
argument_list|()
argument_list|)
decl_stmt|;
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
name|MAVEN
operator|.
name|GROUP_ID
argument_list|,
operator|new
name|StringSearchExpression
argument_list|(
literal|"org.apache.archiva"
argument_list|)
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
name|MAVEN
operator|.
name|ARTIFACT_ID
argument_list|,
operator|new
name|StringSearchExpression
argument_list|(
literal|"archiva-index-methods-jar-test"
argument_list|)
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|indexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|containsKey
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
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
name|mavenIndexerUtils
operator|.
name|getAllIndexCreators
argument_list|()
argument_list|)
decl_stmt|;
name|context
operator|.
name|setSearchable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
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
name|assertFalse
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
annotation|@
name|Test
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
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repositoryConfig
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|,
name|getIndexingContext
argument_list|()
argument_list|)
decl_stmt|;
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
name|MAVEN
operator|.
name|GROUP_ID
argument_list|,
operator|new
name|StringSearchExpression
argument_list|(
literal|"org.apache.archiva"
argument_list|)
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
name|MAVEN
operator|.
name|ARTIFACT_ID
argument_list|,
operator|new
name|StringSearchExpression
argument_list|(
literal|"archiva-index-methods-jar-test"
argument_list|)
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
name|indexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|get
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|getIndexSearcher
argument_list|()
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
name|searcher
operator|.
name|close
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
name|assertFalse
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
annotation|@
name|Test
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
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repositoryConfig
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|,
name|getIndexingContext
argument_list|()
argument_list|)
decl_stmt|;
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
name|MAVEN
operator|.
name|GROUP_ID
argument_list|,
operator|new
name|SourcedSearchExpression
argument_list|(
literal|"org.apache.archiva"
argument_list|)
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
comment|//q.add(
comment|//    indexer.constructQuery( MAVEN.ARTIFACT_ID, new SourcedSearchExpression( "archiva-index-methods-jar-test" ) ),
comment|//    Occur.SHOULD );
name|FlatSearchRequest
name|flatSearchRequest
init|=
operator|new
name|FlatSearchRequest
argument_list|(
name|q
argument_list|,
name|indexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|get
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|FlatSearchResponse
name|response
init|=
name|indexer
operator|.
name|searchFlat
argument_list|(
name|flatSearchRequest
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
name|assertFalse
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
name|response
operator|.
name|getTotalHitsCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// remove added artifact from index
name|task
operator|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repositoryConfig
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|DELETE
argument_list|,
name|getIndexingContext
argument_list|()
argument_list|)
expr_stmt|;
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|task
operator|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repositoryConfig
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|FINISH
argument_list|,
name|getIndexingContext
argument_list|()
argument_list|)
expr_stmt|;
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
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
name|MAVEN
operator|.
name|GROUP_ID
argument_list|,
operator|new
name|SourcedSearchExpression
argument_list|(
literal|"org.apache.archiva"
argument_list|)
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
name|MAVEN
operator|.
name|ARTIFACT_ID
argument_list|,
operator|new
name|SourcedSearchExpression
argument_list|(
literal|"archiva-index-methods-jar-test"
argument_list|)
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
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
name|assertFalse
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
name|flatSearchRequest
operator|=
operator|new
name|FlatSearchRequest
argument_list|(
name|q
argument_list|,
name|getIndexingContext
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|=
name|indexer
operator|.
name|searchFlat
argument_list|(
name|flatSearchRequest
argument_list|)
expr_stmt|;
comment|// artifact should have been removed from the index!
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|response
operator|.
name|getTotalHitsCount
argument_list|()
argument_list|)
expr_stmt|;
comment|//.totalHits );
comment|// TODO: test it was removed from the packaged index also
block|}
annotation|@
name|Test
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
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repositoryConfig
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|,
name|getIndexingContext
argument_list|()
argument_list|)
decl_stmt|;
name|task
operator|.
name|setExecuteOnEntireRepo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|task
operator|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repositoryConfig
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|FINISH
argument_list|,
name|getIndexingContext
argument_list|()
argument_list|)
expr_stmt|;
name|task
operator|.
name|setExecuteOnEntireRepo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
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
literal|".indexer/tmp"
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
literal|".indexer"
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
name|MAVEN
operator|.
name|GROUP_ID
argument_list|,
operator|new
name|StringSearchExpression
argument_list|(
literal|"org.apache.archiva"
argument_list|)
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
name|MAVEN
operator|.
name|ARTIFACT_ID
argument_list|,
operator|new
name|StringSearchExpression
argument_list|(
literal|"archiva-index-methods-jar-test"
argument_list|)
argument_list|)
argument_list|,
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|FlatSearchRequest
name|request
init|=
operator|new
name|FlatSearchRequest
argument_list|(
name|q
argument_list|,
name|getIndexingContext
argument_list|()
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

