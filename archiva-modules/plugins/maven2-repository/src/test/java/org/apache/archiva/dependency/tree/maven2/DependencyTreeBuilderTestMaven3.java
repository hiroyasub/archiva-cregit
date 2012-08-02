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
name|maven
operator|.
name|project
operator|.
name|DependencyResolutionResult
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
name|RepositorySystem
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
name|RepositorySystemSession
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
name|collection
operator|.
name|CollectRequest
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
name|collection
operator|.
name|CollectResult
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
name|collection
operator|.
name|DependencyCollectionException
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
name|graph
operator|.
name|Dependency
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
name|graph
operator|.
name|DependencyNode
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
name|graph
operator|.
name|DependencyVisitor
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
name|impl
operator|.
name|DependencyCollector
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
name|impl
operator|.
name|internal
operator|.
name|DefaultRepositorySystem
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
name|sonatype
operator|.
name|aether
operator|.
name|util
operator|.
name|graph
operator|.
name|DefaultDependencyNode
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
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|DependencyTreeBuilderTestMaven3
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
literal|"dependencyTreeBuilder#maven3"
argument_list|)
specifier|private
name|Maven3DependencyTreeBuilder
name|builder
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|PlexusSisuBridge
name|plexusSisuBridge
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
name|DefaultRepositorySystem
name|defaultRepositorySystem
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
name|defaultRepositorySystem
operator|=
operator|(
name|DefaultRepositorySystem
operator|)
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|RepositorySystem
operator|.
name|class
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DependencyNode
argument_list|>
name|nodes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DependencyNode
argument_list|>
argument_list|()
decl_stmt|;
name|DefaultDependencyNode
name|springContext
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.springframework"
argument_list|,
literal|"spring-context"
argument_list|,
literal|"2.5.6"
argument_list|)
argument_list|,
literal|"compile"
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
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|springContext
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|springContext
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|springTest
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.springframework"
argument_list|,
literal|"spring-test"
argument_list|,
literal|"2.5.5"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|springTest
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|springTest
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|plexusUtils
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-utils"
argument_list|,
literal|"1.4.5"
argument_list|)
argument_list|,
literal|"compile"
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
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|plexusUtils
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|plexusUtils
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|slf4jLog4j12
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.slf4j"
argument_list|,
literal|"slf4j-log4j12"
argument_list|,
literal|"1.5.0"
argument_list|)
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
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|slf4jLog4j12
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|slf4jLog4j12
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|plexusLog4j
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-log4j-logging"
argument_list|,
literal|"1.1-alpha-3"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|plexusLog4j
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|plexusLog4j
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|log4j
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"log4j"
argument_list|,
literal|"log4j"
argument_list|,
literal|"1.2.14"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|log4j
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|log4j
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|mavenArtifact
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-artifact"
argument_list|,
literal|"2.0.8"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|mavenArtifact
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|mavenArtifact
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|mavenProject
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-project"
argument_list|,
literal|"2.0.8"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|mavenProject
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|mavenProject
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|mavenCore
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-core"
argument_list|,
literal|"2.0.8"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|mavenCore
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|mavenCore
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|mavenSettings
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-settings"
argument_list|,
literal|"2.0.8"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|mavenSettings
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|mavenSettings
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|mavenModel
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"maven-model"
argument_list|,
literal|"2.0.8"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|mavenModel
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|mavenModel
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|plexusCommandLine
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-command-line"
argument_list|,
literal|"1.0-alpha-2"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|plexusCommandLine
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|plexusCommandLine
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|plexusRegistryCommons
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus.registry"
argument_list|,
literal|"plexus-registry-commons"
argument_list|,
literal|"1.0-alpha-2"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|plexusRegistryCommons
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|plexusRegistryCommons
argument_list|)
expr_stmt|;
name|plexusRegistryCommons
operator|.
name|setPremanagedVersion
argument_list|(
literal|"1.0-alpha-3"
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|plexusRegistryApi
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus.registry"
argument_list|,
literal|"plexus-registry-api"
argument_list|,
literal|"1.0-alpha-2"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|plexusRegistryApi
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|plexusRegistryApi
argument_list|)
expr_stmt|;
name|plexusRegistryApi
operator|.
name|setPremanagedVersion
argument_list|(
literal|"1.0-alpha-3"
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|plexusSpring
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-spring"
argument_list|,
literal|"1.2"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|plexusSpring
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|plexusSpring
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|springContext
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|springTest
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|plexusUtils
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|slf4jLog4j12
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|plexusLog4j
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|log4j
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|mavenArtifact
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|mavenProject
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|mavenCore
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|mavenSettings
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|mavenModel
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|plexusCommandLine
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|plexusRegistryCommons
argument_list|)
expr_stmt|;
name|plexusSpring
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|plexusRegistryApi
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|commonsLang
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.2"
argument_list|)
argument_list|,
literal|"compile"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|commonsLang
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|commonsLang
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|commonsIO
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"commons-io"
argument_list|,
literal|"commons-io"
argument_list|,
literal|"1.4"
argument_list|)
argument_list|,
literal|"compile"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|commonsIO
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|commonsIO
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|slf4j
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.slf4j"
argument_list|,
literal|"slf4j-api"
argument_list|,
literal|"1.5.0"
argument_list|)
argument_list|,
literal|"compile"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|slf4j
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|slf4j
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|plexusAPI
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"org.codehaus.plexus"
argument_list|,
literal|"plexus-component-api"
argument_list|,
literal|"1.0-alpha-22"
argument_list|)
argument_list|,
literal|"compile"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|plexusAPI
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|plexusAPI
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|xalan
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"xalan"
argument_list|,
literal|"xalan"
argument_list|,
literal|"2.7.0"
argument_list|)
argument_list|,
literal|"compile"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|xalan
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|xalan
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|dom4j
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"dom4j"
argument_list|,
literal|"dom4j"
argument_list|,
literal|"1.6.1"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|dom4j
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|dom4j
argument_list|)
expr_stmt|;
comment|//dom4j.setFailedUpdateScope("compile");
name|DefaultDependencyNode
name|junit
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"junit"
argument_list|,
literal|"junit"
argument_list|,
literal|"3.8.1"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|junit
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|junit
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|easymock
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"easymock"
argument_list|,
literal|"easymock"
argument_list|,
literal|"1.2_Java1.3"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|easymock
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|easymock
argument_list|)
expr_stmt|;
name|DefaultDependencyNode
name|easymockExt
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
argument_list|(
name|createArtifact
argument_list|(
literal|"easymock"
argument_list|,
literal|"easymockclassextension"
argument_list|,
literal|"1.2"
argument_list|)
argument_list|,
literal|"test"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|easymockExt
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|easymockExt
argument_list|)
expr_stmt|;
name|DependencyNode
name|mainNode
init|=
operator|new
name|DefaultDependencyNode
argument_list|(
operator|new
name|Dependency
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
literal|"compile"
argument_list|)
argument_list|)
decl_stmt|;
name|nodes
operator|.
name|put
argument_list|(
name|getId
argument_list|(
name|mainNode
operator|.
name|getDependency
argument_list|()
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|,
name|mainNode
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|commonsLang
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|commonsIO
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|slf4j
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|plexusAPI
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|plexusSpring
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|xalan
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|dom4j
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|junit
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|easymock
argument_list|)
expr_stmt|;
name|mainNode
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|easymockExt
argument_list|)
expr_stmt|;
name|defaultRepositorySystem
operator|.
name|setDependencyCollector
argument_list|(
operator|new
name|DependencyCollector
argument_list|()
block|{
specifier|public
name|CollectResult
name|collectDependencies
parameter_list|(
name|RepositorySystemSession
name|session
parameter_list|,
name|CollectRequest
name|request
parameter_list|)
throws|throws
name|DependencyCollectionException
block|{
name|CollectResult
name|collectResult
init|=
operator|new
name|CollectResult
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|collectResult
operator|.
name|setRoot
argument_list|(
operator|new
name|DefaultDependencyNode
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Dependency
name|dependency
range|:
name|request
operator|.
name|getDependencies
argument_list|()
control|)
block|{
name|DependencyNode
name|node
init|=
name|nodes
operator|.
name|get
argument_list|(
name|getId
argument_list|(
name|dependency
operator|.
name|getArtifact
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|collectResult
operator|.
name|getRoot
argument_list|()
operator|.
name|getChildren
argument_list|()
operator|.
name|add
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|collectResult
return|;
block|}
block|}
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
comment|//artifactFactory = ((DefaultDependencyTreeBuilder)this.builder).getFactory();
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
literal|null
argument_list|,
name|version
argument_list|)
return|;
block|}
specifier|private
name|String
name|getId
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
return|return
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|+
literal|":"
operator|+
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|":"
operator|+
name|artifact
operator|.
name|getVersion
argument_list|()
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBuilderDependencies
parameter_list|()
throws|throws
name|Exception
block|{
name|DependencyResolutionResult
name|resolutionResult
init|=
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
operator|new
name|DependencyVisitor
argument_list|()
block|{
specifier|public
name|boolean
name|visitEnter
parameter_list|(
name|DependencyNode
name|dependencyNode
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|visitLeave
parameter_list|(
name|DependencyNode
name|dependencyNode
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|resolutionResult
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|resolutionResult
operator|.
name|getDependencies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

