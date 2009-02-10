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
name|rss
operator|.
name|stubs
operator|.
name|ArtifactDAOStub
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
name|archiva
operator|.
name|model
operator|.
name|ArchivaArtifact
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
name|spring
operator|.
name|PlexusInSpringTestCase
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

begin_class
specifier|public
class|class
name|NewVersionsOfArtifactRssFeedProcessorTest
extends|extends
name|PlexusInSpringTestCase
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
name|NewVersionsOfArtifactRssFeedProcessor
name|newVersionsProcessor
decl_stmt|;
specifier|private
name|ArtifactDAOStub
name|artifactDAOStub
decl_stmt|;
specifier|private
name|RssFeedGenerator
name|rssFeedGenerator
decl_stmt|;
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
name|artifactDAOStub
operator|=
operator|new
name|ArtifactDAOStub
argument_list|()
expr_stmt|;
name|rssFeedGenerator
operator|=
operator|new
name|RssFeedGenerator
argument_list|()
expr_stmt|;
name|newVersionsProcessor
operator|.
name|setGenerator
argument_list|(
name|rssFeedGenerator
argument_list|)
expr_stmt|;
name|newVersionsProcessor
operator|.
name|setArtifactDAO
argument_list|(
name|artifactDAOStub
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProcess
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|ArchivaArtifact
argument_list|>
argument_list|()
decl_stmt|;
name|Date
name|whenGathered
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|whenGathered
operator|.
name|setTime
argument_list|(
literal|123456789
argument_list|)
expr_stmt|;
name|ArchivaArtifact
name|artifact
init|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-two"
argument_list|,
literal|"1.0.1"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
name|TEST_REPO
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-two"
argument_list|,
literal|"1.0.2"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
name|TEST_REPO
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|Date
name|whenGatheredNext
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|whenGatheredNext
operator|.
name|setTime
argument_list|(
literal|345678912
argument_list|)
expr_stmt|;
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
literal|"org.apache.archiva"
argument_list|,
literal|"artifact-two"
argument_list|,
literal|"1.0.3-SNAPSHOT"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
name|TEST_REPO
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenGathered
argument_list|(
name|whenGatheredNext
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifactDAOStub
operator|.
name|setArtifacts
argument_list|(
name|artifacts
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
literal|"test-repo"
argument_list|)
expr_stmt|;
name|reqParams
operator|.
name|put
argument_list|(
name|RssFeedProcessor
operator|.
name|KEY_GROUP_ID
argument_list|,
literal|"org.apache.archiva"
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
literal|"artifact-two"
argument_list|)
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
literal|"New versions of artifact 'org.apache.archiva:artifact-two' found in repository 'test-repo' during repository scan."
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
name|artifacts
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getModel
argument_list|()
operator|.
name|getWhenGathered
argument_list|()
argument_list|,
name|feed
operator|.
name|getPublishedDate
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
name|assertEquals
argument_list|(
literal|"New Versions of Artifact 'org.apache.archiva:artifact-two' as of "
operator|+
name|whenGathered
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|whenGathered
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
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"New Versions of Artifact 'org.apache.archiva:artifact-two' as of "
operator|+
name|whenGatheredNext
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|whenGatheredNext
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
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

