begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
name|metadata
operator|.
name|model
operator|.
name|ArtifactMetadata
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
name|storage
operator|.
name|maven2
operator|.
name|Maven2RepositoryPathTranslator
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
name|ManagedRepositoryContent
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
name|RepositoryContentProvider
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
name|events
operator|.
name|RepositoryListener
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
name|ArtifactCleanupFeature
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
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
name|time
operator|.
name|Period
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
name|Locale
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
name|assertFalse
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
name|assertTrue
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
comment|/**  */
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
name|AbstractRepositoryPurgeTest
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
name|BasicManagedRepository
name|config
decl_stmt|;
specifier|private
name|ManagedRepositoryContent
name|repo
decl_stmt|;
specifier|protected
name|RepositoryPurge
name|repoPurge
decl_stmt|;
specifier|protected
name|IMocksControl
name|listenerControl
decl_stmt|;
specifier|protected
name|RepositoryListener
name|listener
decl_stmt|;
specifier|protected
name|RepositorySession
name|repositorySession
decl_stmt|;
specifier|protected
name|MetadataRepository
name|metadataRepository
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|NexusIndexer
name|nexusIndexer
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
name|removeMavenIndexes
argument_list|()
expr_stmt|;
name|listenerControl
operator|=
name|EasyMock
operator|.
name|createControl
argument_list|()
expr_stmt|;
name|listener
operator|=
name|listenerControl
operator|.
name|createMock
argument_list|(
name|RepositoryListener
operator|.
name|class
argument_list|)
expr_stmt|;
name|repositorySession
operator|=
name|mock
argument_list|(
name|RepositorySession
operator|.
name|class
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
name|when
argument_list|(
name|repositorySession
operator|.
name|getRepository
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metadataRepository
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
name|removeMavenIndexes
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
specifier|protected
name|void
name|removeMavenIndexes
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|IndexingContext
name|indexingContext
range|:
name|nexusIndexer
operator|.
name|getIndexingContexts
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|nexusIndexer
operator|.
name|removeIndexingContext
argument_list|(
name|indexingContext
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
name|String
name|fixPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|.
name|contains
argument_list|(
literal|" "
argument_list|)
condition|)
block|{
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractRepositoryPurgeTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|error
argument_list|(
literal|"You are building and testing with a path: \n {} containing space. Consider relocating."
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
name|path
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"&amp;20"
argument_list|)
return|;
block|}
return|return
name|path
return|;
block|}
specifier|public
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
name|getRepoConfiguration
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|repoName
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|Path
name|basePath
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-"
operator|+
name|getName
argument_list|()
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
decl_stmt|;
name|config
operator|=
operator|new
name|BasicManagedRepository
argument_list|(
name|repoId
argument_list|,
name|repoName
argument_list|,
name|basePath
argument_list|)
expr_stmt|;
name|config
operator|.
name|addActiveReleaseScheme
argument_list|(
name|ReleaseScheme
operator|.
name|RELEASE
argument_list|)
expr_stmt|;
name|config
operator|.
name|addActiveReleaseScheme
argument_list|(
name|ReleaseScheme
operator|.
name|SNAPSHOT
argument_list|)
expr_stmt|;
name|ArtifactCleanupFeature
name|atf
init|=
name|config
operator|.
name|getFeature
argument_list|(
name|ArtifactCleanupFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|atf
operator|.
name|setRetentionPeriod
argument_list|(
name|Period
operator|.
name|ofDays
argument_list|(
name|TEST_DAYS_OLDER
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|AbstractRepositoryPurgeTest
operator|.
name|fixPath
argument_list|(
name|basePath
operator|.
name|resolve
argument_list|(
name|repoId
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|config
operator|.
name|setLocation
argument_list|(
operator|new
name|URI
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|atf
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|atf
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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
name|repoCfg
init|=
name|getRepoConfiguration
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_REPO_NAME
argument_list|)
decl_stmt|;
name|RepositoryContentProvider
name|provider
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"repositoryContentProvider#maven"
argument_list|,
name|RepositoryContentProvider
operator|.
name|class
argument_list|)
decl_stmt|;
name|repo
operator|=
name|provider
operator|.
name|createManagedContent
argument_list|(
name|repoCfg
argument_list|)
expr_stmt|;
block|}
return|return
name|repo
return|;
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
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|path
argument_list|)
argument_list|)
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
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|path
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Path
name|getTestRepoRoot
parameter_list|()
block|{
return|return
name|Paths
operator|.
name|get
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
name|Path
name|getTestRepoRootPath
parameter_list|()
block|{
return|return
name|Paths
operator|.
name|get
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
name|Exception
block|{
name|removeMavenIndexes
argument_list|()
expr_stmt|;
name|Path
name|testDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|AbstractRepositoryPurgeTest
operator|.
name|fixPath
argument_list|(
name|getTestRepoRoot
argument_list|()
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
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
name|testDir
argument_list|)
expr_stmt|;
name|Path
name|sourceDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-classes/"
operator|+
name|TEST_REPO_ID
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|sourceDir
operator|.
name|toFile
argument_list|()
argument_list|,
name|testDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
name|Path
name|releasesTestDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|AbstractRepositoryPurgeTest
operator|.
name|fixPath
argument_list|(
name|Paths
operator|.
name|get
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
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
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
name|releasesTestDir
argument_list|)
expr_stmt|;
name|Path
name|sourceReleasesDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-classes/"
operator|+
name|RELEASES_TEST_REPO_ID
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyDirectory
argument_list|(
name|sourceReleasesDir
operator|.
name|toFile
argument_list|()
argument_list|,
name|releasesTestDir
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|AbstractRepositoryPurgeTest
operator|.
name|fixPath
argument_list|(
name|testDir
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"."
argument_list|)
return|;
block|}
specifier|protected
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactMetadataFromDir
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|projectName
parameter_list|,
name|Path
name|repoDir
parameter_list|,
name|Path
name|vDir
parameter_list|)
throws|throws
name|IOException
block|{
name|Maven2RepositoryPathTranslator
name|translator
init|=
operator|new
name|Maven2RepositoryPathTranslator
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(  )
argument_list|)
decl_stmt|;
return|return
name|Files
operator|.
name|find
argument_list|(
name|vDir
argument_list|,
literal|1
argument_list|,
parameter_list|(
name|path
parameter_list|,
name|basicFileAttributes
parameter_list|)
lambda|->
name|basicFileAttributes
operator|.
name|isRegularFile
argument_list|()
operator|&&
name|path
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
name|projectName
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|path
lambda|->
name|translator
operator|.
name|getArtifactForPath
argument_list|(
name|repoId
argument_list|,
name|repoDir
operator|.
name|relativize
argument_list|(
name|path
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

