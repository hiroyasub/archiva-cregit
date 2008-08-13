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
name|consumers
operator|.
name|core
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
name|ArchivaDatabaseException
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
name|repository
operator|.
name|ManagedRepositoryContent
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
name|URL
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
comment|/**  * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryPurgeTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TEST_REPO_ID
init|=
literal|"test-repo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEST_REPO_NAME
init|=
literal|"Test Repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TEST_RETENTION_COUNT
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TEST_DAYS_OLDER
init|=
literal|30
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_BY_DAYS_OLD_ARTIFACT
init|=
literal|"org/apache/maven/plugins/maven-install-plugin/2.2-SNAPSHOT/maven-install-plugin-2.2-20061118.060401-2.jar"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_BY_DAYS_OLD_METADATA_DRIVEN_ARTIFACT
init|=
literal|"org/codehaus/plexus/plexus-utils/1.4.3-SNAPSHOT/plexus-utils-1.4.3-20070113.163208-4.jar"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_BY_RETENTION_COUNT_ARTIFACT
init|=
literal|"org/jruby/plugins/jruby-rake-plugin/1.0RC1-SNAPSHOT/jruby-rake-plugin-1.0RC1-20070504.153317-1.jar"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_BY_RETENTION_COUNT_POM
init|=
literal|"org/codehaus/castor/castor-anttasks/1.1.2-SNAPSHOT/castor-anttasks-1.1.2-20070506.163513-2.pom"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PATH_TO_TEST_ORDER_OF_DELETION
init|=
literal|"org/apache/maven/plugins/maven-assembly-plugin/1.1.2-SNAPSHOT/maven-assembly-plugin-1.1.2-20070615.105019-3.jar"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|RELEASES_TEST_REPO_ID
init|=
literal|"releases-test-repo-one"
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|RELEASES_TEST_REPO_NAME
init|=
literal|"Releases Test Repo One"
decl_stmt|;
specifier|private
name|ManagedRepositoryConfiguration
name|config
decl_stmt|;
specifier|private
name|ManagedRepositoryContent
name|repo
decl_stmt|;
specifier|protected
name|ArtifactDAO
name|dao
decl_stmt|;
specifier|protected
name|RepositoryPurge
name|repoPurge
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
literal|"jdbc:hsqldb:mem:testdb"
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
name|dao
operator|=
operator|(
name|ArtifactDAO
operator|)
name|lookup
argument_list|(
name|ArtifactDAO
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"jdo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|config
operator|=
literal|null
expr_stmt|;
name|repo
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|ManagedRepositoryConfiguration
name|getRepoConfiguration
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|repoName
parameter_list|)
block|{
name|config
operator|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
expr_stmt|;
name|config
operator|.
name|setId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|config
operator|.
name|setName
argument_list|(
name|repoName
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDaysOlder
argument_list|(
name|TEST_DAYS_OLDER
argument_list|)
expr_stmt|;
name|config
operator|.
name|setLocation
argument_list|(
name|getTestFile
argument_list|(
literal|"target/test-"
operator|+
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|repoId
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setRetentionCount
argument_list|(
name|TEST_RETENTION_COUNT
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
specifier|public
name|ManagedRepositoryContent
name|getRepository
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|repo
operator|==
literal|null
condition|)
block|{
name|repo
operator|=
operator|(
name|ManagedRepositoryContent
operator|)
name|lookup
argument_list|(
name|ManagedRepositoryContent
operator|.
name|class
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setRepository
argument_list|(
name|getRepoConfiguration
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_REPO_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|repo
return|;
block|}
specifier|protected
name|void
name|populateDb
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|versions
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
for|for
control|(
name|String
name|version
range|:
name|versions
control|)
block|{
name|ArchivaArtifact
name|artifact
init|=
name|dao
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setLastModified
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setOrigin
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|ArchivaArtifact
name|savedArtifact
init|=
name|dao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|savedArtifact
argument_list|)
expr_stmt|;
comment|//POM
name|artifact
operator|=
name|dao
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|""
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setLastModified
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setOrigin
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|savedArtifact
operator|=
name|dao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|savedArtifact
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|assertDeleted
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"File should have been deleted: "
operator|+
name|path
argument_list|,
operator|new
name|File
argument_list|(
name|path
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertExists
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"File should exist: "
operator|+
name|path
argument_list|,
operator|new
name|File
argument_list|(
name|path
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|File
name|getTestRepoRoot
parameter_list|()
block|{
return|return
name|getTestFile
argument_list|(
literal|"target/test-"
operator|+
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|TEST_REPO_ID
argument_list|)
return|;
block|}
specifier|protected
name|String
name|prepareTestRepos
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|testDir
init|=
name|getTestRepoRoot
argument_list|()
decl_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|testDir
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|getTestFile
argument_list|(
literal|"target/test-classes/"
operator|+
name|TEST_REPO_ID
argument_list|)
argument_list|,
name|testDir
argument_list|)
expr_stmt|;
name|File
name|releasesTestDir
init|=
name|getTestFile
argument_list|(
literal|"target/test-"
operator|+
name|getName
argument_list|()
operator|+
literal|"/"
operator|+
name|RELEASES_TEST_REPO_ID
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|releasesTestDir
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|getTestFile
argument_list|(
literal|"target/test-classes/"
operator|+
name|RELEASES_TEST_REPO_ID
argument_list|)
argument_list|,
name|releasesTestDir
argument_list|)
expr_stmt|;
return|return
name|testDir
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
specifier|protected
name|void
name|populateDbForTestOrderOfDeletion
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.1.2-20070427.065136-1"
argument_list|)
expr_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.1.2-20070506.163513-2"
argument_list|)
expr_stmt|;
name|versions
operator|.
name|add
argument_list|(
literal|"1.1.2-20070615.105019-3"
argument_list|)
expr_stmt|;
name|populateDb
argument_list|(
literal|"org.apache.maven.plugins"
argument_list|,
literal|"maven-assembly-plugin"
argument_list|,
name|versions
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

