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
name|repository
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
name|TestRepositorySessionFactory
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
name|stats
operator|.
name|RepositoryStatisticsManager
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
name|redback
operator|.
name|components
operator|.
name|taskqueue
operator|.
name|execution
operator|.
name|TaskExecutor
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
name|Calendar
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

begin_comment
comment|/**  * ArchivaRepositoryScanningTaskExecutorPhase1Test  *  * @version $Id$  */
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
name|ArchivaRepositoryScanningTaskExecutorAbstractTest
extends|extends
name|TestCase
block|{
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"taskExecutor#test-repository-scanning"
argument_list|)
specifier|protected
name|TaskExecutor
name|taskExecutor
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaConfiguration#test-repository-scanning"
argument_list|)
specifier|protected
name|ArchivaConfiguration
name|archivaConfig
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositoryStatisticsManager#test"
argument_list|)
specifier|protected
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"knownRepositoryContentConsumer#test-consumer"
argument_list|)
specifier|protected
name|TestConsumer
name|testConsumer
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositorySessionFactory#test"
argument_list|)
specifier|private
name|TestRepositorySessionFactory
name|factory
decl_stmt|;
specifier|protected
name|File
name|repoDir
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_REPO_ID
init|=
literal|"testRepo"
decl_stmt|;
specifier|protected
name|MetadataRepository
name|metadataRepository
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
name|File
name|sourceRepoDir
init|=
operator|new
name|File
argument_list|(
literal|"./src/test/repositories/default-repository"
argument_list|)
decl_stmt|;
name|repoDir
operator|=
operator|new
name|File
argument_list|(
literal|"./target/default-repository"
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repoDir
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Default Test Repository should not exist."
argument_list|,
name|repoDir
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|repoDir
operator|.
name|mkdir
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|copyDirectoryStructure
argument_list|(
name|sourceRepoDir
argument_list|,
name|repoDir
argument_list|)
expr_stmt|;
comment|// set the timestamps to a time well in the past
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|File
name|f
range|:
operator|(
name|List
argument_list|<
name|File
argument_list|>
operator|)
name|FileUtils
operator|.
name|getFiles
argument_list|(
name|repoDir
argument_list|,
literal|"**"
argument_list|,
literal|null
argument_list|)
control|)
block|{
name|f
operator|.
name|setLastModified
argument_list|(
name|cal
operator|.
name|getTimeInMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: test they are excluded instead
for|for
control|(
name|String
name|dir
range|:
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|FileUtils
operator|.
name|getDirectoryNames
argument_list|(
name|repoDir
argument_list|,
literal|"**/.svn"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
control|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|repoDir
argument_list|,
name|dir
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Default Test Repository should exist."
argument_list|,
name|repoDir
operator|.
name|exists
argument_list|()
operator|&&
name|repoDir
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|archivaConfig
argument_list|)
expr_stmt|;
comment|// Create it
name|ManagedRepositoryConfiguration
name|repositoryConfiguration
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repositoryConfiguration
operator|.
name|setId
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
name|repositoryConfiguration
operator|.
name|setName
argument_list|(
literal|"Test Repository"
argument_list|)
expr_stmt|;
name|repositoryConfiguration
operator|.
name|setLocation
argument_list|(
name|repoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfig
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|archivaConfig
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addManagedRepository
argument_list|(
name|repositoryConfiguration
argument_list|)
expr_stmt|;
name|metadataRepository
operator|=
name|mock
argument_list|(
name|MetadataRepository
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setRepository
argument_list|(
name|metadataRepository
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
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repoDir
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoDir
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
block|}
end_class

end_unit

