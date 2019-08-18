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
name|jcr
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
name|DefaultMetadataResolver
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
name|MetadataRepositoryException
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
name|MetadataSessionException
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
name|jackrabbit
operator|.
name|oak
operator|.
name|segment
operator|.
name|file
operator|.
name|InvalidFileStoreVersionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|BeforeClass
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
name|javax
operator|.
name|jcr
operator|.
name|RepositoryException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Session
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
name|Collection
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
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_comment
comment|/**  * Using static sessionFactory and repository, because initialization is expensive if we rebuild the whole repository for  * each test.  */
end_comment

begin_class
specifier|public
class|class
name|JcrMetadataRepositoryTest
extends|extends
name|AbstractMetadataRepositoryTest
block|{
specifier|private
specifier|static
name|JcrRepositorySessionFactory
name|sessionFactory
decl_stmt|;
specifier|private
specifier|static
name|JcrMetadataRepository
name|repository
decl_stmt|;
annotation|@
name|Override
specifier|public
name|JcrMetadataRepository
name|getRepository
parameter_list|( )
block|{
return|return
name|repository
return|;
block|}
annotation|@
name|Override
specifier|public
name|JcrRepositorySessionFactory
name|getSessionFactory
parameter_list|( )
block|{
return|return
name|sessionFactory
return|;
block|}
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setupSpec
parameter_list|( )
throws|throws
name|IOException
throws|,
name|InvalidFileStoreVersionException
block|{
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
name|List
argument_list|<
name|MetadataFacetFactory
argument_list|>
name|factories
init|=
name|createTestMetadataFacetFactories
argument_list|( )
decl_stmt|;
name|MetadataService
name|metadataService
init|=
operator|new
name|MetadataService
argument_list|()
decl_stmt|;
name|metadataService
operator|.
name|setMetadataFacetFactories
argument_list|(
name|factories
argument_list|)
expr_stmt|;
name|JcrRepositorySessionFactory
name|jcrSessionFactory
init|=
operator|new
name|JcrRepositorySessionFactory
argument_list|( )
decl_stmt|;
name|jcrSessionFactory
operator|.
name|setMetadataResolver
argument_list|(
operator|new
name|DefaultMetadataResolver
argument_list|( )
argument_list|)
expr_stmt|;
name|jcrSessionFactory
operator|.
name|setMetadataService
argument_list|(
name|metadataService
argument_list|)
expr_stmt|;
name|jcrSessionFactory
operator|.
name|open
argument_list|( )
expr_stmt|;
name|sessionFactory
operator|=
name|jcrSessionFactory
expr_stmt|;
name|repository
operator|=
name|jcrSessionFactory
operator|.
name|getMetadataRepository
argument_list|( )
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|MetadataRepositoryException
throws|,
name|RepositoryException
throws|,
name|MetadataSessionException
block|{
try|try
init|(
name|JcrRepositorySession
name|session
init|=
operator|(
name|JcrRepositorySession
operator|)
name|getSessionFactory
argument_list|()
operator|.
name|createSession
argument_list|()
init|)
block|{
name|Session
name|jcrSession
init|=
name|session
operator|.
name|getJcrSession
argument_list|( )
decl_stmt|;
if|if
condition|(
name|jcrSession
operator|.
name|itemExists
argument_list|(
literal|"/repositories/test"
argument_list|)
condition|)
block|{
name|jcrSession
operator|.
name|removeItem
argument_list|(
literal|"/repositories/test"
argument_list|)
expr_stmt|;
name|session
operator|.
name|save
argument_list|( )
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|stopSpec
parameter_list|( )
throws|throws
name|Exception
block|{
if|if
condition|(
name|repository
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|repository
operator|.
name|close
argument_list|( )
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//
block|}
block|}
if|if
condition|(
name|sessionFactory
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|sessionFactory
operator|.
name|close
argument_list|( )
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSearchArtifactsByKey
parameter_list|( )
throws|throws
name|Exception
block|{
try|try
init|(
name|RepositorySession
name|session
init|=
name|sessionFactory
operator|.
name|createSession
argument_list|( )
init|)
block|{
name|createArtifactWithData
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
name|tryAssert
argument_list|(
parameter_list|( )
lambda|->
block|{
try|try
init|(
name|RepositorySession
name|session
init|=
name|sessionFactory
operator|.
name|createSession
argument_list|( )
init|)
block|{
name|session
operator|.
name|refreshAndDiscard
argument_list|( )
expr_stmt|;
name|Session
name|jcrSession
init|=
operator|(
operator|(
name|JcrRepositorySession
operator|)
name|session
operator|)
operator|.
name|getJcrSession
argument_list|( )
decl_stmt|;
name|assertThat
argument_list|(
name|jcrSession
operator|.
name|propertyExists
argument_list|(
literal|"/repositories/test/content/mytest/myproject/1.0/url"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactsByProperty
init|=
name|repository
operator|.
name|searchArtifacts
argument_list|(
name|session
argument_list|,
name|TEST_REPO_ID
argument_list|,
literal|"url"
argument_list|,
name|TEST_URL
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|artifactsByProperty
argument_list|)
operator|.
name|isNotNull
argument_list|( )
operator|.
name|isNotEmpty
argument_list|( )
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSearchArtifactsByKeyExact
parameter_list|()
throws|throws
name|Exception
block|{
try|try
init|(
name|RepositorySession
name|session
init|=
name|sessionFactory
operator|.
name|createSession
argument_list|()
init|)
block|{
name|createArtifactWithData
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|RepositorySession
name|session
init|=
name|sessionFactory
operator|.
name|createSession
argument_list|()
init|)
block|{
name|session
operator|.
name|refreshAndDiscard
argument_list|()
expr_stmt|;
name|tryAssert
argument_list|(
parameter_list|()
lambda|->
block|{
name|Session
name|jcrSession
init|=
operator|(
operator|(
name|JcrRepositorySession
operator|)
name|session
operator|)
operator|.
name|getJcrSession
argument_list|( )
decl_stmt|;
name|assertThat
argument_list|(
name|jcrSession
operator|.
name|propertyExists
argument_list|(
literal|"/repositories/test/content/mytest/myproject/1.0/url"
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactsByProperty
init|=
name|repository
operator|.
name|searchArtifacts
argument_list|(
name|session
argument_list|,
name|TEST_REPO_ID
argument_list|,
literal|"url"
argument_list|,
name|TEST_URL
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|artifactsByProperty
argument_list|)
operator|.
name|describedAs
argument_list|(
literal|"Artifact search by url=%s must give a result."
argument_list|,
name|TEST_URL
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
name|artifactsByProperty
operator|=
name|repository
operator|.
name|searchArtifacts
argument_list|(
name|session
argument_list|,
name|TEST_REPO_ID
argument_list|,
literal|"org.name"
argument_list|,
literal|"pache"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artifactsByProperty
argument_list|)
operator|.
name|describedAs
argument_list|(
literal|"Artifact search by text org.name='pache' must be empty"
argument_list|)
operator|.
name|isNotNull
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

