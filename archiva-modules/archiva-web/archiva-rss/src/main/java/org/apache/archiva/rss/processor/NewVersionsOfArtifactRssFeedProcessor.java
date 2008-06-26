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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|ArtifactDAO
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
name|database
operator|.
name|Constraint
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
name|database
operator|.
name|constraints
operator|.
name|ArtifactVersionsConstraint
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

begin_comment
comment|/**  * Retrieve and process new versions of an artifact from the database and  * generate a rss feed. The versions will be grouped by the date when the artifact   * was gathered. Each group will appear as one entry in the feed.  *   * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  * @version  * @plexus.component role="org.apache.archiva.rss.processor.RssFeedProcessor" role-hint="new-versions"  */
end_comment

begin_class
specifier|public
class|class
name|NewVersionsOfArtifactRssFeedProcessor
extends|extends
name|AbstractArtifactsRssFeedProcessor
block|{
specifier|private
specifier|static
specifier|final
name|String
name|title
init|=
literal|"New Versions of Artifact "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|desc
init|=
literal|"These are the new versions of artifact "
decl_stmt|;
comment|/**      * @plexus.requirement      */
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
name|NewVersionsOfArtifactRssFeedProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArtifactDAO
name|artifactDAO
decl_stmt|;
comment|/**      * Process all versions of the artifact which had a rss feed request.      */
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
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
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
name|String
name|groupId
init|=
name|reqParams
operator|.
name|get
argument_list|(
name|RssFeedProcessor
operator|.
name|KEY_GROUP_ID
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|reqParams
operator|.
name|get
argument_list|(
name|RssFeedProcessor
operator|.
name|KEY_ARTIFACT_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupId
operator|!=
literal|null
operator|&&
name|artifactId
operator|!=
literal|null
condition|)
block|{
return|return
name|processNewVersionsOfArtifact
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|SyndFeed
name|processNewVersionsOfArtifact
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|Constraint
name|artifactVersions
init|=
operator|new
name|ArtifactVersionsConstraint
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
literal|"whenGathered"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|artifactDAO
operator|.
name|queryArtifacts
argument_list|(
name|artifactVersions
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RssFeedEntry
argument_list|>
name|entries
init|=
name|processData
argument_list|(
name|artifacts
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
decl_stmt|;
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
name|key
operator|+
literal|"\'"
argument_list|,
literal|"New versions of artifact "
operator|+
literal|"\'"
operator|+
name|key
operator|+
literal|"\' found in repository "
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
name|ArtifactDAO
name|getArtifactDAO
parameter_list|()
block|{
return|return
name|artifactDAO
return|;
block|}
specifier|public
name|void
name|setArtifactDAO
parameter_list|(
name|ArtifactDAO
name|artifactDAO
parameter_list|)
block|{
name|this
operator|.
name|artifactDAO
operator|=
name|artifactDAO
expr_stmt|;
block|}
block|}
end_class

end_unit

