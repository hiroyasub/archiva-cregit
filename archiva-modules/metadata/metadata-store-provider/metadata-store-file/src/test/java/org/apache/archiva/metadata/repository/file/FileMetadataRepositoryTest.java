begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
name|file
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|provider
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
name|model
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
name|model
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
name|MetadataFacetFactory
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
name|AbstractMetadataRepositoryTest
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
name|MetadataService
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
name|RepositorySessionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|BeforeEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Disabled
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
specifier|public
class|class
name|FileMetadataRepositoryTest
extends|extends
name|AbstractMetadataRepositoryTest
block|{
specifier|private
name|FileMetadataRepository
name|repository
decl_stmt|;
specifier|private
name|RepositorySessionFactory
name|sessionFactory
init|=
operator|new
name|FileRepositorySessionFactory
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|MetadataRepository
name|getRepository
parameter_list|( )
block|{
return|return
name|this
operator|.
name|repository
return|;
block|}
annotation|@
name|Override
specifier|protected
name|RepositorySessionFactory
name|getSessionFactory
parameter_list|( )
block|{
return|return
name|this
operator|.
name|sessionFactory
return|;
block|}
annotation|@
name|BeforeEach
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
name|assertMaxTries
operator|=
literal|1
expr_stmt|;
name|assertRetrySleepMs
operator|=
literal|10
expr_stmt|;
name|Path
name|directory
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-repositories"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|directory
argument_list|)
condition|)
block|{
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
name|directory
argument_list|)
expr_stmt|;
block|}
name|ArchivaConfiguration
name|config
init|=
name|createTestConfiguration
argument_list|(
name|directory
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|MetadataFacetFactory
argument_list|>
name|factories
init|=
name|createTestMetadataFacetFactories
argument_list|()
decl_stmt|;
name|MetadataService
name|metadataService
init|=
operator|new
name|MetadataService
argument_list|( )
decl_stmt|;
name|metadataService
operator|.
name|setMetadataFacetFactories
argument_list|(
name|factories
argument_list|)
expr_stmt|;
name|this
operator|.
name|repository
operator|=
operator|new
name|FileMetadataRepository
argument_list|(
name|metadataService
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testGetArtifactsByProjectVersionMetadata
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testGetArtifactsByProjectVersionMetadataNoRepository
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testGetArtifactsByProjectVersionMetadataAllRepositories
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testGetArtifactsByMetadataAllRepositories
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testGetArtifactsByPropertySingleResult
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testSearchArtifactsByKey
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testSearchArtifactsByKeyExact
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testSearchArtifactsFullText
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testSearchArtifactsFullTextExact
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testSearchArtifactsByFacetKeyAllRepos
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testSearchArtifactsByFacetKey
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
annotation|@
name|Override
annotation|@
name|Disabled
specifier|public
name|void
name|testSearchArtifactsFullTextByFacet
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO not implemented
block|}
specifier|protected
specifier|static
name|ArchivaConfiguration
name|createTestConfiguration
parameter_list|(
name|Path
name|directory
parameter_list|)
block|{
name|ArchivaConfiguration
name|config
init|=
name|mock
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
name|Configuration
name|configData
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|configData
operator|.
name|addManagedRepository
argument_list|(
name|createManagedRepository
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|directory
argument_list|)
argument_list|)
expr_stmt|;
name|configData
operator|.
name|addManagedRepository
argument_list|(
name|createManagedRepository
argument_list|(
literal|"other-repo"
argument_list|,
name|directory
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|config
operator|.
name|getConfiguration
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|configData
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
specifier|private
specifier|static
name|ManagedRepositoryConfiguration
name|createManagedRepository
parameter_list|(
name|String
name|repoId
parameter_list|,
name|Path
name|directory
parameter_list|)
block|{
name|ManagedRepositoryConfiguration
name|managedRepository
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|managedRepository
operator|.
name|setId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setLocation
argument_list|(
name|directory
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
expr_stmt|;
return|return
name|managedRepository
return|;
block|}
block|}
end_class

end_unit

