begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|dependency
operator|.
name|tree
operator|.
name|maven2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|maven
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
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|factory
operator|.
name|ArtifactFactory
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
name|artifact
operator|.
name|versioning
operator|.
name|VersionRange
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyNode
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|DependencyTreeBuilderException
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
name|shared
operator|.
name|dependency
operator|.
name|tree
operator|.
name|traversal
operator|.
name|DependencyNodeVisitor
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
name|Collections
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
class|class
name|DependencyTreeBuilderTest
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
literal|"dependencyTreeBuilder#maven2"
argument_list|)
specifier|private
name|DependencyTreeBuilder
name|builder
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO_ID
init|=
literal|"test"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VERSION
init|=
literal|"1.2.1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_ARTIFACT_ID
init|=
literal|"archiva-common"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_GROUP_ID
init|=
literal|"org.apache.archiva"
decl_stmt|;
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaConfiguration#test"
argument_list|)
name|ArchivaConfiguration
name|config
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
operator|new
name|File
argument_list|(
literal|"target/test-repository"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
name|config
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|artifactFactory
operator|=
operator|(
operator|(
name|DefaultDependencyTreeBuilder
operator|)
name|this
operator|.
name|builder
operator|)
operator|.
name|getFactory
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuilder
parameter_list|()
throws|throws
name|DependencyTreeBuilderException
block|{
name|MockControl
name|control
init|=
name|MockControl
operator|.
name|createStrictControl
argument_list|(
name|DependencyNodeVisitor
operator|.
name|class
argument_list|)
decl_stmt|;
name|DependencyNodeVisitor
name|visitor
init|=
operator|(
name|DependencyNodeVisitor
operator|)
name|control
operator|.
name|getMock
argument_list|()
decl_stmt|;
name|DependencyNode
name|springContext
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.springframework"
argument_list|,
literal|"spring-context"
argument_list|,
literal|"2.5.6"
argument_list|)
argument_list|)
decl_stmt|;
name|springContext
operator|.
name|setPremanagedVersion
argument_list|(
literal|"2.5.5"
argument_list|)
expr_stmt|;
name|DependencyNode
name|springTest
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.springframework"
argument_list|,
literal|"spring-test"
argument_list|,
literal|"2.5.5"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|plexusUtils
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-utils"
argument_list|,
literal|"1.4.5"
argument_list|)
argument_list|)
decl_stmt|;
name|plexusUtils
operator|.
name|setPremanagedVersion
argument_list|(
literal|"1.5.1"
argument_list|)
expr_stmt|;
name|DependencyNode
name|slf4jLog4j12
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.slf4j"
argument_list|,
literal|"slf4j-log4j12"
argument_list|,
literal|"1.5.0"
argument_list|,
literal|"runtime"
argument_list|)
argument_list|)
decl_stmt|;
name|slf4jLog4j12
operator|.
name|setPremanagedScope
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|DependencyNode
name|plexusLog4j
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-log4j-logging"
argument_list|,
literal|"1.1-alpha-3"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|log4j
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"log4j"
argument_list|,
literal|"log4j"
argument_list|,
literal|"1.2.14"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|mavenArtifact
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-artifact"
argument_list|,
literal|"2.0.8"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|mavenProject
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-project"
argument_list|,
literal|"2.0.8"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|mavenCore
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-core"
argument_list|,
literal|"2.0.8"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|mavenSettings
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-settings"
argument_list|,
literal|"2.0.8"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|mavenModel
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-model"
argument_list|,
literal|"2.0.8"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|plexusCommandLine
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-command-line"
argument_list|,
literal|"1.0-alpha-2"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|plexusRegistryCommons
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus.registry"
argument_list|,
literal|"plexus-registry-commons"
argument_list|,
literal|"1.0-alpha-2"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|plexusRegistryCommons
operator|.
name|setPremanagedVersion
argument_list|(
literal|"1.0-alpha-3"
argument_list|)
expr_stmt|;
name|DependencyNode
name|plexusRegistryApi
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus.registry"
argument_list|,
literal|"plexus-registry-api"
argument_list|,
literal|"1.0-alpha-2"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|plexusRegistryApi
operator|.
name|setPremanagedVersion
argument_list|(
literal|"1.0-alpha-3"
argument_list|)
expr_stmt|;
name|DependencyNode
name|plexusSpring
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-spring"
argument_list|,
literal|"1.2"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|springContext
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|springTest
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|plexusUtils
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|slf4jLog4j12
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|plexusLog4j
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|log4j
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|mavenArtifact
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|mavenProject
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|mavenCore
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|mavenSettings
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|mavenModel
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|plexusCommandLine
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|plexusRegistryCommons
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|addChild
argument_list|(
name|plexusRegistryApi
argument_list|)
expr_stmt|;
name|DependencyNode
name|commonsLang
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.2"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|commonsIO
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"commons-io"
argument_list|,
literal|"commons-io"
argument_list|,
literal|"1.4"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|slf4j
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.slf4j"
argument_list|,
literal|"slf4j-api"
argument_list|,
literal|"1.5.0"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|plexusAPI
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-component-api"
argument_list|,
literal|"1.0-alpha-22"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|xalan
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"xalan"
argument_list|,
literal|"xalan"
argument_list|,
literal|"2.7.0"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|dom4j
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"dom4j"
argument_list|,
literal|"dom4j"
argument_list|,
literal|"1.6.1"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|dom4j
operator|.
name|setFailedUpdateScope
argument_list|(
literal|"compile"
argument_list|)
expr_stmt|;
name|DependencyNode
name|junit
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"junit"
argument_list|,
literal|"junit"
argument_list|,
literal|"3.8.1"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|easymock
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"easymock"
argument_list|,
literal|"easymock"
argument_list|,
literal|"1.2_Java1.3"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|easymockExt
init|=
operator|new
name|DependencyNode
argument_list|(
name|createArtifact
argument_list|(
literal|"easymock"
argument_list|,
literal|"easymockclassextension"
argument_list|,
literal|"1.2"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|DependencyNode
name|mainNode
init|=
operator|new
name|DependencyNode
argument_list|(
name|createProjectArtifact
argument_list|(
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|)
argument_list|)
decl_stmt|;
name|mainNode
operator|.
name|addChild
argument_list|(
name|commonsLang
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|addChild
argument_list|(
name|commonsIO
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|addChild
argument_list|(
name|slf4j
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|addChild
argument_list|(
name|plexusAPI
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|addChild
argument_list|(
name|plexusSpring
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|addChild
argument_list|(
name|xalan
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|addChild
argument_list|(
name|dom4j
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|addChild
argument_list|(
name|junit
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|addChild
argument_list|(
name|easymock
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|addChild
argument_list|(
name|easymockExt
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|mainNode
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|commonsLang
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|commonsLang
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|commonsIO
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|commonsIO
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|slf4j
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|slf4j
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|plexusAPI
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|plexusAPI
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|plexusSpring
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|springContext
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|springContext
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|springTest
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|springTest
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|plexusUtils
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|plexusUtils
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|slf4jLog4j12
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|slf4jLog4j12
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|plexusLog4j
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|plexusLog4j
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|log4j
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|log4j
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|mavenArtifact
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|mavenArtifact
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|mavenProject
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|mavenProject
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|mavenCore
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|mavenCore
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|mavenSettings
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|mavenSettings
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|mavenModel
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|mavenModel
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|plexusCommandLine
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|plexusCommandLine
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|plexusRegistryCommons
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|plexusRegistryCommons
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|plexusRegistryApi
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|plexusRegistryApi
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|plexusSpring
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|xalan
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|xalan
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|dom4j
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|dom4j
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|junit
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|junit
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|easymock
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|easymock
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|visit
argument_list|(
name|easymockExt
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|easymockExt
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|visitor
operator|.
name|endVisit
argument_list|(
name|mainNode
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|builder
operator|.
name|buildDependencyTree
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
name|TEST_VERSION
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Artifact
name|createProjectArtifact
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
name|artifactFactory
operator|.
name|createProjectArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
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
parameter_list|,
name|String
name|scope
parameter_list|)
block|{
return|return
name|artifactFactory
operator|.
name|createDependencyArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|VersionRange
operator|.
name|createFromVersion
argument_list|(
name|version
argument_list|)
argument_list|,
literal|"jar"
argument_list|,
literal|null
argument_list|,
name|scope
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
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|Artifact
operator|.
name|SCOPE_COMPILE
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuilderMissingDependency
parameter_list|()
throws|throws
name|DependencyTreeBuilderException
block|{
name|MockControl
name|control
init|=
name|MockControl
operator|.
name|createStrictControl
argument_list|(
name|DependencyNodeVisitor
operator|.
name|class
argument_list|)
decl_stmt|;
name|DependencyNodeVisitor
name|visitor
init|=
operator|(
name|DependencyNodeVisitor
operator|)
name|control
operator|.
name|getMock
argument_list|()
decl_stmt|;
comment|// not visited
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|builder
operator|.
name|buildDependencyTree
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|,
name|TEST_GROUP_ID
argument_list|,
name|TEST_ARTIFACT_ID
argument_list|,
literal|"not-a-version"
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

