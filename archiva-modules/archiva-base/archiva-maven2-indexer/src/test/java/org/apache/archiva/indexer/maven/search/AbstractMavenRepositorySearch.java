begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|maven
operator|.
name|search
package|;
end_package

begin_comment
comment|/* * Licensed to the Apache Software Foundation (ASF) under one * or more contributor license agreements.  See the NOTICE file * distributed with this work for additional information * regarding copyright ownership.  The ASF licenses this file * to you under the Apache License, Version 2.0 (the * "License"); you may not use this file except in compliance * with the License.  You may obtain a copy of the License at * *  http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, * software distributed under the License is distributed on an * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY * KIND, either express or implied.  See the License for the * specific language governing permissions and limitations * under the License. */
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
name|repository
operator|.
name|proxyconnector
operator|.
name|DefaultProxyConnectorAdmin
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
name|archiva
operator|.
name|configuration
operator|.
name|ConfigurationListener
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
name|archiva
operator|.
name|indexer
operator|.
name|search
operator|.
name|SearchResultHit
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
name|search
operator|.
name|SearchResults
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
name|Repository
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
name|RepositoryRegistry
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
name|commons
operator|.
name|lang
operator|.
name|SystemUtils
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
name|ArtifactContext
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
name|ArtifactContextProducer
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
name|ArtifactScanningListener
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
name|DefaultScannerListener
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
name|IndexerEngine
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
name|Scanner
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
name|ScanningRequest
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
name|ScanningResult
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
name|easymock
operator|.
name|EasyMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|IMocksControl
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
name|runner
operator|.
name|RunWith
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
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
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractMavenRepositorySearch
extends|extends
name|TestCase
block|{
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
specifier|static
name|String
name|TEST_REPO_1
init|=
literal|"maven-search-test-repo"
decl_stmt|;
specifier|public
specifier|static
name|String
name|TEST_REPO_2
init|=
literal|"maven-search-test-repo-2"
decl_stmt|;
specifier|public
specifier|static
name|String
name|REPO_RELEASE
init|=
literal|"repo-release"
decl_stmt|;
name|MavenRepositorySearch
name|search
decl_stmt|;
name|ArchivaConfiguration
name|archivaConfig
decl_stmt|;
annotation|@
name|Inject
name|ArtifactContextProducer
name|artifactContextProducer
decl_stmt|;
annotation|@
name|Inject
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|IndexerEngine
name|indexerEngine
decl_stmt|;
name|IMocksControl
name|archivaConfigControl
decl_stmt|;
name|Configuration
name|config
decl_stmt|;
annotation|@
name|Inject
name|Indexer
name|indexer
decl_stmt|;
annotation|@
name|Inject
name|Scanner
name|scanner
decl_stmt|;
annotation|@
name|Inject
name|QueryCreator
name|queryCreator
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
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"/target/repos/"
operator|+
name|TEST_REPO_1
operator|+
literal|"/.indexer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"/target/repos/"
operator|+
name|TEST_REPO_1
operator|+
literal|"/.indexer"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"/target/repos/"
operator|+
name|TEST_REPO_2
operator|+
literal|"/.indexer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"/target/repos/"
operator|+
name|TEST_REPO_2
operator|+
literal|"/.indexer"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|archivaConfigControl
operator|=
name|EasyMock
operator|.
name|createControl
argument_list|()
expr_stmt|;
name|archivaConfig
operator|=
name|archivaConfigControl
operator|.
name|createMock
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|DefaultProxyConnectorAdmin
name|defaultProxyConnectorAdmin
init|=
operator|new
name|DefaultProxyConnectorAdmin
argument_list|()
decl_stmt|;
name|defaultProxyConnectorAdmin
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfig
argument_list|)
expr_stmt|;
name|repositoryRegistry
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfig
argument_list|)
expr_stmt|;
name|search
operator|=
operator|new
name|MavenRepositorySearch
argument_list|(
name|indexer
argument_list|,
name|repositoryRegistry
argument_list|,
name|defaultProxyConnectorAdmin
argument_list|,
name|queryCreator
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|repositoryRegistry
argument_list|)
expr_stmt|;
name|config
operator|=
operator|new
name|Configuration
argument_list|()
expr_stmt|;
name|config
operator|.
name|addManagedRepository
argument_list|(
name|createRepositoryConfig
argument_list|(
name|TEST_REPO_1
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|addManagedRepository
argument_list|(
name|createRepositoryConfig
argument_list|(
name|TEST_REPO_2
argument_list|)
argument_list|)
expr_stmt|;
name|config
operator|.
name|addManagedRepository
argument_list|(
name|createRepositoryConfig
argument_list|(
name|REPO_RELEASE
argument_list|)
argument_list|)
expr_stmt|;
name|archivaConfig
operator|.
name|addListener
argument_list|(
name|EasyMock
operator|.
name|anyObject
argument_list|(
name|ConfigurationListener
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|archivaConfig
operator|.
name|getDefaultLocale
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Locale
operator|.
name|getDefault
argument_list|( )
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|archivaConfig
operator|.
name|getConfiguration
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|config
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|archivaConfig
operator|.
name|save
argument_list|(
name|EasyMock
operator|.
name|anyObject
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|archivaConfigControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|repositoryRegistry
operator|.
name|reload
argument_list|()
expr_stmt|;
name|archivaConfigControl
operator|.
name|reset
argument_list|()
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
name|archivaConfigControl
operator|.
name|reset
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|archivaConfig
operator|.
name|getDefaultLocale
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Locale
operator|.
name|getDefault
argument_list|( )
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|archivaConfig
operator|.
name|getConfiguration
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|config
argument_list|)
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|archivaConfig
operator|.
name|save
argument_list|(
name|EasyMock
operator|.
name|anyObject
argument_list|(
name|Configuration
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|anyTimes
argument_list|()
expr_stmt|;
name|archivaConfigControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|repositoryRegistry
operator|.
name|removeRepository
argument_list|(
name|TEST_REPO_1
argument_list|)
expr_stmt|;
name|repositoryRegistry
operator|.
name|removeRepository
argument_list|(
name|TEST_REPO_2
argument_list|)
expr_stmt|;
name|repositoryRegistry
operator|.
name|removeRepository
argument_list|(
name|REPO_RELEASE
argument_list|)
expr_stmt|;
name|repositoryRegistry
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"/target/repos/"
operator|+
name|TEST_REPO_1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"/target/repos/"
operator|+
name|TEST_REPO_1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"/target/repos/"
operator|+
name|TEST_REPO_2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"/target/repos/"
operator|+
name|TEST_REPO_2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|ManagedRepositoryConfiguration
name|createRepositoryConfig
parameter_list|(
name|String
name|repository
parameter_list|)
block|{
name|ManagedRepositoryConfiguration
name|repositoryConfig
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repositoryConfig
operator|.
name|setId
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|repositoryConfig
operator|.
name|setLocation
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
operator|+
literal|"/target/repos/"
operator|+
name|repository
argument_list|)
expr_stmt|;
name|Path
name|f
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repositoryConfig
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|f
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not create directories for {}"
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
block|}
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
name|repository
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
name|repositoryConfig
operator|.
name|setIndexDir
argument_list|(
literal|".indexer"
argument_list|)
expr_stmt|;
return|return
name|repositoryConfig
return|;
block|}
specifier|protected
name|void
name|createIndex
parameter_list|(
name|String
name|repository
parameter_list|,
name|List
argument_list|<
name|Path
argument_list|>
name|filesToBeIndexed
parameter_list|,
name|boolean
name|scan
parameter_list|)
throws|throws
name|Exception
block|{
name|createIndex
argument_list|(
name|repository
argument_list|,
name|filesToBeIndexed
argument_list|,
name|scan
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|createIndex
parameter_list|(
name|String
name|repository
parameter_list|,
name|List
argument_list|<
name|Path
argument_list|>
name|filesToBeIndexed
parameter_list|,
name|boolean
name|scan
parameter_list|,
name|Path
name|indexDir
parameter_list|)
throws|throws
name|Exception
block|{
name|Repository
name|rRepo
init|=
name|repositoryRegistry
operator|.
name|getRepository
argument_list|(
name|repository
argument_list|)
decl_stmt|;
name|IndexCreationFeature
name|icf
init|=
name|rRepo
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
name|IndexingContext
name|context
init|=
name|rRepo
operator|.
name|getIndexingContext
argument_list|()
operator|.
name|getBaseContext
argument_list|(
name|IndexingContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|Path
name|repoDir
init|=
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"target"
argument_list|)
operator|.
name|resolve
argument_list|(
literal|"repos"
argument_list|)
operator|.
name|resolve
argument_list|(
name|repository
argument_list|)
decl_stmt|;
name|Path
name|indexerDirectory
init|=
name|repoDir
operator|.
name|resolve
argument_list|(
literal|".indexer"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|indexerDirectory
argument_list|)
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexerDirectory
argument_list|)
expr_stmt|;
block|}
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|indexerDirectory
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|lockFile
init|=
name|repoDir
operator|.
name|resolve
argument_list|(
literal|".indexer/write.lock"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|lockFile
argument_list|)
condition|)
block|{
name|Files
operator|.
name|delete
argument_list|(
name|lockFile
argument_list|)
expr_stmt|;
block|}
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|lockFile
argument_list|)
argument_list|)
expr_stmt|;
name|Path
name|repo
init|=
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/"
operator|+
name|repository
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|repo
argument_list|)
argument_list|)
expr_stmt|;
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
name|repo
operator|.
name|toFile
argument_list|()
argument_list|,
name|repoDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|indexDir
operator|==
literal|null
condition|)
block|{
name|Path
name|indexDirectory
init|=
name|Paths
operator|.
name|get
argument_list|(
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
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"target/index/test-"
operator|+
name|Long
operator|.
name|toString
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|indexDirectory
operator|.
name|toFile
argument_list|()
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|indexDirectory
argument_list|)
expr_stmt|;
name|icf
operator|.
name|setIndexPath
argument_list|(
name|indexDirectory
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|icf
operator|.
name|setIndexPath
argument_list|(
name|indexDir
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|context
operator|=
name|rRepo
operator|.
name|getIndexingContext
argument_list|()
operator|.
name|getBaseContext
argument_list|(
name|IndexingContext
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// minimize datas in memory
comment|//        context.getIndexWriter().setMaxBufferedDocs( -1 );
comment|//        context.getIndexWriter().setRAMBufferSizeMB( 1 );
for|for
control|(
name|Path
name|artifactFile
range|:
name|filesToBeIndexed
control|)
block|{
name|assertTrue
argument_list|(
literal|"file not exists "
operator|+
name|artifactFile
argument_list|,
name|Files
operator|.
name|exists
argument_list|(
name|artifactFile
argument_list|)
argument_list|)
expr_stmt|;
name|ArtifactContext
name|ac
init|=
name|artifactContextProducer
operator|.
name|getArtifactContext
argument_list|(
name|context
argument_list|,
name|artifactFile
operator|.
name|toFile
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifactFile
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".pom"
argument_list|)
condition|)
block|{
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|setFileExtension
argument_list|(
literal|"pom"
argument_list|)
expr_stmt|;
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|setPackaging
argument_list|(
literal|"pom"
argument_list|)
expr_stmt|;
name|ac
operator|.
name|getArtifactInfo
argument_list|()
operator|.
name|setClassifier
argument_list|(
literal|"pom"
argument_list|)
expr_stmt|;
block|}
name|indexer
operator|.
name|addArtifactToIndex
argument_list|(
name|ac
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|updateTimestamp
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|scan
condition|)
block|{
name|DefaultScannerListener
name|listener
init|=
operator|new
name|DefaultScannerListener
argument_list|(
name|context
argument_list|,
name|indexerEngine
argument_list|,
literal|true
argument_list|,
operator|new
name|ArtifactScanListener
argument_list|()
argument_list|)
decl_stmt|;
name|ScanningRequest
name|req
init|=
operator|new
name|ScanningRequest
argument_list|(
name|context
argument_list|,
name|listener
argument_list|)
decl_stmt|;
name|scanner
operator|.
name|scan
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|context
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
comment|// force flushing
name|context
operator|.
name|commit
argument_list|()
expr_stmt|;
comment|//  context.getIndexWriter().commit();
name|context
operator|.
name|setSearchable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|static
class|class
name|ArtifactScanListener
implements|implements
name|ArtifactScanningListener
block|{
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|scanningStarted
parameter_list|(
name|IndexingContext
name|ctx
parameter_list|)
block|{
comment|//
block|}
annotation|@
name|Override
specifier|public
name|void
name|scanningFinished
parameter_list|(
name|IndexingContext
name|ctx
parameter_list|,
name|ScanningResult
name|result
parameter_list|)
block|{
comment|// no op
block|}
annotation|@
name|Override
specifier|public
name|void
name|artifactError
parameter_list|(
name|ArtifactContext
name|ac
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"artifactError {}"
argument_list|,
name|ac
operator|.
name|getArtifact
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|artifactDiscovered
parameter_list|(
name|ArtifactContext
name|ac
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"artifactDiscovered {}:{}"
argument_list|,
comment|//
name|ac
operator|.
name|getArtifact
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|ac
operator|.
name|getArtifact
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
comment|//
name|ac
operator|.
name|getArtifact
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|ac
operator|.
name|getArtifactInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|String
name|niceDisplay
parameter_list|(
name|SearchResults
name|searchResults
parameter_list|)
throws|throws
name|Exception
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|SearchResultHit
name|hit
range|:
name|searchResults
operator|.
name|getHits
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|hit
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|SystemUtils
operator|.
name|LINE_SEPARATOR
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

