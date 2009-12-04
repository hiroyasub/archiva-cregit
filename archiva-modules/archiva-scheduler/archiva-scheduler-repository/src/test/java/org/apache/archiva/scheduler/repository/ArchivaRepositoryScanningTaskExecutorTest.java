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
name|URL
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
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|PersistenceManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|PersistenceManagerFactory
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
name|database
operator|.
name|ArchivaDAO
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
name|database
operator|.
name|ArtifactDAO
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
name|database
operator|.
name|constraints
operator|.
name|MostRecentRepositoryScanStatistics
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
name|ArchivaArtifact
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
name|RepositoryContentStatistics
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
name|jdo
operator|.
name|DefaultConfigurableJdoFactory
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
name|jdo
operator|.
name|JdoFactory
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
name|org
operator|.
name|jpox
operator|.
name|SchemaTool
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
specifier|protected
name|ArchivaDAO
name|dao
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
name|DefaultConfigurableJdoFactory
name|jdoFactory
init|=
operator|(
name|DefaultConfigurableJdoFactory
operator|)
name|lookup
argument_list|(
name|JdoFactory
operator|.
name|ROLE
argument_list|,
literal|"archiva"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|DefaultConfigurableJdoFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|jdoFactory
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setPersistenceManagerFactoryClass
argument_list|(
literal|"org.jpox.PersistenceManagerFactoryImpl"
argument_list|)
expr_stmt|;
comment|/* derby version        File derbyDbDir = new File( "target/plexus-home/testdb" );        if ( derbyDbDir.exists() )        {            FileUtils.deleteDirectory( derbyDbDir );        }         jdoFactory.setDriverName( System.getProperty( "jdo.test.driver", "org.apache.derby.jdbc.EmbeddedDriver" ) );        jdoFactory.setUrl( System.getProperty( "jdo.test.url", "jdbc:derby:" + derbyDbDir.getAbsolutePath() + ";create=true" ) );         */
name|jdoFactory
operator|.
name|setDriverName
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"jdo.test.driver"
argument_list|,
literal|"org.hsqldb.jdbcDriver"
argument_list|)
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setUrl
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"jdo.test.url"
argument_list|,
literal|"jdbc:hsqldb:mem:"
operator|+
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setUserName
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"jdo.test.user"
argument_list|,
literal|"sa"
argument_list|)
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setPassword
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"jdo.test.pass"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.transactionIsolation"
argument_list|,
literal|"READ_COMMITTED"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.poid.transactionIsolation"
argument_list|,
literal|"READ_COMMITTED"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.autoCreateSchema"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"javax.jdo.option.RetainValues"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"javax.jdo.option.RestoreValues"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
comment|// jdoFactory.setProperty( "org.jpox.autoCreateColumns", "true" );
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.validateTables"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.validateColumns"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.validateConstraints"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
name|jdoFactory
operator|.
name|getProperties
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
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
name|URL
name|jdoFileUrls
index|[]
init|=
operator|new
name|URL
index|[]
block|{
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/maven/archiva/model/package.jdo"
argument_list|)
block|}
decl_stmt|;
if|if
condition|(
operator|(
name|jdoFileUrls
operator|==
literal|null
operator|)
operator|||
operator|(
name|jdoFileUrls
index|[
literal|0
index|]
operator|==
literal|null
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"Unable to process test "
operator|+
name|getName
argument_list|()
operator|+
literal|" - missing package.jdo."
argument_list|)
expr_stmt|;
block|}
name|File
name|propsFile
init|=
literal|null
decl_stmt|;
comment|// intentional
name|boolean
name|verbose
init|=
literal|true
decl_stmt|;
name|SchemaTool
operator|.
name|deleteSchemaTables
argument_list|(
name|jdoFileUrls
argument_list|,
operator|new
name|URL
index|[]
block|{}
argument_list|,
name|propsFile
argument_list|,
name|verbose
argument_list|)
expr_stmt|;
name|SchemaTool
operator|.
name|createSchemaTables
argument_list|(
name|jdoFileUrls
argument_list|,
operator|new
name|URL
index|[]
block|{}
argument_list|,
name|propsFile
argument_list|,
name|verbose
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|PersistenceManagerFactory
name|pmf
init|=
name|jdoFactory
operator|.
name|getPersistenceManagerFactory
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|pmf
argument_list|)
expr_stmt|;
name|PersistenceManager
name|pm
init|=
name|pmf
operator|.
name|getPersistenceManager
argument_list|()
decl_stmt|;
name|pm
operator|.
name|close
argument_list|()
expr_stmt|;
name|this
operator|.
name|dao
operator|=
operator|(
name|ArchivaDAO
operator|)
name|lookup
argument_list|(
name|ArchivaDAO
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"jdo"
argument_list|)
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
name|ArtifactDAO
name|adao
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|unprocessedResultList
init|=
name|adao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
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
name|ArtifactDAO
name|adao
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|unprocessedResultList
init|=
name|adao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
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
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
name|results
init|=
operator|(
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
operator|new
name|MostRecentRepositoryScanStatistics
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|)
decl_stmt|;
name|RepositoryContentStatistics
name|newStats
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
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
name|TEST_REPO_ID
argument_list|,
name|newStats
operator|.
name|getRepositoryId
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
comment|// TODO: can't test these as they weren't stored in the database
comment|//        assertEquals( 8, newStats.getTotalArtifactCount() );
comment|//        assertEquals( 3, newStats.getTotalGroupCount() );
comment|//        assertEquals( 5, newStats.getTotalProjectCount() );
name|assertEquals
argument_list|(
literal|14159
argument_list|,
name|newStats
operator|.
name|getTotalSize
argument_list|()
argument_list|)
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
name|adao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
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
name|results
operator|=
operator|(
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
operator|new
name|MostRecentRepositoryScanStatistics
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|)
expr_stmt|;
name|RepositoryContentStatistics
name|updatedStats
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
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
name|TEST_REPO_ID
argument_list|,
name|updatedStats
operator|.
name|getRepositoryId
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
comment|// TODO: can't test these as they weren't stored in the database
comment|//        assertEquals( 8, newStats.getTotalArtifactCount() );
comment|//        assertEquals( 3, newStats.getTotalGroupCount() );
comment|//        assertEquals( 5, newStats.getTotalProjectCount() );
name|assertEquals
argument_list|(
literal|19301
argument_list|,
name|updatedStats
operator|.
name|getTotalSize
argument_list|()
argument_list|)
expr_stmt|;
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
name|ArtifactDAO
name|adao
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|unprocessedResultList
init|=
name|adao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
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
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
name|results
init|=
operator|(
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
operator|new
name|MostRecentRepositoryScanStatistics
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|)
decl_stmt|;
name|RepositoryContentStatistics
name|newStats
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
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
name|TEST_REPO_ID
argument_list|,
name|newStats
operator|.
name|getRepositoryId
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
comment|// TODO: can't test these as they weren't stored in the database
comment|//        assertEquals( 8, newStats.getTotalArtifactCount() );
comment|//        assertEquals( 3, newStats.getTotalGroupCount() );
comment|//        assertEquals( 5, newStats.getTotalProjectCount() );
name|assertEquals
argument_list|(
literal|19301
argument_list|,
name|newStats
operator|.
name|getTotalSize
argument_list|()
argument_list|)
expr_stmt|;
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
name|ArtifactDAO
name|adao
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|unprocessedResultList
init|=
name|adao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
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
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
name|results
init|=
operator|(
name|List
argument_list|<
name|RepositoryContentStatistics
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
operator|new
name|MostRecentRepositoryScanStatistics
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|)
decl_stmt|;
name|RepositoryContentStatistics
name|newStats
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
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
name|TEST_REPO_ID
argument_list|,
name|newStats
operator|.
name|getRepositoryId
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
comment|// TODO: can't test these as they weren't stored in the database
comment|//        assertEquals( 8, newStats.getTotalArtifactCount() );
comment|//        assertEquals( 3, newStats.getTotalGroupCount() );
comment|//        assertEquals( 5, newStats.getTotalProjectCount() );
name|assertEquals
argument_list|(
literal|19301
argument_list|,
name|newStats
operator|.
name|getTotalSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createAndSaveTestStats
parameter_list|()
block|{
name|RepositoryContentStatistics
name|stats
init|=
operator|new
name|RepositoryContentStatistics
argument_list|()
decl_stmt|;
name|stats
operator|.
name|setDuration
argument_list|(
literal|1234567
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
name|setRepositoryId
argument_list|(
name|TEST_REPO_ID
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
name|setTotalSize
argument_list|(
literal|38545
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setWhenGathered
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|dao
operator|.
name|getRepositoryContentStatisticsDAO
argument_list|()
operator|.
name|saveRepositoryContentStatistics
argument_list|(
name|stats
argument_list|)
expr_stmt|;
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
name|RepositoryContentStatistics
name|stats
init|=
operator|new
name|RepositoryContentStatistics
argument_list|()
decl_stmt|;
name|stats
operator|.
name|setDuration
argument_list|(
literal|1234567
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setNewFileCount
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setRepositoryId
argument_list|(
name|TEST_REPO_ID
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
literal|8
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
name|setTotalSize
argument_list|(
literal|999999
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setWhenGathered
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|dao
operator|.
name|getRepositoryContentStatisticsDAO
argument_list|()
operator|.
name|saveRepositoryContentStatistics
argument_list|(
name|stats
argument_list|)
expr_stmt|;
name|taskExecutor
operator|.
name|executeTask
argument_list|(
name|repoTask
argument_list|)
expr_stmt|;
name|ArtifactDAO
name|adao
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|unprocessedResultList
init|=
name|adao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
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
block|}
end_class

end_unit

