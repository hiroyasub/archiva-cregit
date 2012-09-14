begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|tags
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|ActionContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|config
operator|.
name|ConfigurationManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|config
operator|.
name|providers
operator|.
name|XWorkConfigurationProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|inject
operator|.
name|Container
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|util
operator|.
name|ValueStack
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|util
operator|.
name|ValueStackFactory
import|;
end_import

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
name|common
operator|.
name|ArchivaException
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
name|plexusbridge
operator|.
name|PlexusSisuBridge
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
name|configuration
operator|.
name|RepositoryScanningConfiguration
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
name|ProjectVersionMetadata
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
name|archiva
operator|.
name|webtest
operator|.
name|memory
operator|.
name|TestMetadataResolver
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
name|webtest
operator|.
name|memory
operator|.
name|TestRepositorySessionFactory
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
name|sonatype
operator|.
name|aether
operator|.
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|artifact
operator|.
name|DefaultArtifact
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
name|util
operator|.
name|List
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
block|,
literal|"classpath:/spring-context-DependencyTreeTest.xml"
block|}
argument_list|)
specifier|public
class|class
name|DependencyTreeTest
extends|extends
name|TestCase
block|{
annotation|@
name|Inject
specifier|private
name|DependencyTree
name|tree
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|PlexusSisuBridge
name|plexusSisuBridge
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VERSION
init|=
literal|"version"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO_ID
init|=
literal|"test-repo"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_GROUP_ID
init|=
literal|"groupId"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_ARTIFACT_ID
init|=
literal|"artifactId"
decl_stmt|;
annotation|@
name|Override
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
name|ConfigurationManager
name|configurationManager
init|=
operator|new
name|ConfigurationManager
argument_list|()
decl_stmt|;
name|configurationManager
operator|.
name|addContainerProvider
argument_list|(
operator|new
name|XWorkConfigurationProvider
argument_list|()
argument_list|)
expr_stmt|;
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|config
operator|.
name|Configuration
name|config
init|=
name|configurationManager
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|Container
name|container
init|=
name|config
operator|.
name|getContainer
argument_list|()
decl_stmt|;
name|ValueStack
name|stack
init|=
name|container
operator|.
name|getInstance
argument_list|(
name|ValueStackFactory
operator|.
name|class
argument_list|)
operator|.
name|createValueStack
argument_list|()
decl_stmt|;
name|stack
operator|.
name|getContext
argument_list|()
operator|.
name|put
argument_list|(
name|ActionContext
operator|.
name|CONTAINER
argument_list|,
name|container
argument_list|)
expr_stmt|;
name|ActionContext
operator|.
name|setContext
argument_list|(
operator|new
name|ActionContext
argument_list|(
name|stack
operator|.
name|getContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ActionContext
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|ManagedRepositoryConfiguration
name|repoConfig
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repoConfig
operator|.
name|setId
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLocation
argument_list|(
literal|"src/test/repositories/test"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setRepositoryScanning
argument_list|(
operator|new
name|RepositoryScanningConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|TestMetadataResolver
name|metadataResolver
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|TestMetadataResolver
operator|.
name|class
argument_list|)
decl_stmt|;
name|ProjectVersionMetadata
name|metadata
init|=
operator|new
name|ProjectVersionMetadata
argument_list|()
decl_stmt|;
name|metadata
operator|.
name|setId
argument_list|(
name|TEST_VERSION
argument_list|)
expr_stmt|;
name|metadataResolver
operator|.
name|setProjectVersion
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
name|RepositorySession
name|repositorySession
init|=
name|mock
argument_list|(
name|RepositorySession
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|repositorySession
operator|.
name|getResolver
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metadataResolver
argument_list|)
expr_stmt|;
name|TestRepositorySessionFactory
name|repositorySessionFactory
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|TestRepositorySessionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|repositorySessionFactory
operator|.
name|setRepositorySession
argument_list|(
name|repositorySession
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTree
parameter_list|()
throws|throws
name|ArchivaException
block|{
name|List
argument_list|<
name|DependencyTree
operator|.
name|TreeEntry
argument_list|>
name|entries
init|=
name|tree
operator|.
name|gatherTreeList
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyTree
operator|.
name|TreeEntry
name|artifactId
init|=
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<ul><li>"
argument_list|,
name|artifactId
operator|.
name|getPre
argument_list|()
argument_list|)
expr_stmt|;
comment|// olamy tree with aether always create jar so createPomArtifact failed but it's not used
name|assertTrue
argument_list|(
name|assertArtifactsEquals
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
argument_list|,
name|artifactId
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"</li>"
argument_list|,
name|artifactId
operator|.
name|getPost
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyTree
operator|.
name|TreeEntry
name|child1
init|=
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<ul><li>"
argument_list|,
name|child1
operator|.
name|getPre
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|assertArtifactsEquals
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
literal|"child1"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|child1
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"</li>"
argument_list|,
name|child1
operator|.
name|getPost
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyTree
operator|.
name|TreeEntry
name|grandchild
init|=
name|entries
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<ul><li>"
argument_list|,
name|grandchild
operator|.
name|getPre
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|assertArtifactsEquals
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
literal|"grandchild1"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|,
name|grandchild
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"</li>"
argument_list|,
name|grandchild
operator|.
name|getPost
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyTree
operator|.
name|TreeEntry
name|greatGrandchild
init|=
name|entries
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<ul><li>"
argument_list|,
name|greatGrandchild
operator|.
name|getPre
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|assertArtifactsEquals
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
literal|"great-grandchild"
argument_list|,
literal|"3.0"
argument_list|)
argument_list|,
name|greatGrandchild
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"</li></ul></ul>"
argument_list|,
name|greatGrandchild
operator|.
name|getPost
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyTree
operator|.
name|TreeEntry
name|child2
init|=
name|entries
operator|.
name|get
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<li>"
argument_list|,
name|child2
operator|.
name|getPre
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|assertArtifactsEquals
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
literal|"child2"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|child2
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"</li>"
argument_list|,
name|child2
operator|.
name|getPost
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyTree
operator|.
name|TreeEntry
name|grandchild2
init|=
name|entries
operator|.
name|get
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<ul><li>"
argument_list|,
name|grandchild2
operator|.
name|getPre
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|assertArtifactsEquals
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
literal|"grandchild2"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|,
name|grandchild2
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"</li>"
argument_list|,
name|grandchild2
operator|.
name|getPost
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyTree
operator|.
name|TreeEntry
name|grandchild3
init|=
name|entries
operator|.
name|get
argument_list|(
literal|6
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<li>"
argument_list|,
name|grandchild3
operator|.
name|getPre
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|assertArtifactsEquals
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
literal|"grandchild3"
argument_list|,
literal|"2.0"
argument_list|)
argument_list|,
name|grandchild3
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"</li></ul>"
argument_list|,
name|grandchild3
operator|.
name|getPost
argument_list|()
argument_list|)
expr_stmt|;
name|DependencyTree
operator|.
name|TreeEntry
name|child3
init|=
name|entries
operator|.
name|get
argument_list|(
literal|7
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<li>"
argument_list|,
name|child3
operator|.
name|getPre
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|assertArtifactsEquals
argument_list|(
name|createArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
literal|"child3"
argument_list|,
literal|"1.0"
argument_list|)
argument_list|,
name|child3
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"</li></ul></ul>"
argument_list|,
name|child3
operator|.
name|getPost
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Artifact
name|createPomArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
argument_list|)
return|;
block|}
specifier|private
name|Artifact
name|createArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
operator|new
name|DefaultArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
literal|"jar"
argument_list|,
name|version
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|assertArtifactsEquals
parameter_list|(
name|Artifact
name|a
parameter_list|,
name|Artifact
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getArtifactId
argument_list|()
operator|.
name|equals
argument_list|(
name|b
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|&&
name|a
operator|.
name|getGroupId
argument_list|()
operator|.
name|equals
argument_list|(
name|b
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|&&
name|a
operator|.
name|getVersion
argument_list|()
operator|.
name|equals
argument_list|(
name|b
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|&&
name|a
operator|.
name|getExtension
argument_list|()
operator|.
name|equals
argument_list|(
name|b
operator|.
name|getExtension
argument_list|()
argument_list|)
operator|&&
name|a
operator|.
name|getClassifier
argument_list|()
operator|.
name|equals
argument_list|(
name|b
operator|.
name|getClassifier
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

