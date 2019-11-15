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
name|rometools
operator|.
name|rome
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
name|rometools
operator|.
name|rome
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
name|AbstractMetadataRepository
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
name|time
operator|.
name|LocalDateTime
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
name|time
operator|.
name|temporal
operator|.
name|ChronoUnit
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
name|Calendar
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
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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
name|NewArtifactsRssFeedProcessorTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO
init|=
literal|"test-repo"
decl_stmt|;
specifier|private
name|NewArtifactsRssFeedProcessor
name|newArtifactsProcessor
decl_stmt|;
specifier|private
name|MetadataRepositoryMock
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
name|newArtifactsProcessor
operator|=
operator|new
name|NewArtifactsRssFeedProcessor
argument_list|()
expr_stmt|;
name|newArtifactsProcessor
operator|.
name|setGenerator
argument_list|(
operator|new
name|RssFeedGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|metadataRepository
operator|=
operator|new
name|MetadataRepositoryMock
argument_list|()
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
name|newArtifactsProcessor
operator|.
name|setRepositorySessionFactory
argument_list|(
name|sessionFactory
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
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|newArtifacts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|ZonedDateTime
name|whenGathered
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|()
decl_stmt|;
name|newArtifacts
operator|.
name|add
argument_list|(
name|createArtifact
argument_list|(
literal|"artifact-one"
argument_list|,
literal|"1.0"
argument_list|,
name|whenGathered
argument_list|)
argument_list|)
expr_stmt|;
name|newArtifacts
operator|.
name|add
argument_list|(
name|createArtifact
argument_list|(
literal|"artifact-one"
argument_list|,
literal|"1.1"
argument_list|,
name|whenGathered
argument_list|)
argument_list|)
expr_stmt|;
name|newArtifacts
operator|.
name|add
argument_list|(
name|createArtifact
argument_list|(
literal|"artifact-one"
argument_list|,
literal|"2.0"
argument_list|,
name|whenGathered
argument_list|)
argument_list|)
expr_stmt|;
name|newArtifacts
operator|.
name|add
argument_list|(
name|createArtifact
argument_list|(
literal|"artifact-two"
argument_list|,
literal|"1.0.1"
argument_list|,
name|whenGathered
argument_list|)
argument_list|)
expr_stmt|;
name|newArtifacts
operator|.
name|add
argument_list|(
name|createArtifact
argument_list|(
literal|"artifact-two"
argument_list|,
literal|"1.0.2"
argument_list|,
name|whenGathered
argument_list|)
argument_list|)
expr_stmt|;
name|newArtifacts
operator|.
name|add
argument_list|(
name|createArtifact
argument_list|(
literal|"artifact-two"
argument_list|,
literal|"1.0.3-SNAPSHOT"
argument_list|,
name|whenGathered
argument_list|)
argument_list|)
expr_stmt|;
name|newArtifacts
operator|.
name|add
argument_list|(
name|createArtifact
argument_list|(
literal|"artifact-three"
argument_list|,
literal|"2.0-SNAPSHOT"
argument_list|,
name|whenGathered
argument_list|)
argument_list|)
expr_stmt|;
name|newArtifacts
operator|.
name|add
argument_list|(
name|createArtifact
argument_list|(
literal|"artifact-four"
argument_list|,
literal|"1.1-beta-2"
argument_list|,
name|whenGathered
argument_list|)
argument_list|)
expr_stmt|;
name|metadataRepository
operator|.
name|setArtifactsByDateRange
argument_list|(
name|newArtifacts
argument_list|)
expr_stmt|;
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
name|KEY_REPO_ID
argument_list|,
name|TEST_REPO
argument_list|)
expr_stmt|;
name|SyndFeed
name|feed
init|=
name|newArtifactsProcessor
operator|.
name|process
argument_list|(
name|reqParams
argument_list|)
decl_stmt|;
comment|// check that the date used in the call is close to the one passed (5 seconds difference at most)
name|ZonedDateTime
name|cal
init|=
name|ZonedDateTime
operator|.
name|now
argument_list|()
operator|.
name|minusDays
argument_list|(
literal|30
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|ChronoUnit
operator|.
name|SECONDS
operator|.
name|between
argument_list|(
name|cal
operator|.
name|toInstant
argument_list|()
argument_list|,
name|metadataRepository
operator|.
name|getFrom
argument_list|()
operator|.
name|toInstant
argument_list|()
argument_list|)
operator|<
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|metadataRepository
operator|.
name|getTo
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_REPO
argument_list|,
name|metadataRepository
operator|.
name|getRepoId
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|feed
operator|.
name|getTitle
argument_list|()
operator|.
name|equals
argument_list|(
literal|"New Artifacts in Repository 'test-repo'"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|feed
operator|.
name|getDescription
argument_list|()
operator|.
name|equals
argument_list|(
literal|"New artifacts found in repository 'test-repo' during repository scan."
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|feed
operator|.
name|getLanguage
argument_list|()
operator|.
name|equals
argument_list|(
literal|"en-us"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|feed
operator|.
name|getPublishedDate
argument_list|()
operator|.
name|toInstant
argument_list|()
operator|.
name|truncatedTo
argument_list|(
name|ChronoUnit
operator|.
name|MILLIS
argument_list|)
operator|.
name|equals
argument_list|(
name|whenGathered
operator|.
name|toInstant
argument_list|()
operator|.
name|truncatedTo
argument_list|(
name|ChronoUnit
operator|.
name|MILLIS
argument_list|)
argument_list|)
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
name|entries
operator|.
name|size
argument_list|()
argument_list|,
literal|1
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
literal|"New Artifacts in Repository 'test-repo' as of "
argument_list|)
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
name|getPublishedDate
argument_list|()
operator|.
name|toInstant
argument_list|()
operator|.
name|truncatedTo
argument_list|(
name|ChronoUnit
operator|.
name|MILLIS
argument_list|)
operator|.
name|equals
argument_list|(
name|whenGathered
operator|.
name|toInstant
argument_list|()
operator|.
name|truncatedTo
argument_list|(
name|ChronoUnit
operator|.
name|MILLIS
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ArtifactMetadata
name|createArtifact
parameter_list|(
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|ZonedDateTime
name|whenGathered
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
literal|"org.apache.archiva"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setId
argument_list|(
name|artifactId
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
name|setRepositoryId
argument_list|(
name|TEST_REPO
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setProject
argument_list|(
name|artifactId
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
return|return
name|artifact
return|;
block|}
comment|// TODO: replace with mockito
specifier|private
class|class
name|MetadataRepositoryMock
extends|extends
name|AbstractMetadataRepository
block|{
specifier|private
name|ZonedDateTime
name|from
decl_stmt|,
name|to
decl_stmt|;
specifier|private
name|String
name|repoId
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactsByDateRange
decl_stmt|;
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByDateRange
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|ZonedDateTime
name|from
parameter_list|,
name|ZonedDateTime
name|to
parameter_list|)
block|{
name|setRepoId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
name|setFrom
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|setTo
argument_list|(
name|to
argument_list|)
expr_stmt|;
return|return
name|artifactsByDateRange
return|;
block|}
specifier|public
name|void
name|setFrom
parameter_list|(
name|ZonedDateTime
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
block|}
specifier|public
name|ZonedDateTime
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
specifier|public
name|void
name|setTo
parameter_list|(
name|ZonedDateTime
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
expr_stmt|;
block|}
specifier|public
name|ZonedDateTime
name|getTo
parameter_list|()
block|{
return|return
name|to
return|;
block|}
specifier|public
name|void
name|setRepoId
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
name|this
operator|.
name|repoId
operator|=
name|repoId
expr_stmt|;
block|}
specifier|public
name|String
name|getRepoId
parameter_list|()
block|{
return|return
name|repoId
return|;
block|}
specifier|public
name|void
name|setArtifactsByDateRange
parameter_list|(
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactsByDateRange
parameter_list|)
block|{
name|this
operator|.
name|artifactsByDateRange
operator|=
name|artifactsByDateRange
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifacts
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|)
block|{
return|return
name|artifactsByDateRange
return|;
block|}
block|}
block|}
end_class

end_unit

