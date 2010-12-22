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
name|MetadataRepositoryException
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
name|RepositoryStatistics
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
name|consumers
operator|.
name|KnownRepositoryContentConsumer
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
name|model
operator|.
name|ArtifactReference
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
name|codehaus
operator|.
name|plexus
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_comment
comment|/**  * ArchivaRepositoryScanningTaskExecutorTest  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaRepositoryScanningTaskExecutorTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
name|TaskExecutor
name|taskExecutor
decl_stmt|;
specifier|private
name|File
name|repoDir
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO_ID
init|=
literal|"testRepo"
decl_stmt|;
specifier|private
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
decl_stmt|;
specifier|private
name|TestConsumer
name|testConsumer
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
name|taskExecutor
operator|=
operator|(
name|TaskExecutor
operator|)
name|lookup
argument_list|(
name|TaskExecutor
operator|.
name|class
argument_list|,
literal|"test-repository-scanning"
argument_list|)
expr_stmt|;
name|File
name|sourceRepoDir
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repositories/default-repository"
argument_list|)
decl_stmt|;
name|repoDir
operator|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"target/default-repository"
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
name|ArchivaConfiguration
name|archivaConfig
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|repositoryStatisticsManager
operator|=
operator|(
name|RepositoryStatisticsManager
operator|)
name|lookup
argument_list|(
name|RepositoryStatisticsManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|testConsumer
operator|=
operator|(
name|TestConsumer
operator|)
name|lookup
argument_list|(
name|KnownRepositoryContentConsumer
operator|.
name|class
argument_list|,
literal|"test-consumer"
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
specifier|public
name|void
name|testExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryTask
name|repoTask
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|repoTask
operator|.
name|setRepositoryId
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
name|taskExecutor
operator|.
name|executeTask
argument_list|(
name|repoTask
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ArtifactReference
argument_list|>
name|unprocessedResultList
init|=
name|testConsumer
operator|.
name|getConsumed
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|unprocessedResultList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect number of unprocessed artifacts detected."
argument_list|,
literal|8
argument_list|,
name|unprocessedResultList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExecutorScanOnlyNewArtifacts
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryTask
name|repoTask
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|repoTask
operator|.
name|setRepositoryId
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
name|repoTask
operator|.
name|setScanAll
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|createAndSaveTestStats
argument_list|()
expr_stmt|;
name|taskExecutor
operator|.
name|executeTask
argument_list|(
name|repoTask
argument_list|)
expr_stmt|;
comment|// check no artifacts processed
name|Collection
argument_list|<
name|ArtifactReference
argument_list|>
name|unprocessedResultList
init|=
name|testConsumer
operator|.
name|getConsumed
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|unprocessedResultList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect number of unprocessed artifacts detected. No new artifacts should have been found."
argument_list|,
literal|0
argument_list|,
name|unprocessedResultList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// check correctness of new stats
name|RepositoryStatistics
name|newStats
init|=
name|repositoryStatisticsManager
operator|.
name|getLastStatistics
argument_list|(
name|TEST_REPO_ID
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|newStats
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|31
argument_list|,
name|newStats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// FIXME: can't test these as they weren't stored in the database, move to tests for RepositoryStatisticsManager implementation
comment|//        assertEquals( 8, newStats.getTotalArtifactCount() );
comment|//        assertEquals( 3, newStats.getTotalGroupCount() );
comment|//        assertEquals( 5, newStats.getTotalProjectCount() );
comment|//        assertEquals( 14159, newStats.getTotalArtifactFileSize() );
name|File
name|newArtifactGroup
init|=
operator|new
name|File
argument_list|(
name|repoDir
argument_list|,
literal|"org/apache/archiva"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectoryStructure
argument_list|(
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"target/test-classes/test-repo/org/apache/archiva"
argument_list|)
argument_list|,
name|newArtifactGroup
argument_list|)
expr_stmt|;
comment|// update last modified date
operator|new
name|File
argument_list|(
name|newArtifactGroup
argument_list|,
literal|"archiva-index-methods-jar-test/1.0/pom.xml"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTimeInMillis
argument_list|()
operator|+
literal|1000
argument_list|)
expr_stmt|;
operator|new
name|File
argument_list|(
name|newArtifactGroup
argument_list|,
literal|"archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTimeInMillis
argument_list|()
operator|+
literal|1000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|newArtifactGroup
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|taskExecutor
operator|.
name|executeTask
argument_list|(
name|repoTask
argument_list|)
expr_stmt|;
name|unprocessedResultList
operator|=
name|testConsumer
operator|.
name|getConsumed
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|unprocessedResultList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect number of unprocessed artifacts detected. One new artifact should have been found."
argument_list|,
literal|1
argument_list|,
name|unprocessedResultList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// check correctness of new stats
name|RepositoryStatistics
name|updatedStats
init|=
name|repositoryStatisticsManager
operator|.
name|getLastStatistics
argument_list|(
name|TEST_REPO_ID
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|updatedStats
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33
argument_list|,
name|updatedStats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// FIXME: can't test these as they weren't stored in the database, move to tests for RepositoryStatisticsManager implementation
comment|//        assertEquals( 8, newStats.getTotalArtifactCount() );
comment|//        assertEquals( 3, newStats.getTotalGroupCount() );
comment|//        assertEquals( 5, newStats.getTotalProjectCount() );
comment|//        assertEquals( 19301, updatedStats.getTotalArtifactFileSize() );
block|}
specifier|public
name|void
name|testExecutorScanOnlyNewArtifactsChangeTimes
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryTask
name|repoTask
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|repoTask
operator|.
name|setRepositoryId
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
name|repoTask
operator|.
name|setScanAll
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|createAndSaveTestStats
argument_list|()
expr_stmt|;
name|File
name|newArtifactGroup
init|=
operator|new
name|File
argument_list|(
name|repoDir
argument_list|,
literal|"org/apache/archiva"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectoryStructure
argument_list|(
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"target/test-classes/test-repo/org/apache/archiva"
argument_list|)
argument_list|,
name|newArtifactGroup
argument_list|)
expr_stmt|;
comment|// update last modified date, placing shortly after last scan
operator|new
name|File
argument_list|(
name|newArtifactGroup
argument_list|,
literal|"archiva-index-methods-jar-test/1.0/pom.xml"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTimeInMillis
argument_list|()
operator|+
literal|1000
argument_list|)
expr_stmt|;
operator|new
name|File
argument_list|(
name|newArtifactGroup
argument_list|,
literal|"archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTimeInMillis
argument_list|()
operator|+
literal|1000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|newArtifactGroup
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// scan using the really long previous duration
name|taskExecutor
operator|.
name|executeTask
argument_list|(
name|repoTask
argument_list|)
expr_stmt|;
comment|// check no artifacts processed
name|Collection
argument_list|<
name|ArtifactReference
argument_list|>
name|unprocessedResultList
init|=
name|testConsumer
operator|.
name|getConsumed
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|unprocessedResultList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect number of unprocessed artifacts detected. One new artifact should have been found."
argument_list|,
literal|1
argument_list|,
name|unprocessedResultList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// check correctness of new stats
name|RepositoryStatistics
name|newStats
init|=
name|repositoryStatisticsManager
operator|.
name|getLastStatistics
argument_list|(
name|TEST_REPO_ID
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|newStats
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33
argument_list|,
name|newStats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// FIXME: can't test these as they weren't stored in the database, move to tests for RepositoryStatisticsManager implementation
comment|//        assertEquals( 8, newStats.getTotalArtifactCount() );
comment|//        assertEquals( 3, newStats.getTotalGroupCount() );
comment|//        assertEquals( 5, newStats.getTotalProjectCount() );
comment|//        assertEquals( 19301, newStats.getTotalArtifactFileSize() );
block|}
specifier|public
name|void
name|testExecutorScanOnlyNewArtifactsMidScan
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryTask
name|repoTask
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|repoTask
operator|.
name|setRepositoryId
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
name|repoTask
operator|.
name|setScanAll
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|createAndSaveTestStats
argument_list|()
expr_stmt|;
name|File
name|newArtifactGroup
init|=
operator|new
name|File
argument_list|(
name|repoDir
argument_list|,
literal|"org/apache/archiva"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectoryStructure
argument_list|(
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"target/test-classes/test-repo/org/apache/archiva"
argument_list|)
argument_list|,
name|newArtifactGroup
argument_list|)
expr_stmt|;
comment|// update last modified date, placing in middle of last scan
operator|new
name|File
argument_list|(
name|newArtifactGroup
argument_list|,
literal|"archiva-index-methods-jar-test/1.0/pom.xml"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTimeInMillis
argument_list|()
operator|-
literal|50000
argument_list|)
expr_stmt|;
operator|new
name|File
argument_list|(
name|newArtifactGroup
argument_list|,
literal|"archiva-index-methods-jar-test/1.0/archiva-index-methods-jar-test-1.0.jar"
argument_list|)
operator|.
name|setLastModified
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTimeInMillis
argument_list|()
operator|-
literal|50000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|newArtifactGroup
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// scan using the really long previous duration
name|taskExecutor
operator|.
name|executeTask
argument_list|(
name|repoTask
argument_list|)
expr_stmt|;
comment|// check no artifacts processed
name|Collection
argument_list|<
name|ArtifactReference
argument_list|>
name|unprocessedResultList
init|=
name|testConsumer
operator|.
name|getConsumed
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|unprocessedResultList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect number of unprocessed artifacts detected. One new artifact should have been found."
argument_list|,
literal|1
argument_list|,
name|unprocessedResultList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// check correctness of new stats
name|RepositoryStatistics
name|newStats
init|=
name|repositoryStatisticsManager
operator|.
name|getLastStatistics
argument_list|(
name|TEST_REPO_ID
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|newStats
operator|.
name|getNewFileCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|33
argument_list|,
name|newStats
operator|.
name|getTotalFileCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// FIXME: can't test these as they weren't stored in the database, move to tests for RepositoryStatisticsManager implementation
comment|//        assertEquals( 8, newStats.getTotalArtifactCount() );
comment|//        assertEquals( 3, newStats.getTotalGroupCount() );
comment|//        assertEquals( 5, newStats.getTotalProjectCount() );
comment|//        assertEquals( 19301, newStats.getTotalArtifactFileSize() );
block|}
specifier|public
name|void
name|testExecutorForceScanAll
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositoryTask
name|repoTask
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|repoTask
operator|.
name|setRepositoryId
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
name|repoTask
operator|.
name|setScanAll
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Date
name|date
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|repositoryStatisticsManager
operator|.
name|addStatisticsAfterScan
argument_list|(
name|TEST_REPO_ID
argument_list|,
operator|new
name|Date
argument_list|(
name|date
operator|.
name|getTime
argument_list|()
operator|-
literal|1234567
argument_list|)
argument_list|,
name|date
argument_list|,
literal|8
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|taskExecutor
operator|.
name|executeTask
argument_list|(
name|repoTask
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|ArtifactReference
argument_list|>
name|unprocessedResultList
init|=
name|testConsumer
operator|.
name|getConsumed
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|unprocessedResultList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Incorrect number of unprocessed artifacts detected."
argument_list|,
literal|8
argument_list|,
name|unprocessedResultList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createAndSaveTestStats
parameter_list|()
throws|throws
name|MetadataRepositoryException
block|{
name|Date
name|date
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|RepositoryStatistics
name|stats
init|=
operator|new
name|RepositoryStatistics
argument_list|()
decl_stmt|;
name|stats
operator|.
name|setScanStartTime
argument_list|(
operator|new
name|Date
argument_list|(
name|date
operator|.
name|getTime
argument_list|()
operator|-
literal|1234567
argument_list|)
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setScanEndTime
argument_list|(
name|date
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setNewFileCount
argument_list|(
literal|31
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setTotalArtifactCount
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setTotalFileCount
argument_list|(
literal|31
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setTotalGroupCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setTotalProjectCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setTotalArtifactFileSize
argument_list|(
literal|38545
argument_list|)
expr_stmt|;
name|repositoryStatisticsManager
operator|.
name|addStatisticsAfterScan
argument_list|(
name|TEST_REPO_ID
argument_list|,
operator|new
name|Date
argument_list|(
name|date
operator|.
name|getTime
argument_list|()
operator|-
literal|1234567
argument_list|)
argument_list|,
name|date
argument_list|,
literal|31
argument_list|,
literal|31
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

