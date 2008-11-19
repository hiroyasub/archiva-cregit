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
name|ArtifactsProcessedConstraint
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
name|RepositoryTask
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
name|jpox
operator|.
name|SchemaTool
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

begin_comment
comment|/**  * ArchivaRepositoryScanningTaskExecutorTest   *  * @version $Id$  */
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
comment|/* derby version         File derbyDbDir = new File( "target/plexus-home/testdb" );         if ( derbyDbDir.exists() )         {             FileUtils.deleteDirectory( derbyDbDir );         }          jdoFactory.setDriverName( System.getProperty( "jdo.test.driver", "org.apache.derby.jdbc.EmbeddedDriver" ) );            jdoFactory.setUrl( System.getProperty( "jdo.test.url", "jdbc:derby:" + derbyDbDir.getAbsolutePath() + ";create=true" ) );          */
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
name|Iterator
name|it
init|=
name|properties
operator|.
name|entrySet
argument_list|()
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
block|}
specifier|public
name|void
name|testExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|repoDir
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
name|repo
init|=
name|createRepository
argument_list|(
literal|"testRepo"
argument_list|,
literal|"Test Repository"
argument_list|,
name|repoDir
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repo
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
name|repo
argument_list|)
expr_stmt|;
name|RepositoryTask
name|repoTask
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|repoTask
operator|.
name|setName
argument_list|(
literal|"testRepoTask"
argument_list|)
expr_stmt|;
name|repoTask
operator|.
name|setRepositoryId
argument_list|(
literal|"testRepo"
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
name|unprocessedResultList
init|=
name|adao
operator|.
name|queryArtifacts
argument_list|(
operator|new
name|ArtifactsProcessedConstraint
argument_list|(
literal|false
argument_list|)
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
specifier|protected
name|ManagedRepositoryConfiguration
name|createRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|File
name|location
parameter_list|)
block|{
name|ManagedRepositoryConfiguration
name|repo
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|location
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
block|}
end_class

end_unit

