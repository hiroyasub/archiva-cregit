begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|stagerepository
operator|.
name|merge
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
name|mockito
operator|.
name|MockitoAnnotations
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
name|List
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
name|ArchivaSpringJUnit4ClassRunner
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
name|*
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
literal|"classpath*:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|Maven2RepositoryMergerTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO_ID
init|=
literal|"test"
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|Maven2RepositoryMerger
name|repositoryMerger
decl_stmt|;
annotation|@
name|Inject
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
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
name|MockitoAnnotations
operator|.
name|initMocks
argument_list|(
name|this
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
block|}
specifier|private
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifacts
parameter_list|()
block|{
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|metadata
init|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|()
decl_stmt|;
name|ArtifactMetadata
name|artifact1
init|=
operator|new
name|ArtifactMetadata
argument_list|()
decl_stmt|;
name|artifact1
operator|.
name|setNamespace
argument_list|(
literal|"com.example.test"
argument_list|)
expr_stmt|;
name|artifact1
operator|.
name|setProject
argument_list|(
literal|"test-artifact"
argument_list|)
expr_stmt|;
name|artifact1
operator|.
name|setVersion
argument_list|(
literal|"1.0-SNAPSHOT"
argument_list|)
expr_stmt|;
name|artifact1
operator|.
name|setProjectVersion
argument_list|(
literal|"1.0-SNAPSHOT"
argument_list|)
expr_stmt|;
name|artifact1
operator|.
name|setId
argument_list|(
literal|"test-artifact-1.0-20100308.230825-1.jar"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|add
argument_list|(
name|artifact1
argument_list|)
expr_stmt|;
return|return
name|metadata
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMerge
parameter_list|()
throws|throws
name|Exception
block|{
name|Configuration
name|c
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|ManagedRepositoryConfiguration
name|testRepo
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|testRepo
operator|.
name|setId
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
name|testRepo
operator|.
name|setLocation
argument_list|(
literal|"target"
operator|+
name|File
operator|.
name|separatorChar
operator|+
literal|"test-repository"
argument_list|)
expr_stmt|;
name|RepositoryScanningConfiguration
name|repoScanConfig
init|=
operator|new
name|RepositoryScanningConfiguration
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|knownContentConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|knownContentConsumers
operator|.
name|add
argument_list|(
literal|"metadata-updater12"
argument_list|)
expr_stmt|;
name|repoScanConfig
operator|.
name|setKnownContentConsumers
argument_list|(
name|knownContentConsumers
argument_list|)
expr_stmt|;
name|c
operator|.
name|setRepositoryScanning
argument_list|(
name|repoScanConfig
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|targetRepo
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|targetRepo
operator|.
name|setId
argument_list|(
literal|"target-rep"
argument_list|)
expr_stmt|;
name|targetRepo
operator|.
name|setLocation
argument_list|(
literal|"target"
argument_list|)
expr_stmt|;
name|c
operator|.
name|addManagedRepository
argument_list|(
name|testRepo
argument_list|)
expr_stmt|;
name|c
operator|.
name|addManagedRepository
argument_list|(
name|targetRepo
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|save
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|getArtifacts
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryMerger
operator|.
name|merge
argument_list|(
name|metadataRepository
argument_list|,
name|TEST_REPO_ID
argument_list|,
literal|"target-rep"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|)
operator|.
name|getArtifacts
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMergeWithOutConflictArtifacts
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sourceRepoId
init|=
literal|"source-repo"
decl_stmt|;
name|ArtifactMetadata
name|artifact1
init|=
operator|new
name|ArtifactMetadata
argument_list|()
decl_stmt|;
name|artifact1
operator|.
name|setNamespace
argument_list|(
literal|"org.testng"
argument_list|)
expr_stmt|;
name|artifact1
operator|.
name|setProject
argument_list|(
literal|"testng"
argument_list|)
expr_stmt|;
name|artifact1
operator|.
name|setVersion
argument_list|(
literal|"5.8"
argument_list|)
expr_stmt|;
name|artifact1
operator|.
name|setProjectVersion
argument_list|(
literal|"5.8"
argument_list|)
expr_stmt|;
name|artifact1
operator|.
name|setId
argument_list|(
literal|"testng-5.8-jdk15.jar"
argument_list|)
expr_stmt|;
name|artifact1
operator|.
name|setRepositoryId
argument_list|(
name|sourceRepoId
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|sourceRepoArtifactsList
init|=
name|getArtifacts
argument_list|()
decl_stmt|;
name|sourceRepoArtifactsList
operator|.
name|add
argument_list|(
name|artifact1
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|targetRepoArtifactsList
init|=
name|getArtifacts
argument_list|()
decl_stmt|;
name|Configuration
name|c
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|ManagedRepositoryConfiguration
name|testRepo
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|testRepo
operator|.
name|setId
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
name|testRepo
operator|.
name|setLocation
argument_list|(
literal|"target"
operator|+
name|File
operator|.
name|separatorChar
operator|+
literal|"test-repository"
argument_list|)
expr_stmt|;
name|String
name|sourceRepo
init|=
literal|"src"
operator|+
name|File
operator|.
name|separatorChar
operator|+
literal|"test"
operator|+
name|File
operator|.
name|separatorChar
operator|+
literal|"resources"
operator|+
name|File
operator|.
name|separatorChar
operator|+
literal|"test-repository-with-conflict-artifacts"
decl_stmt|;
name|ManagedRepositoryConfiguration
name|testRepoWithConflicts
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|testRepoWithConflicts
operator|.
name|setId
argument_list|(
name|sourceRepoId
argument_list|)
expr_stmt|;
name|testRepoWithConflicts
operator|.
name|setLocation
argument_list|(
name|sourceRepo
argument_list|)
expr_stmt|;
name|RepositoryScanningConfiguration
name|repoScanConfig
init|=
operator|new
name|RepositoryScanningConfiguration
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|knownContentConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|knownContentConsumers
operator|.
name|add
argument_list|(
literal|"metadata-updater"
argument_list|)
expr_stmt|;
name|repoScanConfig
operator|.
name|setKnownContentConsumers
argument_list|(
name|knownContentConsumers
argument_list|)
expr_stmt|;
name|c
operator|.
name|setRepositoryScanning
argument_list|(
name|repoScanConfig
argument_list|)
expr_stmt|;
name|c
operator|.
name|addManagedRepository
argument_list|(
name|testRepo
argument_list|)
expr_stmt|;
name|c
operator|.
name|addManagedRepository
argument_list|(
name|testRepoWithConflicts
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|save
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|File
name|targetRepoFile
init|=
operator|new
name|File
argument_list|(
literal|"/target/test-repository/com/example/test/test-artifact/1.0-SNAPSHOT/test-artifact-1.0-20100308.230825-1.jar"
argument_list|)
decl_stmt|;
name|targetRepoFile
operator|.
name|setReadOnly
argument_list|()
expr_stmt|;
name|when
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|sourceRepoId
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|sourceRepoArtifactsList
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|TEST_REPO_ID
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|targetRepoArtifactsList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repositoryMerger
operator|.
name|getConflictingArtifacts
argument_list|(
name|metadataRepository
argument_list|,
name|sourceRepoId
argument_list|,
name|TEST_REPO_ID
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|metadataRepository
argument_list|)
operator|.
name|getArtifacts
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

