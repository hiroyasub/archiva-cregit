begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rss
operator|.
name|processor
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndEntry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndFeed
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
name|filelock
operator|.
name|DefaultFileLockManager
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
name|RepositorySessionFactory
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
name|base
operator|.
name|ArchivaRepositoryRegistry
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
name|base
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
name|Repository
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
name|RepositoryRegistry
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
name|storage
operator|.
name|FilesystemStorage
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
name|rss
operator|.
name|RssFeedGenerator
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
name|ArchivaBlockJUnit4ClassRunner
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
name|ZoneId
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
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
name|Arrays
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
name|Date
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
name|List
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

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createControl
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaBlockJUnit4ClassRunner
operator|.
name|class
argument_list|)
specifier|public
class|class
name|NewVersionsOfArtifactRssFeedProcessorTest
extends|extends
name|TestCase
block|{
specifier|private
name|NewVersionsOfArtifactRssFeedProcessor
name|newVersionsProcessor
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO
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
literal|"artifact-two"
decl_stmt|;
specifier|private
name|IMocksControl
name|metadataRepositoryControl
decl_stmt|;
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
specifier|private
name|IMocksControl
name|sessionFactoryControl
decl_stmt|;
specifier|private
name|RepositorySessionFactory
name|sessionFactory
decl_stmt|;
specifier|private
name|IMocksControl
name|sessionControl
decl_stmt|;
specifier|private
name|RepositorySession
name|session
decl_stmt|;
specifier|private
name|IMocksControl
name|repositoryRegistryControl
decl_stmt|;
specifier|private
name|RepositoryRegistry
name|repositoryRegistry
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
name|newVersionsProcessor
operator|=
operator|new
name|NewVersionsOfArtifactRssFeedProcessor
argument_list|()
expr_stmt|;
name|newVersionsProcessor
operator|.
name|setGenerator
argument_list|(
operator|new
name|RssFeedGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|metadataRepositoryControl
operator|=
name|createControl
argument_list|()
expr_stmt|;
name|metadataRepository
operator|=
name|metadataRepositoryControl
operator|.
name|createMock
argument_list|(
name|MetadataRepository
operator|.
name|class
argument_list|)
expr_stmt|;
name|sessionFactoryControl
operator|=
name|EasyMock
operator|.
name|createControl
argument_list|()
expr_stmt|;
name|sessionControl
operator|=
name|EasyMock
operator|.
name|createControl
argument_list|()
expr_stmt|;
name|sessionControl
operator|.
name|resetToNice
argument_list|()
expr_stmt|;
name|sessionFactory
operator|=
name|sessionFactoryControl
operator|.
name|createMock
argument_list|(
name|RepositorySessionFactory
operator|.
name|class
argument_list|)
expr_stmt|;
name|session
operator|=
name|sessionControl
operator|.
name|createMock
argument_list|(
name|RepositorySession
operator|.
name|class
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|sessionFactory
operator|.
name|createSession
argument_list|()
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|session
operator|.
name|getRepository
argument_list|( )
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|metadataRepository
argument_list|)
expr_stmt|;
name|sessionFactoryControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|sessionControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|repositoryRegistryControl
operator|=
name|EasyMock
operator|.
name|createControl
argument_list|()
expr_stmt|;
name|repositoryRegistry
operator|=
name|repositoryRegistryControl
operator|.
name|createMock
argument_list|(
name|ArchivaRepositoryRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Repository
argument_list|>
name|reg
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|( )
decl_stmt|;
name|reg
operator|.
name|add
argument_list|(
operator|new
name|BasicManagedRepository
argument_list|(
name|TEST_REPO
argument_list|,
name|TEST_REPO
argument_list|,
operator|new
name|FilesystemStorage
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target/test-storage"
argument_list|)
argument_list|,
operator|new
name|DefaultFileLockManager
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expect
argument_list|(
name|repositoryRegistry
operator|.
name|getRepositories
argument_list|()
argument_list|)
operator|.
name|andStubReturn
argument_list|(
name|reg
argument_list|)
expr_stmt|;
name|repositoryRegistryControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|newVersionsProcessor
operator|.
name|setRepositorySessionFactory
argument_list|(
name|sessionFactory
argument_list|)
expr_stmt|;
name|newVersionsProcessor
operator|.
name|setRepositoryRegistry
argument_list|(
name|repositoryRegistry
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
specifier|public
name|void
name|testProcess
parameter_list|()
throws|throws
name|Exception
block|{
name|Date
name|whenGatheredDate
init|=
operator|new
name|Date
argument_list|(
literal|123456789
argument_list|)
decl_stmt|;
name|ZonedDateTime
name|whenGathered
init|=
name|ZonedDateTime
operator|.
name|ofInstant
argument_list|(
name|whenGatheredDate
operator|.
name|toInstant
argument_list|()
argument_list|,
name|ZoneId
operator|.
name|systemDefault
argument_list|()
argument_list|)
decl_stmt|;
name|ArtifactMetadata
name|artifact1
init|=
name|createArtifact
argument_list|(
name|whenGathered
argument_list|,
literal|"1.0.1"
argument_list|)
decl_stmt|;
name|ArtifactMetadata
name|artifact2
init|=
name|createArtifact
argument_list|(
name|whenGathered
argument_list|,
literal|"1.0.2"
argument_list|)
decl_stmt|;
name|Date
name|whenGatheredNextDate
init|=
operator|new
name|Date
argument_list|(
literal|345678912
argument_list|)
decl_stmt|;
name|ZonedDateTime
name|whenGatheredNext
init|=
name|ZonedDateTime
operator|.
name|ofInstant
argument_list|(
name|whenGatheredNextDate
operator|.
name|toInstant
argument_list|()
argument_list|,
name|ZoneId
operator|.
name|systemDefault
argument_list|()
argument_list|)
decl_stmt|;
name|ArtifactMetadata
name|artifact3
init|=
name|createArtifact
argument_list|(
name|whenGatheredNext
argument_list|,
literal|"1.0.3-SNAPSHOT"
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reqParams
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|reqParams
operator|.
name|put
argument_list|(
name|RssFeedProcessor
operator|.
name|KEY_GROUP_ID
argument_list|,
name|GROUP_ID
argument_list|)
expr_stmt|;
name|reqParams
operator|.
name|put
argument_list|(
name|RssFeedProcessor
operator|.
name|KEY_ARTIFACT_ID
argument_list|,
name|ARTIFACT_ID
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|metadataRepository
operator|.
name|getProjectVersions
argument_list|(
name|session
argument_list|,
name|TEST_REPO
argument_list|,
name|GROUP_ID
argument_list|,
name|ARTIFACT_ID
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"1.0.1"
argument_list|,
literal|"1.0.2"
argument_list|,
literal|"1.0.3-SNAPSHOT"
argument_list|)
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|session
argument_list|,
name|TEST_REPO
argument_list|,
name|GROUP_ID
argument_list|,
name|ARTIFACT_ID
argument_list|,
literal|"1.0.1"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|artifact1
argument_list|)
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|session
argument_list|,
name|TEST_REPO
argument_list|,
name|GROUP_ID
argument_list|,
name|ARTIFACT_ID
argument_list|,
literal|"1.0.2"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|artifact2
argument_list|)
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|session
argument_list|,
name|TEST_REPO
argument_list|,
name|GROUP_ID
argument_list|,
name|ARTIFACT_ID
argument_list|,
literal|"1.0.3-SNAPSHOT"
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|artifact3
argument_list|)
argument_list|)
expr_stmt|;
name|metadataRepositoryControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|SyndFeed
name|feed
init|=
name|newVersionsProcessor
operator|.
name|process
argument_list|(
name|reqParams
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"New Versions of Artifact 'org.apache.archiva:artifact-two'"
argument_list|,
name|feed
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"New versions of artifact 'org.apache.archiva:artifact-two' found during repository scan."
argument_list|,
name|feed
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"en-us"
argument_list|,
name|feed
operator|.
name|getLanguage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|whenGatheredNext
operator|.
name|toInstant
argument_list|()
argument_list|,
name|ZonedDateTime
operator|.
name|ofInstant
argument_list|(
name|feed
operator|.
name|getPublishedDate
argument_list|()
operator|.
name|toInstant
argument_list|()
argument_list|,
name|ZoneId
operator|.
name|systemDefault
argument_list|()
argument_list|)
operator|.
name|toInstant
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SyndEntry
argument_list|>
name|entries
init|=
name|feed
operator|.
name|getEntries
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|entries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTitle
argument_list|()
operator|.
name|contains
argument_list|(
literal|"New Versions of Artifact 'org.apache.archiva:artifact-two' as of "
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|whenGathered
operator|.
name|toInstant
argument_list|()
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getPublishedDate
argument_list|()
operator|.
name|toInstant
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getTitle
argument_list|()
operator|.
name|contains
argument_list|(
literal|"New Versions of Artifact 'org.apache.archiva:artifact-two' as of "
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|whenGatheredNext
operator|.
name|toInstant
argument_list|()
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getPublishedDate
argument_list|()
operator|.
name|toInstant
argument_list|()
argument_list|)
expr_stmt|;
name|metadataRepositoryControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|ArtifactMetadata
name|createArtifact
parameter_list|(
name|ZonedDateTime
name|whenGathered
parameter_list|,
name|String
name|version
parameter_list|)
block|{
name|ArtifactMetadata
name|artifact
init|=
operator|new
name|ArtifactMetadata
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setNamespace
argument_list|(
name|GROUP_ID
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setProject
argument_list|(
name|ARTIFACT_ID
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setProjectVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setRepositoryId
argument_list|(
name|TEST_REPO
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setId
argument_list|(
name|ARTIFACT_ID
operator|+
literal|"-"
operator|+
name|version
operator|+
literal|".jar"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
block|}
end_class

end_unit

