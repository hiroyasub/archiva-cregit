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
name|action
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|beanlib
operator|.
name|provider
operator|.
name|replicator
operator|.
name|BeanReplicator
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
name|admin
operator|.
name|repository
operator|.
name|managed
operator|.
name|DefaultManagedRepositoryAdmin
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
name|RepositoryContentFactory
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
name|content
operator|.
name|ManagedDefaultRepositoryContent
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
name|rest
operator|.
name|services
operator|.
name|DefaultRepositoriesService
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
name|struts2
operator|.
name|StrutsSpringTestCase
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
name|redback
operator|.
name|users
operator|.
name|User
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|rest
operator|.
name|services
operator|.
name|RedbackAuthenticationThreadLocal
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|rest
operator|.
name|services
operator|.
name|RedbackRequestInformation
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
name|easymock
operator|.
name|classextension
operator|.
name|MockClassControl
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
name|never
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
specifier|public
class|class
name|DeleteArtifactActionTest
extends|extends
name|StrutsSpringTestCase
block|{
specifier|private
name|DeleteArtifactAction
name|action
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
name|MockControl
name|configurationControl
decl_stmt|;
specifier|private
name|RepositoryContentFactory
name|repositoryFactory
decl_stmt|;
specifier|private
name|MockControl
name|repositoryFactoryControl
decl_stmt|;
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
specifier|private
name|MockControl
name|metadataRepositoryControl
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPOSITORY_ID
init|=
literal|"test-repo"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|GROUP_ID
init|=
literal|"org.apache.archiva"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACT_ID
init|=
literal|"npe-metadata"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|VERSION
init|=
literal|"1.0"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPO_LOCATION
init|=
literal|"target/test-classes/test-repo"
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getContextLocations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath*:/spring-context.xml"
block|}
return|;
block|}
annotation|@
name|Override
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
name|action
operator|=
operator|(
name|DeleteArtifactAction
operator|)
name|getActionProxy
argument_list|(
literal|"/deleteArtifact.action"
argument_list|)
operator|.
name|getAction
argument_list|()
expr_stmt|;
name|action
operator|.
name|setPrincipal
argument_list|(
literal|"admin"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|configurationControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|configuration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|configurationControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|repositoryFactoryControl
operator|=
name|MockClassControl
operator|.
name|createControl
argument_list|(
name|RepositoryContentFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|repositoryFactory
operator|=
operator|(
name|RepositoryContentFactory
operator|)
name|repositoryFactoryControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|metadataRepositoryControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|MetadataRepository
operator|.
name|class
argument_list|)
expr_stmt|;
name|metadataRepository
operator|=
operator|(
name|MetadataRepository
operator|)
name|metadataRepositoryControl
operator|.
name|getMock
argument_list|()
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
name|getRepository
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metadataRepository
argument_list|)
expr_stmt|;
name|TestRepositorySessionFactory
name|repositorySessionFactory
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"repositorySessionFactory#test"
argument_list|,
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
operator|(
operator|(
name|DefaultManagedRepositoryAdmin
operator|)
operator|(
operator|(
name|DefaultRepositoriesService
operator|)
name|action
operator|.
name|getRepositoriesService
argument_list|()
operator|)
operator|.
name|getManagedRepositoryAdmin
argument_list|()
operator|)
operator|.
name|setArchivaConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
operator|(
operator|(
name|DefaultRepositoriesService
operator|)
name|action
operator|.
name|getRepositoriesService
argument_list|()
operator|)
operator|.
name|setRepositoryFactory
argument_list|(
name|repositoryFactory
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
name|action
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testNPEInDeleteArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|action
operator|.
name|setGroupId
argument_list|(
name|GROUP_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setArtifactId
argument_list|(
name|ARTIFACT_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setVersion
argument_list|(
name|VERSION
argument_list|)
expr_stmt|;
name|action
operator|.
name|setRepositoryId
argument_list|(
name|REPOSITORY_ID
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|createConfiguration
argument_list|()
decl_stmt|;
name|ManagedRepositoryContent
name|repoContent
init|=
operator|new
name|ManagedDefaultRepositoryContent
argument_list|()
decl_stmt|;
name|repoContent
operator|.
name|setRepository
argument_list|(
operator|new
name|BeanReplicator
argument_list|()
operator|.
name|replicateBean
argument_list|(
name|config
operator|.
name|findManagedRepositoryById
argument_list|(
name|REPOSITORY_ID
argument_list|)
argument_list|,
name|ManagedRepository
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|configurationControl
operator|.
name|expectAndReturn
argument_list|(
name|configuration
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|repositoryFactoryControl
operator|.
name|expectAndReturn
argument_list|(
name|repositoryFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|REPOSITORY_ID
argument_list|)
argument_list|,
name|repoContent
argument_list|)
expr_stmt|;
name|metadataRepositoryControl
operator|.
name|expectAndReturn
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|REPOSITORY_ID
argument_list|,
name|GROUP_ID
argument_list|,
name|ARTIFACT_ID
argument_list|,
name|VERSION
argument_list|)
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|configurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|repositoryFactoryControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|metadataRepositoryControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|doDelete
argument_list|()
expr_stmt|;
name|String
name|artifactPath
init|=
name|REPO_LOCATION
operator|+
literal|"/"
operator|+
name|StringUtils
operator|.
name|replace
argument_list|(
name|GROUP_ID
argument_list|,
literal|"."
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|"/"
operator|+
name|StringUtils
operator|.
name|replace
argument_list|(
name|ARTIFACT_ID
argument_list|,
literal|"."
argument_list|,
literal|"/"
argument_list|)
operator|+
literal|"/"
operator|+
name|VERSION
operator|+
literal|"/"
operator|+
name|ARTIFACT_ID
operator|+
literal|"-"
operator|+
name|VERSION
decl_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
name|artifactPath
operator|+
literal|".jar"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
name|artifactPath
operator|+
literal|".jar.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
name|artifactPath
operator|+
literal|".jar.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
name|artifactPath
operator|+
literal|".pom"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
name|artifactPath
operator|+
literal|".pom.sha1"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|File
argument_list|(
name|artifactPath
operator|+
literal|".pom.md5"
argument_list|)
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Configuration
name|createConfiguration
parameter_list|()
block|{
name|ManagedRepositoryConfiguration
name|managedRepo
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|managedRepo
operator|.
name|setId
argument_list|(
name|REPOSITORY_ID
argument_list|)
expr_stmt|;
name|managedRepo
operator|.
name|setName
argument_list|(
literal|"Test Repository"
argument_list|)
expr_stmt|;
name|managedRepo
operator|.
name|setLocation
argument_list|(
name|REPO_LOCATION
argument_list|)
expr_stmt|;
name|managedRepo
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|config
operator|.
name|addManagedRepository
argument_list|(
name|managedRepo
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
block|}
end_class

end_unit

