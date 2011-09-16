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
name|easymock
operator|.
name|MockControl
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
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
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
name|SpringJUnit4ClassRunner
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
extends|extends
name|TestCase
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
name|ManagedRepository
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
name|MockControl
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
name|Before
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
name|listenerControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|RepositoryListener
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
operator|=
operator|(
name|RepositoryListener
operator|)
name|listenerControl
operator|.
name|getMock
argument_list|()
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
name|ManagedRepository
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
name|ManagedRepository
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
operator|new
name|File
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
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"managedRepositoryContent#default"
argument_list|,
name|ManagedRepositoryContent
operator|.
name|class
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
operator|new
name|File
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
operator|new
name|File
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
operator|new
name|File
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
operator|new
name|File
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
annotation|@
name|Override
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
block|}
end_class

end_unit

