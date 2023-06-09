begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|maven
operator|.
name|scheduler
operator|.
name|indexing
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|indexer
operator|.
name|ArchivaIndexingContext
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
name|indexer
operator|.
name|UnsupportedBaseContextException
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
name|repository
operator|.
name|ReleaseScheme
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
name|base
operator|.
name|ArchivaRepositoryRegistry
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
name|base
operator|.
name|RepositoryHandlerDependencies
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
name|base
operator|.
name|managed
operator|.
name|BasicManagedRepository
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
name|features
operator|.
name|IndexCreationFeature
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
name|storage
operator|.
name|StorageAsset
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
name|indexing
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
name|archiva
operator|.
name|test
operator|.
name|utils
operator|.
name|ArchivaSpringJUnit4ClassRunner
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
name|Indexer
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
name|apache
operator|.
name|maven
operator|.
name|index
operator|.
name|updater
operator|.
name|DefaultIndexUpdater
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
name|updater
operator|.
name|IndexUpdateRequest
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
name|updater
operator|.
name|IndexUpdater
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
name|index_shaded
operator|.
name|lucene
operator|.
name|search
operator|.
name|BooleanClause
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
name|index_shaded
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
name|maven
operator|.
name|index_shaded
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
name|maven
operator|.
name|index_shaded
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
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
annotation|@
name|Inject
name|ArchivaRepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|Inject
name|RepositoryHandlerDependencies
name|repositoryHandlerDependencies
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|IndexUpdater
name|indexUpdater
decl_stmt|;
specifier|private
name|ManagedRepository
name|repo
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|Indexer
name|indexer
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
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
name|Path
name|baseDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
argument_list|,
literal|"target/test-classes"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
decl_stmt|;
name|BasicManagedRepository
name|repositoryConfig
init|=
name|BasicManagedRepository
operator|.
name|newFilesystemInstance
argument_list|(
literal|"test-repo"
argument_list|,
literal|"Test Repository"
argument_list|,
name|baseDir
operator|.
name|resolve
argument_list|(
literal|"test-repo"
argument_list|)
argument_list|)
decl_stmt|;
name|Path
name|repoLocation
init|=
name|baseDir
operator|.
name|resolve
argument_list|(
literal|"test-repo"
argument_list|)
decl_stmt|;
name|repositoryConfig
operator|.
name|setLocation
argument_list|(
name|repoLocation
operator|.
name|toUri
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
name|setScanned
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|addActiveReleaseScheme
argument_list|(
name|ReleaseScheme
operator|.
name|RELEASE
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|removeActiveReleaseScheme
argument_list|(
name|ReleaseScheme
operator|.
name|SNAPSHOT
argument_list|)
expr_stmt|;
name|repositoryRegistry
operator|.
name|putRepository
argument_list|(
name|repositoryConfig
argument_list|)
expr_stmt|;
name|repo
operator|=
name|repositoryRegistry
operator|.
name|getManagedRepository
argument_list|(
name|repositoryConfig
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
annotation|@
name|Override
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|repositoryRegistry
operator|.
name|destroy
argument_list|()
expr_stmt|;
comment|/*         removeIndexingContext with true cleanup files.         // delete created index in the repository         File indexDir = new File( repositoryConfig.getLocation(), ".indexer" );         FileUtils.deleteDirectory( indexDir );         assertFalse( indexDir.exists() );          indexDir = new File( repositoryConfig.getLocation(), ".index" );         FileUtils.deleteDirectory( indexDir );         assertFalse( indexDir.exists() );         */
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
throws|throws
name|UnsupportedBaseContextException
block|{
assert|assert
name|repo
operator|!=
literal|null
assert|;
name|ArchivaIndexingContext
name|ctx
init|=
name|repo
operator|.
name|getIndexingContext
argument_list|()
decl_stmt|;
assert|assert
name|ctx
operator|!=
literal|null
assert|;
return|return
name|ctx
operator|.
name|getBaseContext
argument_list|(
name|IndexingContext
operator|.
name|class
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
name|Path
name|basePath
init|=
name|repo
operator|.
name|getRoot
argument_list|()
operator|.
name|getFilePath
argument_list|()
decl_stmt|;
name|Path
name|artifactFile
init|=
name|basePath
operator|.
name|resolve
argument_list|(
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
name|ArtifactIndexingTask
name|task
init|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repo
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|,
name|repo
operator|.
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
name|task
operator|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repo
argument_list|,
literal|null
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|FINISH
argument_list|,
name|repo
operator|.
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
name|BooleanQuery
operator|.
name|Builder
name|queryBuilder
init|=
operator|new
name|BooleanQuery
operator|.
name|Builder
argument_list|( )
decl_stmt|;
name|queryBuilder
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
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|queryBuilder
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
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|BooleanQuery
name|q
init|=
name|queryBuilder
operator|.
name|build
argument_list|()
decl_stmt|;
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
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
literal|".indexer"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
literal|".index"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
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
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-index-methods-jar-test"
argument_list|,
name|artifactInfo
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test-repo"
argument_list|,
name|artifactInfo
operator|.
name|getRepository
argument_list|()
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
name|Path
name|basePath
init|=
name|repo
operator|.
name|getRoot
argument_list|()
operator|.
name|getFilePath
argument_list|()
decl_stmt|;
name|Path
name|artifactFile
init|=
name|basePath
operator|.
name|resolve
argument_list|(
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
name|ArtifactIndexingTask
name|task
init|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repo
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|,
name|repo
operator|.
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
operator|.
name|Builder
name|qb
init|=
operator|new
name|BooleanQuery
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|qb
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
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|qb
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
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|IndexingContext
name|ctx
init|=
name|getIndexingContext
argument_list|()
decl_stmt|;
name|IndexSearcher
name|searcher
init|=
name|ctx
operator|.
name|acquireIndexSearcher
argument_list|()
decl_stmt|;
name|TopDocs
name|topDocs
init|=
name|searcher
operator|.
name|search
argument_list|(
name|qb
operator|.
name|build
argument_list|()
argument_list|,
literal|10
argument_list|)
decl_stmt|;
comment|//searcher.close();
name|ctx
operator|.
name|releaseIndexSearcher
argument_list|(
name|searcher
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
literal|".indexer"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
literal|".index"
argument_list|)
argument_list|)
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
name|Path
name|basePath
init|=
name|repo
operator|.
name|getRoot
argument_list|()
operator|.
name|getFilePath
argument_list|()
decl_stmt|;
name|Path
name|artifactFile
init|=
name|basePath
operator|.
name|resolve
argument_list|(
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
name|ArtifactIndexingTask
name|task
init|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repo
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|,
name|repo
operator|.
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
operator|.
name|Builder
name|qb
init|=
operator|new
name|BooleanQuery
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|qb
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
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
comment|//q.add(
comment|//    indexer.constructQuery( MAVEN.ARTIFACT_ID, new SourcedSearchExpression( "archiva-index-methods-jar-test" ) ),
comment|//    Occur.SHOULD );
name|IndexingContext
name|ctx
init|=
name|repo
operator|.
name|getIndexingContext
argument_list|( )
operator|.
name|getBaseContext
argument_list|(
name|IndexingContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|FlatSearchRequest
name|flatSearchRequest
init|=
operator|new
name|FlatSearchRequest
argument_list|(
name|qb
operator|.
name|build
argument_list|()
argument_list|,
name|ctx
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
name|Files
operator|.
name|exists
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
literal|".indexer"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
literal|".index"
argument_list|)
argument_list|)
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
name|repo
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|DELETE
argument_list|,
name|repo
operator|.
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
name|repo
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|FINISH
argument_list|,
name|repo
operator|.
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
name|qb
operator|=
operator|new
name|BooleanQuery
operator|.
name|Builder
argument_list|()
expr_stmt|;
name|qb
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
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|qb
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
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
literal|".indexer"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
literal|".index"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|flatSearchRequest
operator|=
operator|new
name|FlatSearchRequest
argument_list|(
name|qb
operator|.
name|build
argument_list|()
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
name|Path
name|basePath
init|=
name|repo
operator|.
name|getRoot
argument_list|()
operator|.
name|getFilePath
argument_list|()
decl_stmt|;
name|IndexCreationFeature
name|icf
init|=
name|repo
operator|.
name|getFeature
argument_list|(
name|IndexCreationFeature
operator|.
name|class
argument_list|)
decl_stmt|;
name|StorageAsset
name|packedIndexDirectory
init|=
name|icf
operator|.
name|getLocalPackedIndexPath
argument_list|()
decl_stmt|;
name|StorageAsset
name|indexerDirectory
init|=
name|icf
operator|.
name|getLocalIndexPath
argument_list|()
decl_stmt|;
for|for
control|(
name|StorageAsset
name|dir
range|:
operator|new
name|StorageAsset
index|[]
block|{
name|packedIndexDirectory
block|,
name|indexerDirectory
block|}
control|)
block|{
if|if
condition|(
name|dir
operator|.
name|getFilePath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Path
name|localDirPath
init|=
name|dir
operator|.
name|getFilePath
argument_list|()
decl_stmt|;
name|Files
operator|.
name|list
argument_list|(
name|localDirPath
argument_list|)
operator|.
name|filter
argument_list|(
name|path
lambda|->
name|path
operator|.
name|getFileName
argument_list|( )
operator|.
name|toString
argument_list|( )
operator|.
name|startsWith
argument_list|(
literal|"nexus-maven-repository-index"
argument_list|)
argument_list|)
operator|.
name|forEach
argument_list|(
name|path
lambda|->
block|{
lambda|try
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Deleting "
operator|+
name|path
argument_list|)
expr_stmt|;
name|Files
operator|.
name|delete
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|( )
expr_stmt|;
block|}
block|}
block_content|)
empty_stmt|;
block|}
block|}
end_class

begin_decl_stmt
name|Path
name|artifactFile
init|=
name|basePath
operator|.
name|resolve
argument_list|(
literal|"org/apache/archiva/archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
decl_stmt|;
end_decl_stmt

begin_decl_stmt
name|ArtifactIndexingTask
name|task
init|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repo
argument_list|,
name|artifactFile
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|ADD
argument_list|,
name|repo
operator|.
name|getIndexingContext
argument_list|()
argument_list|)
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|task
operator|.
name|setExecuteOnEntireRepo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|task
operator|=
operator|new
name|ArtifactIndexingTask
argument_list|(
name|repo
argument_list|,
literal|null
argument_list|,
name|ArtifactIndexingTask
operator|.
name|Action
operator|.
name|FINISH
argument_list|,
name|repo
operator|.
name|getIndexingContext
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|task
operator|.
name|setExecuteOnEntireRepo
argument_list|(
literal|false
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|indexingExecutor
operator|.
name|executeTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|packedIndexDirectory
operator|.
name|getFilePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|indexerDirectory
operator|.
name|getFilePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_comment
comment|// test packed index file creation
end_comment

begin_comment
comment|//no more zip
end_comment

begin_comment
comment|//Assertions.assertThat(new File( indexerDirectory, "nexus-maven-repository-index.zip" )).exists();
end_comment

begin_expr_stmt
name|Assertions
operator|.
name|assertThat
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|packedIndexDirectory
operator|.
name|getFilePath
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"nexus-maven-repository-index.properties"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|Assertions
operator|.
name|assertThat
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|packedIndexDirectory
operator|.
name|getFilePath
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"nexus-maven-repository-index.gz"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|packedIndexDirectory
operator|.
name|getFilePath
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"nexus-maven-repository-index.1.gz"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
end_expr_stmt

begin_comment
comment|// unpack .zip index
end_comment

begin_comment
comment|//unzipIndex( indexerDirectory.getPath(), destDir.getPath() );
end_comment

begin_decl_stmt
name|DefaultIndexUpdater
operator|.
name|FileFetcher
name|fetcher
init|=
operator|new
name|DefaultIndexUpdater
operator|.
name|FileFetcher
argument_list|(
name|packedIndexDirectory
operator|.
name|getFilePath
argument_list|()
operator|.
name|toFile
argument_list|()
argument_list|)
decl_stmt|;
end_decl_stmt

begin_decl_stmt
name|IndexUpdateRequest
name|updateRequest
init|=
operator|new
name|IndexUpdateRequest
argument_list|(
name|getIndexingContext
argument_list|()
argument_list|,
name|fetcher
argument_list|)
decl_stmt|;
end_decl_stmt

begin_comment
comment|//updateRequest.setLocalIndexCacheDir( indexerDirectory );
end_comment

begin_expr_stmt
name|indexUpdater
operator|.
name|fetchAndUpdateIndex
argument_list|(
name|updateRequest
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|BooleanQuery
operator|.
name|Builder
name|qb
init|=
operator|new
name|BooleanQuery
operator|.
name|Builder
argument_list|()
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|qb
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
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|qb
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
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
expr_stmt|;
end_expr_stmt

begin_decl_stmt
name|FlatSearchRequest
name|request
init|=
operator|new
name|FlatSearchRequest
argument_list|(
name|qb
operator|.
name|build
argument_list|()
argument_list|,
name|getIndexingContext
argument_list|()
argument_list|)
decl_stmt|;
end_decl_stmt

begin_decl_stmt
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
end_decl_stmt

begin_expr_stmt
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
end_expr_stmt

begin_decl_stmt
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
end_decl_stmt

begin_decl_stmt
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
end_decl_stmt

begin_expr_stmt
name|assertEquals
argument_list|(
literal|"org.apache.archiva"
argument_list|,
name|artifactInfo
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|assertEquals
argument_list|(
literal|"archiva-index-methods-jar-test"
argument_list|,
name|artifactInfo
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

begin_expr_stmt
name|assertEquals
argument_list|(
literal|"test-repo"
argument_list|,
name|artifactInfo
operator|.
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
end_expr_stmt

unit|}  }
end_unit

