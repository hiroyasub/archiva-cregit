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
name|indexer
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|utils
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
name|IndexCreationFailedException
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
name|RepositoryType
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
name|features
operator|.
name|RemoteIndexFeature
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
name|maven
operator|.
name|repository
operator|.
name|MavenManagedRepository
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
name|maven
operator|.
name|repository
operator|.
name|MavenRemoteRepository
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
name|QueryCreator
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
name|UserInputSearchExpression
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
name|Query
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

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
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|MavenIndexManagerTest
block|{
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
specifier|private
name|Path
name|indexPath
decl_stmt|;
specifier|private
name|MavenManagedRepository
name|repository
decl_stmt|;
specifier|private
name|ArchivaIndexingContext
name|ctx
decl_stmt|;
specifier|private
name|MavenRemoteRepository
name|repositoryRemote
decl_stmt|;
annotation|@
name|Inject
name|MavenIndexManager
name|mavenIndexManager
decl_stmt|;
annotation|@
name|Inject
name|QueryCreator
name|queryCreator
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|repositoryRegistry
operator|.
name|destroy
argument_list|()
expr_stmt|;
if|if
condition|(
name|ctx
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ctx
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//
block|}
block|}
if|if
condition|(
name|indexPath
operator|!=
literal|null
operator|&&
name|Files
operator|.
name|exists
argument_list|(
name|indexPath
argument_list|)
condition|)
block|{
name|FileUtils
operator|.
name|deleteQuietly
argument_list|(
name|indexPath
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|pack
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestContext
argument_list|()
expr_stmt|;
name|Path
name|destDir
init|=
name|repository
operator|.
name|getRoot
argument_list|()
operator|.
name|getFilePath
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"org/apache/archiva/archiva-webapp/1.0"
argument_list|)
decl_stmt|;
name|Path
name|srcDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/maven-search-test-repo/org/apache/archiva/archiva-webapp/1.0"
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|srcDir
operator|.
name|toFile
argument_list|()
argument_list|,
name|destDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
name|mavenIndexManager
operator|.
name|scan
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|mavenIndexManager
operator|.
name|pack
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|list
argument_list|(
name|indexPath
argument_list|)
operator|.
name|filter
argument_list|(
name|path
lambda|->
block|{
try|try
block|{
return|return
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".gz"
argument_list|)
operator|&&
name|Files
operator|.
name|size
argument_list|(
name|path
argument_list|)
operator|>
literal|0
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|isPresent
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|scan
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestContext
argument_list|()
expr_stmt|;
name|Path
name|destDir
init|=
name|repository
operator|.
name|getRoot
argument_list|()
operator|.
name|getFilePath
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"org/apache/archiva/archiva-webapp/1.0"
argument_list|)
decl_stmt|;
name|Path
name|srcDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/maven-search-test-repo/org/apache/archiva/archiva-webapp/1.0"
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|srcDir
operator|.
name|toFile
argument_list|()
argument_list|,
name|destDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
name|mavenIndexManager
operator|.
name|scan
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|IndexingContext
name|mvnCtx
init|=
name|mavenIndexManager
operator|.
name|getMvnContext
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|String
name|term
init|=
literal|"org.apache.archiva"
decl_stmt|;
name|Query
name|q
init|=
operator|new
name|BooleanQuery
operator|.
name|Builder
argument_list|()
operator|.
name|add
argument_list|(
name|queryCreator
operator|.
name|constructQuery
argument_list|(
name|MAVEN
operator|.
name|GROUP_ID
argument_list|,
operator|new
name|UserInputSearchExpression
argument_list|(
name|term
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|mvnCtx
operator|.
name|acquireIndexSearcher
argument_list|()
operator|.
name|count
argument_list|(
name|q
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*      * Does only a index update via file uri, no HTTP uri      */
annotation|@
name|Test
specifier|public
name|void
name|update
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestContext
argument_list|()
expr_stmt|;
name|mavenIndexManager
operator|.
name|pack
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|close
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|createTestContextForRemote
argument_list|()
expr_stmt|;
name|mavenIndexManager
operator|.
name|update
argument_list|(
name|ctx
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|addArtifactsToIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaIndexingContext
name|ctx
init|=
name|createTestContext
argument_list|()
decl_stmt|;
try|try
block|{
name|Path
name|destDir
init|=
name|repository
operator|.
name|getRoot
argument_list|()
operator|.
name|getFilePath
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"org/apache/archiva/archiva-search/1.0"
argument_list|)
decl_stmt|;
name|Path
name|srcDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/maven-search-test-repo/org/apache/archiva/archiva-search/1.0"
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|srcDir
operator|.
name|toFile
argument_list|()
argument_list|,
name|destDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|URI
argument_list|>
name|uriList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|uriList
operator|.
name|add
argument_list|(
name|destDir
operator|.
name|resolve
argument_list|(
literal|"archiva-search-1.0.jar"
argument_list|)
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
name|uriList
operator|.
name|add
argument_list|(
name|destDir
operator|.
name|resolve
argument_list|(
literal|"archiva-search-1.0-sources.jar"
argument_list|)
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
name|mavenIndexManager
operator|.
name|addArtifactsToIndex
argument_list|(
name|ctx
argument_list|,
name|uriList
argument_list|)
expr_stmt|;
name|IndexingContext
name|mvnCtx
init|=
name|mavenIndexManager
operator|.
name|getMvnContext
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|String
name|term
init|=
literal|"org.apache.archiva"
decl_stmt|;
name|Query
name|q
init|=
operator|new
name|BooleanQuery
operator|.
name|Builder
argument_list|()
operator|.
name|add
argument_list|(
name|queryCreator
operator|.
name|constructQuery
argument_list|(
name|MAVEN
operator|.
name|GROUP_ID
argument_list|,
operator|new
name|UserInputSearchExpression
argument_list|(
name|term
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mvnCtx
operator|.
name|acquireIndexSearcher
argument_list|()
operator|.
name|count
argument_list|(
name|q
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|ctx
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|removeArtifactsFromIndex
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaIndexingContext
name|ctx
init|=
name|createTestContext
argument_list|()
decl_stmt|;
name|Path
name|destDir
init|=
name|repository
operator|.
name|getRoot
argument_list|()
operator|.
name|getFilePath
argument_list|()
operator|.
name|resolve
argument_list|(
literal|"org/apache/archiva/archiva-search/1.0"
argument_list|)
decl_stmt|;
name|Path
name|srcDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/maven-search-test-repo/org/apache/archiva/archiva-search/1.0"
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|srcDir
operator|.
name|toFile
argument_list|()
argument_list|,
name|destDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|URI
argument_list|>
name|uriList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|uriList
operator|.
name|add
argument_list|(
name|destDir
operator|.
name|resolve
argument_list|(
literal|"archiva-search-1.0.jar"
argument_list|)
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
name|uriList
operator|.
name|add
argument_list|(
name|destDir
operator|.
name|resolve
argument_list|(
literal|"archiva-search-1.0-sources.jar"
argument_list|)
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
name|mavenIndexManager
operator|.
name|addArtifactsToIndex
argument_list|(
name|ctx
argument_list|,
name|uriList
argument_list|)
expr_stmt|;
name|IndexingContext
name|mvnCtx
init|=
name|mavenIndexManager
operator|.
name|getMvnContext
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|String
name|term
init|=
literal|"org.apache.archiva"
decl_stmt|;
name|Query
name|q
init|=
operator|new
name|BooleanQuery
operator|.
name|Builder
argument_list|()
operator|.
name|add
argument_list|(
name|queryCreator
operator|.
name|constructQuery
argument_list|(
name|MAVEN
operator|.
name|GROUP_ID
argument_list|,
operator|new
name|UserInputSearchExpression
argument_list|(
name|term
argument_list|)
argument_list|)
argument_list|,
name|BooleanClause
operator|.
name|Occur
operator|.
name|SHOULD
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|mvnCtx
operator|.
name|acquireIndexSearcher
argument_list|()
operator|.
name|count
argument_list|(
name|q
argument_list|)
argument_list|)
expr_stmt|;
name|uriList
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mavenIndexManager
operator|.
name|removeArtifactsFromIndex
argument_list|(
name|ctx
argument_list|,
name|uriList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|mvnCtx
operator|.
name|acquireIndexSearcher
argument_list|()
operator|.
name|count
argument_list|(
name|q
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|supportsRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|mavenIndexManager
operator|.
name|supportsRepository
argument_list|(
name|RepositoryType
operator|.
name|MAVEN
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|mavenIndexManager
operator|.
name|supportsRepository
argument_list|(
name|RepositoryType
operator|.
name|NPM
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ArchivaIndexingContext
name|createTestContext
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|IndexCreationFailedException
throws|,
name|IOException
block|{
name|String
name|indexPathName
init|=
literal|".index-test."
operator|+
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
name|indexPath
operator|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/repositories/test-repo"
argument_list|)
operator|.
name|resolve
argument_list|(
name|indexPathName
argument_list|)
expr_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|indexPath
argument_list|)
condition|)
block|{
try|try
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexPath
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|String
name|destName
init|=
name|indexPath
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"."
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|Files
operator|.
name|move
argument_list|(
name|indexPath
argument_list|,
name|indexPath
operator|.
name|getParent
argument_list|()
operator|.
name|resolve
argument_list|(
name|destName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|repository
operator|=
name|MavenManagedRepository
operator|.
name|newLocalInstance
argument_list|(
literal|"test-repo"
argument_list|,
literal|"Test Repo"
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
literal|"target/repositories"
argument_list|)
argument_list|)
expr_stmt|;
comment|// repository.setLocation(new URI("test-repo"));
name|IndexCreationFeature
name|icf
init|=
name|repository
operator|.
name|getFeature
argument_list|(
name|IndexCreationFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|icf
operator|.
name|setIndexPath
argument_list|(
operator|new
name|URI
argument_list|(
name|indexPathName
argument_list|)
argument_list|)
expr_stmt|;
name|ctx
operator|=
name|mavenIndexManager
operator|.
name|createContext
argument_list|(
name|repository
argument_list|)
expr_stmt|;
return|return
name|ctx
return|;
block|}
specifier|private
name|ArchivaIndexingContext
name|createTestContextForRemote
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|IndexCreationFailedException
throws|,
name|IOException
block|{
comment|// indexPath = Paths.get("target/repositories/test-repo/.index-test");
name|Path
name|repoPath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/repositories"
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
decl_stmt|;
name|repositoryRemote
operator|=
name|MavenRemoteRepository
operator|.
name|newLocalInstance
argument_list|(
literal|"test-repo"
argument_list|,
literal|"Test Repo"
argument_list|,
name|repoPath
argument_list|)
expr_stmt|;
name|repositoryRemote
operator|.
name|setLocation
argument_list|(
name|repoPath
operator|.
name|resolve
argument_list|(
literal|"test-repo"
argument_list|)
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteIndexFeature
name|icf
init|=
name|repositoryRemote
operator|.
name|getFeature
argument_list|(
name|RemoteIndexFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|icf
operator|.
name|setIndexUri
argument_list|(
operator|new
name|URI
argument_list|(
name|indexPath
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ctx
operator|=
name|mavenIndexManager
operator|.
name|createContext
argument_list|(
name|repositoryRemote
argument_list|)
expr_stmt|;
return|return
name|ctx
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createContext
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaIndexingContext
name|ctx
init|=
name|createTestContext
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|repository
argument_list|,
name|ctx
operator|.
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test-repo"
argument_list|,
name|ctx
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|indexPath
operator|.
name|toAbsolutePath
argument_list|()
argument_list|,
name|ctx
operator|.
name|getPath
argument_list|()
operator|.
name|getFilePath
argument_list|()
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|indexPath
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Path
argument_list|>
name|li
init|=
name|Files
operator|.
name|list
argument_list|(
name|indexPath
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|li
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

