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
name|SyndFeed
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
name|io
operator|.
name|FeedException
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
name|rss
operator|.
name|RssFeedEntry
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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

begin_comment
comment|/**  * Retrieve and process all artifacts of a repository from the database and generate a rss feed.  * The artifacts will be grouped by the date when the artifacts were gathered.  * Each group will appear as one entry in the feed.  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"rssFeedProcessor#new-artifacts"
argument_list|)
specifier|public
class|class
name|NewArtifactsRssFeedProcessor
extends|extends
name|AbstractArtifactsRssFeedProcessor
block|{
specifier|private
name|int
name|numberOfDaysBeforeNow
init|=
literal|30
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|title
init|=
literal|"New Artifacts in Repository "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|desc
init|=
literal|"These are the new artifacts found in the repository "
decl_stmt|;
comment|/**      * plexus.requirement      */
annotation|@
name|Inject
specifier|private
name|RssFeedGenerator
name|generator
decl_stmt|;
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|NewArtifactsRssFeedProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|TimeZone
name|GMT_TIME_ZONE
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT"
argument_list|)
decl_stmt|;
comment|/**      * Process the newly discovered artifacts in the repository. Generate feeds for new artifacts in the repository and      * new versions of artifact.      */
specifier|public
name|SyndFeed
name|process
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|reqParams
parameter_list|,
name|MetadataRepository
name|metadataRepository
parameter_list|)
throws|throws
name|FeedException
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Process new artifacts into rss feeds."
argument_list|)
expr_stmt|;
name|String
name|repoId
init|=
name|reqParams
operator|.
name|get
argument_list|(
name|RssFeedProcessor
operator|.
name|KEY_REPO_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|repoId
operator|!=
literal|null
condition|)
block|{
return|return
name|processNewArtifactsInRepo
argument_list|(
name|repoId
argument_list|,
name|metadataRepository
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|SyndFeed
name|processNewArtifactsInRepo
parameter_list|(
name|String
name|repoId
parameter_list|,
name|MetadataRepository
name|metadataRepository
parameter_list|)
throws|throws
name|FeedException
block|{
name|Calendar
name|greaterThanThisDate
init|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|GMT_TIME_ZONE
argument_list|)
decl_stmt|;
name|greaterThanThisDate
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|DATE
argument_list|,
operator|-
operator|(
name|getNumberOfDaysBeforeNow
argument_list|()
operator|)
argument_list|)
expr_stmt|;
name|greaterThanThisDate
operator|.
name|clear
argument_list|(
name|Calendar
operator|.
name|MILLISECOND
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
decl_stmt|;
try|try
block|{
name|artifacts
operator|=
name|metadataRepository
operator|.
name|getArtifactsByDateRange
argument_list|(
name|repoId
argument_list|,
name|greaterThanThisDate
operator|.
name|getTime
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|FeedException
argument_list|(
literal|"Unable to construct feed, metadata could not be retrieved: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|long
name|tmp
init|=
literal|0
decl_stmt|;
name|RssFeedEntry
name|entry
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|RssFeedEntry
argument_list|>
name|entries
init|=
operator|new
name|ArrayList
argument_list|<
name|RssFeedEntry
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|description
init|=
literal|""
decl_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|artifact
range|:
name|artifacts
control|)
block|{
name|long
name|whenGathered
init|=
name|artifact
operator|.
name|getWhenGathered
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|artifact
operator|.
name|getNamespace
argument_list|()
operator|+
literal|"/"
operator|+
name|artifact
operator|.
name|getProject
argument_list|()
operator|+
literal|"/"
operator|+
name|artifact
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|tmp
operator|!=
name|whenGathered
condition|)
block|{
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|entry
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|entry
operator|=
literal|null
expr_stmt|;
block|}
name|String
name|repoId1
init|=
name|artifact
operator|.
name|getRepositoryId
argument_list|()
decl_stmt|;
name|entry
operator|=
operator|new
name|RssFeedEntry
argument_list|(
name|this
operator|.
name|getTitle
argument_list|()
operator|+
literal|"\'"
operator|+
name|repoId1
operator|+
literal|"\'"
operator|+
literal|" as of "
operator|+
operator|new
name|Date
argument_list|(
name|whenGathered
argument_list|)
argument_list|)
expr_stmt|;
name|entry
operator|.
name|setPublishedDate
argument_list|(
name|artifact
operator|.
name|getWhenGathered
argument_list|()
argument_list|)
expr_stmt|;
name|description
operator|=
name|this
operator|.
name|getDescription
argument_list|()
operator|+
literal|"\'"
operator|+
name|repoId1
operator|+
literal|"\'"
operator|+
literal|": \n"
operator|+
name|id
operator|+
literal|" | "
expr_stmt|;
block|}
else|else
block|{
name|description
operator|=
name|description
operator|+
name|id
operator|+
literal|" | "
expr_stmt|;
block|}
if|if
condition|(
name|idx
operator|==
operator|(
name|artifacts
operator|.
name|size
argument_list|()
operator|-
literal|1
operator|)
condition|)
block|{
name|entry
operator|.
name|setDescription
argument_list|(
name|description
argument_list|)
expr_stmt|;
name|entries
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
name|tmp
operator|=
name|whenGathered
expr_stmt|;
name|idx
operator|++
expr_stmt|;
block|}
return|return
name|generator
operator|.
name|generateFeed
argument_list|(
name|getTitle
argument_list|()
operator|+
literal|"\'"
operator|+
name|repoId
operator|+
literal|"\'"
argument_list|,
literal|"New artifacts found in repository "
operator|+
literal|"\'"
operator|+
name|repoId
operator|+
literal|"\'"
operator|+
literal|" during repository scan."
argument_list|,
name|entries
argument_list|)
return|;
block|}
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|desc
return|;
block|}
specifier|public
name|RssFeedGenerator
name|getGenerator
parameter_list|()
block|{
return|return
name|generator
return|;
block|}
specifier|public
name|void
name|setGenerator
parameter_list|(
name|RssFeedGenerator
name|generator
parameter_list|)
block|{
name|this
operator|.
name|generator
operator|=
name|generator
expr_stmt|;
block|}
specifier|public
name|int
name|getNumberOfDaysBeforeNow
parameter_list|()
block|{
return|return
name|numberOfDaysBeforeNow
return|;
block|}
specifier|public
name|void
name|setNumberOfDaysBeforeNow
parameter_list|(
name|int
name|numberOfDaysBeforeNow
parameter_list|)
block|{
name|this
operator|.
name|numberOfDaysBeforeNow
operator|=
name|numberOfDaysBeforeNow
expr_stmt|;
block|}
block|}
end_class

end_unit

