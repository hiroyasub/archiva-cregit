begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|cassandra
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|datastax
operator|.
name|oss
operator|.
name|driver
operator|.
name|api
operator|.
name|core
operator|.
name|CqlSession
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
name|metadata
operator|.
name|model
operator|.
name|MetadataFacetFactory
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
name|metadata
operator|.
name|repository
operator|.
name|AbstractMetadataRepositoryTest
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepository
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataService
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySession
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySessionFactory
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
name|metadata
operator|.
name|repository
operator|.
name|cassandra
operator|.
name|model
operator|.
name|ProjectVersionMetadataModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|AfterAll
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|AfterEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|BeforeEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
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
name|jupiter
operator|.
name|api
operator|.
name|TestInstance
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|extension
operator|.
name|ExtendWith
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
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|SpringExtension
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|CassandraContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|output
operator|.
name|Slf4jLogConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|utility
operator|.
name|DockerImageName
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
name|javax
operator|.
name|inject
operator|.
name|Named
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
name|Arrays
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
name|concurrent
operator|.
name|CompletableFuture
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CompletionStage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|com
operator|.
name|datastax
operator|.
name|oss
operator|.
name|driver
operator|.
name|api
operator|.
name|querybuilder
operator|.
name|QueryBuilder
operator|.
name|truncate
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|ExtendWith
argument_list|(
name|SpringExtension
operator|.
name|class
argument_list|)
annotation|@
name|TestInstance
argument_list|(
name|TestInstance
operator|.
name|Lifecycle
operator|.
name|PER_CLASS
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|CassandraMetadataRepositoryTest
extends|extends
name|AbstractMetadataRepositoryTest
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CassandraMetadataRepositoryTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaEntityManagerFactory#cassandra"
argument_list|)
name|CassandraArchivaManager
name|cassandraArchivaManager
decl_stmt|;
name|CassandraMetadataRepository
name|cmr
decl_stmt|;
name|RepositorySessionFactory
name|sessionFactory
decl_stmt|;
name|RepositorySession
name|session
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|CassandraContainer
name|CASSANDRA
init|=
operator|new
name|CassandraContainer
argument_list|(
name|DockerImageName
operator|.
name|parse
argument_list|(
literal|"cassandra"
argument_list|)
operator|.
name|withTag
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"cassandraVersion"
argument_list|,
literal|"3.11.2"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
comment|// because of @ExtendWith( SpringExtension.class ) @BeforeAll will not be executed before spring resolution so need to use this...
static|static
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"initCassandra"
argument_list|)
expr_stmt|;
name|CASSANDRA
operator|.
name|withLogConsumer
argument_list|(
operator|new
name|Slf4jLogConsumer
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"org.apache.archiva.metadata.repository.cassandra.logs"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|CASSANDRA
operator|.
name|start
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"cassandra.host"
argument_list|,
name|CASSANDRA
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"cassandra.port"
argument_list|,
name|CASSANDRA
operator|.
name|getMappedPort
argument_list|(
literal|9042
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|long
name|cTime
decl_stmt|;
name|int
name|testNum
init|=
literal|0
decl_stmt|;
specifier|final
name|AtomicBoolean
name|clearedTables
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|RepositorySessionFactory
name|getSessionFactory
parameter_list|( )
block|{
return|return
name|sessionFactory
return|;
block|}
annotation|@
name|Override
specifier|protected
name|MetadataRepository
name|getRepository
parameter_list|( )
block|{
return|return
name|cmr
return|;
block|}
annotation|@
name|AfterAll
specifier|public
specifier|static
name|void
name|stopCassandra
parameter_list|()
throws|throws
name|Exception
block|{
name|CASSANDRA
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|BeforeEach
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|cTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|( )
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|assertMaxTries
operator|=
literal|1
expr_stmt|;
name|assertRetrySleepMs
operator|=
literal|10
expr_stmt|;
name|Path
name|directory
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-repositories"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|directory
argument_list|)
condition|)
block|{
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
name|deleteDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|MetadataFacetFactory
argument_list|>
name|factories
init|=
name|createTestMetadataFacetFactories
argument_list|()
decl_stmt|;
name|MetadataService
name|metadataService
init|=
operator|new
name|MetadataService
argument_list|( )
decl_stmt|;
name|metadataService
operator|.
name|setMetadataFacetFactories
argument_list|(
name|factories
argument_list|)
expr_stmt|;
name|this
operator|.
name|cmr
operator|=
operator|new
name|CassandraMetadataRepository
argument_list|(
name|metadataService
argument_list|,
name|cassandraArchivaManager
argument_list|)
expr_stmt|;
name|sessionFactory
operator|=
name|mock
argument_list|(
name|RepositorySessionFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|session
operator|=
name|mock
argument_list|(
name|RepositorySession
operator|.
name|class
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|sessionFactory
operator|.
name|createSession
argument_list|( )
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|session
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|clearedTables
operator|.
name|get
argument_list|()
condition|)
block|{
name|clearReposAndNamespace
argument_list|(
name|cassandraArchivaManager
argument_list|,
name|clearedTables
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * ensure all dependant tables are cleaned up (mailinglist, license, dependencies)      *      * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|clean_dependant_tables
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|testUpdateProjectVersionMetadataWithAllElements
argument_list|()
expr_stmt|;
name|String
name|key
init|=
operator|new
name|ProjectVersionMetadataModel
operator|.
name|KeyBuilder
argument_list|()
operator|.
name|withRepository
argument_list|(
name|TEST_REPO_ID
argument_list|)
comment|//
operator|.
name|withNamespace
argument_list|(
name|TEST_NAMESPACE
argument_list|)
comment|//
operator|.
name|withProjectId
argument_list|(
name|TEST_PROJECT
argument_list|)
comment|//
operator|.
name|withProjectVersion
argument_list|(
name|TEST_PROJECT_VERSION
argument_list|)
comment|//
operator|.
name|withId
argument_list|(
name|TEST_PROJECT_VERSION
argument_list|)
comment|//
operator|.
name|build
argument_list|()
decl_stmt|;
name|this
operator|.
name|cmr
operator|.
name|removeProjectVersion
argument_list|(
literal|null
argument_list|,
name|TEST_REPO_ID
argument_list|,
name|TEST_NAMESPACE
argument_list|,
name|TEST_PROJECT
argument_list|,
name|TEST_PROJECT_VERSION
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cmr
operator|.
name|getProjectVersion
argument_list|(
literal|null
argument_list|,
name|TEST_REPO_ID
argument_list|,
name|TEST_NAMESPACE
argument_list|,
name|TEST_PROJECT
argument_list|,
name|TEST_PROJECT_VERSION
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|cmr
operator|.
name|getMailingLists
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|cmr
operator|.
name|getLicenses
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|cmr
operator|.
name|getDependencies
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterEach
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|clearReposAndNamespace
argument_list|(
name|cassandraArchivaManager
argument_list|,
name|clearedTables
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|static
name|void
name|clearReposAndNamespace
parameter_list|(
specifier|final
name|CassandraArchivaManager
name|cassandraArchivaManager
parameter_list|,
specifier|final
name|AtomicBoolean
name|clearedFlag
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|cassandraArchivaManager
operator|!=
literal|null
condition|)
block|{
name|CqlSession
name|session
init|=
name|cassandraArchivaManager
operator|.
name|getSession
argument_list|( )
decl_stmt|;
block|{
name|List
argument_list|<
name|String
argument_list|>
name|tables
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|cassandraArchivaManager
operator|.
name|getProjectFamilyName
argument_list|( )
argument_list|,
name|cassandraArchivaManager
operator|.
name|getNamespaceFamilyName
argument_list|( )
argument_list|,
name|cassandraArchivaManager
operator|.
name|getRepositoryFamilyName
argument_list|( )
argument_list|,
name|cassandraArchivaManager
operator|.
name|getProjectVersionMetadataFamilyName
argument_list|( )
argument_list|,
name|cassandraArchivaManager
operator|.
name|getArtifactMetadataFamilyName
argument_list|( )
argument_list|,
name|cassandraArchivaManager
operator|.
name|getMetadataFacetFamilyName
argument_list|( )
argument_list|,
name|cassandraArchivaManager
operator|.
name|getMailingListFamilyName
argument_list|( )
argument_list|,
name|cassandraArchivaManager
operator|.
name|getLicenseFamilyName
argument_list|( )
argument_list|,
name|cassandraArchivaManager
operator|.
name|getDependencyFamilyName
argument_list|( )
argument_list|)
decl_stmt|;
name|CompletableFuture
operator|.
name|allOf
argument_list|(
name|tables
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|table
lambda|->
name|session
operator|.
name|executeAsync
argument_list|(
name|truncate
argument_list|(
name|table
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|CompletionStage
operator|::
name|toCompletableFuture
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|( )
argument_list|)
operator|.
name|toArray
argument_list|(
operator|new
name|CompletableFuture
index|[
literal|0
index|]
argument_list|)
argument_list|)
operator|.
name|whenComplete
argument_list|(
parameter_list|(
name|c
parameter_list|,
name|e
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|clearedFlag
operator|!=
literal|null
condition|)
name|clearedFlag
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"TRUNCATE ERROR DETECTED: "
operator|+
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
block_content|)
block|.get(
block_content|)
function|;
block|}
end_class

begin_block
unit|} else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"cassandraArchivaManager is null"
argument_list|)
expr_stmt|;
block|}
end_block

begin_function
unit|}      static
name|void
name|clearReposAndNamespace
parameter_list|(
specifier|final
name|CassandraArchivaManager
name|cassandraArchivaManager
parameter_list|)
throws|throws
name|Exception
block|{
name|clearReposAndNamespace
argument_list|(
name|cassandraArchivaManager
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

